/**
 * base/org.reed.utils/JavaUtil.java
 */
package org.reed.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;

/**
 * @author chenxiwen
 * @date 2017年8月23日下午4:27:03
 */
public final class JavaUtil {
    /**
     * 获取当前java环境的版本号
     * @return like 1.8.0_131
     */
    public static String getJavaVersion(){
        return System.getProperty("java.version");  //like 1.8.0_131
    }
    
   /**
    * 获取当前java环境的CPU架构版本
    * @return like 64
    */
    public static String getJavaArch(){
        return System.getProperty("sun.arch.data.model"); //like  64
    }
    /**
     * 判断当前java环境是否为1.5版本
     * @return
     */
    public static boolean isJava5(){
        return getJavaVersion().contains("1.5.");
    }
    /**
     * 判断当前java环境是否为1.6版本
     * @return
     */
    public static boolean isJava6(){
        return getJavaVersion().contains("1.6.");
    }
    /**
     * 判断当前java环境是否为1.7版本
     * @return
     */
    public static boolean isJava7(){
        return getJavaVersion().contains("1.7.");
    }
    /**
     * 判断当前java环境是否为1.8版本
     * @return
     */
    public static boolean isJava8(){
        return getJavaVersion().contains("1.8.");
    }
    
    
    public static void main(String[] args){
        System.out.println(JavaUtil.getJavaVersion());
        System.out.println(JavaUtil.getJavaArch());
    }

    public static String[] getModifiers(Class<?> clz){
        return Modifier.toString(clz.getModifiers()).split("");
    }

    public static boolean isPublic(Class<?> clz){
        return (clz.getModifiers() & Modifier.PUBLIC) != 0;
    }

    public static boolean isPublic(Method method){
        return (method.getModifiers() & Modifier.PUBLIC) != 0;
    }

    public static boolean isPublic(Parameter param){
        return (param.getModifiers() & Modifier.PUBLIC) != 0;
    }

    public static boolean isPrivate(Class<?> clz){
        return (clz.getModifiers() & Modifier.PRIVATE) != 0;
    }

    public static boolean isPrivate(Method method){
        return (method.getModifiers() & Modifier.PRIVATE) != 0;
    }

    public static boolean isPrivate(Parameter param){
        return (param.getModifiers() & Modifier.PRIVATE) != 0;
    }

    public static boolean isProtected(Class<?> clz){
        return (clz.getModifiers() & Modifier.PROTECTED) != 0;
    }

    public static boolean isProtected(Method method){
        return (method.getModifiers() & Modifier.PROTECTED) != 0;
    }

    public static boolean isProtected(Parameter param){
        return (param.getModifiers() & Modifier.PROTECTED) != 0;
    }

    public static boolean isStatic(Class<?> clz){
        return (clz.getModifiers() & Modifier.STATIC) != 0;
    }

    public static boolean isStatic(Method method){
        return (method.getModifiers() & Modifier.STATIC) != 0;
    }

    public static boolean isStatic(Parameter param){
        return (param.getModifiers() & Modifier.STATIC) != 0;
    }

    public static boolean isFinal(Class<?> clz){
        return (clz.getModifiers() & Modifier.FINAL) != 0;
    }

    public static boolean isFinal(Method method){
        return (method.getModifiers() & Modifier.FINAL) != 0;
    }

    public static boolean isFinal(Parameter param){
        return (param.getModifiers() & Modifier.FINAL) != 0;
    }

    public static boolean isSynchronized(Class<?> clz){
        return (clz.getModifiers() & Modifier.SYNCHRONIZED) != 0;
    }

    public static boolean isSynchronized(Method method){
        return (method.getModifiers() & Modifier.SYNCHRONIZED) != 0;
    }

    public static boolean isSynchronized(Parameter param){
        return (param.getModifiers() & Modifier.SYNCHRONIZED) != 0;
    }

    public static boolean isVolatile(Class<?> clz){
        return (clz.getModifiers() & Modifier.VOLATILE) != 0;
    }

    public static boolean isVolatile(Method method){
        return (method.getModifiers() & Modifier.VOLATILE) != 0;
    }

    public static boolean isVolatile(Parameter param){
        return (param.getModifiers() & Modifier.VOLATILE) != 0;
    }

    public static boolean isTransient(Class<?> clz){
        return (clz.getModifiers() & Modifier.TRANSIENT) != 0;
    }

    public static boolean isTransient(Method method){
        return (method.getModifiers() & Modifier.TRANSIENT) != 0;
    }

    public static boolean isTransient(Parameter param){
        return (param.getModifiers() & Modifier.TRANSIENT) != 0;
    }

    public static boolean isNative(Class<?> clz){
        return (clz.getModifiers() & Modifier.NATIVE) != 0;
    }

    public static boolean isNative(Method method){
        return (method.getModifiers() & Modifier.NATIVE) != 0;
    }

    public static boolean isNative(Parameter param){
        return (param.getModifiers() & Modifier.NATIVE) != 0;
    }

    public static boolean isInterface(Class<?> clz){
        return (clz.getModifiers() & Modifier.INTERFACE) != 0;
    }

    public static boolean isInterface(Method method){
        return (method.getModifiers() & Modifier.INTERFACE) != 0;
    }

    public static boolean isInterface(Parameter param){
        return (param.getModifiers() & Modifier.INTERFACE) != 0;
    }

    public static boolean isAbstract(Class<?> clz){
        return (clz.getModifiers() & Modifier.ABSTRACT) != 0;
    }

    public static boolean isAbstract(Method method){
        return (method.getModifiers() & Modifier.ABSTRACT) != 0;
    }

    public static boolean isAbstract(Parameter param){
        return (param.getModifiers() & Modifier.ABSTRACT) != 0;
    }

    public static boolean isStrict(Class<?> clz){
        return (clz.getModifiers() & Modifier.STRICT) != 0;
    }

    public static boolean isStrict(Method method){
        return (method.getModifiers() & Modifier.STRICT) != 0;
    }

    public static boolean isStrict(Parameter param){
        return (param.getModifiers() & Modifier.STRICT) != 0;
    }

    public static int pid(){
        return -1;
    }

}
