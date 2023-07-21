/**
 * base/com.reed/ReedContext.java
 */
package org.reed.system;

import org.reed.utils.DESUtil;
import org.reed.utils.MapUtil;
import org.reed.utils.StringUtil;
import org.reed.utils.TimeUtil;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author chenxiwen
 * @date 2017年8月10日下午4:50:47
 */
public final class ReedContext {
    
    /**
     * 目录分割符号
     */
    public static final String SepDirs=File.separator;
    /**
     * 类路径分割符号
     */
    public static final String SepPath=File.pathSeparator;
    /**
     * 显示行字符
     */
    public static final String SepLine=System.getProperty("line.separator");

    public static ReedContext instance;
    
    private final Hashtable<String,String> sysvals;

    private final Hashtable<String, Object> appvals;

    private static String pid;
    
    static{
        instance = new ReedContext();
    }

    private ReedContext(){
        List<String> opts=new ArrayList<String>();
        opts.add("Xmode");
        opts.add("Xproj.name");
        opts.add("Xproj_name");
        opts.add("XProjectName");
        opts.add("Xrfenv");  //用来决定当前跑在什么环境里  如：sit/uat/prod
        opts.add("os.name");
        opts.add("user.dir");
        opts.add("user.name");
        opts.add("user.home");
        opts.add("java.home");
        opts.add("java.vm.name");
        opts.add("java.version");
        opts.add("java.ext.dirs");
        opts.add("file.separator");
        opts.add("path.separator");
        opts.add("java.vm.version");
        opts.add("java.class.path");
        opts.add("java.library.path");
        opts.add("java.class.version");
        sysvals=new Hashtable<String,String>();
        for(Object prop:System.getProperties().keySet()){
            if(opts.contains(prop)){
              sysvals.put(prop.toString(),System.getProperty(prop.toString()));
            }
            if(prop.toString().startsWith("X")){
              sysvals.put(prop.toString().substring(1),System.getProperty(prop.toString()));
            }
        }

        putOperatingSystemInfo(sysvals);

//        netWorkInit(null);
//
//        SystemInfo.loadLib();
////        Sigar sigar = new Sigar();
//        EnderOperatingSystem os = EnderOperatingSystem.getInstance();
//        try {
////            CpuInfo[] cpuInfoArr = sigar.getCpuInfoList();
////            sysvals.put("cpu.cores", cpuInfoArr.length+"");
//            sysvals.put("cpu.cores", os.getCpu().getCoreCount()+"");
//            sysvals.put("cpu.threads", os.getCpu().getThreadCount()+"");
//            sysvals.put("cpu", os.getCpu().toString());
////            for(int i=0;i<cpuInfoArr.length;i++){
//////                System.out.println(cpuInfoArr[i].toString());
////                sysvals.put("cpu.cores."+i, cpuInfoArr[i].toString());
////            }
////            Mem mem = sigar.getMem();
////            sysvals.put("memery", mem.getTotal()+"bytes");
//            sysvals.put("memory", os.getMemory().getTotalMem()+"");
////            Swap swap = sigar.getSwap();
////            sysvals.put("swap", swap.getTotal()+"bytes");
//            sysvals.put("swap", os.getMemory().getSwapTotal()+"");
////            OperatingSystem osInfo = OperatingSystem.getInstance();
////            sysvals.put("os.info", osInfo.toString());
//            sysvals.put("os.info",os.toString());
////            FileSystem[] fileSystemArr = sigar.getFileSystemList();
////            sysvals.put("disk.size", fileSystemArr.length+"");
//            sysvals.put("disk.size", os.getDiskSpace()+"");
////            for(int i=0;i<fileSystemArr.length;i++){
////                sysvals.put("disk."+i, MapUtil.toString(fileSystemArr[i].toMap(), ", ", "="));
////            }
//            for (int i = 0; i < os.getFileSystems().size(); i++) {
//                sysvals.put("disk."+i)
//            }
//        } catch (SigarException e) {
//            e.printStackTrace();
//        }

        appvals = new Hashtable<String, Object>();

        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        String name = runtimeMXBean.getName();
        if(StringUtil.isEmpty(name)){
            throw new RuntimeException("Can not read JVM information, shutting down!");
        }
        if(!name.contains("@") || name.split("@").length != 2){
            throw new RuntimeException("runtime information illegal: "+name);
        }
        pid = name.split("@")[0];
    }

