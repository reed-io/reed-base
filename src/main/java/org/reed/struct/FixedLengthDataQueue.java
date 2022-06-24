/**
 * base/org.reed.struct/FixedLengthDataQueue.java
 */
package org.reed.struct;

import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.reed.utils.EnderUtil;

/**
 * @author chenxiwen
 * @date 2017年8月17日下午4:47:03
 */
public final class FixedLengthDataQueue<E> {
    /**
     * 节点定义
     * <h3></h3>
     * @author Ender
     * @since 2016年4月7日
     */
    class DataNode{
      DataNode next;
      volatile E data;

      DataNode(E data){
        this(data,null);
      }

      DataNode(E data,DataNode next){
        this.data=data;
        this.next=next;
      }
    }

    private int count;
    private DataNode head,tail;
    private final ReentrantLock oplock;
    private final transient Condition pvdata;
    
    private final int fixedLength;

    public FixedLengthDataQueue(int length){
      this.count=new Integer(0);
      this.oplock=new ReentrantLock();
      this.pvdata=oplock.newCondition();
      this.head=this.tail=new DataNode(null);
      this.fixedLength = length;
    }

    /**
     * 获取当前队列大小
     */
    public int size(){
      return count;
    }
    
    /**
     * 获取当前队列定长大小
     * @return
     */
    public int getFixedLength(){
        return fixedLength;
    }

    /**
     * <li>定长队列，方法不对外暴露</li>
     * 队列元素，如果队列为空，一直等待
     */
    @SuppressWarnings("unused")
    private E pop(){
      E result=null;
      try{
        oplock.lock();
        try{
          while(count==0){
            try{
              pvdata.await();
            }
            catch(Exception e){
              e.printStackTrace();
              pvdata.signalAll();
            }
          }
          if(this.count>0){
            result=rmvHead();
            if(this.count>0){
              pvdata.signalAll();
            }
          }
        }
        finally{
          oplock.unlock();
        }
      }
      catch(Throwable th){}
      return result;
    }

    public E peek(){
      oplock.lock();
      try{
        if(this.count==0){
          return null;
        }
        else{
          return head.next.data;
        }
      }
      finally{
        oplock.unlock();
      }
    }
    
    @SuppressWarnings({ "unchecked", "hiding" })
    public <E> E[] toArray(E[] e){
        if (e.length < count)
            e = (E[])java.lang.reflect.Array.newInstance(
                    e.getClass().getComponentType(), count);
        int i = 0;
        Object[] result = e;
        for (DataNode x = head.next; x != null; x = x.next)
            result[i++] = x.data;
        
        if (e.length > count)
            e[count] = null;
        
        return e;
    }
    
    public Object[] toArray() {
        Object[] result = new Object[count];
        int i = 0;
        for (DataNode x = head.next; x != null; x = x.next)
            result[i++] = x.data;
        return result;
    }

    /**
     * <li>定长队列，方法不对外暴露</li>
     * 在指定时间内获取对象
     * @param time 等待时间，单位为毫秒
     */
    @SuppressWarnings("unused")
    private E pop(int time){
      E result=null;
      long curtime=System.currentTimeMillis();
      try{
        oplock.lock();
        try{
          while(count==0){
            try{
              pvdata.awaitNanos(10);
              if((System.currentTimeMillis()-curtime)>time){
                // Logger.log(false,Level.Error,"数据库链路获取超时"+(System.currentTimeMillis()-curtime)+">"+time);
                break;
              }
            }
            catch(Exception e){
              e.printStackTrace();
              pvdata.signalAll();
            }
          }
          if(this.count>0){
            result=rmvHead();
            if(this.count>0){
              pvdata.signalAll();
            }
          }
        }
        finally{
          oplock.unlock();
        }
      }
      catch(Throwable th){
        // th.printStackTrace();
      }
      return result;
    }

    /**
     * 将元素添加到对列末尾
     */
    public FixedLengthDataQueue<E> push(E item){
      if(item!=null){
        // System.out.println("+++++++++++++++++++++");
        oplock.lock();
        // System.out.println("---------------------");
        try{
            if(this.count == this.fixedLength){
                rmvHead();
            }
            addTail(item);
            pvdata.signalAll();
        }
        finally{
          oplock.unlock();
        }
      }
      return FixedLengthDataQueue.this;
    }

    private E rmvHead(){
      this.count=this.count-1;
      return (head=head.next).data;
    }

    private void addTail(E data){
      this.count=this.count+1;
      tail=(tail.next=new DataNode(data));
    }

    public static void main(String[] args){
      final int queue_push=1000000;
      final FixedLengthDataQueue<String> queue=new FixedLengthDataQueue<String>(20);
      for(int idx=0;idx<6;idx++){
        new Thread("P:"+idx){
          @Override
          public void run(){
            int count=0;
            try{
                sleep(1);
            }catch(Exception e){
                e.printStackTrace();
            }
            while(count++<queue_push){
              queue.push("a");
            }
            System.out.println(getName()+":finish queue.size="+queue.size());
          }
        }.start();
        new Thread("C:"+idx){
          @Override
          public void run(){
            int count=0;
            while(count++<queue_push){
              queue.pop();
            }

            System.out.println(getName()+":finish queue.size="+queue.size());
          }
        }.start();
      }
      
        
//      final FixedLengthDataQueue<String> queue=new FixedLengthDataQueue<String>(20);
//      for(int i=0;i<30;i++){
//          queue.push(String.valueOf(i));
//      }
        
        
        
      System.out.println(queue.size());
      String[] arr = new String[queue.size()];
      arr = queue.toArray(arr);
//      Object[] arr = queue.toArray();
      
      System.out.println("arr.length="+arr.length);
      for(int i=0;i<arr.length;i++){
          System.out.print(arr[i]+">");
      }
      Scanner scanner = new Scanner(System.in);
      scanner.next();
      scanner.close();
      while(true){
        if(queue.size()>10){
          long time=System.currentTimeMillis();
          queue.push("a");
          System.out.println("size"+queue.size()+"-"+(System.currentTimeMillis()-time));
        }
        EnderUtil.block(100);
      }
    }
}
