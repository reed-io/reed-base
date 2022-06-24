/**
 * base/org.reed.utils/CollectionUtil.java
 */
package org.reed.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author chenxiwen
 * @date 2017年8月23日下午4:22:43
 */
public final class CollectionUtil<E> {
    
    public static <E> boolean isEmpty(Collection<E> collection){
        return collection==null || collection.size() == 0;
    }
    
    /**
     * 得到两个结合的交集, c1和c2共有的部分
     * @param c1
     * @param c2
     * @return
     */
    public static <E> Collection<E> intersect(Collection<E> c1, Collection<E> c2){
        if(isEmpty(c1)||isEmpty(c2)){
            return null;
        }
        Collection<E> result = clone(c1);
        if(isEmpty(result)){
            return null;
        }
        result.retainAll(c2);
        return result;
    }
    
    public static <E> Collection<E> intersect(Collection<E>... collections){
        if(collections==null || collections.length<2){
            return null;
        }
        Collection<E> c1 = collections[0];
        Collection<E> c2 = collections[1];
        Collection<E> result = intersect(c1, c2);
        for(int i=2;i<collections.length && result !=null;i++){
            result = intersect(result, collections[i]);
        }
        return result;
    }
    
    /**
     * 求两个集合的差集，c1有c2没有的部分
     * @param c1
     * @param c2
     * @return
     */
    public static <E> Collection<E> subtract(Collection<E> c1, Collection<E> c2){
        if(isEmpty(c1)){
            return null;
        }
        if(isEmpty(c2)){
            return c1;
        }
        Collection<E> result = clone(c1);
        if(isEmpty(result)){
            return null;
        }
        result.removeAll(c2);
        return result;
    }
    
    public static <E> Collection<E> subtract(Collection<E>... collections){
        if(collections==null || collections.length<2){
            return null;
        }
        Collection<E> c1 = collections[0];
        Collection<E> c2 = collections[1];
        Collection<E> result = subtract(c1, c2);
        for(int i=2;i<collections.length && result !=null;i++){
            result = subtract(result, collections[i]);
        }
        return result;
    }
    
    /**
     * 求两个集合的合集，c1+c2
     * @param c1
     * @param c2
     * @return
     */
    public static<E> Collection<E> union(Collection<E> c1, Collection<E> c2){
        Collection<E> result = null;
        try{
            if(c1!=null){
                result = c1.getClass().newInstance();
            }
            if(result == null && c2 != null){
                result = c2.getClass().newInstance();
            }
        }catch(InstantiationException e){
            e.printStackTrace();
        }catch(IllegalAccessException e){
            e.printStackTrace();
        }
        if(result == null){
            return null;
        }
        if(!isEmpty(c1)){
            result.addAll(c1);
        }
        if(!isEmpty(c2)){
            result.addAll(c2);
        }
        return result;
    }
    
    public static <E> Collection<E> union(Collection<E>... collections){
        Collection<E> result = null;
        try{
            for(Collection<E> c : collections){
                if(c!=null && result == null){
                    result = c.getClass().newInstance();
                }
                if(result != null){
                    result = union(result, c);
                }
            }
        }catch(InstantiationException e){
            e.printStackTrace();
            return null;
        }catch(IllegalAccessException e){
            e.printStackTrace();
            return null;
        }
        return result;
    }
    
    public static <E> Collection<E> clone(Collection<E> collection){
        if(isEmpty(collection)){
            return null;
        }
        try {
            Collection<E> result = collection.getClass().newInstance();
            result.addAll(collection);
            return result;
        } catch (InstantiationException e ) {
            e.printStackTrace();
            return null;
        }catch(IllegalAccessException e){
            e.printStackTrace();
            return null;
        }
    }
    
    public static <E> String toString(Collection<E> collection, String open, String close, String seperator){
        String result = null;
        StringBuffer strBuf = new StringBuffer();
        for(E e : collection){
            strBuf.append(e.toString());
            strBuf.append(seperator);
        }
        if(strBuf.length() > seperator.length() && strBuf.lastIndexOf(seperator) == (strBuf.length()-seperator.length())){
            result = strBuf.substring(0, strBuf.length()-seperator.length());
        }
        return open+result+close;
    }
    
    public static <E> String toString(Collection<E> collection){
        return toString(collection, "[", "]", ",");
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        Set<String> s1 = new HashSet<String>();
        s1.add("1");
        s1.add("2");
        s1.add("3");
        s1.add("4");
        s1.add("5");
        Set<String> s2 = new HashSet<String>();
        s2.add("1");
        s2.add("3");
        s2.add("5");
        s2.add("7");
        s2.add("9");
        Set<String> _s = new HashSet<String>();
        _s.add("1");
        _s.add("2");
        _s.add("4");
        _s.add("6");
        _s.add("8");
        List<String> l1 = new ArrayList<String>();
        l1.add("1");
        l1.add("2");
        l1.add("4");
        l1.add("6");
        l1.add("8");
        System.out.println("---------------------intersect------------------");
        Collection<String> s3 = CollectionUtil.intersect(s1, s2, _s, l1);
        for(String s: s3){
            System.out.println(s);
        }
        System.out.println("---------------------subtract------------------");
        Collection<String> s4 = CollectionUtil.subtract(s1, s2);
        for(String s: s4){
            System.out.println(s);
        }
        System.out.println("---------------------union------------------");
        Collection<String> s5 = CollectionUtil.union(s1, s2);
        for(String s: s5){
            System.out.println(s);
        }

        System.out.println("---------------------off------------------");
        System.out.println(("ender".lastIndexOf("er"))==("ender".length()-"er".length()));
    }

}
