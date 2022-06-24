/**
 * base/org.reed.utils/ArrayUtil.java
 */
package org.reed.utils;

import org.reed.exceptions.ParamTypeException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author chenxiwen
 * @date 2017年8月30日下午4:02:36
 */
public final class ArrayUtil<E> {

    public static <E> boolean isEmpty(E e){
        if(e == null){
            return false;
        }
        if(e.getClass().isArray()){
            if(e instanceof int[]){
                int[] iarr = (int[])e;
                return iarr.length == 0;
            }else if(e instanceof long[]){
                long[] larr = (long[])e;
                return larr.length == 0;
            }else if(e instanceof char[]){
                char[] carr = (char[])e;
                return carr.length == 0;
            }else if(e instanceof byte[]){
                byte[] barr = (byte[])e;
                return barr.length == 0;
            }else if(e instanceof short[]){
                short[] sarr = (short[])e;
                return sarr.length == 0;
            }else if(e instanceof boolean[]){
                boolean[] barr = (boolean[])e;
                return barr.length == 0;
            }else if(e instanceof double[]){
                double[] darr = (double[])e;
                return darr.length == 0;
            }else if(e instanceof float[]){
                float[] farr = (float[])e;
                return farr.length == 0;
            }else {
                E[] _e = (E[]) e;
                return _e.length == 0;
            }
        }
        return false;
    }

//    public static <E> boolean isEmpty(E[] arr){
//        return arr == null || arr.length==0;
//    }

    public static <E> List<E> merge(E[]... e){
        List<E> list = new ArrayList<E>();
        for(E[] arr : e){
            for(E element : arr){
                list.add(element);
            }
        }
        return list;
    }

    public static <E> E mergePro(E... e) throws ParamTypeException{
        int len = 0;
        for (E e1 : e) {
            assertArray(e1);
            len+=Array.getLength(e1);
        }
        Class clazz = e.getClass().getComponentType();
        E result;
        int idx = 0;
        if(clazz == int[].class){
            result = (E) new int[len];
            for (E e1 : e) {
                int[] iarr = (int[])e1;
                for (int i : iarr) {
//                    Array.setInt(result, idx++, i);
                    ((int[])result)[idx++] = i;
                }
            }
        }else if(clazz == long[].class){
            result = (E) new long[len];
            for (E e1 : e) {
                long[] larr = (long[])e1;
                for (long l : larr) {
                    ((long[])result)[idx++] = l;
                }
            }
        }else if(clazz == short[].class){
            result = (E) new short[len];
            for (E e1 : e) {
                short[] sarr = (short[])e1;
                for (short s : sarr) {
                    ((short[])result)[idx++] = s;
                }
            }
        }else if(clazz == double[].class){
            result = (E) new double[len];
            for (E e1 : e) {
                double[] darr = (double[])e1;
                for (double d : darr) {
                    ((double[])result)[idx++] = d;
                }
            }
        }else if(clazz == float[].class){
            result = (E) new float[len];
            for (E e1 : e) {
                float[] farr = (float[])e1;
                for (float f : farr) {
                    ((float[])result)[idx++] = f;
                }
            }
        }else if(clazz == char[].class){
            result = (E) new char[len];
            for (E e1 : e) {
                char[] carr = (char[])e1;
                for (char c : carr) {
                    ((char[])result)[idx++] = c;
                }
            }
        }else if(clazz == byte[].class){
            result = (E) new byte[len];
            for (E e1 : e) {
                byte[] barr = (byte[])e1;
                for (byte b : barr) {
                    ((byte[])result)[idx++] = b;
                }
            }
        }else if(clazz == boolean[].class){
            result = (E) new boolean[len];
            for (E e1 : e) {
                boolean[] barr = (boolean[])e1;
                for (boolean b : barr) {
                    ((boolean[])result)[idx++] = b;
                }
            }
        }else{
            result = (E)Array.newInstance(clazz.getComponentType(), len);
            for (E e1 : e) {
                E[] earr = (E[])e1;
                for (E e2 : earr) {
                    ((E[])result)[idx++] = e2;
                }
            }
        }

        return result;
    }

    public static <E> boolean contains(E[] array, E element){
        for(E e : array){
            if(e.equals(element)){
                return true;
            }
        }
        return false;
    }


