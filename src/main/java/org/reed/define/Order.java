/**
 * E5Projects @ org.reed.define/Order.java
 */
package org.reed.define;

import java.lang.annotation.*;

/**
 * @author chenxiwen
 * @createTime 2019年12月08日 下午6:45
 * @description
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Documented
public @interface Order {
    int value() default Integer.MAX_VALUE;
}