    private void putOperatingSystemInfo(final Hashtable<String, String> sysvals){
        EnderOperatingSystem os = EnderOperatingSystem.getInstance();

        sysvals.put("os.info",os.toString());
        sysvals.put("cpu", os.getCpu().toString());
        sysvals.put("cpu.cores", os.getCpu().getCoreCount()+"");
        sysvals.put("cpu.threads", os.getCpu().getThreadCount()+"");

        sysvals.put("memory", os.getMemory().toString());
        sysvals.put("memory.total", os.getMemory().getTotalMem()+"");
        sysvals.put("swap.total", os.getMemory().getSwapTotal()+"");

        sysvals.put("disk.size", os.getDiskSpace()+"");
        for (int i = 0; i < os.getFileSystems().size(); i++) {
            sysvals.put("disk."+i, os.getFileSystems().get(i).toString());
        }


        sysvals.put("os.network.main", os.getMainNetworkConfig().toString());
        sysvals.put("os.ip", os.getMainNetworkConfig().getIpv4());
        sysvals.put("os.mac", os.getMainNetworkConfig().getMacAddress());
    }
    
    
    static public boolean islinux(){
        return instance.sysvals.get("os.name").toLowerCase().indexOf("linux")>=0;
      }
    static public boolean isMac(){
        return instance.sysvals.get("os.name").toLowerCase().indexOf("mac")>=0;
    }
    static public boolean isWdindows(){
        return instance.sysvals.get("os.name").toLowerCase().indexOf("windows")>=0;
    }

    /**
     * put os.ip and os.mac into system value(a hashtable defined within ReedContext.java)
     * @param ipaddr
     */
    @Deprecated
    public void netWorkInit(String ipaddr){
        try{
          boolean islinux=sysvals.get("os.name").toLowerCase().indexOf("windows")==-1;
          if(islinux){
            String ipstr="",macstr="";
            boolean bFindIP=false;
            Enumeration<NetworkInterface> netInterfaces= NetworkInterface.getNetworkInterfaces();
            while(netInterfaces.hasMoreElements()){
              if(bFindIP){
                break;
              }
              NetworkInterface ni= netInterfaces.nextElement();
              if(ni.getName().startsWith("eth")||ni.getName().startsWith("em")||ni.getName().startsWith("bond") || ni.getName().startsWith("en")){
                Enumeration<InetAddress> ips=ni.getInetAddresses();
                while(ips.hasMoreElements()){
                  InetAddress ip= ips.nextElement();
                  if(ip.getHostAddress().indexOf(":")==-1){
                    // System.out.println(ip);
                    NetworkInterface netCard=NetworkInterface.getByInetAddress(ip);

                    byte[] mac=netCard.getHardwareAddress();
                    Formatter formatter=new Formatter();
                    for(int i=0;i<mac.length;i++){
                      macstr=formatter.format(Locale.getDefault(),"%02X%s",mac[i],(i<mac.length-1)?":":"").toString();
                    }
                    formatter.close();
                    ipstr=ip.getHostAddress();
                    if((ipaddr==null)||(ipaddr!=null&&ipaddr.equals(ipstr))){
                      bFindIP=true;
                    }
                    break;
                  }
                }
              }
            }
            sysvals.put("os.ip",ipstr);
            sysvals.put("os.mac",macstr.toUpperCase());
          }
          else{
            InetAddress address=InetAddress.getLocalHost();
            NetworkInterface ni=NetworkInterface.getByInetAddress(address);
            ni.getInetAddresses().nextElement().getAddress();
            byte[] mac=ni.getHardwareAddress();
            String sMAC="";
            Formatter formatter=new Formatter();
            for(int i=0;i<mac.length;i++){
              sMAC=formatter.format(Locale.getDefault(),"%02X%s",mac[i],(i<mac.length-1)?":":"").toString();
            }
            formatter.close();
            sysvals.put("os.ip",address.getHostAddress());
            sysvals.put("os.mac",sMAC.toUpperCase());
          }

        }
        catch(Throwable th){
          th.printStackTrace();
        }
      }
      
    
    /**
     * 更新属性变量
     */
    static public void update(String valid,Object value){
      if(value.getClass().isPrimitive()){
        instance.sysvals.put(valid,value.toString());
      }
      else if(value instanceof Date){
        instance.sysvals.put(valid,TimeUtil.getDateTime((Date)value));
      }
      else if(value instanceof Timestamp){
        instance.sysvals.put(valid,TimeUtil.getDateTime((Timestamp)value));
      }
      else{
        instance.sysvals.put(valid,value.toString());
      }
    }

