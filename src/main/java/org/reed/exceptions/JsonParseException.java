/**
 * base/org.reed.exceptions/JsonParseException.java
 */
package org.reed.exceptions;

import org.reed.standard.CallbackAble;

/**
 * @author chenxiwen
 * @date 2017年8月30日下午8:10:47
 */
public class JsonParseException extends ReedBaseException implements CallbackAble {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /* (non-Javadoc)
     * @see org.reed.exceptions.ReedBaseException#prepare()
     */
    @Override
    public boolean prepare() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.reed.standard.CallbackAble#callbackHandler()
     */
    @Override
    public void callbackHandler(Object... obj) {
        // TODO Auto-generated method stub
    }

    /**
     * 
     */
    public JsonParseException() {
        super();
//        setErrorCode(BaseErrorCode.JsonParseException);
    }

    /**
     * @param errorCode
     * @param callback
     */
    public JsonParseException(int errorCode, CallbackAble callback) {
//        super(BaseErrorCode.JsonParseException, callback);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param errorCode
     * @param excepMsg
     * @param callback
     */
    public JsonParseException(int errorCode, String excepMsg, CallbackAble callback) {
//        super(BaseErrorCode.JsonParseException, excepMsg, callback);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param errorCode
     * @param excepMsg
     * @param cause
     * @param callback
     */
    public JsonParseException(int errorCode, String excepMsg, Throwable cause, CallbackAble callback) {
//        super(BaseErrorCode.JsonParseException, excepMsg, cause, callback);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param excepMsg
     * @param cause
     */
    public JsonParseException(String excepMsg, Throwable cause) {
        super(excepMsg, cause);
//        setErrorCode(BaseErrorCode.JsonParseException);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param excepMsg
     */
    public JsonParseException(String excepMsg) {
        super(excepMsg);
//        setErrorCode(BaseErrorCode.JsonParseException);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public JsonParseException(Throwable cause) {
        super(cause);
//        setErrorCode(BaseErrorCode.JsonParseException);
        // TODO Auto-generated constructor stub
    }
    
    
}
