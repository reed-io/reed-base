/**
 * base/org.reed.define/MethodChain.java
 */
package org.reed.define;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.reed.event.ReedEvent;

@Retention(RUNTIME)
@Target(METHOD)
/**
 * @author chenxiwen
 * @date 2017年9月20日下午1:09:17
 */
public @interface MethodChain {
    int index() default 0;
    Class<? extends ReedEvent> event() default ReedEvent.class;
    boolean skip() default false;
}
