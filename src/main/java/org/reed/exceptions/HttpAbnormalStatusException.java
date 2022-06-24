/**
 * base/org.reed.exceptions/HttpAbnormalStatusException.java
 */
package org.reed.exceptions;

import org.reed.standard.CallbackAble;

/**
 * @author chenxiwen
 * @date 2017年8月9日上午11:08:36
 */
public class HttpAbnormalStatusException extends ReedBaseException {

    private final int errCode = -1;

    /**
     * 
     */
    private static final long serialVersionUID = -7899534681403519577L;

    /**
     * 
     */
    public HttpAbnormalStatusException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param errorCode
     * @param callback
     */
    public HttpAbnormalStatusException(int errorCode, CallbackAble callback) {
        super(errorCode, callback);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param errorCode
     * @param excepMsg
     * @param callback
     */
    public HttpAbnormalStatusException(int errorCode, String excepMsg, CallbackAble callback) {
        super(errorCode, excepMsg, callback);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param errorCode
     * @param excepMsg
     * @param cause
     * @param callback
     */
    public HttpAbnormalStatusException(int errorCode, String excepMsg, Throwable cause, CallbackAble callback) {
        super(errorCode, excepMsg, cause, callback);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param excepMsg
     */
    public HttpAbnormalStatusException(String excepMsg) {
        super(excepMsg);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public HttpAbnormalStatusException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param excepMsg
     * @param cause
     */
    public HttpAbnormalStatusException(String excepMsg, Throwable cause) {
        super(excepMsg, cause);
        // TODO Auto-generated constructor stub
    }

//    /**
//     * @param excepMsg
//     * @param cause
//     * @param enableSuppression
//     * @param writableStackTrace
//     */
//    public HttpAbnormalStatusException(String excepMsg, Throwable cause, boolean enableSuppression,
//            boolean writableStackTrace) {
//        super(excepMsg, cause, enableSuppression, writableStackTrace);
//        // TODO Auto-generated constructor stub
//    }

    /* (non-Javadoc)
     * @see org.reed.exceptions.ReedBaseException#prepare()
     */
    @Override
    public boolean prepare() {
        // TODO Auto-generated method stub
        return false;
    }

}
