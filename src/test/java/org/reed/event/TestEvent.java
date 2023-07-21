/**
 * base/org.reed.event/TestEvent.java
 */
package org.reed.event;

import org.reed.utils.ThreadUtil;
import org.reed.utils.TimeUtil;

/**
 * @author chenxiwen
 * @date 2017年9月21日上午10:47:08
 */
public class TestEvent extends ReedEvent {

    /* (non-Javadoc)
     * @see org.reed.standard.CallbackAble#callbackHandler(java.lang.Object[])
     */
    @Override
    public void callbackHandler(Object... args) {
        System.out.println("TestEvent - "+Thread.currentThread().getName()+" - cost:"+(System.currentTimeMillis()-this.getTimestamp()));

    }
    
    public static void main(String[] args){
//        System.out.println(TestEventListener.class.getDeclaredMethods().length);
//        for(Method m :  TestEventListener.class.getDeclaredMethods()){
//            System.out.println(m.getName());
//        }
//        System.out.println("------------------------");
//        System.out.println(TestEventListener.class.getMethods().length);
//        for(Method m :  TestEventListener.class.getMethods()){
//            System.out.println(m.getName());
//        }
        
        TestEvent t1 = new TestEvent();
        ThreadUtil.block(100);
        DemoEvent t2 = new DemoEvent();
        TestEvent t3 = new TestEvent();
        System.out.println(t1.getId()+" - "+t1.getTimestamp());
        System.out.println(t2.getId()+" - "+t2.getTimestamp());
        System.out.println(t2.equals(t1)+" - "+Math.abs(t1.getTimestamp()-t2.getTimestamp()));
        System.out.println(t3.equals(null)+" - "+Math.abs(t1.getTimestamp()-t3.getTimestamp()));
        System.out.println(TimeUtil.getDateTime(Long.MAX_VALUE));
    }

}
