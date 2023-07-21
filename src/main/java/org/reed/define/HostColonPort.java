/**
 * E5Projects @ org.reed.define/IPColonPort.java
 */
package org.reed.define;

import org.reed.utils.StringUtil;

/**
 * @author chenxiwen
 * @createTime 2019年08月26日 上午11:17
 * @description host:port Object for developers
 */
public class HostColonPort {
    public static final String COLON = ":";

    private String host;
    private int port;

    public HostColonPort(String host, int port) {
        if(!StringUtil.isIPV4(host) && !StringUtil.isIPv6(host) && !StringUtil.isDomain(host)){
            throw new RuntimeException("only validate ipv4 supported");
        }
        if(!StringUtil.isPort(port)){
            throw new RuntimeException("port should within [0. 65535]");
        }
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        if(!StringUtil.isIPV4(host) && !StringUtil.isIPv6(host) && !StringUtil.isDomain(host)){
            throw new RuntimeException("only validate ipv4 supported");
        }
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        if(!StringUtil.isPort(port)){
            throw new RuntimeException("port should within [0. 65535]");
        }
        this.port = port;
    }

    @Override
    public String toString() {
        return "HostColonPort{" +
                "host='" + host + '\'' +
                ", port=" + port +
                '}';
    }

    public String toSimpleString(){
        return host+":"+port;
    }

    public static HostColonPort parse(String str){
        if(StringUtil.isEmpty(str)){
            throw new RuntimeException("HostColonPort parse args is Empty!");
        }
        if(str.lastIndexOf(COLON)==-1 || str.lastIndexOf(COLON) == str.length()-1){
            throw new RuntimeException(COLON+" check failed with "+str);
        }
        String portStr = str.substring(str.lastIndexOf(COLON)+1);
        String hostStr = str.substring(0, str.lastIndexOf(COLON));
        return new HostColonPort(hostStr, Integer.parseInt(portStr));
    }
}
