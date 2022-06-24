/**
 * E5Projects @ org.reed.system/VMTest.java
 */
package org.reed.system;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * @author chenxiwen
 * @createTime 2020年08月05日 下午3:52
 * @description
 */
public class VMTest {
    public static void main(String[] args){
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        String name = runtimeMXBean.getName();
        System.out.println(name);

        try {
            int a = System.in.read();
            System.out.println(a);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