    public static <E> void swap(E arr, int i, int j) throws ParamTypeException{
        assertArray(arr);
        if(arr instanceof int[]){
            int[] iarr = (int[])arr;
            if(i>=iarr.length || j>=iarr.length){
                throw new ParamTypeException("Array["+arr.getClass().getName()+"] length is "+iarr.length+", but swap index is["+i+", "+j+"]");
            }
            int tmp = iarr[i];
            iarr[i] = iarr[j];
            iarr[j] = tmp;
        }else if(arr instanceof long[]){
            long[] larr = (long[])arr;
            if(i>=larr.length || j>=larr.length){
                throw new ParamTypeException("Array["+arr.getClass().getName()+"] length is "+larr.length+", but swap index is["+i+", "+j+"]");
            }
            long tmp = larr[i];
            larr[i] = larr[j];
            larr[j] = tmp;
        }else if(arr instanceof double[]){
            double[] darr = (double[])arr;
            if(i>=darr.length || j>=darr.length){
                throw new ParamTypeException("Array["+arr.getClass().getName()+"] length is "+darr.length+", but swap index is["+i+", "+j+"]");
            }
            double tmp = darr[i];
            darr[i] = darr[j];
            darr[j] = tmp;
        }else if(arr instanceof float[]){
            float[] farr = (float[])arr;
            if(i>=farr.length || j>=farr.length){
                throw new ParamTypeException("Array["+arr.getClass().getName()+"] length is "+farr.length+", but swap index is["+i+", "+j+"]");
            }
            float tmp = farr[i];
            farr[i] = farr[j];
            farr[j] = tmp;
        }else if(arr instanceof char[]){
            char[] carr = (char[])arr;
            if(i>=carr.length || j>=carr.length){
                throw new ParamTypeException("Array["+arr.getClass().getName()+"] length is "+carr.length+", but swap index is["+i+", "+j+"]");
            }
            char tmp = carr[i];
            carr[i] = carr[j];
            carr[j] = tmp;
        }else if(arr instanceof byte[]){
            byte[] barr = (byte[])arr;
            if(i>=barr.length || j>=barr.length){
                throw new ParamTypeException("Array["+arr.getClass().getName()+"] length is "+barr.length+", but swap index is["+i+", "+j+"]");
            }
            byte tmp = barr[i];
            barr[i] = barr[j];
            barr[j] = tmp;
        }else if(arr instanceof short[]){
            short[] sarr = (short[])arr;
            if(i>=sarr.length || j>=sarr.length){
                throw new ParamTypeException("Array["+arr.getClass().getName()+"] length is "+sarr.length+", but swap index is["+i+", "+j+"]");
            }
            short tmp = sarr[i];
            sarr[i] = sarr[j];
            sarr[j] = tmp;
        }else if(arr instanceof boolean[]){
            boolean[] barr = (boolean[])arr;
            if(i>=barr.length || j>=barr.length){
                throw new ParamTypeException("Array["+arr.getClass().getName()+"] length is "+barr.length+", but swap index is["+i+", "+j+"]");
            }
            boolean tmp = barr[i];
            barr[i] = barr[j];
            barr[j] = tmp;
        }else{
            E[] earr = (E[])arr;
            if(i>=earr.length || j>=earr.length){
                throw new ParamTypeException("Array["+arr.getClass().getName()+"] length is "+earr.length+", but swap index is["+i+", "+j+"]");
            }
            E tmp = earr[i];
            earr[i] = earr[j];
            earr[j] = tmp;
        }
    }

    public static <E> E clone(E arr) throws ParamTypeException{
        assertArray(arr);
        if(arr instanceof int[]){
            int[] iarr = ((int[])arr).clone();
            return (E)iarr;
        }else if(arr instanceof long[]){
            long[] larr = ((long[])arr).clone();
            return (E)larr;
        }else if(arr instanceof char[]){
            char[] carr = ((char[])arr).clone();
            return (E)carr;
        }else if(arr instanceof byte[]){
            byte[] barr = ((byte[])arr).clone();
            return (E)barr;
        }else if(arr instanceof short[]){
            short[] sarr = ((short[])arr).clone();
            return (E)sarr;
        }else if(arr instanceof boolean[]){
            boolean[] barr = ((boolean[])arr).clone();
            return (E)barr;
        }else if(arr instanceof double[]){
            double[] darr = ((double[])arr).clone();
            return (E)darr;
        }else if(arr instanceof float[]){
            float[] farr = ((float[])arr).clone();
            return (E)farr;
        }else {
            E[] earr = (E[])arr;
            return (E)earr.clone();
        }
    }

