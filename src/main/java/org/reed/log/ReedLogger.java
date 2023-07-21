/**
 * IdeaProject @ org.reed.log/ReedLoger.java
 */
package org.reed.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.jul.LevelChangePropagator;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.rolling.TimeBasedFileNamingAndTriggeringPolicy;
import ch.qos.logback.core.util.FileSize;
import ch.qos.logback.core.util.OptionHelper;
//import org.reed.system.ReedContext;
import org.reed.event.EventCenter;
import org.reed.utils.StringUtil;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author chenxiwen
 * @createTime 2018年11月13日 下午5:57
 * @description
 */
public final class ReedLogger {
    public static final String DEFAULT_PATTERN = "%d{yyyy-MM-dd HH:mm:ss} [%thread] <%-5level> {%logger} - %msg%n";

    private static final String NAME = "Reed";

    private static final LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

//    private static final LogAlarmHandler logAlarmHandler = LogAlarmHandler.getInstance();

//    private static Logger logger = context.getLogger(NAME);
    private static Logger logger;

    private static boolean useStaticLogger = false;
    private static boolean enableFileAppender = false;
    private static boolean showMethodName = false;
    private static boolean showLineNumber = false;
    private static boolean enableAlarm = true;
    private static ReedLoggerConfig.ReedLoggerLevel alarmLevel = ReedLoggerConfig.ReedLoggerLevel.ERROR;

    private ReedLogger(){
//        NAME = Starter.defaultModuleName();
//        logger = genLogger(NAME);
    }

    static{
        if(logger == null){
            logger = genLogger(NAME);
        }
    }

    public static boolean init(final String name, final boolean useStaticLogger, final boolean enableFileAppender,
                               final boolean showMethodName, final boolean showLineNumber,
                               final boolean enableAlarm, final ReedLoggerConfig.ReedLoggerLevel alarmLevel){
        ReedLogger.useStaticLogger = useStaticLogger;
        ReedLogger.enableFileAppender = enableFileAppender;
        ReedLogger.showMethodName = showMethodName;
        ReedLogger.showLineNumber = showLineNumber;
        ReedLogger.enableAlarm = enableAlarm;
        ReedLogger.alarmLevel = alarmLevel;
        return init(name);
    }

    public static boolean init(final String name){
        if(StringUtil.isEmpty(name)){
            return false;
        }
        logger = genLogger(name);
//        if(enableAlarm){
//            EventCenter.addEventListener(LogAlarmEvent.class, LogAlarmHandler.getInstance());
//        }
        return logger != null;
    }

