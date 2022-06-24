/**
 * base/org.reed.utils/MapUtil.java
 */
package org.reed.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chenxiwen
 * @date 2017年8月15日上午11:04:33
 */
public final class MapUtil {
    public static final String DEFAULT_CONNECTION_MARK = ",";
    public static final String DEFAULT_SPLIT_MARK = ",";
    
    public static <K, V> boolean isEmpty(Map<K, V> map){
        return null == map || null == map.keySet() || map.size() == 0;  // jdk1.7 or later map.isEmpty() 
    }
    
    public static <K, V> Map<K, V> safetyInstance(){
        return new ConcurrentHashMap<K, V>();
    }
    
    public static <K, V> Map<K, V> hashInstance(){
        return new HashMap<K, V>();
    }
    
    public static<K, V> String toString(Map<K, V> map){
        return toString(map, DEFAULT_CONNECTION_MARK, DEFAULT_SPLIT_MARK);
    }
    
    public static <K, V>String toString(Map<K, V> map, String conn, String split){
        if(isEmpty(map)){
            return "";
        }
        if(StringUtil.isEmpty(conn) || StringUtil.isEmpty(split)){
            return map.toString();
        }
        StringBuffer strBuf = new StringBuffer();
        for(K k : map.keySet()){
            strBuf.append(k.toString()).append(split).append(map.get(k).toString()).append(conn);
        }
        if(strBuf.lastIndexOf(conn) == (strBuf.length()-conn.length())){
            return strBuf.substring(0, strBuf.length()-conn.length());
        }
        return strBuf.toString();
    }

    public static <K, V> String connect(Map<K, V> map, char connMark, char equalMark){
        if(isEmpty(map) || connMark == '0' || equalMark == '0'){
            return "";
        }
        StringBuffer strBuf = new StringBuffer();
        for(K k : map.keySet()){
            strBuf.append(k.toString()).append(equalMark).append(map.get(k).toString()).append(connMark);
        }
        return strBuf.substring(0, strBuf.length()-1);
    }
}
