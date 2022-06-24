/**
 * reed_redis/org.reed.redis.event/DemoHandler.java
 */
package org.reed.event;

import org.reed.utils.ThreadUtil;

/**
 * @author chenxiwen
 * @date 2017年9月19日下午1:59:22
 */
public class DemoHandler implements DemoEventListener, TestEventListener {
    
    /* (non-Javadoc)
     * @see org.reed.redis.event.DemoEventListener#doHandleDemo1(org.reed.redis.event.DemoEvent)
     */
    @Override
    public void doHandleDemo1(DemoEvent event) {
        System.out.println("here 1");
        
    }

    /* (non-Javadoc)
     * @see org.reed.redis.event.DemoEventListener#doHandleDemo2(org.reed.redis.event.DemoEvent)
     */
    @Override
    public void doHandleDemo2(DemoEvent event) {
        System.out.println("here 2");        
    }

    /* (non-Javadoc)
     * @see org.reed.redis.event.DemoEventListener#doHandleDemo3(org.reed.redis.event.DemoEvent)
     */
    @Override
    public void doHandleDemo3(DemoEvent event) {
        System.out.println("here 3");        
    }

    /* (non-Javadoc)
     * @see org.reed.redis.event.DemoEventListener#doHandleDemo4(org.reed.redis.event.DemoEvent)
     */
    @Override
    public void doHandleDemo4(DemoEvent event) {
        System.out.println("here 4");        
    }

    /* (non-Javadoc)
     * @see org.reed.redis.event.DemoEventListener#doHandleDemo5(org.reed.redis.event.DemoEvent)
     */
    @Override
    public void doHandleDemo5(DemoEvent event) {
        System.out.println("here 5");        
    }

    /* (non-Javadoc)
     * @see org.reed.redis.event.DemoEventListener#doHandleDemo8(org.reed.redis.event.OnlineEvent)
     */
    @Override
    public void doHandleDemo8(DemoEvent event) {
        System.out.println("here 8"); 
        
    }

    /* (non-Javadoc)
     * @see TestEventListener#doHandleTestEvent(TestEvent)
     */
    @Override
    public Object doHandleTestEvent(TestEvent event) {
        System.out.println("doHandleTestEvent");
        return null;
    }
    
    
    public static void main(String[] args){
        final DemoEvent de = new DemoEvent();
       
        DemoHandler dh  = new DemoHandler();
//        EventCenter.addEventListener(DemoEvent.class, dh);
        EventCenter.addEventListener(dh);
        ThreadUtil.block(300);
        EventCenter.fire(de);
        
        final TestEvent te = new TestEvent();
        EventCenter.fire(te);
        for(int i=0; i<100; i++){
            Thread t = new Thread(new Runnable() {
                
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    EventCenter.fire(te);
                }
            });
            t.start();
        }
    }

}
