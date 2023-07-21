/**
 * IdeaProject @ org.reed.utils/Base64UtilTest.java
 */
package org.reed.utils;

import java.io.IOException;

/**
 * @author chenxiwen
 * @createTime 2019年05月06日 下午6:59
 * @description
 */
public class Base64UtilTest {



    public static void main(String[] args){
        try {
            System.out.println(Base64Util.encode("测试一下而已".getBytes()));
            System.out.println(Base64Util.decode2Utf8Str("6Zi/6JCo5b635Y+R6YCB5Zyw5pa56Zi/6JCo5b635Y+R5Y+RICAg6Zi/5pav6aG/5Y+R5ZWK"));
            System.out.println(Base64Util.decode("6Zi/6JCo5b635Y+R6YCB5Zyw5pa56Zi/6JCo5b635Y+R5Y+RICAg6Zi/5pav6aG/5Y+R5ZWK", CharEncoding.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
