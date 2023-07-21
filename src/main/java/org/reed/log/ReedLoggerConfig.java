/**
 * springbootup @ org.reed.log.ReedLoggerConfig.java
 */
package org.reed.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author chenxiwen
 * @createTime 2021-12-12 10:51
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReedLoggerConfig {
    boolean isStaticLogger() default false;
    boolean enableFileAppender() default false;
    boolean showMethodName() default false;
    boolean showLineNumber() default false;
    boolean enableAlarm() default true;
    ReedLoggerLevel alarmLevel() default ReedLoggerLevel.ERROR;

    enum ReedLoggerLevel{
        DEBUG(1), INFO(2), WARN(3), ERROR(4), TRACE(5);

        private final int idx;

        ReedLoggerLevel(int idx){
            this.idx = idx;
        }

        public int getIdx(){
            return idx;
        }
    }
}
