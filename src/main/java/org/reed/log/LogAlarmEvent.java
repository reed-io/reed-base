/**
 * springbootup @ org.reed.log.ErrorEvent.java
 */
package org.reed.log;

import org.reed.event.ReedEvent;

/**
 * @author chenxiwen
 * @createTime 2021-11-11 10:40
 */
public class LogAlarmEvent extends ReedEvent {

    private LoggerObject loggerObject;

    public LogAlarmEvent(LoggerObject loggerObject){
        this.loggerObject = loggerObject;
    }

    public LoggerObject getLoggerObject() {
        return loggerObject;
    }

    public void setLoggerObject(LoggerObject loggerObject) {
        this.loggerObject = loggerObject;
    }

    @Override
    public void callbackHandler(Object... args) {

    }
}
