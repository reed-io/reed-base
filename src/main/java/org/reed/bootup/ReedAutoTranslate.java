/**
 * E5Projects @ org.reed.bootup/ReedAutoTranslate.java
 */
package org.reed.bootup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author chenxiwen
 * @createTime 2019年12月13日 上午9:25
 * @description
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReedAutoTranslate {
    boolean enable() default true;
//    String language() default "";
}
