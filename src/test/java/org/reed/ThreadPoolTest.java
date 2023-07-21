/**
 * IdeaProject @ com.reed/ThreadPoolTest.java
 */
package org.reed;

import org.reed.utils.EnderUtil;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author chenxiwen
 * @createTime 2018年11月26日 上午9:57
 * @description
 */
public class ThreadPoolTest {

//    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);
    ThreadPoolExecutor executor = new ThreadPoolExecutor(1,1,1000,TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(1));


    public boolean doJob(Runnable job){
        if(executor.getActiveCount()>0){
            return false;
        }

        executor.execute(job);

//        fixedThreadPool.execute(job);
        return true;
    }

    public static void main(String[] args){
        ThreadPoolTest tpt = new ThreadPoolTest();
        System.out.println(tpt.doJob(new Job()));
        System.out.println(tpt.doJob(new Job1()));
        System.out.println(tpt.doJob(new Job()));
        try {
            while(tpt.executor.getActiveCount()>0){
                System.out.println(tpt.executor.toString());
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(tpt.doJob(new Job1()));
    }

    /**
     * POI导入  先读取excel 文件 然后解析 然后逐行调用rest接口之后再入库
     */
    public static class Job implements Runnable{

        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        @Override
        public void run() {
            try {
                for(int i=0;i<10;i++){
                    Thread.sleep(1000);
                    System.out.println(EnderUtil.devInfo()+" - counter:"+i);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 单条添加   从controller接受参数，调用rest校验  然后入库
     */
    public static class  Job1 implements Runnable{

        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        @Override
        public void run() {
            try {
                for(int i=0;i<5;i++){
                    Thread.sleep(1000);
                    System.out.println(EnderUtil.devInfo()+" - counter:"+i);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
