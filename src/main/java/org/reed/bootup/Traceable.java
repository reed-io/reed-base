/**
 * E5Projects @ org.reed.bootup/Traceable.java
 */
package org.reed.bootup;

import org.reed.define.HostColonPort;

import java.util.Set;

/**
 * @author chenxiwen
 * @createTime 2019年08月20日 上午11:09
 * @description service Traceable within Cluster
 */
public interface Traceable extends Reedable{

    void startServiceTrace(TraceArg traceArg);

    class TraceArg{
        //comma split if more than one server
        private String traceServer;
        //if 1 report every 10 calls then u should set 0.1
        private double percentage;
        //KAFKA, HTTP ETC...
        private TraceType traceType;

        private TraceKafka kafka;

        public String getTraceServer() {
            return traceServer;
        }

        public void setTraceServer(String traceServer) {
            this.traceServer = traceServer;
        }

        public double getPercentage() {
            return percentage;
        }

        public void setPercentage(double percentage) {
            this.percentage = percentage;
        }

        public TraceType getTraceType() {
            return traceType;
        }

        public void setTraceType(TraceType traceType) {
            this.traceType = traceType;
        }

        public TraceKafka getKafka() {
            return kafka;
        }

        public void setKafka(TraceKafka kafka) {
            this.kafka = kafka;
        }

        @Override
        public String toString() {
            return "TraceArg{" +
                    "traceServer='" + traceServer + '\'' +
                    ", percentage=" + percentage +
                    ", traceType=" + traceType +
                    ", kafka=" + kafka +
                    '}';
        }
    }

    enum TraceType{
        HTTP,KAFKA
    }

    class TraceKafka{
        private Set<HostColonPort> hosts;
        private String topic;

        public Set<HostColonPort> getHosts() {
            return hosts;
        }

        public void setHosts(Set<HostColonPort> hosts) {
            this.hosts = hosts;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        @Override
        public String toString() {
            return "TraceKafka{" +
                    "hosts=" + hosts +
                    ", topic='" + topic + '\'' +
                    '}';
        }
    }
}
