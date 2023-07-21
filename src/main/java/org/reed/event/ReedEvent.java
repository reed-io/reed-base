/**
 * reed_redis/org.reed.event/ReedEvent.java
 */
package org.reed.event;

import org.reed.standard.CallbackAble;
import org.reed.utils.StringUtil;

/**
 * @author chenxiwen
 * @date 2017年9月19日上午9:36:52
 */
public abstract class ReedEvent implements CallbackAble{
    private static final long TIME = 500L;
    
    private final String id;
    private String type;
    private Object data;
    private final long timestamp;

//    public abstract String uuid();
    public String uuid(){
        return super.getClass().getName();
    }

    public ReedEvent(){
        this.id = uuid();
        this.timestamp = System.currentTimeMillis();
    }
    
    public ReedEvent(String type){
        this();
        this.type = type;
    }
    
    public ReedEvent(String type, Object data){
        this();
        this.type = type;
        this.data = data;
    }
    
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    /**
     * @return the type
     */
    public String getType() {
        return StringUtil.isEmpty(type)?this.getClass().getSimpleName():type;
    }
    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * @return the data
     */
    public Object getData() {
        return data;
    }
    /**
     * @param data the data to set
     */
    public void setData(Object data) {
        this.data = data;
    }

    /**
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((data == null) ? 0 : data.hashCode());
        result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ReedEvent other = (ReedEvent) obj;
        if (data == null) {
            if (other.data != null)
                return false;
        } else if (!data.equals(other.data))
            return false;
        if (Math.abs(timestamp - other.timestamp)>TIME)
            return false;
        if (type == null) {
            return other.type == null;
        } else return type.equals(other.type);
    }
    
}
