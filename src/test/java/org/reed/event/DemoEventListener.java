/**
 * reed_redis/org.reed.redis.event/DemoEventListener.java
 */
package org.reed.event;

import org.reed.define.MethodChain;

/**
 * @author chenxiwen
 * @date 2017年9月19日上午10:41:52
 */
public interface DemoEventListener extends ReedEventListener {
//    @Override
//    @MethodChain
//    public RedisClient doHandle(DemoEvent event);
    
    @MethodChain(index=1, event=DemoEvent.class)
    void doHandleDemo1(DemoEvent event);
    @MethodChain(index=2, skip=true, event=DemoEvent.class)
    void doHandleDemo2(DemoEvent event);
    @MethodChain(index=3, event=DemoEvent.class)
    void doHandleDemo3(DemoEvent event);
    @MethodChain(index=200, event=DemoEvent.class)
    void doHandleDemo4(DemoEvent event);
    @MethodChain(index=999, event=DemoEvent.class)
    void doHandleDemo5(DemoEvent event);
    @MethodChain(index=88)
    void doHandleDemo8(DemoEvent event);
}
