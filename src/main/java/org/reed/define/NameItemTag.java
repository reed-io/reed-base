/**
 * base/org.reed.define/NameItemTag.java
 */
package org.reed.define;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author chenxiwen
 * @date 2017年8月11日下午3:39:50
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NameItemTag{
  String[] namespace() default "DEFAULT";
}