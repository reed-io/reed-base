/**
 * E5Projects @ org.reed.bootup/EnableAPMAnalysis.java
 */
package org.reed.bootup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author chenxiwen
 * @createTime 2020年08月11日 下午3:40
 * @description using Apache-Skywalking as impl, java-agent is not from official but compiled by customized sources
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableAPMAnalysis {
    String server() default "";
    APMLogType log() default APMLogType.STDOUT;
    String logLevel() default "INFO";

    enum APMLogType{
        FILE, STDOUT
    }
}
