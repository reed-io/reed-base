/**
 * base/org.reed.utils/FileUtil.java
 */
package org.reed.utils;

import sun.reflect.Reflection;

import java.io.*;
import java.math.BigInteger;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author chenxiwen
 * @date 2017年8月11日上午11:12:57
 */
public final class FileUtil {
    
    /**
     * 检测文件存在
     */
    static public boolean exists(String file){
      return new File(file).exists();
    }

    public static boolean isFolder(String path){
        if(!exists(path)){
            return false;
        }
        File file = new File(path);
        return file.isDirectory();
    }

    public static byte[] md5_digest(File file){
        try {
            FileInputStream fis = new FileInputStream(file);
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = fis.read(buffer, 0, 1024)) != -1) {
                md.update(buffer, 0, length);
            }
            fis.close();
            return md.digest();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static String md5(File file){
        try {
            FileInputStream fis = new FileInputStream(file);
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = fis.read(buffer, 0, 1024)) != -1) {
                md.update(buffer, 0, length);
            }
            fis.close();
            BigInteger bigInt = new BigInteger(1, md.digest());
//            System.out.println("文件md5值：" + bigInt.toString(16));
            String result = bigInt.toString(16);
            while(result.length()!=32){
                result = '0'+result;
            }
            return result;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    static public URL toURL(File file){
        String path=file.getAbsolutePath();
        if(File.separatorChar!='/'){
          path.replace(File.separatorChar,'/');
        }
        if(!path.startsWith("/")){
          path="/"+path;
        }
        if(file.isDirectory()&&!path.endsWith("/")){
          path=path+"/";
        }
        try{
          return new URL("file","",-1,path);
        }
        catch(Throwable th){
          throw new RuntimeException(th);
        }
      }
    
    /**
     * 创建目录
     * @param dir
     * @return
     */
    static public boolean mkdir(String dir){
        File file=new File(dir);
        if(!file.exists()){
          return file.mkdirs();
        }
        return true;
      }
    
    /**
     * 删除文件
     */
    static public boolean rmfile(String path){
      File file=new File(path);
      if(file.exists()&&file.isFile()){
        return file.delete();
      }
      return true;
    }
    
    /**
     * 删除目录
     */
    static public boolean rmdir(String dir){
      Stack<File> dirs=new Stack<File>();
      dirs.push(new File(dir));
      while(dirs.size()>0){
        File _dir=dirs.pop();
        if(!_dir.isDirectory()){
          if(!_dir.delete()){
            return false;
          }
          continue;
        }
        File[] _sfiles=_dir.listFiles();
        if(_sfiles.length>0){
          dirs.push(_dir);
          for(File _sfile:_sfiles){
            dirs.push(_sfile);
          }
        }
        else{
          if(!_dir.delete()){
            return false;
          }
        }
      }
      return true;
    }
    
    /**
     * 生成新文件
     */
    static public boolean touch(String path,String cont){
        return touch(path,cont,"UTF8");
    }
    
    static public boolean touch(String path,String cont,String charset){
      File file=new File(path);
      return touch(file.getParent(),file.getName(),cont,charset);
    }
    /**
     * 生成新文件
     */
    static public boolean touch(String dir,String name,String cont,String charset){
      try {
          return touch(dir,name,cont.getBytes(charset));
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }
      return false;
    }

    static public boolean touch(String dir,String name,byte[] cont){
      try{
        if(!mkdir(dir)){
          System.err.println("目录不符合规范或者创建目录--->失败");
          return false;
        }
        String file=dir+File.separator+name;
        OutputStream writer=new FileOutputStream(file,false);
        int buffsize=1024*1024*2;
        int loop=cont.length/buffsize;
        loop=cont.length%buffsize!=0?loop+1:loop;
        for(int idx=0;idx<loop;idx++){
          writer.write(cont,idx*buffsize,Math.min(buffsize,cont.length-idx*buffsize));
        }
        writer.close();
        return true;
      }
      catch(Exception ex){
        ex.printStackTrace();
        System.err.println("保存文件操作--->异常\n"+ex.getMessage());
        return false;
      }
    }

