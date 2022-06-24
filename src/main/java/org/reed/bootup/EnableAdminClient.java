/**
 * E5Projects @ org.reed.bootup/EnableAdminClient.java
 */
package org.reed.bootup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author chenxiwen
 * @createTime 2019年12月17日 下午3:53
 * @description
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableAdminClient {

    String username() default "";

    String password() default "";

    /**
     * if more than one server, use comma to split
     * @return
     */
    String server() default "";
}
