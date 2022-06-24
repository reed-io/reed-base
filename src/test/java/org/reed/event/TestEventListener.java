/**
 * base/org.reed.event/TestEventListener.java
 */
package org.reed.event;

import org.reed.define.MethodChain;

/**
 * @author chenxiwen
 * @date 2017年9月21日上午10:49:19
 */
public interface TestEventListener extends ReedEventListener {
    @MethodChain(index=1, event=TestEvent.class)
    Object doHandleTestEvent(TestEvent event);
}
