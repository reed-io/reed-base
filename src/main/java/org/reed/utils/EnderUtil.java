/**
 * RedisClient/com.reed.redisclient.utils/EnderUtils.java
 */
package org.reed.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * @author chenxiwen
 * @date 2017年7月25日下午4:55:25
 */
public final class EnderUtil {

    public static String getFileName(){
        return Thread.currentThread().getStackTrace()[2].getFileName();
    }
    
    public static String getMethodName(){
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }
    
    public static int getLineNumber(){
        return Thread.currentThread().getStackTrace()[2].getLineNumber();
    }
    
    public static String getDevInfo(){
        return TimeUtil.nowDateTime()+" ["+Thread.currentThread().getName()+"] <TRACE> {"+Thread.currentThread().getStackTrace()[2].getFileName()+" - "+Thread.currentThread().getStackTrace()[2].getMethodName()+" - "+Thread.currentThread().getStackTrace()[2].getLineNumber()+"}";
    }
    
    public static String devInfo(){
        return "["+Thread.currentThread().getStackTrace()[2].getFileName()+" - "+Thread.currentThread().getStackTrace()[2].getMethodName()+" - "+Thread.currentThread().getStackTrace()[2].getLineNumber()+"]";
    }
    
    public static void block(long millseconds){
        try {
            Thread.sleep(millseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public static <T> Method getMethodByName(Class<T> c, String methodName, Class<?>... paramTypes) throws NoSuchMethodException, SecurityException{
        return c.getDeclaredMethod(methodName, paramTypes);
    }
    
    public static Field getFieldByName(String name,Class<?> clazz) throws NoSuchFieldException, SecurityException{
        return clazz.getDeclaredField(name);
      }
    
    public static <E> boolean isEmpty(Collection<E> c){
        return c == null || c.size() == 0;
    }
    
    
    public static void main(String[] args){
        System.out.println(getLineNumber());
        System.out.println(getMethodName());
        System.out.println(getDevInfo());
    }
}
