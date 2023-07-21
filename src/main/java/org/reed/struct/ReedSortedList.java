/**
 * E5Projects @ org.reed.struct/ReedSortedList.java
 */
package org.reed.struct;

import java.util.LinkedList;
import java.util.List;

/**
 * @author chenxiwen
 * @createTime 2019年12月09日 下午1:56
 * @description  结果按大小递增顺序 非线程安全！！！
 */
public final class ReedSortedList<T> {

    private final LinkedList<T> list = new LinkedList<T>();

    private final LinkedList<Integer> keyList = new LinkedList<Integer>();

    public LinkedList<T> linkedList(){
        return list;
    }

    public int insert(Integer key, T value){
        if(keyList.size() == 0 || key>=keyList.getLast()){
            keyList.add(key);
            list.add(value);
            return keyList.size()-1;
        }

        for(int i=0; i< keyList.size() ; i++){
            if(key<keyList.get(i)){
                keyList.add(i, key);
                list.add(i, value);
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args){
        ReedSortedList<String> list = new ReedSortedList<>();
        list.insert(1, "1");
        list.insert(3, "3");
        list.insert(5, "5");
        list.insert(7, "7");
        list.insert(9, "9");
        list.insert(2, "2");
        list.insert(4, "4");
        list.insert(6, "6");
        list.insert(8, "8");
        list.insert(10, "10");
        list.insert(0, "0");
        List<String> result = list.list;
        for(String s:result){
            System.out.print(s+", ");
        }
    }
}
