/**
 * reed_redis/org.reed.event/EventCenter.java
 */
package org.reed.event;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.reed.define.MethodChain;
import org.reed.log.ReedLogger;
import org.reed.utils.EnderUtil;

/**
 * @author chenxiwen
 * @date 2017年9月19日上午9:33:07
 */
public final class EventCenter {
    public static final int DEFAULT_FIRE_THRED_POOL_SIZE = 30;
    public static final int DEFAULT_ADD_LISTENER_THRED_POOL_SIZE = 30;
    private static ExecutorService fireThreadPool = Executors.newFixedThreadPool(DEFAULT_FIRE_THRED_POOL_SIZE);
    private static ExecutorService addListenerThreadPool = Executors.newFixedThreadPool(DEFAULT_ADD_LISTENER_THRED_POOL_SIZE);

    private EventCenter(){}
    
    private static volatile Map<Class<? extends ReedEvent>, Set<ReedEventListener>> listenerMap = new ConcurrentHashMap<Class<? extends ReedEvent>, Set<ReedEventListener>>();
    private static volatile Map<Class<? extends ReedEvent>, Map<Class<ReedEventListener>, TreeMap<Integer, Method>>> chainMap = 
               new HashMap<Class<? extends ReedEvent>, Map<Class<ReedEventListener>, TreeMap<Integer, Method>>>();
    
    static{
        if(chainMap == null){
            chainMap =  new HashMap<Class<? extends ReedEvent>, Map<Class<ReedEventListener>, TreeMap<Integer, Method>>>();
        }
        if(listenerMap == null){
            listenerMap = new ConcurrentHashMap<Class<? extends ReedEvent>, Set<ReedEventListener>>();
        }
        if(fireThreadPool == null){
            fireThreadPool = Executors.newFixedThreadPool(DEFAULT_FIRE_THRED_POOL_SIZE);
        }
        if(addListenerThreadPool == null){
            addListenerThreadPool = Executors.newFixedThreadPool(DEFAULT_ADD_LISTENER_THRED_POOL_SIZE);
        }
    }
    
