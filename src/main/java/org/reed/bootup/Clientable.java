/**
 * E5Projects @ org.reed.bootup/Clientable.java
 */
package org.reed.bootup;


/**
 * @author chenxiwen
 * @createTime 2019年12月17日 下午4:00
 * @description
 */
public interface Clientable extends Reedable{

    void config(Configration configration);

    class Configration{
        private String username;
        private String password;
        private String serverUrl;

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

        public String getServerUrl() {
            return serverUrl;
        }

        public void setServerUrl(String serverUrl) {
            this.serverUrl = serverUrl;
        }

        @Override
        public String toString() {
            return "Configration{" +
                    "username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    ", serverUrl='" + serverUrl + '\'' +
                    '}';
        }
    }
}
