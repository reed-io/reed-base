/**
 * E5Projects @ org.reed.utils/ArrayUtilTest.java
 */
package org.reed.utils;

import org.reed.exceptions.ParamTypeException;

import java.lang.reflect.Array;

/**
 * @author chenxiwen
 * @createTime 2020年08月07日 下午5:14
 * @description
 */
public class ArrayUtilTest {
    public static void main(String[] args) {
        String [] a = new String[]{"1"};
        int[] b = new int[]{1};
        System.out.println(a.getClass().getName());
        System.out.println(a.getClass().isArray());
        String[] array = new String[]{"a"};
        String[] arr = new String[]{"b","c","d","e","f"};
        String c = "a";
        System.out.println(ArrayUtil.isEmpty(b));
        System.out.println(ArrayUtil.isEmpty(a));
//        System.out.println(ArrayUtil.merge(array, arr));
        System.out.println(ArrayUtil.contains(arr, c));


        int[] iarr = new int[]{1,2,3};
        try {
            ArrayUtil.swap(iarr, 0, 2);
            for (int i : iarr) {
                System.out.println(i);
            }
        } catch (ParamTypeException e) {
            e.printStackTrace();
        }

        System.out.println("-------------------------------");
        int[] iiarr = new int[]{1,2,3,4,5};
        try {
            int[] iiiarr = ArrayUtil.reset(iiarr);
            for (int i : iiiarr) {
                System.out.println(i);
            }
        } catch (ParamTypeException e) {
            e.printStackTrace();
        }

        System.out.println("-------------------------------");
        String[] sarr = new String[]{"a", "b","c","d","e"};
        try {
            String[] ssarr = ArrayUtil.reset(sarr);
            for (String i : ssarr) {
                System.out.println(i);
            }
            System.out.println(ArrayUtil.toString(sarr));
        } catch (ParamTypeException e) {
            e.printStackTrace();
        }

        try {
            System.out.println(ArrayUtil.toString(iiarr));
            System.out.println(ArrayUtil.toString(sarr));
            System.out.println(ArrayUtil.toString("a"));

        } catch (ParamTypeException e) {
            e.printStackTrace();
        }
        System.out.println("-------------------------------");


        Class clazz = iarr.getClass().getComponentType();
        System.out.println(clazz);
        System.out.println(clazz.isPrimitive());
        int[] earr = (int[]) Array.newInstance(clazz, Array.getLength(iarr));
        System.out.println(earr);


        try {
            int[] r = ArrayUtil.mergePro(iarr, iiarr);
            System.out.println(ArrayUtil.toString(r));

            String[] s = ArrayUtil.mergePro(arr, array);
            System.out.println(ArrayUtil.toString(s));
        } catch (ParamTypeException e) {
            e.printStackTrace();
        }
    }

}
