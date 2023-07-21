/**
 * E5Projects @ org.reed.exceptions/NormalException.java
 */
package org.reed.exceptions;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenxiwen
 * @createTime 2020年09月07日 上午10:33
 * @description
 */
public class NormalException extends BaseException{

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public NormalException(String message) {
        super(message);
    }

    /**
     * error code which should also defined in sub class of {@Link BaseErrorCode}
     *
     * @return error code of this exception
     */
    @Override
    public int errorCode() {
        return -1;
    }

    @Override
    public Map<String, String> msgFmtMapper() {
        Map<String, String> map = new HashMap();
        map.put("arg1", "value1");
        map.put("arg2", "value2");
        map.put("arg3", "value3");
        return map;
    }

    public static void main(String[] args){
        NormalException e = new NormalException("tst message");
        NormalOct obj = new NormalOct("test1", "test2", "test3");
        String msg = e.getErrMsg(obj);
        System.out.println(msg);
    }

    static class NormalOct{

        public NormalOct(String arg1, String arg2, String arg3) {
            this.arg1 = arg1;
            this.arg2 = arg2;
            this.arg3 = arg3;
        }

        private final String arg1;
        private final String arg2;
        private final String arg3;
    }
}
