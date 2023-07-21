package org.reed.system;

import org.reed.define.BaseErrorCode;
import org.reed.define.NameItemTag;
import org.reed.log.ReedLogger;
import org.reed.utils.EnderUtil;
import org.reed.utils.FileUtil;
import org.reed.utils.StringUtil;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;
import java.io.*;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 
 * @author chenxiwen
 * @date 2017年8月11日上午10:46:27
 */
public final class SysEngine extends ClassLoader{

  public static final String[] INTERNAL_PACKAGES = {"org.reed","com.reed","com.ender","org.ender"};

  static{
    install();
  }

  private static SysEngine enginer;

  public static SysEngine fetchEnginer(){
    return enginer;
  }

  public static SysEngine install(){
    if(enginer==null) {
      enginer=new SysEngine(SysEngine.class.getClassLoader());
    }
    String _jvmpath=System.getProperty("java.class.path");
//    System.out.println(">>>>>"+_jvmpath);
     ReedLogger.info(_jvmpath);
    List<String> jvmpaths=StringUtil.split2List(_jvmpath,ReedContext.SepPath);
    for(int index=0;index<jvmpaths.size();index++){
      String jvmpath=jvmpaths.get(index);
      if(jvmpath.endsWith(".jar")||jvmpath.endsWith(".zip")) {
        loadClassNamesInZips(jvmpath);
      }
      else{
        File file=new File(jvmpath);
        if(file.isDirectory()) {
          String[] files=file.list();
          for(int idx=0;idx<files.length;idx++){
            if(files[idx].endsWith(".jar")||files[idx].endsWith(".zip")) {
              boolean isendsep=jvmpath.endsWith(File.separator);
              String fpath=jvmpath+(isendsep?files[idx]:File.separator+files[idx]);
              loadClassNamesInZips(fpath);
            }
          }
          loadClassNamesInPath(jvmpath);
        }
      }
    }
    return enginer;
  }

  public static void destroy(){
    enginer=null;
    install();
  }

  static class StringObject extends SimpleJavaFileObject{
    private final String context;

    public StringObject(String name,String context) throws Exception{
      super(new URI(name),Kind.SOURCE);
      this.context=context;
    }

    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException{
      return context;
    }
  }

  class Container{
    @SuppressWarnings("unused")
    private final String namespace;
    private final Map<String,byte[]> clzbytes;
    private final Map<String,Class<?>> clzobjs;

    public Container(){
      this(null);
    }

    public Container(String namespace){
      this.namespace=namespace;
      this.clzbytes=new HashMap<String,byte[]>();
      this.clzobjs=new HashMap<String,Class<?>>();
    }
  }

  private final Container global;
  private ClassLoader resource;
  private final Map<String,Container> namesclazz;

  private SysEngine(ClassLoader loader){
    super(loader);
    this.global=new Container();
    this.namesclazz=new HashMap<String,Container>();
  }

  @Override
  public InputStream getResourceAsStream(String paramString){
    return resource.getResourceAsStream(paramString);
  }

  @Override
  public URL getResource(String paramString){
    return resource.getResource(paramString);
  }

  private void initnames(String namespace){
    if(!namesclazz.containsKey(namespace)) {
      namesclazz.put(namespace,new Container());
    }
  }


