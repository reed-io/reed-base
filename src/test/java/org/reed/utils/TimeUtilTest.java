/**
 * E5Projects @ org.reed.utils/TimeUtilTest.java
 */
package org.reed.utils;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author chenxiwen
 * @createTime 2020年06月28日 下午4:25
 * @description
 */
public class TimeUtilTest {
    public static void main(String[] args){
        System.out.println(TimeUtil.format("yyyy->MM->dd HHmmss", new Date()));
        System.out.println(TimeUtil.getDateTime(new Date()));
        System.out.println(TimeUtil.nowDate());
        //threads test
        ExecutorService pool = Executors.newFixedThreadPool(3);
        CountDownLatch count = new CountDownLatch(3);
        long startTime = System.currentTimeMillis();
        for(int i=0;i<10000;i++){
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(Thread.currentThread().getId()+" - "+TimeUtil.nowDateTime());
                        count.countDown();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }
        try{
            count.await();
            System.out.println("Cost:"+(System.currentTimeMillis()-startTime));
        }catch(InterruptedException e){
            e.printStackTrace();
        }finally {
            pool.shutdown();
        }
    }
}