    private static Logger genLogger(String name){

        LevelChangePropagator levelChangePropagator = new LevelChangePropagator();
        levelChangePropagator.setContext(context);
        levelChangePropagator.setResetJUL(true);
//        levelChangePropagator.start();
        context.addListener(levelChangePropagator);
        context.putObject("org.springframework.boot.logging.LoggingSystem", new Object());
        logger = context.getLogger(name);
        logger.setLevel(Level.DEBUG);

//        <logger name="org.springframework.data.mybatis" level="INFO"/>
        Logger mybatisLogger = context.getLogger("org.springframework.data.mybatis");
        mybatisLogger.setLevel(Level.INFO);
//        <logger name="org.springframework.aop.aspectj" level="ERROR"/>
        Logger aspectjLogger = context.getLogger("org.springframework.aop.aspectj");
        aspectjLogger.setLevel(Level.ERROR);
//        <logger name="org.springframework.web" level="INFO"/>
        Logger webLogger = context.getLogger("org.springframework.web");
        webLogger.setLevel(Level.INFO);
//        <logger name="org.springframework.security" level="WARN"/>
        Logger securityLogger = context.getLogger("org.springframework.security");
        securityLogger.setLevel(Level.WARN);
//        <logger name="org.springframework.cache" level="WARN"/>
        Logger cacheLogger = context.getLogger("org.springframework.cache");
        cacheLogger.setLevel(Level.WARN);
//        <logger name="org.springframework.core" level="WARN"/>
        Logger springCore = context.getLogger("org.springframework.core");
        springCore.setLevel(Level.INFO);
//        <logger name="org.springframework.boot" level="INFO"/>
        Logger springBoot = context.getLogger("org.springframework.boot");
        springBoot.setLevel(Level.INFO);
//        <logger name="org.springframework.beans" level="INFO"/>
        Logger springBeans = context.getLogger("org.springframework.beans");
        springBeans.setLevel(Level.INFO);
//        <logger name="org.springframework" level="INFO"/>
        Logger spring = context.getLogger("org.springframework");
        spring.setLevel(Level.INFO);
//        <logger name="org.apache.kafka" level="INFO"/>
        Logger kafkaSpringLogger = context.getLogger("org.apache.kafka");
        kafkaSpringLogger.setLevel(Level.INFO);
//        <logger name="org.mybatis.spring" level="INFO"/>
        Logger mybatisSpringLogger = context.getLogger("org.mybatis.spring");
        mybatisSpringLogger.setLevel(Level.INFO);
//        <logger name="org.mongodb.driver.cluster" level="WARN" />
        Logger mongoLogger = context.getLogger("org.mongodb.driver.cluster");
        mongoLogger.setLevel(Level.WARN);

//    <logger name="javax.activation" level="WARN"/>
        Logger activationLogger = context.getLogger("javax.activation");
        activationLogger.setLevel(Level.WARN);
//    <logger name="javax.mail" level="WARN"/>
        Logger mailLogger = context.getLogger("javax.mail");
        mailLogger.setLevel(Level.WARN);
//    <logger name="javax.xml.bind" level="WARN"/>
        Logger xmlBindLogger = context.getLogger("javax.xml.bind");
        xmlBindLogger.setLevel(Level.WARN);
//    <logger name="ch.qos.logback" level="WARN"/>
        Logger logback = context.getLogger("ch.qos.logback");
        logback.setLevel(Level.WARN);
//    <logger name="com.sun" level="WARN"/>
        Logger sunLogger = context.getLogger("com.sun");
        sunLogger.setLevel(Level.WARN);
//    <logger name="org.apache" level="WARN"/>
        Logger apacheLogger = context.getLogger("org.apache");
        apacheLogger.setLevel(Level.WARN);
//    <logger name="org.apache.catalina.startup.DigesterFactory" level="OFF"/>
        Logger digesterFactoryLogger = context.getLogger("org.apache.catalina.startup.DigesterFactory");
        digesterFactoryLogger.setLevel(Level.OFF);
//    <logger name="org.hibernate.validator" level="WARN"/>
        Logger hibernateValidatorLogger = context.getLogger("org.hibernate.validator");
        hibernateValidatorLogger.setLevel(Level.WARN);
//    <logger name="org.hibernate" level="WARN"/>
        Logger hibernateLogger = context.getLogger("org.hibernate");
        hibernateLogger.setLevel(Level.WARN);
//    <logger name="org.hibernate.ejb.HibernatePersistence" level="OFF"/>
        Logger hibernatePersistenceLogger = context.getLogger("org.hibernate.ejb.HibernatePersistence");
        hibernatePersistenceLogger.setLevel(Level.OFF);
//    <logger name="sun.rmi.transport" level="WARN"/>
        Logger rmiTransportLogger = context.getLogger("sun.rmi.transport");
        rmiTransportLogger.setLevel(Level.WARN);

//            <logger name="org.reed" level="DEBUG"/>
        Logger reedLogger = context.getLogger("org.reed");
        reedLogger.setLevel(Level.INFO);
//    <logger name="com.reed" level="DEBUG"/>
        Logger reedOrgLogger = context.getLogger("com.reed");
        reedOrgLogger.setLevel(Level.DEBUG);
//    <logger name="org.reed.dao" level="INFO" />
        Logger orgReedDaoLogger = context.getLogger("org.reed.dao");
        orgReedDaoLogger.setLevel(Level.INFO);
//    <logger name="com.reed.dao" level="INFO"/>
        Logger comReedDaoLogger = context.getLogger("com.reed.dao");
        comReedDaoLogger.setLevel(Level.INFO);

        context.getLogger("ROOT").detachAndStopAllAppenders();
        context.getLogger("ROOT").setAdditive(true);
        context.getLogger("ROOT").setLevel(Level.DEBUG);
        if(enableFileAppender){
            Appender rollingFileAppender = rollingFileAppender(context);
            context.getLogger("ROOT").addAppender(rollingFileAppender);
        }
        Appender consoleAppender = consoleAppender(context);
        context.getLogger("ROOT").addAppender(consoleAppender);
        context.getLogger("ROOT").setAdditive(false);
//        context.start();

//        logger.addAppender(consoleAppender);
//        logger.addAppender(rollingFileAppender);
//        ConfigurationAction ca = new ConfigurationAction();


//        System.out.println("~~~"+logger.getName());
//        List<Logger> list = context.getLoggerList();
//        for(Logger l : list){
//            System.out.println(">>"+l.getName()+" - "+l.getLevel()+" - "+l.getEffectiveLevel());
//            Iterator<Appender<ILoggingEvent>> iterator = l.iteratorForAppenders();
//            while(iterator.hasNext()){
//                Appender a = iterator.next();
//                System.out.println("\t\t@"+a.toString());
//            }
//        }


        return logger;
    }

