/**
 * E5Projects @ org.reed.log/ReedLoggerTest.java
 */
package org.reed.log;

import org.reed.utils.EnderUtil;

/**
 * @author chenxiwen
 * @createTime 2019年07月26日 下午3:17
 * @description
 */
public class ReedLoggerTest {
    public static void main(String[] args){
        System.out.println("abc");
        ReedLogger.debug(EnderUtil.devInfo()+"123");
    }
}