  /**
   * add classes in jar package,and next level only!
   * such as spring boot maven package plugin(spring-boot-maven-plugin)
   * @param path
   */
  public static void addJarPath(String path){
    String _jvmpath=System.getProperty("java.class.path");
    _jvmpath=_jvmpath+ReedContext.SepPath;
    System.setProperty("java.class.path",_jvmpath);
    if(path==null||path.length()==0) {
      return;
    }
    if(path.endsWith(".jar")){
      try {
        JarFile jar = new JarFile(path);
        Enumeration<JarEntry> entrys = jar.entries();
        while (entrys.hasMoreElements()){
          JarEntry entry = entrys.nextElement();
          String entryName = entry.getName();
//          if(!entry.getName().startsWith("BOOT-INF")){
//            continue;
//          }
//          System.err.println(entryName);
          if(entryName.endsWith(".jar")){
            InputStream is = jar.getInputStream(entry);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            byte[] bytes;
            int n;
            while((n=is.read(buf))!=-1){
              bos.write(buf, 0, n);
            }
            bytes = bos.toByteArray();
            File tmp = new File(ReedContext.getString("user.dir")+File.separator+"enderTmp");
//            System.err.println(tmp.getAbsolutePath());
            FileOutputStream fos = new FileOutputStream(tmp);
            fos.write(bytes);
            fos.close();

            synchronized (tmp){
              loadClassNamesInZips(tmp.getAbsolutePath());
            }

//            JarFile _jar = new JarFile(tmp);
//            Enumeration<JarEntry> _entries = _jar.entries();
//            while(_entries.hasMoreElements()){
//              JarEntry _entry = _entries.nextElement();
//              String _entryName = _entry.getName();
//              System.err.println("---"+_entryName);
//              if(_entryName.endsWith(".class")){
//                String clazzname = _entryName.substring(0, _entryName.lastIndexOf(".class"));
//                clazzname = clazzname.replace("/", ".");
//                if (isInternalClass(clazzname)) {
//                  InputStream input = _jar.getInputStream(_entry);
//                  byte[] clazzbyte = new byte[input.available()];
//                  try {
//                    int rlen = 0;
//                    do {
//                      rlen += input.read(clazzbyte, rlen, clazzbyte.length - rlen);
//                    } while (rlen != clazzbyte.length);
//                  } finally {
//                    input.close();
//                  }
//                  dyloadClass(clazzname, clazzbyte);
//                }
//              }else{
//                System.err.println(_entryName+": ignored!");
//              }
//            }
          }else if(entryName.endsWith(".class")){
            String clazzname = entryName.substring(0, entryName.lastIndexOf(".class"));
            clazzname = clazzname.replace("/", ".");
            if(clazzname.startsWith("BOOT-INF.classes")){
              clazzname = clazzname.substring("BOOT-INF.classes".length()+1);
            }
            if (isInternalClass(clazzname)) {
              InputStream input = jar.getInputStream(entry);
              byte[] clazzbyte = new byte[input.available()];
              try {
                int rlen = 0;
                do {
                  rlen += input.read(clazzbyte, rlen, clazzbyte.length - rlen);
                } while (rlen != clazzbyte.length);
              } finally {
                input.close();
              }
              dyloadClass(clazzname, clazzbyte);
            }
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }


  /**
   * 添加引擎分析路径
   */
  public static void addGenericPath(String path){
//      System.out.println("~~~~~~addGenericPath: "+path);
    String _jvmpath=System.getProperty("java.class.path");
    _jvmpath=_jvmpath+ReedContext.SepPath;
    System.setProperty("java.class.path",_jvmpath);
    if(path==null||path.length()==0) {
      return;
    }
    File pfile=new File(path);
    if(pfile.exists()) {
      if(pfile.isDirectory()) {
        loadClassNamesInPath(path);
        File[] sfiles=pfile.listFiles();
        for(int idx=0;idx<sfiles.length;idx++){
          String nfile=sfiles[idx].getName();
          if(nfile.endsWith(".jar")||nfile.endsWith(".zip")) {
            loadClassNamesInZips(sfiles[idx].getAbsolutePath());
          }
        }
      }
      else{
        if(path.endsWith(".jar")||path.endsWith(".zip")) {
          loadClassNamesInZips(pfile.getAbsolutePath());
        }
      }
    }
  }

  public static void addResource(ClassLoader resource){
    enginer.resource=resource;
  }

  /**
   * 以类名获取该类是否被加载过
   */
  public static boolean hasClassByName(final String clsname){
    return hasClassByName(clsname,null);
  }

  public static boolean hasClassByName(final String clsname,final String namespace){
    if(namespace!=null) {
      if(enginer.namesclazz.containsKey(namespace)) {
        return enginer.namesclazz.get(namespace).clzbytes.containsKey(clsname);
      }
    }
    else{
      return enginer.global.clzbytes.containsKey(clsname);
    }
    return Boolean.FALSE;
  }

  /**
   * 以类名获取类描述二进制码
   */
  public static byte[] findBytesByName(final String clsname){
    return findBytesByName(clsname,null);
  }

  public static byte[] findBytesByName(final String clsname,final String namespace){
    if(namespace!=null) {
      if(enginer.namesclazz.containsKey(namespace)) {
        return enginer.namesclazz.get(namespace).clzbytes.get(clsname);
      }
      return new byte[0];
    }
    else{
      return enginer.global.clzbytes.get(clsname);
    }
  }

  /**
   * 以包名获取类描述对象
   */
  public static List<Class<?>> findClassesByPkgs(final String pkgname){
    return findClassesByPkgs(pkgname,null);
  }

  public static List<Class<?>> findClassesByPkgs(final String pkgname,final String namespace){
    List<Class<?>> classes=new ArrayList<Class<?>>();
    if(namespace!=null) {
      if(enginer.namesclazz.containsKey(namespace)) {
        Map<String,Class<?>> maps=enginer.namesclazz.get(namespace).clzobjs;
        for(String key:maps.keySet()){
          Class<?> cls=maps.get(key);
          if(key.startsWith(cls.getPackage().getName())) {
            classes.add(cls);
          }
        }
      }
      return null;
    }
    else{
      Map<String,Class<?>> maps=enginer.global.clzobjs;
      for(String key:maps.keySet()){
        Class<?> cls=maps.get(key);
        if(key.startsWith(cls.getPackage().getName())) {
          classes.add(cls);
        }
      }
    }
    return classes;
  }

  /**
   * 以包名获取指定类描述对象
   */
  public static List<Class<?>> findRealizeClassesByPkgs(final String pkgname,final Class<?> clazz){
    return findRealizeClassesByPkgs(pkgname,clazz,null);
  }

  public static List<Class<?>> findRealizeClassesByPkgs(final String pkgname,final Class<?> clazz,final String namespace){
    List<Class<?>> classes=new ArrayList<Class<?>>();
    List<Class<?>> backes=new ArrayList<Class<?>>();
    if(namespace!=null) {
      if(enginer.namesclazz.containsKey(namespace)) {
        Map<String,Class<?>> maps=enginer.namesclazz.get(namespace).clzobjs;
        for(String key:maps.keySet()){
          Class<?> cls=maps.get(key);
          Package pkname=cls.getPackage();
          if(pkgname==null||(pkname!=null&&pkgname.equals(pkname.getName()))) {
            classes.add(cls);
          }
        }
      }
      Map<String,Class<?>> maps=enginer.namesclazz.get("DEFAULT").clzobjs;
      for(String key:maps.keySet()){
        Class<?> cls=maps.get(key);
        Package pkname=cls.getPackage();
        if(pkgname==null||(pkname!=null&&pkgname.equals(pkname.getName()))) {
          classes.add(cls);
        }
      }
    }
    else{
      Map<String,Class<?>> maps=enginer.global.clzobjs;
      for(String key:maps.keySet()){
        Class<?> cls=maps.get(key);
        Package pkname=cls.getPackage();
        if(pkgname==null||(pkname!=null&&pkgname.equals(pkname.getName()))) {
          classes.add(cls);
        }
      }
    }
    for(int idx=0;idx<classes.size();idx++){
      Class<?> _clazz=classes.get(idx);
      if(implOfInterface(_clazz,clazz)) {
        if(!backes.contains(_clazz)) {
          backes.add(_clazz);
        }
      }
    }
    return backes;
  }

  /**
   * 以类名获取类描述对象
   */
  public static Class<?> findClassByName(final String clsname){
    return findClassByName(clsname,null);
  }

  public static Class<?> findClassByName(final String clsname,final String namespace){
    if(namespace!=null) {
      if(enginer.namesclazz.containsKey(namespace)) {
        return enginer.namesclazz.get(namespace).clzobjs.get(clsname);
      }
      return null;
    }
    else{
      if(enginer.global.clzobjs.containsKey(clsname))
        return enginer.global.clzobjs.get(clsname);
      else if(enginer.global.clzbytes.containsKey(clsname))
        return dyloadClass(clsname,enginer.global.clzbytes.get(clsname));
      return null;
    }
  }

  /**
   * 查找指定包下面所有的类
   */
  public static List<Class<?>> classInPackage(String pkgname,String namespace){
    List<Class<?>> classList=new ArrayList<Class<?>>();
    if(namespace!=null) {
      if(enginer.namesclazz.containsKey(namespace)) {
        Map<String,Class<?>> inclazzs=enginer.namesclazz.get(namespace).clzobjs;
        for(String key:inclazzs.keySet()){
          if(key.startsWith(pkgname)) {
            classList.add(inclazzs.get(key));
          }
        }
      }
    }
    else{
      Map<String,Class<?>> inclazzs=enginer.global.clzobjs;
      for(String key:inclazzs.keySet()){
        if(key.startsWith(pkgname)) {
          if(key.indexOf("$")==-1) {
            classList.add(inclazzs.get(key));
          }
          else{
            continue;
          }
        }
      }
    }
    return classList;
  }

  /**
   * 查找所有实现接口的类
   */
  public static List<Class<?>> realizeClass(Class<?> clazz,String namespace){
    List<Class<?>> classList=new ArrayList<Class<?>>();
    Map<String,Class<?>> inclazzs=null;
    if(namespace!=null) {
      if(enginer.namesclazz.containsKey(namespace)) {
        inclazzs=enginer.namesclazz.get(namespace).clzobjs;
      }
      else{
        inclazzs=enginer.namesclazz.get("DEFAULT").clzobjs;
      }
    }
    else{
      inclazzs=enginer.global.clzobjs;
    }
    for(String key:inclazzs.keySet()){
      try{
        Class<?> _clazz=inclazzs.get(key);
        if(implOfInterface(_clazz,clazz)) {
          if(!classList.contains(_clazz)) {
            classList.add(_clazz);
          }
        }
      }
      catch(Exception e){}
    }
    return classList;
  }
  
  public static <T>List<Class<? extends T>> realizeClassWithType(Class<T> clazz,String namespace){
      List<Class<? extends T>> classList=new ArrayList<Class<? extends T>>();
      Map<String,Class<?>> inclazzs=null;
      if(namespace!=null) {
        if(enginer.namesclazz.containsKey(namespace)) {
          inclazzs=enginer.namesclazz.get(namespace).clzobjs;
        }
        else{
          inclazzs=enginer.namesclazz.get("DEFAULT").clzobjs;
        }
      }
      else{
        inclazzs=enginer.global.clzobjs;
      }
      for(String key:inclazzs.keySet()){
        try{
          Class<? extends T> _clazz=(Class<? extends T>) inclazzs.get(key);
          if(implOfInterface(_clazz,clazz)) {
            if(!classList.contains(_clazz)) {
              classList.add(_clazz);
            }
          }
        }
        catch(Exception e){}
      }
      return classList;
    }

  /**
   * 类是否为接口的实现
   * @param clazz
   * @param inface
   * @return
   */
  public static boolean implOfInterface(Class<?> clazz,Class<?> inface){
    if(clazz.equals(inface)) {
      return false;
    }
    if(clazz.getInterfaces().equals(inface.getInterfaces())) {
      return true;
    }
    return inface.isAssignableFrom(clazz);
  }

  /**
   * 查找指定类的所有接口
   */
  public static List<Class<?>> interfaces(Class<?> clazz,boolean toroot){
    Class<?>[] clazzes=clazz.getInterfaces();
    List<Class<?>> list=Arrays.asList(clazzes);
    if(toroot) {
      Queue<Class<?>> queue=new LinkedList<Class<?>>();
      for(int idx=0;idx<clazzes.length;idx++){
        queue.add(clazzes[idx]);
      }
      if(!Modifier.isInterface(clazz.getModifiers())) {
        queue.add(clazz.getSuperclass());
      }
      while(queue.size()>0){
        Class<?> pclazz=queue.remove();
        clazzes=pclazz.getInterfaces();

        for(int idx=0;idx<clazzes.length;idx++){
          if(!list.contains(clazzes[idx])) {
            list.add(clazzes[idx]);
          }
        }
      }
    }
    return list;
  }

  // /////////////////////加载操作/////////////////////////////////////

  /**
   * 按照JAVA文件动态加载Class
   */
  public static Class<?> dyloadClassByJava(final String clsname,final String flname,final String filepath){
    String context=FileUtil.Loader.loadFile(filepath);
    return dyloadClassBySource(clsname,flname,context);
  }

  /**
   * 按照JAVA源码动态加载Class
   */
  public static Class<?> dyloadClassBySource(final String clsname,final String flname,final String source){
    try{
      StringObject jcontext=new StringObject(flname,source);
      JavaCompiler compiler=ToolProvider.getSystemJavaCompiler();
      String path=StringUtil.replace(System.getProperty("user.dir"),";","/");
      path=path.endsWith(File.separator)?(path+"temp/"):(path+File.separator+"temp/");
      if(!FileUtil.exists(path)) {
        FileUtil.mkdir(path);
      }
      String clz=StringUtil.replace(clsname,".","/")+".class";
      Iterable<String> options=Arrays.asList("-d",path);
      Iterable<? extends JavaFileObject> files=Arrays.asList(jcontext);
      CompilationTask task=compiler.getTask(null,null,null,options,null,files);
      if(task.call()) {
        String clspath=path+clz;
        InputStream in=FileUtil.StreamLoader.loadFile(clspath);
        byte[] bytes=new byte[in.available()];
        in.read(bytes,0,bytes.length);
        in.close();
        FileUtil.rmdir(path);
        return dyloadClass(clsname,bytes);
      }
      else{
        return null;
      }
    }
    catch(Exception ex){
      ex.printStackTrace();
      return null;
    }
  }

  /**
   * 按照二进制文件动态加载Class(filepath为完整路径)
   */
  public static Class<?> dyloadClassByClass(final String clsname,final String filepath){
    try{
      File file=new File(filepath);
      if(file.exists()) {
        InputStream input=new FileInputStream(file);
        byte[] bytes=new byte[input.available()];
        input.read(bytes,0,bytes.length);
        input.close();
        return dyloadClass(clsname,bytes);
      }
      return null;
    }
    catch(Exception ex){
      return null;
    }
  }

  /**
   * 按照二进制字节流动态加载Class
   */
  public static Class<?> dyloadClass(String name,byte[] clsbyte){
    return dyloadClass(name,clsbyte,0,clsbyte.length);
  }

  /**
   * 按照二进制字节流动态加载Class
   */
  public static Class<?> dyloadClass(final String name,final byte[] clsbyte,final int off,final int len){
    try{
      Class<?> clazz=null;
      try{
        clazz=enginer.findClass(name);
      }
      catch(Exception ex){}
      if(enginer.global.clzobjs.containsKey(name)) {
//          System.out.println(EnderUtil.devInfo()+" - enginer.global.clzobjs.get("+name+")");
          ReedLogger.info(EnderUtil.devInfo()+" - enginer.global.clzobjs.get("+name+")");
        return enginer.global.clzobjs.get(name);
      }
      else if(clazz!=null) {
        add2enginer(clazz,name,clsbyte,off,len);
      }
      else{
        enginer.global.clzbytes.put(name,clsbyte);
        clazz=enginer.loadClass(name);
        if(clazz==null) {
          clazz=enginer.defineClass(name,clsbyte,off,len);
        }
        add2enginer(clazz,name,clsbyte,off,len);
      }
//      System.out.println(EnderUtil.devInfo()+" - "+clazz);
      ReedLogger.info(EnderUtil.devInfo()+" - "+clazz);
      return clazz;
    }
    catch(Exception ex){
       ex.printStackTrace();
      return null;
    }
  }

  private static void add2enginer(Class<?> clazz,String name,byte[] clsbyte,int off,int len){
    // System.out.println(name);
    byte[] bytes=new byte[len];
    System.arraycopy(clsbyte,off,bytes,0,len);
    enginer.global.clzbytes.put(name,bytes);
    enginer.global.clzobjs.put(name,clazz);
    if(clazz.isAnnotationPresent(NameItemTag.class)) {
      NameItemTag item=clazz.getAnnotation(NameItemTag.class);
      String[] namespaces=item.namespace();
      for(int idx=0;idx<namespaces.length;idx++){
        enginer.initnames(namespaces[idx]);
        enginer.namesclazz.get(namespaces[idx]).clzbytes.put(name,bytes);
        enginer.namesclazz.get(namespaces[idx]).clzobjs.put(name,clazz);
      }
    }
    else{
      enginer.initnames("DEFAULT");
      enginer.namesclazz.get("DEFAULT").clzbytes.put(name,bytes);
      enginer.namesclazz.get("DEFAULT").clzobjs.put(name,clazz);
    }
  }

  // ///////////////////实际操作/////////////////////////////////
  @Override
  protected Class<?> findClass(String name) throws ClassNotFoundException{
    if(global.clzobjs.containsKey(name)) {
      return global.clzobjs.get(name);
    }
    try{
      return super.findClass(name);
    }
    catch(Exception ex){
      return null;
    }
  }

    /**
     * 从zip中加载class
     */
    private static void loadClassNamesInZips(final String path) {

        try {
            ZipFile file = new ZipFile(path);
            Enumeration<?> entries = file.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                if (entry.isDirectory()) {
                    continue;
                }
                String subname = entry.getName();
                if (!subname.endsWith(".class")) {
                    continue;
                }
                String clazzname = subname.substring(0, subname.lastIndexOf(".class"));
                clazzname = clazzname.replace("/", ".");
                if (isInternalClass(clazzname)) {
                    InputStream input = file.getInputStream(entry);
                    byte[] clazzbyte = new byte[input.available()];
                    try {
                        int rlen = 0;
                        do {
                            rlen += input.read(clazzbyte, rlen, clazzbyte.length - rlen);
                        } while (rlen != clazzbyte.length);
                    } finally {
                        input.close();
                    }
                    dyloadClass(clazzname, clazzbyte);
                }

            }
            file.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
      
    
  }


  //useless delete in future
  private static void loadClassNamesInJar(String path){
    if(path.endsWith(".jar")){
      try {
        JarFile jar = new JarFile(path);
        Enumeration<JarEntry> entrys = jar.entries();
        while (entrys.hasMoreElements()){
          JarEntry entry = entrys.nextElement();
//          if(!entry.getName().startsWith("BOOT-INF")){
//            continue;
//          }
          System.err.println(entry.getName());
          if(entry.getName().endsWith(".jar")){
            InputStream is = jar.getInputStream(entry);
            File tmp = new File(ReedContext.getString("user.dir")+File.separator+"tmp");
            FileOutputStream fos = new FileOutputStream(tmp);
            FileChannel fci = ((FileInputStream)is).getChannel();
            FileChannel fco = fos.getChannel();
            ByteBuffer bb = ByteBuffer.allocate(4096);

            while(fci.read(bb)!=-1){
              bb.flip();
              fco.write(bb);
              bb.clear();
            }

            JarFile j = new JarFile(tmp);
//            JarEntry j = jar.getJarEntry(entry.getName());
//            String clazzname = subname.substring(0, subname.lastIndexOf(".class"));
//            clazzname = clazzname.replace("/", ".");
//            if (isInternalClass(clazzname)) {
//              InputStream input = file.getInputStream(entry);
//              byte[] clazzbyte = new byte[input.available()];
//              try {
//                int rlen = 0;
//                do {
//                  rlen += input.read(clazzbyte, rlen, clazzbyte.length - rlen);
//                } while (rlen != clazzbyte.length);
//              } finally {
//                input.close();
//              }
//              dyloadClass(clazzname, clazzbyte);
//            }

          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 从路径中加载class
   */
  private static void loadClassNamesInPath(String path){
//      System.out.println(">>>>>>loadClassNamesInPath: "+path);
    if(path!=null) {
      List<File> files=new ArrayList<File>();
      files.add(new File(path));
      while(files.size()>0){
        File dir=files.remove(0);
        File[] subdir=dir.listFiles();
        for(int idx=0;subdir!=null&&idx<subdir.length;idx++){
          if(subdir[idx].isDirectory()) {
            files.add(subdir[idx]);
            continue;
          }
          try{
//              System.out.println(">>>>>CanonicalPath: "+subdir[idx].getCanonicalPath());
            if(subdir[idx].getCanonicalPath().endsWith(".class")) {
              String subname=subdir[idx].getCanonicalPath();
              String clazzname=path.endsWith(File.separator)?
                          subname.substring(path.length(),subname.lastIndexOf(".class")):
                          subname.substring(path.length()+1,subname.lastIndexOf(".class"));
              clazzname=clazzname.replace(File.separator,".");
//              System.out.println(">>>>>clazzname: "+clazzname);
              if(isInternalClass(clazzname)) {
                InputStream input=new FileInputStream(subdir[idx]);
                byte[] clazzbyte=new byte[input.available()];
                try{
                  int rlen=0;
                  do{
                    rlen+=input.read(clazzbyte,rlen,clazzbyte.length-rlen);
                  }
                  while(rlen!=clazzbyte.length);
                }
                finally{
                  input.close();
                }
                dyloadClass(clazzname,clazzbyte);
              }
            }
          }
          catch(Throwable th){
             th.printStackTrace();
          }
        }
      }
    }
  }

  /**
   * return true if it's an internal package, define in ${INTERNAL_PACKAGES}
   * @param clazzname
   * @return
   */
  private static boolean isInternalClass(String clazzname){
    for(String packageName : INTERNAL_PACKAGES){
      if(clazzname.startsWith(packageName)){
        return true;
      }
    }
    return false;
  }
  
  public static void main(String[] args){
//      SysEngine.addGenericPath("C:\\Develop\\Eclipse\\eclipse_workspace\\base\\target\\test-classes");
      System.setProperty("java.class.path", "C:\\Develop\\Eclipse\\eclipse_workspace\\base\\target\\test-classes;"+System.getProperty("java.class.path"));
      System.out.println(System.getProperty("java.class.path"));
      Class<?> cl = SysEngine.findClassByName("org.reed.system.SystemInfo");
      System.out.println(cl);
      List<Class<?>> list = SysEngine.realizeClass(BaseErrorCode.class, null);
      System.out.println("size="+list.size());
      for(Class<?> clz : list){
          System.out.println(clz);
      }
   }
}
