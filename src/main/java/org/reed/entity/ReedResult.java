/**
 * push/org.reed.push.entity/ReedResult.java
 */
package org.reed.entity;

import org.reed.define.CodeDescTranslator;
import org.reed.utils.StringUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;

import java.io.Serializable;

/**
 * @author chenxiwen
 * @date 2017年8月7日下午9:07:51
 */
public class ReedResult<T> implements Serializable {
    private int code;
    private String message;
    private T data;



    /**
     * @return the message
     */
    public String getMessage() {
        return StringUtil.isEmpty(message)?CodeDescTranslator.explain(code): message;
    }




    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }




    /**
     * @param code the code to set
     */
    public void setCode(int code) {
        this.code = code;
    }




    /**
     * @return the data
     */
    public T getData() {
        return data;
    }




    /**
     * @param data the data to set
     */
    public void setData(T data) {
        this.data = data;
    }




    @Override
    public String toString(){
//        System.out.println(JSONObject.toJSONString(this, SerializerFeature.WRITE_MAP_NULL_FEATURES));
        if(StringUtil.isEmpty(this.message)){
            this.message = CodeDescTranslator.explain(code);
        }
        return JSON.toJSONString(this, JSONWriter.Feature.WriteNulls);
    }

    /**
     * 
     */
    public ReedResult() {
        // TODO Auto-generated constructor stub
    }

    public ReedResult(Builder<T> builder){
        this.code = builder.code;
        this.message = builder.message;
        this.data = builder.data;
    }


    public static class Builder<T>{
        private int code;
        private String message;
        private T data;

        public Builder<T> code(int code){
            this.code = code;
            return this;
        }

        public Builder<T> message(String message){
            this.message = message;
            return this;
        }

        public Builder<T> data(T data){
            this.data = data;
            return this;
        }

        public ReedResult<T> build(){
            return new ReedResult<>(this);
        }
    }
    
    public static void main(String[] args){
        long t = System.currentTimeMillis();
        ReedResult result = new ReedResult();
        System.out.println(result.toString());
        System.out.println(result.getMessage());
        System.out.println("cost:"+(System.currentTimeMillis()-t));
//        System.out.println(result.getData().toString());

        System.out.println(new Builder().code(0).data("asdfasdf").build());
    }

}
