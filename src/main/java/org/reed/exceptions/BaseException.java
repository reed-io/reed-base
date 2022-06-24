/**
 * E5Projects @ org.reed.exceptions/BaseException.java
 */
package org.reed.exceptions;

import org.reed.define.CodeDescTranslator;
import org.reed.utils.MapUtil;

import java.security.PrivilegedActionException;
import java.util.Arrays;
import java.util.Map;

/**
 * @author chenxiwen
 * @createTime 2020年09月03日 上午11:09
 * @description
 */
public abstract class BaseException extends Exception{

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public BaseException() {
        super();
    }

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public BaseException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * {@code cause} is <i>not</i> automatically incorporated in
     * this exception's detail message.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method).  (A <tt>null</tt> value is
     *                permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     * @since 1.4
     */
    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new exception with the specified cause and a detail
     * message of <tt>(cause==null ? null : cause.toString())</tt> (which
     * typically contains the class and detail message of <tt>cause</tt>).
     * This constructor is useful for exceptions that are little more than
     * wrappers for other throwables (for example, {@link
     * PrivilegedActionException}).
     *
     * @param cause the cause (which is saved for later retrieval by the
     *              {@link #getCause()} method).  (A <tt>null</tt> value is
     *              permitted, and indicates that the cause is nonexistent or
     *              unknown.)
     * @since 1.4
     */
    public BaseException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new exception with the specified detail message,
     * cause, suppression enabled or disabled, and writable stack
     * trace enabled or disabled.
     *
     * @param message            the detail message.
     * @param cause              the cause.  (A {@code null} value is permitted,
     *                           and indicates that the cause is nonexistent or unknown.)
     * @param enableSuppression  whether or not suppression is enabled
     *                           or disabled
     * @param writableStackTrace whether or not the stack trace should
     *                           be writable
     * @since 1.7
     */
    protected BaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * error code which should also defined in sub class of {@Link BaseErrorCode}
     * @return error code of this exception
     */
    public abstract int errorCode();


    public abstract Map<String, String> msgFmtMapper();


    public String getErrMsg(){
        return getErrMsg(null);
    }


    public <T extends Object>String getErrMsg(T t){
        if(MapUtil.isEmpty(msgFmtMapper())){
            return CodeDescTranslator.explain(errorCode());
        }
        String[] args = new String[msgFmtMapper().size()];
        msgFmtMapper().keySet().toArray(args);
        Arrays.stream(args).map(key -> key+":"+msgFmtMapper().get(key));
        return CodeDescTranslator.explain(errorCode(), t, args);
    }

    public String getErrMsg(String... args){
        return CodeDescTranslator.explain(errorCode(), null, args);
    }

    public <T extends Object>String getErrMsg(T t, String...args){
        return CodeDescTranslator.explain(errorCode(), t, args);
    }

}
