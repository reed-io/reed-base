/**
 * reed_redis/org.reed.redis.event/DemoEvent.java
 */
package org.reed.event;

/**
 * @author chenxiwen
 * @date 2017年9月19日上午10:43:38
 */
public class DemoEvent extends ReedEvent {
    /* (non-Javadoc)
     * @see org.reed.standard.CallbackAble#callbackHandler()
     */
    @Override
    public void callbackHandler(Object... obj) {
        System.out.println(">>>FireCost:"+(System.currentTimeMillis()-this.getTimestamp()));
        
    }
}