    /**
     * set permission rw for owner and group
     * @param dir
     * @param name
     * @return
     */
    public static boolean setPermission(String dir,String name){
        Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();
        perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.OWNER_WRITE);
//        perms.add(PosixFilePermission.OWNER_EXECUTE);
        perms.add(PosixFilePermission.GROUP_READ);
        perms.add(PosixFilePermission.GROUP_WRITE);
//        perms.add(PosixFilePermission.GROUP_EXECUTE);
        perms.add(PosixFilePermission.OTHERS_READ);
        perms.add(PosixFilePermission.OTHERS_WRITE);
//        perms.add(PosixFilePermission.OTHERS_EXECUTE);
        try {
            Path path = Paths.get(dir, File.separator, name);
            Files.setPosixFilePermissions(path, perms);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 复制单个文件
     */
    static public boolean copy(String path,String dirs,String file){
      try{
        File oldfile=new File(path);
        if(oldfile.exists()){
          if(!mkdir(dirs)){
            return false;
          }
          int byteread=0;
          InputStream is=new FileInputStream(path); // 读入原文件
          OutputStream os=new FileOutputStream(dirs+File.separator+file);
          byte[] buffer=new byte[1024];
          while((byteread=is.read(buffer))!=-1){
            os.write(buffer,0,byteread);
          }
          is.close();
          os.flush();
          os.close();
          return true;
        }
        return true;
      }
      catch(Exception ex){
        ex.printStackTrace();
        System.err.println("复制文件操作--->异常\n"+ex.getMessage());
        return false;
      }
    }
    
    /**
     * 复制整个文件夹内容
     */
    static public int copys(String odir,String ndir){
      return copys(odir,ndir,new ArrayList<String>());
    }
    
    static public int copys(String odir,String ndir,List<String> exincludes){
        int flag=0;
        try{
          if(new File(odir).exists()){
            if(!mkdir(ndir)){
              System.err.println("目录不符合规范或者创建目录--->失败");
              return -1;
            }
            for(File file:new File(odir).listFiles()){
              if(file.isFile()&&!exincludes.contains(file.getName())){
                flag++;
                if(!copy(file.getCanonicalPath(),ndir,file.getName())){
                  System.err.println("复制文件["+file.getName()+"]到目录{"+ndir+"}操作--->失败");
                }
              }
              if(file.isDirectory()&&!file.isHidden()){
                int tmp=copys(file.getCanonicalPath(),ndir+File.separator+file.getName(),exincludes);
                if(tmp<0){
                  System.err.println("复制目录["+file.getName()+"]到目录{"+ndir+"}操作--->失败");
                }
                else{
                  flag+=tmp;
                }
              }
            }
            return flag;
          }
          return flag;
        }
        catch(Exception ex){
            ex.printStackTrace();
          System.err.println("复制文件夹操作--->异常\n"+ex.getMessage());
          return -1;
        }
      }

    /**
     * return folder's md5, will ignore hidden file
     * @param path
     * @return
     */
    public static String getDirMd5(String path, String... ignoreSubDir){
        if(!FileUtil.exists(path) || !FileUtil.isFolder(path)){
            return null;
        }
        File folder = new File(path);
        StringBuilder strBuilder = new StringBuilder();
        getDirStr(strBuilder, folder, ignoreSubDir);
        if(strBuilder.length() == 0){
            return null;
        }
        return StringUtil.md5(strBuilder.toString());
    }

    private static void getDirStr(StringBuilder strBuilder, File f, String... ignoreSubDir){
        if(f.isDirectory() && !ArrayUtil.contains(ignoreSubDir, f.getName())){
            File[] files = f.listFiles();
            for(File file : files){
                getDirStr(strBuilder, file, ignoreSubDir);
            }
        }else if(f.isFile()){
            if(f.isHidden()){
                return;
            }
            String fileMd5 = FileUtil.md5(f);
            strBuilder.append(fileMd5);
        }
    }


    public static class Loader{
        public static InputStream loadStreamFromJar(final String jarpath,final String filename){
          ZipFile file=null;
          InputStream input=null;
          try{
            file=new ZipFile(jarpath);
            Enumeration<?> entries=file.entries();
            while(entries.hasMoreElements()){
              ZipEntry entry=(ZipEntry)entries.nextElement();
              if(entry.isDirectory()){
                continue;
              }
              String subname=entry.getName();
              if(subname.equals(filename)){
                input=file.getInputStream(entry);
                byte[] bytes=new byte[input.available()];
                new DataInputStream(input).readFully(bytes);
                ByteArrayInputStream bin=new ByteArrayInputStream(bytes);
                return bin;
              }
            }
          }
          catch(Exception ex){
            System.err.println(EnderUtil.getDevInfo()+" - ZIP  exceptions"+ex);
          }
          finally{
            if(file!=null){
              try{
                input.close();
                file.close();
              }
              catch(IOException e){
                e.printStackTrace();
              }
            }
          }
          return null;
        }

        public static String loadStrFromJar(final String jarpath,final String filename){
          return loadStream(loadStreamFromJar(jarpath,filename),true,"UTF-8");
        }
        public static String loadFile(final String file){
            return loadFile(file,"UTF-8");
        }

        public static String loadFile(final String file,final String charset){
            try {
                return loadStream(new FileInputStream(file),true,charset);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }

        public static String loadProp(final String prop){
          return loadFile(System.getProperty(prop),"UTF-8");
        }

        public static boolean hasResc(final String path){
          for(int idx=1;idx<=20;idx++){
            Class<?> clazz= Reflection.getCallerClass(idx);
            if(clazz!=null){
              if(clazz.getResource(path)!=null){
                return Boolean.TRUE.booleanValue();
              }
              ClassLoader loader=clazz.getClassLoader();
              if(loader!=null&&loader.getResource(path)!=null){
                return Boolean.TRUE.booleanValue();
              }
            }
          }
          return Boolean.FALSE.booleanValue();
        }

        public static String loadResc(final String path){
            return loadResc(path,"UTF-8");
        }
        
        public static String loadResc(final String path,final String charset){
          for(int idx=1;idx<=20;idx++){
            Class<?> clazz=Reflection.getCallerClass(idx);
            if(clazz!=null){
              if(clazz.getResource(path)!=null){
                return loadStream(clazz.getResourceAsStream(path),true,charset);
              }
              ClassLoader loader=clazz.getClassLoader();
              if(loader!=null&&loader.getResource(path)!=null){
                return loadStream(loader.getResourceAsStream(path),true,charset);
              }
            }
          }
          return null;
        }

        public static String loadResc(final String path,ClassLoader loader){
          return loadStream(loader.getResourceAsStream(path),true,"UTF-8");
        }

        public static String loadStream(final InputStream stream, boolean doclose, final String charset) {
            ByteArrayOutputStream bio = new ByteArrayOutputStream();

            try {
                while (stream.available() > 0) {
                    bio.write(stream.read());
                }
                if (doclose) {
                    stream.close();
                }
                stream.close();
                bio.close();
                return bio.toString(charset);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        public static String loadReader(Reader reader) throws Exception{
          int rd_size=Integer.SIZE;
          char[] carray=new char[512];
          StringBuffer buffer=new StringBuffer();
          do{
            if((rd_size=reader.read(carray))!=-1){
              buffer.append(carray,0,rd_size);
            }
          }
          while(rd_size!=-1);
          reader.close();
          return buffer.toString();
        }
      }
    
    public static class StreamLoader{

        public static InputStream loadUnknowFile(final String file){
          if(file.startsWith(":")){
            return loadResc(file.substring(1));
          }
          else{
            return loadFile(file);
          }
        }

        public static InputStream loadFile(final String file){
              try {
                return new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }

        public static InputStream loadResc(final String path){
          try{
            for(int idx=1;idx<=20;idx++){
              Class<?> clazz=Reflection.getCallerClass(idx);
              if(clazz!=null){
                if(clazz.getResource(path)!=null){
                  return clazz.getResourceAsStream(path);
                }
                ClassLoader loader=clazz.getClassLoader();
                // System.out.println(loader.getClass().getName());
                if(loader.getResource(path)!=null){
                  return loader.getResourceAsStream(path);
                }
              }
            }
          }
          catch(Exception ex){}
          return null;
        }
      }
}
