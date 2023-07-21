/**
 * base/org.reed.exceptions/ReedBaseException.java
 */
package org.reed.exceptions;

import org.reed.standard.CallbackAble;

/**
 * @author chenxiwen
 * @date 2017年8月9日上午10:24:55
 */
public abstract class ReedBaseException extends Exception {

    private int errorCode;
    
    private CallbackAble callback;

    /**
     * 
     */
    private static final long serialVersionUID = 3323209422355615729L;

    /**
     * 
     */
    public ReedBaseException() {
        // TODO Auto-generated constructor stub
    }
    
    public ReedBaseException(int errorCode, CallbackAble callback){
        this.errorCode = errorCode;
        this.callback = callback;
        handle();
    }
    
    public ReedBaseException(int errorCode, String excepMsg, CallbackAble callback){
        super(excepMsg);
        this.errorCode = errorCode;
        this.callback = callback;
        handle();
    }
    
    public ReedBaseException(int errorCode, String excepMsg, Throwable cause, CallbackAble callback){
        super(excepMsg, cause);
        this.errorCode = errorCode;
        this.callback = callback;
        handle();
    }

    /**
     * @param excepMsg
     */
    public ReedBaseException(String excepMsg) {
        super(excepMsg);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public ReedBaseException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * 
     * @param excepMsg
     * @param cause
     */
    public ReedBaseException(String excepMsg, Throwable cause) {
        super(excepMsg, cause);
        // TODO Auto-generated constructor stub
    }

//    /**
//     * 
//     * @param excepMsg
//     * @param cause
//     * @param enableSuppression
//     * @param writableStackTrace
//     */
//    public ReedBaseException(String excepMsg, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
//        super(excepMsg, cause, enableSuppression, writableStackTrace);
//        // TODO Auto-generated constructor stub
//    }
    
    protected void handle(){
        if(prepare() && null!=this.getCallback()){
            this.callback.callbackHandler(this);
        }
    }
    
    public abstract boolean prepare();


    /**
     * @return the errorCode
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * @param errorCode the errorCode to set
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * @return the callback
     */
    protected CallbackAble getCallback() {
        return callback;
    }

    /**
     * @param callback the callback to set
     */
    protected void setCallback(CallbackAble callback) {
        this.callback = callback;
    }

}
