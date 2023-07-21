/**
 * base/org.reed.struct/DataQueue.java
 */
package org.reed.struct;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.reed.utils.ThreadUtil;

/**
 * @author chenxiwen
 * @date 2017年8月17日下午4:59:56
 */
public class DataQueue<E> {
    class DataNode {
        DataNode next;
        volatile E data;

        DataNode(E data) {
            this(data, null);
        }

        DataNode(E data, DataNode next) {
            this.data = data;
            this.next = next;
        }
    }

    private int count;
    private DataNode head, tail;
    private final ReentrantLock oplock;
    private final transient Condition pvdata;

    public DataQueue() {
        this.count = new Integer(0);
        this.oplock = new ReentrantLock();
        this.pvdata = oplock.newCondition();
        this.head = this.tail = new DataNode(null);
    }

    /**
     * 获取当前队列大小
     */
    public int size() {
        return count;
    }

    /**
     * 队列元素，如果队列为空，一直等待
     */
    public E pop() {
        E result = null;
        try {
            oplock.lock();
            try {
                while (count == 0) {
                    try {
                        pvdata.await();
                    } catch (Exception e) {
                        e.printStackTrace();
                        pvdata.signalAll();
                    }
                }
                if (this.count > 0) {
                    result = rmvHead();
                    if (this.count > 0) {
                        pvdata.signalAll();
                    }
                }
            } finally {
                oplock.unlock();
            }
        } catch (Throwable th) {
        }
        return result;
    }

    public E peek() {
        oplock.lock();
        try {
            if (this.count == 0) {
                return null;
            } else {
                return head.next.data;
            }
        } finally {
            oplock.unlock();
        }
    }

    /**
     * 在指定时间内获取对象
     * 
     * @param time
     *            等待时间，单位为毫秒
     */
    public E pop(int time) {
        E result = null;
        long curtime = System.currentTimeMillis();
        try {
            oplock.lock();
            try {
                while (count == 0) {
                    try {
                        pvdata.awaitNanos(10);
                        if ((System.currentTimeMillis() - curtime) > time) {
                            // Logger.log(false,Level.Error,"数据库链路获取超时"+(System.currentTimeMillis()-curtime)+">"+time);
                            break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        pvdata.signalAll();
                    }
                }
                if (this.count > 0) {
                    result = rmvHead();
                    if (this.count > 0) {
                        pvdata.signalAll();
                    }
                }
            } finally {
                oplock.unlock();
            }
        } catch (Throwable th) {
            // th.printStackTrace();
        }
        return result;
    }

    public DataQueue<E> push(E item) {
        return push(item, false);
    }

    /**
     * 将元素添加到对列末尾
     */
    public DataQueue<E> push(E item, boolean nvlable) {
        if (item != null || nvlable) {
            // System.out.println("+++++++++++++++++++++");
            oplock.lock();
            // System.out.println("---------------------");
            try {
                addTail(item);
                pvdata.signalAll();
            } finally {
                oplock.unlock();
            }
        }
        return DataQueue.this;
    }

    private E rmvHead() {
        this.count = this.count - 1;
        return (head = head.next).data;
    }

    private void addTail(E data) {
        this.count = this.count + 1;
        tail = (tail.next = new DataNode(data));
    }

    public static void main(String[] args) {
        final int queue_push = 1000000;
        final DataQueue<String> queue = new DataQueue<String>();
        for (int idx = 0; idx < 6; idx++) {
            new Thread("P:" + idx) {
                @Override
                public void run() {
                    int count = 0;
                    while (count++ < queue_push) {
                        queue.push("a");
                    }
                    System.out.println(getName() + ":finish");
                }
            }.start();
            new Thread("C:" + idx) {
                @Override
                public void run() {
                    int count = 0;
                    while (count++ < queue_push) {
                        // queue.pop();
                    }

                    System.out.println(getName() + ":finish");
                }
            }.start();
        }
        while (true) {
            if (queue.size() > 100000) {
                long time = System.currentTimeMillis();
                queue.push("a");
                System.out.println("size" + queue.size() + "-" + (System.currentTimeMillis() - time));
            }
            ThreadUtil.block(100);
        }
    }
}
