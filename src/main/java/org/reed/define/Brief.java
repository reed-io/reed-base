package org.reed.define;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author chenxiwen
 * @date 2018年9月24日下午18:39:29
 */
@Target(ElementType.FIELD)
@Retention(RUNTIME)
public @interface Brief {
    /**
     * 不用属性原有名字的话可以设置别名
     * @return
     */
    String alias() default "";
}
