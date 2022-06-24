/**
 * base/org.reed.struct/Test.java
 */
package org.reed.struct;

import com.alibaba.fastjson2.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenxiwen
 * @date 2017年10月26日上午10:19:12
 */
public class Test {

    /**
     * @param args
     */
    public static void main(String[] args) {
        List<Object> list = new ArrayList<Object>();
        list.add("a");
        list.add("B");
        list.add("c");
        list.add("d");
        list.add("e");
        String str =JSON.toJSONString(list);
        System.out.println(str);
//        JSONArray jo = JSONObject.parseObject(str);
//        System.out.println(jo.toJSONString());
    }

}