    /**
     * 获取Int属性变量
     */
    static public int getInt(String valid){
      try{
        if(instance.sysvals.containsKey(valid)){
          return Integer.valueOf(instance.sysvals.get(valid));
        }
        return 0;
      }
      catch(NumberFormatException ex){
        return 0;
      }
    }

    /**
     * 获取Long属性变量
     */
    static public long getLong(String valid){
      try{
        if(instance.sysvals.containsKey(valid)){
          return Long.valueOf(instance.sysvals.get(valid));
        }
        return 0;
      }
      catch(Throwable ex){
        return 0;
      }
    }

    /**
     * 获取Double属性变量
     */
    static public double getDouble(String valid){
      try{
        if(instance.sysvals.containsKey(valid)){
          return Double.valueOf(instance.sysvals.get(valid));
        }
        return 0d;
      }
      catch(Exception ex){
        return 0;
      }
    }

    /**
     * 获取Boolean属性变量
     */
    static public boolean getBoolean(String valid){
      try{
        if(instance.sysvals.containsKey(valid)){
          return Boolean.valueOf(instance.sysvals.get(valid));
        }
        return false;
      }
      catch(NumberFormatException ex){
        return false;
      }
    }

    /**
     * 获取String属性变量
     */
    static public String getString(String valid,String defaut){
      if(instance.sysvals.containsKey(valid)){
          if(StringUtil.isContains(StringUtil.Reed_CIPHER, valid)){
              return StringUtil.decryptCiphertext(valid, DESUtil.DEFAULT_SECURITY_CODE);
          }
        return instance.sysvals.get(valid);
      }
      return defaut;
    }

    static public String getString(String valid){
      return getString(valid,null);
    }

    /**
     * 使用系统定义的属性值解析变量
     */
    static public String resolve(final String value){
      return resolve(value,null);
    }

    /**
     * 服务是否以DEBUG模式启动
     */
    static public boolean isDebug(){
      String mode=getString("mode","").toLowerCase();
      return mode.endsWith("develop")||mode.length()==0;
    }

    /**
     * 使用系统定义和用户定义的属性值解析变量
     */
    static public String resolve(String result,final Map<String,?> extprops){
      out_loop:while(result.indexOf("$")!=-1){
        if(extprops!=null){
          Iterator<String> itr=extprops.keySet().iterator();
          while(itr.hasNext()){
            String propkey=itr.next();
            if(result.indexOf("${"+propkey+"}")!=-1){
              result=StringUtil.replace(result,"${"+propkey+"}",extprops.get(propkey).toString());
              continue out_loop;
            }
          }
        }
        for(Enumeration<String> emkeys=instance.sysvals.keys();emkeys.hasMoreElements();){
          String propkey=emkeys.nextElement();
          if(result.indexOf("${"+propkey+"}")!=-1){
            result=StringUtil.replace(result,"${"+propkey+"}",instance.sysvals.get(propkey));
            continue out_loop;
          }
        }
        break;
      }
      return result;
    }

    static public Set<String> syspros(){
      return instance.sysvals.keySet();
    }

    public static String sysInfo(){
        return MapUtil.toString(instance.sysvals, ";<br>", ":");
    }


    public static Object setAppVal(final String key, final Object value){
        return instance.appvals.put(key, value);
    }

    public static Object getAppVal(final String key){
        return instance.appvals.get(key);
    }
    
//    public static void main(String[] args){
//        SystemInfo.loadLib();
//        Sigar sigar = new Sigar();
//        try {
//            CpuInfo[] cpuInfoArr = sigar.getCpuInfoList();
//            System.out.println(cpuInfoArr.length+"");
//            for(int i=0;i<cpuInfoArr.length;i++){
//                System.out.println(cpuInfoArr[i].toString());
//            }
//            Mem mem = sigar.getMem();
//            System.out.println(mem.getTotal());
//            Swap swap = sigar.getSwap();
//            System.out.println(swap.getTotal());
//            OperatingSystem OS = OperatingSystem.getInstance();
//            System.out.println(OS.toString());
//            FileSystem[] fileSystemArr = sigar.getFileSystemList();
//            for(FileSystem fs : fileSystemArr){
//                System.out.println(MapUtil.toString(fs.toMap(), ", ", "="));
//            }
//        } catch (SigarException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }

    public static String vmPID(){
        return pid;
    }
}
