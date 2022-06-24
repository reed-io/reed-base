/**
 * springbootup @ org.reed.log.LoggerObject.java
 */
package org.reed.log;

/**
 * @author chenxiwen
 * @createTime 2021-12-12 14:54
 */
public final class LoggerObject {
    public ReedLoggerConfig.ReedLoggerLevel loggerLevel;
    public String content;
    public Throwable throwable;
    public String className;
    public String methodName;
    public int lineNumber;
    public long timestamp;
    public ThreadGroup threadGroup;
    public Thread thread;
    public long threadId;

    public LoggerObject(ReedLoggerConfig.ReedLoggerLevel loggerLevel, String content, Throwable throwable, String className, String methodName, int lineNumber, long timestamp, ThreadGroup threadGroup, Thread thread, long threadId) {
        this.loggerLevel = loggerLevel;
        this.content = content;
        this.throwable = throwable;
        this.className = className;
        this.methodName = methodName;
        this.lineNumber = lineNumber;
        this.timestamp = timestamp;
        this.threadGroup = threadGroup;
        this.thread = thread;
        this.threadId = threadId;
    }

    public ReedLoggerConfig.ReedLoggerLevel getLoggerLevel() {
        return loggerLevel;
    }

    public void setLoggerLevel(ReedLoggerConfig.ReedLoggerLevel loggerLevel) {
        this.loggerLevel = loggerLevel;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public ThreadGroup getThreadGroup() {
        return threadGroup;
    }

    public void setThreadGroup(ThreadGroup threadGroup) {
        this.threadGroup = threadGroup;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public long getThreadId() {
        return threadId;
    }

    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }

    @Override
    public String toString() {
        return "LoggerObject{" +
                "loggerLevel=" + loggerLevel +
                ", content='" + content + '\'' +
                ", throwable=" + throwable +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", lineNumber=" + lineNumber +
                ", timestamp=" + timestamp +
                ", threadGroup=" + threadGroup +
                ", thread=" + thread +
                ", threadId=" + threadId +
                '}';
    }
}
