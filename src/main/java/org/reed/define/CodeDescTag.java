/**
 * base/org.reed.define/DescTag.java
 */
package org.reed.define;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @author chenxiwen
 * @date 2017年8月16日上午10:39:29
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CodeDescTag {
    String desc() default "未知编码";
    
    String[] append() default {};
}
