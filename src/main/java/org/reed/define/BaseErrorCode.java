/**
 * base/org.reed.define/BaseErrorCode.java
 */
package org.reed.define;

/**
 * @author chenxiwen
 * @date 2017年8月16日上午10:44:41
 *
 */
public abstract class BaseErrorCode {

    @CodeDescTag(desc="操作处理成功")
    public static final int SUCCESS_OPERATE=0x0000;

    @CodeDescTag(desc = "Unknown Error Detected By Reed Framework")
    public static final int UNKNOWN_ERROR = -1;

    @CodeDescTag(desc="Reed Language service Not Reachable This Time!")
    public static final int LANGUAGE_SERVICE_HYSTRIX = 0x0001;

    @CodeDescTag(desc="The API You Are Calling Needs Reed User Token, Please Contact The Administrator!")
    public static final int REED_USER_TOKEN_MISSED = 0x0002;
    


}
