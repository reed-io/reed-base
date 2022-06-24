/**
 * E5Projects @ org.reed.bootup/ReedUnifiedConfig.java
 */
package org.reed.bootup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author chenxiwen
 * @createTime 2019年12月30日 上午10:39
 * @description
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReedUnifiedConfig {
    String server() default "";
    String namespace() default "";
    String group() default "";
    String fileExtension() default "yml";
    boolean autoRefresh() default true;
    String extConfig() default "environment.yml";

    enum ConfigFileExtension{
        yml, properties
    }
}
