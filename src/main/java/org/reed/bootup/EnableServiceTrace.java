/**
 * E5Projects @ org.reed.bootup/EnableServiceTrace.java
 */
package org.reed.bootup;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Notice:if this feature still needed from 0.0.5-SNAPSHOT,
 * developers should add sleuth,zipkin,kafka(stream) dependencies manually,
 * framework had remove them for compressing the war packages
 * @author chenxiwen
 * @createTime 2019年08月20日 上午11:44
 * @description tell framework to response language trace in the header or structure
 * @deprecated use {@link EnableAPMAnalysis}
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Deprecated
public @interface EnableServiceTrace {
    String traceServer() default "";
    double persentage() default 1.0;
    Traceable.TraceType traceType() default Traceable.TraceType.KAFKA;
    String kafkaServer() default "";
    String traceTopic() default "zipkin";
}
