/**
 * base/org.reed.utils/ThreadUtil.java
 */
package org.reed.utils;

/**
 * @author chenxiwen
 * @date 2017年8月17日下午4:52:29
 */
public final class ThreadUtil {

    public static void sleep(long time, int nanos) {
        try {
            if (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(time, nanos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * sleep
     * 
     * @param time
     */
    public static void block(long time) {
        sleep(time, 0);
    }

    /**
     * sleep Integer.MAX_VALUE
     */
    public static void block() {
        block(Integer.MAX_VALUE);
    }

    public static void _waiton(Object object) {
        _waiton(object, Long.MAX_VALUE);
    }

    public static boolean _waiton(Object object, long time) {
        if (object == null) {
            return Boolean.FALSE;
        }
        try {
            if (time <= 0) {
                return Boolean.FALSE;
            } else {
                synchronized (object) {
                    object.wait(time);
                    return Boolean.FALSE;
                }
            }
        } catch (InterruptedException ex) {
            return Boolean.TRUE;
        }
    }

    public static void _notify(Object object) {
        if (object != null) {
            synchronized (object) {
                object.notify();
            }
        }
    }

    public static void _notifys(Object object) {
        if (object != null) {
            synchronized (object) {
                object.notifyAll();
            }
        }
    }

}
