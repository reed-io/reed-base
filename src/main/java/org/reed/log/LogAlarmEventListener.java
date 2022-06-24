/**
 * springbootup @ org.reed.log.LogAlarmEventListener.java
 */
package org.reed.log;

import org.reed.define.MethodChain;
import org.reed.event.ReedEventListener;

/**
 * @author chenxiwen
 * @createTime 2021-12-12 15:34
 */
public interface LogAlarmEventListener extends ReedEventListener {

    @MethodChain(index=1, event=LogAlarmEvent.class)
    void handleLoggerAlarmEvent(LogAlarmEvent event);
}