    public static Logger getLogger(){
        return logger;
    }

    public static void setLevel(String name, Level level){
        Logger logger = context.getLogger(name);
        logger.setLevel(level);
    }

    public static void info(String content, Throwable thrown){
        if(useStaticLogger){
            logger.info(contentProxy(content), thrown);
        }else{
            context.getLogger(loggerProxy()).info(contentProxy(content), thrown);
        }
        if(enableAlarm && ReedLoggerConfig.ReedLoggerLevel.INFO.getIdx() >= alarmLevel.getIdx()){
            LoggerObject loggerObject = genLoggerObject(ReedLoggerConfig.ReedLoggerLevel.INFO, content, thrown);
            EventCenter.fire(new LogAlarmEvent(loggerObject));
        }
    }

    public static void info(String content){
        if(useStaticLogger){
            logger.info(contentProxy(content));
        }else{
            context.getLogger(loggerProxy()).info(contentProxy(content));
        }
        if(enableAlarm && ReedLoggerConfig.ReedLoggerLevel.INFO.getIdx() >= alarmLevel.getIdx()){
            LoggerObject loggerObject = genLoggerObject(ReedLoggerConfig.ReedLoggerLevel.INFO, content, null);
            EventCenter.fire(new LogAlarmEvent(loggerObject));
        }
    }

    protected static void info(String content, String className){
        context.getLogger(className).info(content);
    }

    protected static void info(String content, Throwable thrown, String className){
        context.getLogger(className).info(content, thrown);
    }

    public static void debug(String content){
        if(useStaticLogger){
            logger.debug(contentProxy(content));
        }else{
            context.getLogger(loggerProxy()).debug(contentProxy(content));
        }
        if(enableAlarm && ReedLoggerConfig.ReedLoggerLevel.DEBUG.getIdx() >= alarmLevel.getIdx()){
            LoggerObject loggerObject = genLoggerObject(ReedLoggerConfig.ReedLoggerLevel.DEBUG, content, null);
            EventCenter.fire(new LogAlarmEvent(loggerObject));
        }
    }

    public static void debug(String content, Throwable thrown){
        if(useStaticLogger){
            logger.debug(contentProxy(content), thrown);
        }else{
            context.getLogger(loggerProxy()).debug(contentProxy(content), thrown);
        }
        if(enableAlarm && ReedLoggerConfig.ReedLoggerLevel.DEBUG.getIdx() >= alarmLevel.getIdx()){
            LoggerObject loggerObject = genLoggerObject(ReedLoggerConfig.ReedLoggerLevel.DEBUG, content, thrown);
            EventCenter.fire(new LogAlarmEvent(loggerObject));
        }
    }

    protected static void debug(String content, String className){context.getLogger(className).debug(content);}

    protected static void debug(String content, Throwable thrown, String className){context.getLogger(className).debug(content, thrown);}