    /**
     * 注册监听器，严格按照ReedEventListener接口中每个@MethodChain注解的内容进行注册，不可省略
     * @param listener
     */
    public static void addEventListener(final ReedEventListener listener){
        addListenerThreadPool.execute(new Runnable(){
            @Override
            public void run() {
                try{
                    for(Class<?> itf : listener.getClass().getInterfaces()){
                        if(ReedEventListener.class.isAssignableFrom(itf)){
                            for(Method method : itf.getDeclaredMethods()){
                                if(method.isAnnotationPresent(MethodChain.class) && method.getParameterTypes().length==1 
                                        && ReedEvent.class.isAssignableFrom(method.getParameterTypes()[0])){
                                    MethodChain mc = method.getAnnotation(MethodChain.class);
                                    int index = mc.index();
                                    boolean skip = mc.skip();
                                    Class<? extends ReedEvent> eventClass = mc.event();
                                    if(!skip && eventClass.equals(method.getParameterTypes()[0])){
                                        if(!chainMap.containsKey(eventClass)){
                                            chainMap.put(eventClass, new HashMap<Class<ReedEventListener>, TreeMap<Integer, Method>>());
                                        }
                                        Map<Class<ReedEventListener>, TreeMap<Integer, Method>> itfMetherdMap = chainMap.get(eventClass);
                                        if(!itfMetherdMap.containsKey(itf)){
                                            itfMetherdMap.put((Class<ReedEventListener>)itf, new TreeMap<Integer, Method>());
                                        }
                                        TreeMap<Integer, Method> methodMap = itfMetherdMap.get(itf);
                                        methodMap.put(index, method);
                                    }
                                    if(!listenerMap.containsKey(eventClass)){
                                        listenerMap.put(eventClass, new HashSet<ReedEventListener>());
                                    }
                                    listenerMap.get(eventClass).add(listener);
                                }
                            }
                        }
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    ReedLogger.error("EventCenter - addEventListener error:"+e.getMessage()+">"+EnderUtil.devInfo());
                }
            }
        });
    }
    
    /**
     * 注册事件和监听者的实现类，本方法只会注册实现类中对此事件的监听方法，并根据@MethodChain注解中的index顺序进行注册，同时调用时也会遵循此顺序。
     * 如果对应的ReedEventListener接口中只有一个方法时可以忽略@MethodChain注解
     * @param clazz
     * @param listener
     */
    public static <T extends ReedEvent> void addEventListener(final Class<T> clazz, final ReedEventListener listener){
        addListenerThreadPool.execute(new Runnable() {
//            final long t1 = System.currentTimeMillis();
            @Override
            public void run() {
                try{
                    if(!listenerMap.containsKey(clazz)){
                        listenerMap.put(clazz, new HashSet<ReedEventListener>());
                        if(!chainMap.containsKey(clazz)){
                            chainMap.put(clazz, new HashMap<Class<ReedEventListener>, TreeMap<Integer, Method>>());
                        }
                        Map<Class<ReedEventListener>, TreeMap<Integer, Method>> listenerMethorMap = chainMap.get(clazz);
                        for(Class<?> itf : listener.getClass().getInterfaces()){
                            if(ReedEventListener.class.isAssignableFrom(itf)){
                                if(!chainMap.containsKey(itf)){
                                    TreeMap<Integer, Method> methodSortMap = new TreeMap<Integer, Method>();
                                    if(itf.getDeclaredMethods().length == 1){
                                        Method method = itf.getDeclaredMethods()[0];
                                        if(method.getParameterTypes().length == 1 && ReedEvent.class.isAssignableFrom(method.getParameterTypes()[0])
                                                && clazz.equals(method.getParameterTypes()[0])){
                                            methodSortMap.put(0, method);
                                        }
                                    }else{
                                        for(Method method : itf.getDeclaredMethods()){
                                            if(method.isAnnotationPresent(MethodChain.class) && method.getParameterTypes().length==1 
                                                    && ReedEvent.class.isAssignableFrom(method.getParameterTypes()[0]) && clazz.equals(method.getParameterTypes()[0])){
                                                MethodChain mc = method.getAnnotation(MethodChain.class);
                                                int index = mc.index();
                                                boolean skip = mc.skip();
                                                Class<? extends ReedEvent> eventClass = mc.event();
                                                if(!skip && (eventClass.equals(clazz) || eventClass == ReedEvent.class)){
                                                    methodSortMap.put(index, method);
                                                }
                                            }
                                        }
                                    }
                                    listenerMethorMap.put((Class<ReedEventListener>)itf, methodSortMap);
                                }
                            }
                        }
                    }
                    listenerMap.get(clazz).add(listener);
                }catch(Exception e){
                    e.printStackTrace();
                    ReedLogger.error("EventCenter - addEventListener error:"+e.getMessage()+">"+EnderUtil.devInfo());
                }
//                System.out.println("AddListenerCost:"+(System.currentTimeMillis()-t1));
            }
        });
//        addListenerThreadPool.shutdown();
//        try {
//            addListenerThreadPool.awaitTermination(10, TimeUnit.MILLISECONDS);
//        } catch (InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
    }
    
    /**
     * 触发事件
     * @param t
     */
    public static <T extends ReedEvent> void fire(final T t){
        fireThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try{
                    Class<? extends ReedEvent> clazz = t.getClass();
                    Set<Object> callBackArgs = new HashSet<Object>();
                    if(listenerMap.containsKey(clazz)){
                        for(ReedEventListener listener : listenerMap.get(clazz)){
                            try{
                                for(Class<?> itf : listener.getClass().getInterfaces()){
                                    if(chainMap.containsKey(clazz) && chainMap.get(clazz).containsKey(itf)){
                                        TreeMap<Integer, Method> map = chainMap.get(clazz).get(itf);
                                        for(Integer index : map.keySet()){
                                            callBackArgs.add(map.get(index).invoke(listener, t));
                                            ReedLogger.info("EventCenter - fire >>>>>>>>>>>>"+t.getClass().getName()+"@"+listener.getClass().toString());
                                        }
                                    }
                                }
                            }catch(Exception ex){
                                ex.printStackTrace();
                                ReedLogger.error("EventCenter - fire error:"+ex.getMessage()+">"+EnderUtil.devInfo());
                            }
                        }
                    }
                    t.callbackHandler(callBackArgs.toArray());
                }catch(Exception e){
                    e.printStackTrace();
                    ReedLogger.error("EventCenter - fire error:"+e.getMessage()+">"+EnderUtil.devInfo());
                }
            }
        });
//        fireThreadPool.shutdown();
//        try {
//            fireThreadPool.awaitTermination(10, TimeUnit.MILLISECONDS);
//        } catch (InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
    }
    
    public static void main(String[] args){
//            for(Method m : ReedEventListener.class.getMethods()){
//                System.out.println(m.getName());
//            }
            System.out.println(Object.class.isAssignableFrom(String.class));
            System.out.println(Collection.class.isAssignableFrom(List.class));
    }
    
}
