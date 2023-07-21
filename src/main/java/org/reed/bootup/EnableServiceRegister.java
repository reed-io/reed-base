/**
 * E5Projects @ org.reed.bootup/EnableServiceRegister.java
 */
package org.reed.bootup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Notice: if Eureka product expected since 0.0.5-SNAPSHOT,
 * developers should add eureka,jersey dependencies manually,
 * framework had remove them for compressing the war packages
 * @author chenxiwen
 * @createTime 2019年08月06日 下午2:44
 * @description enable language auto registation with ModuleName
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableServiceRegister{
    String server() default "";
    DiscoveryProduction production() default DiscoveryProduction.Nacos;
    String namespace() default "";   //nacos default: public
    String group() default "";  //nacos default：DEFAULT_GROUP

    enum DiscoveryProduction{
        Eureka, Nacos
    }
}