    public static <E> E reset(E array) throws ParamTypeException {
        assertArray(array);
        if(isEmpty(array)){
            return array;
        }
        E arr = clone(array);
        Random random = new Random();
        if(arr instanceof int[]){
            int[] iarr = (int[])arr;
            for(int i=iarr.length;i>0;i--){
                int r = random.nextInt(i);
                swap(iarr, r, i-1);
            }
        }else if(arr instanceof long[]){
            long[] larr = (long[])arr;
            for(int i=larr.length;i>0;i--){
                int r = random.nextInt(i);
                swap(larr, r, i-1);
            }
        }else if(arr instanceof char[]){
            char[] carr = (char[])arr;
            for(int i=carr.length;i>0;i--){
                int r = random.nextInt(i);
                swap(carr, r, i-1);
            }
        }else if(arr instanceof byte[]){
            byte[] barr = (byte[])arr;
            for(int i=barr.length;i>0;i--){
                int r = random.nextInt(i);
                swap(barr, r, i-1);
            }
        }else if(arr instanceof short[]){
            short[] sarr = (short[])arr;
            for(int i=sarr.length;i>0;i--){
                int r = random.nextInt(i);
                swap(sarr, r, i-1);
            }
        }else if(arr instanceof boolean[]){
            boolean[] barr = (boolean[])arr;
            for(int i=barr.length;i>0;i--){
                int r = random.nextInt(i);
                swap(barr, r, i-1);
            }
        }else if(arr instanceof double[]){
            double[] darr = (double[])arr;
            for(int i=darr.length;i>0;i--){
                int r = random.nextInt(i);
                swap(darr, r, i-1);
            }
        }else if(arr instanceof float[]){
            float[] farr = (float[])arr;
            for(int i=farr.length;i>0;i--){
                int r = random.nextInt(i);
                swap(farr, r, i-1);
            }
        }else {
            E[] earr = (E[]) arr;
            for(int i=earr.length;i>0;i--){
                int r = random.nextInt(i);
                swap(earr, r, i-1);
            }
        }
        return arr;
    }

    private static <E> void assertArray(E arr) throws ParamTypeException{
        if(!arr.getClass().isArray()){
            throw new ParamTypeException(arr.getClass().getName()+" is not supported, only array accept!");
        }
    }

    public static <E> String toString(E arr) throws ParamTypeException{
        assertArray(arr);
        StringBuffer stringBuffer = new StringBuffer("[");
        if(arr instanceof int[]){
            int[] iarr = (int[])arr;
            for(int i=0;i<iarr.length;i++){
                stringBuffer.append(iarr[i]);
                stringBuffer.append(", ");
            }
        }else if(arr instanceof long[]){
            long[] larr = (long[])arr;
            for(int i=0;i<larr.length;i++){
                stringBuffer.append(larr[i]);
                stringBuffer.append(", ");
            }
        }else if(arr instanceof char[]){
            char[] carr = (char[])arr;
            for(int i=0;i<carr.length;i++){
                stringBuffer.append(carr[i]);
                stringBuffer.append(", ");
            }
        }else if(arr instanceof byte[]){
            byte[] barr = (byte[])arr;
            for(int i=0;i<barr.length;i++){
                stringBuffer.append(barr[i]);
                stringBuffer.append(", ");
            }
        }else if(arr instanceof short[]){
            short[] sarr = (short[])arr;
            for(int i=0;i<sarr.length;i++){
                stringBuffer.append(sarr[i]);
                stringBuffer.append(", ");
            }
        }else if(arr instanceof boolean[]){
            boolean[] barr = (boolean[])arr;
            for(int i=0;i<barr.length;i++){
                stringBuffer.append(barr[i]);
                stringBuffer.append(", ");
            }
        }else if(arr instanceof double[]){
            double[] darr = (double[])arr;
            for(int i=0;i<darr.length;i++){
                stringBuffer.append(darr[i]);
                stringBuffer.append(", ");
            }
        }else if(arr instanceof float[]){
            float[] farr = (float[])arr;
            for(int i=0;i<farr.length;i++){
                stringBuffer.append(farr[i]);
                stringBuffer.append(", ");
            }
        }else {
            E[] earr = (E[])arr;
            for(int i=0;i<earr.length;i++){
                stringBuffer.append(earr[i]);
                stringBuffer.append(", ");
            }
        }
        String result = stringBuffer.toString();
        if(result.endsWith(", ")){
            result = result.substring(0, result.lastIndexOf(", "));
        }
        return result+"]";
    }
}
