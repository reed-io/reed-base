/**
 * E5Projects @ org.reed.define/ReedAutowired.java
 */
package org.reed.define;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author chenxiwen
 * @createTime 2019年12月23日 下午4:06
 * @description
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReedAutowired {
    Class<?> clazz() default Object.class;
}