    public static void error(String content, Throwable thrown){
        if(useStaticLogger){
            logger.error(contentProxy(content), thrown);
        }else{
            context.getLogger(loggerProxy()).error(contentProxy(content), thrown);
        }
        if(enableAlarm && ReedLoggerConfig.ReedLoggerLevel.ERROR.getIdx() >= alarmLevel.getIdx()){
            LoggerObject loggerObject = genLoggerObject(ReedLoggerConfig.ReedLoggerLevel.ERROR, content, thrown);
            EventCenter.fire(new LogAlarmEvent(loggerObject));
        }
    }

    public static void error(String content){
        if(useStaticLogger){
            logger.error(contentProxy(content));
        }else{
            context.getLogger(loggerProxy()).error(contentProxy(content));
        }
        if(enableAlarm && ReedLoggerConfig.ReedLoggerLevel.ERROR.getIdx() >= alarmLevel.getIdx()){
            LoggerObject loggerObject = genLoggerObject(ReedLoggerConfig.ReedLoggerLevel.ERROR, content, null);
            EventCenter.fire(new LogAlarmEvent(loggerObject));
        }
    }

    protected static void error(String content, String className){context.getLogger(className).error(content);}

    protected static void error(String content, Throwable thrown, String className){context.getLogger(className).error(content, thrown);}

    public static void trace(String content, Throwable thrown){
        if(useStaticLogger){
            logger.trace(contentProxy(content), thrown);
        }else{
            context.getLogger(loggerProxy()).trace(contentProxy(content), thrown);
        }
        if(enableAlarm && ReedLoggerConfig.ReedLoggerLevel.TRACE.getIdx() >= alarmLevel.getIdx()){
            LoggerObject loggerObject = genLoggerObject(ReedLoggerConfig.ReedLoggerLevel.TRACE, content, thrown);
            EventCenter.fire(new LogAlarmEvent(loggerObject));
        }
    }

    public static void trace(String content){
        if(useStaticLogger){
            logger.trace(contentProxy(content));
        }else{
            context.getLogger(loggerProxy()).trace(contentProxy(content));
        }
        if(enableAlarm && ReedLoggerConfig.ReedLoggerLevel.TRACE.getIdx() >= alarmLevel.getIdx()){
            LoggerObject loggerObject = genLoggerObject(ReedLoggerConfig.ReedLoggerLevel.TRACE, content, null);
            EventCenter.fire(new LogAlarmEvent(loggerObject));
        }
    }

    protected static void trace(String content, String className){context.getLogger(className).trace(content);}

    protected static void trace(String content, Throwable thrown, String className){context.getLogger(className).trace(content, thrown);}

    public static void warn(String content, Throwable thrown){
        if(useStaticLogger){
            logger.warn(contentProxy(content), thrown);
        }else{
            context.getLogger(loggerProxy()).warn(contentProxy(content), thrown);
        }
        if(enableAlarm && ReedLoggerConfig.ReedLoggerLevel.WARN.getIdx() >= alarmLevel.getIdx()){
            LoggerObject loggerObject = genLoggerObject(ReedLoggerConfig.ReedLoggerLevel.WARN, content, thrown);
            EventCenter.fire(new LogAlarmEvent(loggerObject));
        }
    }

    public static void warn(String content){
        if(useStaticLogger){
            logger.warn(contentProxy(content));
        }else{
            context.getLogger(loggerProxy()).warn(contentProxy(content));
        }
        if(enableAlarm && ReedLoggerConfig.ReedLoggerLevel.WARN.getIdx() >= alarmLevel.getIdx()){
            LoggerObject loggerObject = genLoggerObject(ReedLoggerConfig.ReedLoggerLevel.WARN, content, null);
            EventCenter.fire(new LogAlarmEvent(loggerObject));
        }
    }

    protected static void warn(String content, String className){context.getLogger(className).warn(content);}

    protected static void warn(String content, Throwable thrown, String className){context.getLogger(className).warn(content, thrown);}

    public static String getName(){
        return logger.getName();
    }


