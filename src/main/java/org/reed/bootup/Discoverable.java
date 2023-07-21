/**
 * IdeaProject @ org.reed.bootup/Discoverable.java
 */
package org.reed.bootup;

/**
 * @author chenxiwen
 * @createTime 2019年07月15日 下午1:50
 * @description If ur service is defined as a micro language you should implements this interface
 */
public interface Discoverable extends Reedable{

    /**
     * 注册
     */
    void register(DiscoverArg arg);

    class DiscoverArg{
        private String serviceName;
        private String serverUrl;
        private EnableServiceRegister.DiscoveryProduction discoveryProduction;
        private String namespace;
        private String group;

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public String getServerUrl() {
            return serverUrl;
        }

        public void setServerUrl(String serverUrl) {
            this.serverUrl = serverUrl;
        }

        public EnableServiceRegister.DiscoveryProduction getDiscoveryProduction() {
            return discoveryProduction;
        }

        public void setDiscoveryProduction(EnableServiceRegister.DiscoveryProduction discoveryProduction) {
            this.discoveryProduction = discoveryProduction;
        }

        public String getNamespace() {
            return namespace;
        }

        public void setNamespace(String namespace) {
            this.namespace = namespace;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        @Override
        public String toString() {
            return "DiscoverArg{" +
                    "serviceName='" + serviceName + '\'' +
                    ", serverUrl='" + serverUrl + '\'' +
                    ", discoveryProduction=" + discoveryProduction +
                    ", namespace='" + namespace + '\'' +
                    ", group='" + group + '\'' +
                    '}';
        }
    }

}
