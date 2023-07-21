/**
 * E5Projects @ org.reed.utils/ReedLRUCacheTest.java
 */
package org.reed.utils;

import org.reed.struct.ReedLRUCache;

/**
 * @author chenxiwen
 * @createTime 2019年09月18日 下午4:06
 * @description
 */
public class ReedLRUCacheTest {

    public static void main(String[] args){
        ReedLRUCache<String, String> lru = new ReedLRUCache<>(5);
        System.out.println(lru.put("test1","1"));
        System.out.println("cuurentSize="+lru.size());
        System.out.println(lru.toString());
        System.out.println(lru.put("test2","2"));
        System.out.println("cuurentSize="+lru.size());
        System.out.println(lru.toString());
        System.out.println(lru.put("test1","11"));
        System.out.println("cuurentSize="+lru.size());
        System.out.println(lru.toString());
        System.out.println(lru.put("test4","4"));
        System.out.println("cuurentSize="+lru.size());
        System.out.println(lru.toString());
        System.out.println(lru.put("test3","3"));
        System.out.println("cuurentSize="+lru.size());
        System.out.println(lru.toString());
        System.out.println(lru.put("test5","5"));
        System.out.println("cuurentSize="+lru.size());
        System.out.println(lru.toString());
        System.out.println(lru.get("test2"));
        System.out.println("cuurentSize="+lru.size());
        System.out.println(lru.toString());
        System.out.println(lru.put("test6","6"));
        System.out.println("cuurentSize="+lru.size());
        System.out.println(lru.toString());
    }
}
