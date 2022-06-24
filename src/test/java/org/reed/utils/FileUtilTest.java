package org.reed.utils;

import java.io.File;

public class FileUtilTest {
    public static void main(String[] args){
//        File f = new File("/Users/ender/libsigar-amd64-linux.so");
//        byte[] result = FileUtil.md5_digest(f);
//        BASE64Encoder encoder = new BASE64Encoder();
//        System.out.println(encoder.encode(result));
//        System.out.println(result.length);
//        System.out.println(System.currentTimeMillis());

        long t1 = System.currentTimeMillis();
        System.out.println(FileUtil.getDirMd5("C:\\Users\\ender\\test", "logs"));
        long t2 = System.currentTimeMillis();
        System.out.println(t2-t1);
        System.out.println(FileUtil.getDirMd5("C:\\Users\\ender\\apm-agent", "logs"));
        long t3 = System.currentTimeMillis();
        System.out.println(t3-t2);

        System.out.println(FileUtil.md5(new File("C:\\Users\\ender\\apm-agent\\skywalking-agent-ender.jar")));
    }
}
