/**
 * E5Projects @ org.reed.utils/JavaUtilTest.java
 */
package org.reed.utils;

import java.util.Arrays;

/**
 * @author chenxiwen
 * @createTime 2020年01月08日 上午11:13
 * @description
 */
public class JavaUtilTest {
    public static void main(String[] args){
        System.out.println(Arrays.toString(JavaUtil.getModifiers(ModifierTestClass1.class)));
    }

    static final class ModifierTestClass1{}
}
