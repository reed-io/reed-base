/**
 * IdeaProject @ org.reed.utils/StringUtilTest.java
 */
package org.reed.utils;

import java.util.List;

/**
 * @author chenxiwen
 * @createTime 2018年11月20日 下午7:33
 * @description
 */
public class StringUtilTest {

    public static void main(String[] args){
        String s = "mysql://ReedEnv{ip}:ReedEnv{port}/ReedEnv{dbInstance}@ReedEnv{username}&ReedEnv{password}";

        List<String> list = StringUtil.getMatched(StringUtil.Reed_ENV, s);

        for(String str:list){
            System.out.println(str+">>>"+StringUtil.extractVal(str));
        }

        String x = System.getenv("ender");
        System.out.println("env[ender]:"+x);

        String str = "来ReedCipher{}来来ReedCipher{2f8ab711c0397b48c7e8bab352a8977e}哈哈哈测试一下行不行ReedCipher{2222f27cfecd13bac6499537ddedf47e40a91593270e66ef28901c47e3dab2f87d91c38a3feecae9}";
        String str1 = "啥也没有，或者有个ReedEnv{123123},再来一个ReedTest{asdfadsf}";
        System.out.println(StringUtil.decryptCiphertext(str, DESUtil.DEFAULT_SECURITY_CODE));
        System.out.println(StringUtil.decryptCiphertext(str1, DESUtil.DEFAULT_SECURITY_CODE));

        String pStr = "^[1][3,4,5,7,8][0-9]{9}$";
        System.out.println(StringUtil.isMatched(pStr, "15311418595"));
        System.out.println(StringUtil.isMatched(pStr, "11311418595"));
        System.out.println(StringUtil.isMatched(pStr, "21311418595"));
        System.out.println(StringUtil.isMatched(pStr, "133114185951"));
        System.out.println(StringUtil.isMatched(pStr, "+86-13311418595"));

        System.out.println("---------------");

        String mailPattern = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$";
        System.out.println(StringUtil.isMatched(mailPattern, "chenxiwen@163.com"));
        System.out.println(StringUtil.isMatched(mailPattern, "Chenxiwen123123@163.com"));
        System.out.println(StringUtil.isMatched(mailPattern, "Chenxiwen123123.123123@163.com"));
        System.out.println(StringUtil.isMatched(mailPattern, "Chenxiwen123123_123123@163.com"));
        System.out.println(StringUtil.isMatched(mailPattern, "Chenxiwen123123_123123@163.com"));
        System.out.println(StringUtil.isMatched(mailPattern, "Chenxiwen123123-123123@163.com"));
        System.out.println(StringUtil.isMatched(mailPattern, "Chenxiwen123123-123123@163.com."));
        System.out.println(StringUtil.isMatched(mailPattern, "Chenxiwen123123-123123@163"));
        System.out.println(StringUtil.isMatched(mailPattern, "Chenxiwen123123$123123@163.com"));
        System.out.println(StringUtil.isMatched(mailPattern, "Chenxiwen123123$123123#163.com"));

        System.out.println("_------------------------");

        String telephonePattern = "^0\\d{2,3}-\\d{7,8}";
        System.out.println(StringUtil.isMatched(telephonePattern, "010-57585858"));
        System.out.println(StringUtil.isMatched(telephonePattern, "010-5758585"));
        System.out.println(StringUtil.isMatched(telephonePattern, "0555-57585859"));
        System.out.println(StringUtil.isMatched(telephonePattern, "0555-5758585"));
        System.out.println(StringUtil.isMatched(telephonePattern, "01-57585858"));
        System.out.println(StringUtil.isMatched(telephonePattern, "01100-57585858"));
        System.out.println(StringUtil.isMatched(telephonePattern, "21-57585858"));
        System.out.println(StringUtil.isMatched(telephonePattern, "21-57585858"));
        System.out.println(StringUtil.isMatched(telephonePattern, "201-57585858"));
        System.out.println(StringUtil.isMatched(telephonePattern, "2021-57585858"));
        System.out.println(StringUtil.isMatched(telephonePattern, "021-575858581"));
        System.out.println(StringUtil.isMatched(telephonePattern, "021-575858"));
        System.out.println(StringUtil.isMatched(telephonePattern, "abcadsfasdf"));
        System.out.println(StringUtil.isMatched(telephonePattern, "02112345678"));


        System.out.println("---------------------------------------------------------------------");


        String url = "jdbc:mysql://ReedEnv{Reed_MYSQL_CORE_HOST}/portal";
//        String url = "ReedEnv{Reed_MYSQL_CORE_HOST}";

        System.out.println(StringUtil.isContains(StringUtil.Reed_ENV, url));

        List<String> list1 = StringUtil.getMatched(StringUtil.Reed_ENV, url);

        for(String str2:list1){
            System.out.println(str2+">>>"+StringUtil.extractVal(str2));
        }


//        byte[] env1 = DESUtil.encrypt("访问地址：ReedCipher{1a82e2dc53775fc6bb0c8927846fa50aca02e92ed4a6b4d7},管理员:ReedCipher{574a5ee39f84e95a9ea1f10aa403b0fe},普通用户:ReedCipher{8d73ee1d511eb5a0}".getBytes(), DESUtil.DEFAULT_SECURITY_CODE);
//        try {
//            System.out.println(new String(env1, "UTF8"));
//            System.out.println(StringUtil.decryptCiphertext(new String(env1, "UTF8"), DESUtil.DEFAULT_SECURITY_CODE));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        String asdf = StringUtil.decryptCiphertext("访问地址：ReedCipher{1a82e2dc53775fc6bb0c8927846fa50aca02e92ed4a6b4d7},管理员:ReedCipher{574a5ee39f84e95a9ea1f10aa403b0fe},普通用户:ReedCipher{8d73ee1d511eb5a0}", DESUtil.DEFAULT_SECURITY_CODE);
        System.out.println(asdf);


        System.out.println(StringUtil.isIPv6("fe80:0000:0000:0000:0204:61ff:fe9d:f156"));
        System.out.println(StringUtil.isIPv6("fe80:0:0:0:204:61ff:fe9d:f156"));
        System.out.println(StringUtil.isIPv6("fe80::204:61ff:fe9d:f156"));
        System.out.println(StringUtil.isIPv6("fe80:0000:0000:0000:0204:61ff:254.157.241.86"));
        System.out.println(StringUtil.isIPv6("fe80:0:0:0:0204:61ff:254.157.241.86"));
        System.out.println(StringUtil.isIPv6("fe80::204:61ff:254.157.241.86"));
        System.out.println(StringUtil.isIPv6("::1"));
        System.out.println(StringUtil.isIPv6("fe80::"));
        System.out.println(StringUtil.isIPv6("2001::"));
        System.out.println(StringUtil.isIPv6("11.11.11.11"));

        System.out.println("----------------");
        System.out.println(StringUtil.isUrl("https://123.abc.hk?1=1&name=er"));
        System.out.println(StringUtil.isDomain("https://123.abc.hk/?1=1&name=er"));
        System.out.println(StringUtil.isDomain("123.abc.hk"));
        System.out.println(StringUtil.isWebsite("https://123.abc.hk/?1=1&name=er"));
        System.out.println(StringUtil.isWebsite("https://123.abc.hk"));
        System.out.println(StringUtil.isWebsite("https://123.abc.hk:9000"));

        System.out.println("-----------------");
        System.out.println(StringUtil.isUrl("http://127.0.0.1:8888/demo/getTest"));


        System.out.println("-----------------");
        System.out.println(StringUtil.isMacAddress("Unknown"));
        System.out.println(StringUtil.isMacAddress("00:e0:4c:68:12:ad"));
        System.out.println(StringUtil.isMacAddress("00-e0-4c-68-12-ad"));
        System.out.println(StringUtil.isMacAddress("00:e0:4q:6f:1F:AD"));
        System.out.println(StringUtil.isMacAddress("00-e0-4c-68:12:AD"));
    }
}
