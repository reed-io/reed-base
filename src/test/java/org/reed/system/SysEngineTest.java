/**
 * base/org.reed.system/SysEngineTest.java
 */
package org.reed.system;

/**
 * @author chenxiwen
 * @date 2017年8月16日下午2:23:14
 */
public class SysEngineTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
//        System.setProperty("java.class.path", "C:\\Develop\\Eclipse\\eclipse_workspace\\base\\target\\test-classes;"+System.getProperty("java.class.path"));
//        System.out.println(System.getProperty("java.class.path"));
//        Class<?> cl = SysEngine.findClassByName("org.reed.system.SystemInfo");
//        System.out.println(cl);
//        List<Class<?>> list = SysEngine.realizeClass(BaseErrorCode.class, null);
//        System.out.println("size="+list.size());
//        for(Class<?> clz : list){
//            System.out.println(clz);
//        }


        SysEngine.addJarPath("D:\\develop\\E5Projects\\service-demo\\target\\service-demo.jar");

    }

}
