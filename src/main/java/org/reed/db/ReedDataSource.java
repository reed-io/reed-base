/**
 * E5Projects @ org.reed.entity/ReedDataSource.java
 */
package org.reed.db;

/**
 * @author chenxiwen
 * @createTime 2019年09月19日 下午4:21
 * @description flow spring boot [2.1.8] https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html keyword: #DATASOURCE
 * only a few which developers often used are pick up here
 */
public abstract class ReedDataSource {

    private String url;
    private String username;
    private String password;
    private String separator; // = ";"; //default ；
    private String type; // = "com.alibaba.druid.pool.DruidDataSource"; //default ali druid
    private String driverClassName; // = "com.mysql.jdbc.Driver";

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }
}