    private static RollingFileAppender rollingFileAppender(final LoggerContext context){
        RollingFileAppender appender = new RollingFileAppender();
        appender.setName("rf-file");
        appender.setContext(context);
//        System.out.println(ReedContext.getString("user.dir")+ File.separator+"rflog"+File.separator+logger.getName()+".log");
        System.out.println(System.getProperty("user.dir")+ File.separator+"rflog"+File.separator+logger.getName()+".log");
//        appender.setFile(OptionHelper.substVars(ReedContext.getString("user.dir")+ File.separator+"rflog"+File.separator+logger.getName()+".log", context));
        appender.setFile(OptionHelper.substVars(System.getProperty("user.dir")+ File.separator+"rflog"+File.separator+logger.getName()+".log", context));
        appender.setAppend(true);
        appender.setPrudent(false);



        SizeAndTimeBasedRollingPolicy policy = new SizeAndTimeBasedRollingPolicy();
//        String pattern = OptionHelper.substVars(ReedContext.getString("user.dir")+ File.separator+"rflog"+File.separator+logger.getName()+"-%d{yyyyMMdd}[%i].log", context);
        String pattern = OptionHelper.substVars(System.getProperty("user.dir")+ File.separator+"rflog"+File.separator+logger.getName()+"-%d{yyyyMMdd}[%i].log", context);
        policy.setMaxFileSize(FileSize.valueOf("10MB"));
        policy.setFileNamePattern(pattern);
//        policy.setMaxHistory(15);
//        policy.setTotalSizeCap(FileSize.valueOf("32GB"));
        policy.setParent(appender);
        policy.setContext(context);

        TimeBasedFileNamingAndTriggeringPolicy timeBasedFileNamingAndTriggeringPolicy = new SizeAndTimeBasedFNATP();
        timeBasedFileNamingAndTriggeringPolicy.setContext(context);
        ((SizeAndTimeBasedFNATP) timeBasedFileNamingAndTriggeringPolicy).setMaxFileSize(FileSize.valueOf("10MB"));
        timeBasedFileNamingAndTriggeringPolicy.setTimeBasedRollingPolicy(policy);


        policy.setTimeBasedFileNamingAndTriggeringPolicy(timeBasedFileNamingAndTriggeringPolicy);

//        SizeAndTimeBasedFNATP satbf = new SizeAndTimeBasedFNATP();
//        satbf.setMaxFileSize(FileSize.valueOf("10MB"));
//        satbf.setContext(context);
//        satbf.setTimeBasedRollingPolicy(policy);
//        satbf.start();

        policy.start();

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern(DEFAULT_PATTERN);
        encoder.setCharset(StandardCharsets.UTF_8);
        encoder.start();

        appender.setRollingPolicy(policy);
        appender.setEncoder(encoder);
        appender.start();

        return appender;
    }


    private static ConsoleAppender consoleAppender(final LoggerContext context){
        ConsoleAppender appender = new ConsoleAppender();
        appender.setName("rf-console");
        appender.setContext(context);

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern(DEFAULT_PATTERN);
        encoder.start();

        appender.setEncoder(encoder);
        appender.start();

        return appender;
    }

    private static String loggerProxy(){
        return Thread.currentThread().getStackTrace()[3].getClassName();
    }

    private static String contentProxy(String content){
        return (showMethodName?"["+Thread.currentThread().getStackTrace()[3].getMethodName()+"]":"") +
                (showLineNumber?"["+Thread.currentThread().getStackTrace()[3].getLineNumber()+"]":"") +
                content;
    }

    private static LoggerObject genLoggerObject(ReedLoggerConfig.ReedLoggerLevel reedLoggerLevel, String content, Throwable thrown){
        String className = Thread.currentThread().getStackTrace()[3].getClassName();
        String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
        int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
        Thread thread = Thread.currentThread();
        LoggerObject loggerObject = new LoggerObject(reedLoggerLevel, content, thrown, className, methodName, lineNumber,
                                        System.currentTimeMillis(), thread.getThreadGroup(), thread, thread.getId());
        return loggerObject;
    }
}
