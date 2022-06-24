/**
 * E5Projects @ org.reed.struct/ReedLRUCache.java
 */
package org.reed.struct;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenxiwen
 * @createTime 2019年09月18日 下午3:20
 * @description 注意，非线程安全！！！
 */
public class ReedLRUCache<K, V> {
    private final HashMap<K, Entry<K, V>> cache;
    private final int capacity;
    private Entry<K, V> head;
    private Entry<K, V> tail;

    public ReedLRUCache(int capacity) {
        this.capacity = capacity;
        cache = new HashMap<>(this.capacity);
    }

    /**
     * add a V into cache with Key of K
     * @param k
     * @param v
     * @return old value or null if k not exist in cache
     */
    public V put(K k, V v){
        Entry<K, V> entry = cache.get(k);
        if(entry == null){
            if(cache.size() >= this.capacity){
                cache.remove(tail.key);
                removeTail();
            }
            entry = new Entry<K, V>();
            entry.setKey(k);
            entry.setValue(v);
            moveToHead(entry);
            entry = cache.put(k, entry);
            return entry==null?null:entry.getValue();
        }else{
            entry.setValue(v);
            moveToHead(entry);
            return head.getValue();
        }
    }


    public V get(K k){
        Entry<K, V> entry = cache.get(k);
        if(entry == null){
            return null;
        }
        moveToHead(entry);
        return entry.getValue();
    }


    /**
     * remove V from cache with Key k
     * @param k
     * @return value of k in cache or null if k not exist in cache
     */
    public V remove(K k){
        Entry<K, V> entry = cache.get(k);
        if(entry != null){
            if(entry == this.head){
                Entry<K, V> temp = this.head.next;
                this.head.next = null;
                this.head = temp;
                this.head.previous = null;
            }else if(entry == this.tail){
                Entry<K, V> temp = this.tail.previous;
                this.tail.previous = null;
                this.tail = temp;
                this.tail.next = null;
            }else{
                entry.previous.next = entry.next;
                entry.next.previous = entry.previous;
            }
            entry = cache.remove(k);
            return entry==null?null:entry.getValue();
        }
        return null;
    }


    private void removeTail(){
        if(this.tail != null){
            Entry<K, V> previous = tail.previous;
            if(previous == null){
                this.head = null;
                this.head = null;
            }else{
                this.tail.previous = null;
                this.tail = previous;
                this.tail.next = null;
            }
        }
    }


    private void moveToHead(Entry<K, V> entry){
        if(entry == this.head){
            return;
        }

        if(entry.previous != null){
            entry.previous.next = entry.next;
        }

        if(entry.next != null){
            entry.next.previous = entry.previous;
        }

        if(entry == this.tail){
            Entry<K, V> prev = entry.previous;
            if(prev != null){
                this.tail.previous = null;
                this.tail = prev;
                this.tail.next = null;
            }
        }

        if(this.head == null || this.tail == null){
            this.head = this.tail = entry;
            return;
        }

        entry.next = this.head;
        this.head.previous = entry;
        entry.previous = null;
        this.head = entry;
    }

    public int size(){
        return cache.size();
    }

    @Override
    public String toString() {
        StringBuffer strBuf = new StringBuffer();
        Entry<K, V> entry = this.head;
        while(entry != null){
            strBuf.append(entry.key+":"+entry.value+(entry == tail?"":","));
            entry = entry.next;
        }
        return strBuf.toString();
    }

    private static class Entry<K, V> {
        Entry<K, V> previous;
        Entry<K, V> next;
        K key;
        V value;

        public Entry<K, V> getPrevious() {
            return previous;
        }

        public void setPrevious(Entry<K, V> previous) {
            this.previous = previous;
        }

        public Entry<K, V> getNext() {
            return next;
        }

        public void setNext(Entry<K, V> next) {
            this.next = next;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }
}
