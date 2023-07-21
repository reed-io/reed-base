/**
 * IdeaProject @ com.reed/Test.java
 */
package org.reed;

/**
 * @author chenxiwen
 * @createTime 2018年11月28日 下午4:34
 * @description
 */
public class Test {
    public static void main(String[] args) {
        String s = "file:/D:/develop/E5Projects/service-demo/target/service-demo.jar!/BOOT-INF/classes!/";

        System.out.println(s.endsWith(".jar!/BOOT-INF/classes!/"));

    }
}
