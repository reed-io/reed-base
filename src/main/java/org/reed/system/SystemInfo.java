/**
 * base/com.reed.system/SystemInfo.java
 */
package org.reed.system;

import java.io.*;
import java.net.URL;

/**
 * @author chenxiwen
 * @date 2017年8月10日下午5:14:44
 */
@Deprecated
public class SystemInfo {
//    public static final String AGENT_DIR_MD5 = "6dc21be4e3ebb4fba9da3522816ccd3e";

//    static {
//        loadLib();
//    }

     public static final void loadLib() {
        try {
            String osname = System.getProperty("os.name");
            boolean islinux = osname.toLowerCase().indexOf("windows") == -1;
            boolean isMac = osname.toLowerCase().indexOf("mac") != -1;
            boolean is64bit = System.getProperty("sun.arch.data.model").equalsIgnoreCase("64");
            String libname = null;
            if (is64bit) {
                libname = File.separator + (islinux ? isMac ? "libsigar-universal64-macosx.dylib" : "libsigar-amd64-linux.so" : "sigar-amd64-winnt.dll");
            } else {
                libname = File.separator + (islinux ? isMac ? "libsigar-universal-macosx.dylib" : "libsigar-x86-linux.so" : "sigar-x86-winnt.dll");
            }
            String projhome = System.getProperty("user.home");
            if (!new File(projhome + libname).exists()) {
                String resc = "/org/reed/system/libs/";
                resc += islinux ? isMac ? "mac" : "linux" : "winnt";
                if (is64bit) {
                    resc = resc + "_64";
                }
                try {
                    System.out.println("resc="+resc);
                    URL url = SystemInfo.class.getResource(resc);
                    InputStream in = url.openStream();
                    OutputStream out = new FileOutputStream(projhome + libname);
                    int count = 0;
                    byte[] buffer = new byte[256];
                    while ((count = in.read(buffer)) > 0) {
                        out.write(buffer, 0, count);
                    }
                    in.close();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.setProperty("java.library.path", projhome);
            System.load(projhome + libname);

//            //apm-agent
//            System.out.println("checking apm-agent files...");
//            String agentDir = "apm-agent";
////            String projhome = System.getProperty("user.home");
//            String agentDirFullPath = projhome+File.separator+agentDir;
//            if(FileUtil.exists(agentDirFullPath)){
//                if(FileUtil.isFolder(agentDirFullPath)){
//                    String folderMd5 = FileUtil.getDirMd5(agentDirFullPath, "logs");
//                    if(!AGENT_DIR_MD5.equalsIgnoreCase(folderMd5)){
//                        System.out.println("found old or invalidate apm-agent, will override...");
//                        FileUtil.rmdir(agentDirFullPath);
//                        releaseAgents(agentDirFullPath+".zip");
//                    }else{
//                        System.out.println("apm-agents already exist!");
//                    }
//                }else{
//                    System.out.println("did not found apm-agent folder, will clear the ENV. then release and deploy...");
//                    FileUtil.rmfile(agentDirFullPath);
//                    releaseAgents(agentDirFullPath+".zip");
//                }
//
//            }else{
//                System.out.println("did not found apm-agent folder, release and deploy...");
//                releaseAgents(agentDirFullPath+".zip");
//            }

        } catch (Throwable th) {
            th.printStackTrace();
            System.err.println("Unable to load SystemEx: " + th);
        }
    }


//    private static void releaseAgents(String path){
//        String agentPkgPath = "/org/reed/system/libs/apm-agent.zip";
//        try {
//            System.out.println("agentPkgPath="+agentPkgPath);
//            URL url = SystemInfo.class.getResource(agentPkgPath);
//            InputStream in = url.openStream();
//            OutputStream out = new FileOutputStream(path);
//            int count = 0;
//            byte[] buffer = new byte[256];
//            while ((count = in.read(buffer)) > 0) {
//                out.write(buffer, 0, count);
//            }
//            in.close();
//            out.close();
//            System.out.println("released apm-agent.zip to "+path);
//        } catch (IOException e) {
//            System.err.println("Exception when release apm-agent.zip");
//            e.printStackTrace();
//        }
//
//        //unzip agent pkg
//        try {
//            releaseZipToFile(path, System.getProperty("user.home"));
//        } catch (IOException e) {
//            System.err.println("Exception when unzip apm agents: "+e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//
//    public static void releaseZipToFile(String sourceZip, String outFileName)throws IOException{
//        ZipFile zfile=new ZipFile(sourceZip);
//        System.out.println(zfile.getName());
//        Enumeration zList=zfile.entries();
//        ZipEntry ze=null;
//        byte[] buf=new byte[1024];
//        while(zList.hasMoreElements()){
//            //从ZipFile中得到一个ZipEntry
//            ze=(ZipEntry)zList.nextElement();
//            if(ze.isDirectory()){
//                continue;
//            }
//            //以ZipEntry为参数得到一个InputStream，并写到OutputStream中
//            OutputStream os = new BufferedOutputStream(new FileOutputStream(getRealFileName(outFileName, ze.getName())));
//            InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
//            int readLen=0;
//            while ((readLen=is.read(buf, 0, 1024))!=-1) {
//                os.write(buf, 0, readLen);
//            }
//            is.close();
//            os.close();
//            System.out.println("Extracted: "+ze.getName());
//        }
//        zfile.close();
//    }
//    /**
//     * 给定根目录，返回一个相对路径所对应的实际文件名.
//     *
//     * @param baseDir
//     *            指定根目录
//     * @param absFileName
//     *            相对路径名，来自于ZipEntry中的name
//     * @return java.io.File 实际的文件
//     */
//    private static File getRealFileName(String baseDir, String absFileName) {
//        String[] dirs = absFileName.split("/");
//        //System.out.println(dirs.length);
//        File ret = new File(baseDir);
//        //System.out.println(ret);
//        if (dirs.length > 1) {
//            for (int i = 0; i < dirs.length - 1; i++) {
//                ret = new File(ret, dirs[i]);
//            }
//        }
//        if (!ret.exists()) {
//            ret.mkdirs();
//        }
//        ret = new File(ret, dirs[dirs.length - 1]);
//        return ret;
//    }


    public static void main(String[] args){
//        SystemInfo.loadLib();
    }
}
