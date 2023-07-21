/**
 * E5Projects @ org.reed.bootup/ReedStarter.java
 */
package org.reed.bootup;

import org.reed.define.BaseErrorCode;
import org.reed.define.CodeDescTranslator;
import org.reed.define.HostColonPort;
import org.reed.event.EventCenter;
import org.reed.exceptions.EnderBusinessException;
import org.reed.exceptions.EnderRuntimeException;
import org.reed.log.LogAlarmEvent;
import org.reed.log.LogAlarmHandler;
import org.reed.log.ReedLogger;
import org.reed.log.ReedLoggerConfig;
import org.reed.system.ReedContext;
import org.reed.system.SysEngine;
import org.reed.utils.*;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

//import com.sun.tools.attach.AgentInitializationException;
//import com.sun.tools.attach.AgentLoadException;
//import com.sun.tools.attach.VirtualMachine;

/**
 * @author chenxiwen
 * @createTime 2019年08月02日 下午2:03
 * @description
 */
public abstract class ReedStarter {
    public static final String FRAMEWORK_CONFIG_FILE = "reed.yml";
    public static final String AGENT_DIR_MD5 = "6dc21be4e3ebb4fba9da3522816ccd3e";
    public static final String AGENT_FILE_MD5 = "a5edffc8d5ae11db622c7d077fa5c7e1";
    public static final String AGENT_FILE_NAME = "skywalking-agent-ender.jar";
    protected static final String DEFAULT="REED-DEFAULT-MODULE";

    public static final String REED_SERVICE_REGISTER_CENTER = "REED_SERVICE_REGISTER_CENTER";
    public static final String REED_SERVICE_REGISTER_NACOS_NAMESPACE = "REED_SERVICE_REGISTER_NACOS_NAMESPACE";
    public static final String REED_SERVICE_REGISTER_NACOS_GROUP = "REED_SERVICE_REGISTER_NACOS_GROUP";
    public static final String REED_SERVICE_TRACE_CENTER = "REED_SERVICE_TRACE_CENTER";
    public static final String REED_SERVICE_TRACE_KAFKA_SERVERS = "REED_SERVICE_TRACE_KAFKA_SERVERS";
    public static final String REED_ADMIN_USERNAME = "REED_ADMIN_USERNAME";
    public static final String REED_ADMIN_PASSWORD = "REED_ADMIN_PASSWORD";
    public static final String REED_ADMIN_SERVER_URL = "REED_ADMIN_SERVER_URL";
    public static final String REED_LANGUAGE_SERVICE = "REED_LANGUAGE_SERVICE";
    public static final String REED_UNIFIED_CONFIG_SERVER = "REED_UNIFIED_CONFIG_SERVER";
    public static final String REED_UNIFIED_CONFIG_NAMESPACE = "REED_UNIFIED_CONFIG_NAMESPACE";
    public static final String REED_UNIFIED_CONFIG_GROUP = "REED_UNIFIED_CONFIG_GROUP";
    public static final String REED_APM_SERVER = "REED_APM_SERVER";

    protected static final String[] FRAMEWORK_CONFIG = {ReedStarter.REED_SERVICE_REGISTER_CENTER,//获取注册中心配置参数
            ReedStarter.REED_SERVICE_REGISTER_NACOS_GROUP,//注册中心NACOS的GROUP配置
            ReedStarter.REED_SERVICE_REGISTER_NACOS_NAMESPACE,//注册中心NACOS的NAMESPACE配置
            ReedStarter.REED_SERVICE_TRACE_CENTER, //获取服务链追踪服务配置
            ReedStarter.REED_SERVICE_TRACE_KAFKA_SERVERS,//获取服务链追踪服务KAFKA配置
            ReedStarter.REED_ADMIN_USERNAME, //获取Admin配置的用户名
            ReedStarter.REED_ADMIN_PASSWORD, //获取Admin配置的密码
            ReedStarter.REED_ADMIN_SERVER_URL, //获取Admin的服务器地址
            ReedStarter.REED_LANGUAGE_SERVICE, //语言服务器的名字（ModuleName）
            ReedStarter.REED_UNIFIED_CONFIG_SERVER, //统一配置服务器地址
            ReedStarter.REED_UNIFIED_CONFIG_NAMESPACE,  //统一配置的Namespace(NACOS)
            ReedStarter.REED_UNIFIED_CONFIG_GROUP,  //统一配置的分组(NACOS)
            ReedStarter.REED_APM_SERVER, //APM(skywalking)服务端(APM)地址
    };


    protected static final Map<String, String> reedEnvMap = new ConcurrentHashMap<>();
    protected static ReedStarter reedApplication;

    protected static Map<String, String> remoteConfiguration = new ConcurrentHashMap<>();
    protected static Map<String, String> localConfiguration = new ConcurrentHashMap<>();

    static {
        for(String config : FRAMEWORK_CONFIG){
            String configVal = System.getenv(config);
            if(!StringUtil.isEmpty(configVal)){
                if(StringUtil.isMatched(StringUtil.Reed_CIPHER, configVal)){
                    configVal = StringUtil.decryptCiphertext(configVal, DESUtil.DEFAULT_SECURITY_CODE);
                }
                reedEnvMap.put(config, configVal);
            }
        }
    }

    private final List<Class<?>> internalInterfaces = new ArrayList<>();

    protected ReedStarter(){
        analysis(this.getClass());
        reedApplication = this;
    }

    private void analysis(Class<?> clz){
        Class<?>[] interfaces = clz.getInterfaces();
        for(Class<?> clazz : interfaces){
            for(Class c : clazz.getInterfaces()){
                if(c == Reedable.class){
                    internalInterfaces.add(clazz);
                }
            }
        }
        Class<?> superClz = clz.getSuperclass();
        if(superClz!=null){
            analysis(superClz);
        }

    }

    /**
     *
     * @return Project/Module Name
     */
    public abstract String getModuleName();


    void bootup(){
        String name = StringUtil.isEmpty(getModuleName())?DEFAULT:getModuleName();
        initLogger(name);
        initMsgDict(name);
//        handleServiceRegister(name);
//        handleServiceTrace(name);
//        handleAutoTranslate();  //Still can not pass the language language name to framework dynamically
//        handleInterceptors(); //donot need anymore cause interceptor only used by spring framework, will handle with Reed-Springbootup
//        handleAdminClient();
        getLocalConfigurations();
        getRemoteConfigurations(name);
        handleAgent();
    }

    protected void verifyReedFramework(final String name){
//        handleAgent();
        handleServiceRegister(name);
        handleServiceTrace(name);
        handleAdminClient();
    }

    /**
     * 初始化日志模块
     * @param name
     */
    private void initLogger(String name){
        ReedLoggerConfig reedLoggerConfig = this.getClass().getDeclaredAnnotation(ReedLoggerConfig.class);
        if(reedLoggerConfig != null){
            if(ReedLogger.init(name, reedLoggerConfig.isStaticLogger(), reedLoggerConfig.enableFileAppender(),
                    reedLoggerConfig.showMethodName(), reedLoggerConfig.showLineNumber(),
                    reedLoggerConfig.enableAlarm(), reedLoggerConfig.alarmLevel())){
                ReedLogger.info("\t模块["+name+"]加载ReedLogger成功！ reedLoggerConfig="+reedLoggerConfig.toString());
            }else{
                throw new RuntimeException("\t模块["+name+"]加载ReedLogger失败！reedLoggerConfig=\"+reedLoggerConfig.toString()");
            }
        }else{
            if(ReedLogger.init(name)){
                ReedLogger.info("\t模块["+name+"]加载ReedLogger成功！");
            }else{
                throw new RuntimeException("\t模块["+name+"]加载ReedLogger失败！");
            }
        }
    }

    /**
     * 初始化统一错误编码模块
     * @param name
     */
    private void initMsgDict(String name){
        ReedLogger.info("\t模块["+name+"]开始加载CodeDescTranslator字典");
        List<Class<? extends BaseErrorCode>> errCodeClassList = SysEngine.realizeClassWithType(BaseErrorCode.class, null);
        ReedLogger.info(EnderUtil.devInfo()+" - init BaseErrorCode first");
        CodeDescTranslator.init(BaseErrorCode.class);
        ReedLogger.info(EnderUtil.devInfo()+" - looking up other error code and analysis");
        for(Class<? extends BaseErrorCode> clz : errCodeClassList){
            checkFinal(clz);
            CodeDescTranslator.init(clz);
        }
    }

    private void getLocalConfigurations(){
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(FRAMEWORK_CONFIG_FILE);
        try{
            if(is == null || is.available() == 0){
                ReedLogger.info(EnderUtil.devInfo()+" - can not find local configuration file ["+FRAMEWORK_CONFIG_FILE+"]");
                return;
            }
        }catch (IOException e){
            e.printStackTrace();
            ReedLogger.warn(EnderUtil.devInfo()+" - fail to read the local configuration file["+FRAMEWORK_CONFIG_FILE+"], because of "+e.getMessage());
            return;
        }

        Map map = new Yaml().load(is);
        if(MapUtil.isEmpty(map)){
            return;
        }
        map.forEach((k,v)->localConfiguration.put(k.toString(),v.toString()));
    }

    private void getRemoteConfigurations(final String name){
        Class<? extends  ReedStarter> clz = this.getClass();
        ReedUnifiedConfig annotation = clz.getAnnotation(ReedUnifiedConfig.class);
        if(annotation == null){
            return;
        }
        //annotation>local>env
        String remoteConfigServerFromLocalConfig = localConfiguration.get(REED_UNIFIED_CONFIG_SERVER);
        String remoteConfigServerFromEnv = StringUtil.isEmpty(System.getenv(REED_UNIFIED_CONFIG_SERVER))?
                System.getProperty(REED_UNIFIED_CONFIG_SERVER):System.getenv(REED_UNIFIED_CONFIG_SERVER);
        String remoteConfigServerFromAnnotation = annotation.server();
        String remoteConfigServer = StringUtil.isEmpty(remoteConfigServerFromAnnotation)?
                (StringUtil.isEmpty(remoteConfigServerFromLocalConfig)?remoteConfigServerFromEnv:remoteConfigServerFromLocalConfig)
                :remoteConfigServerFromAnnotation;
        if(StringUtil.isEmpty(remoteConfigServer)){
            throw new EnderRuntimeException("REED_UNIFIED_CONFIG_SERVER can not found from system environment, "+FRAMEWORK_CONFIG_FILE+", or @ReedUnifiedConfig");
        }

        String namespaceFromLocalConfig = localConfiguration.get(REED_UNIFIED_CONFIG_NAMESPACE);
        String namespaceFromEnv = StringUtil.isEmpty(System.getenv(REED_UNIFIED_CONFIG_NAMESPACE))?
                System.getProperty(REED_UNIFIED_CONFIG_NAMESPACE):System.getenv(REED_UNIFIED_CONFIG_NAMESPACE);
        String namespaceFromAnnotation = annotation.server();
        String namespace = StringUtil.isEmpty(namespaceFromAnnotation)?
                (StringUtil.isEmpty(namespaceFromLocalConfig)?namespaceFromEnv:namespaceFromLocalConfig)
                :namespaceFromAnnotation;
        if(StringUtil.isEmpty(namespace)){
            throw new EnderRuntimeException("REED_UNIFIED_CONFIG_NAMESPACE can not found from system environment, "+FRAMEWORK_CONFIG_FILE+", or @ReedUnifiedConfig");
        }

        String groupFromLocalConfig = localConfiguration.get(REED_UNIFIED_CONFIG_GROUP);
        String groupFromEnv = StringUtil.isEmpty(System.getenv(REED_UNIFIED_CONFIG_GROUP))?
                System.getProperty(REED_UNIFIED_CONFIG_GROUP):System.getenv(REED_UNIFIED_CONFIG_GROUP);
        String groupFromAnnotation = annotation.server();
        String group = StringUtil.isEmpty(groupFromAnnotation)?
                (StringUtil.isEmpty(groupFromLocalConfig)?groupFromEnv:groupFromLocalConfig)
                :groupFromAnnotation;
        if(StringUtil.isEmpty(group)){
            throw new EnderRuntimeException("REED_UNIFIED_CONFIG_GROUP can not found from system environment, "+FRAMEWORK_CONFIG_FILE+", or @ReedUnifiedConfig");
        }

        String fileType = annotation.fileExtension();
        String extConfig = annotation.extConfig();
//        if(!fileType.equalsIgnoreCase("yml") && !fileType.equalsIgnoreCase("properties")){
        if(!fileType.equalsIgnoreCase("yml")){
            throw new EnderRuntimeException("file extension only support yml within @ReedUnifiedConfig for now!");
        }

        String[] serverArr = remoteConfigServer.split(",");
        String[] configUrl = new String[serverArr.length];
        String[] extUrl = new String[serverArr.length];
        for (int i = 0; i < serverArr.length; i++) {
            configUrl[i] = "http://"+serverArr[i]+"/nacos/v1/cs/configs?show=all&dataId="+extConfig+"&group="+group+"&tenant="+namespace+"&namespaceId="+namespace;
            extUrl[i] = "http://"+serverArr[i]+"/nacos/v1/cs/configs?show=all&dataId="+name+"."+fileType+"&group="+group+"&tenant="+namespace+"&namespaceId="+namespace;
            if(!StringUtil.isUrl(configUrl[i]) || !StringUtil.isUrl(extUrl[i])){
                ReedLogger.debug(configUrl[i]);
                ReedLogger.debug(extUrl[i]);
            }else{
                throw new EnderRuntimeException("@ReedUnifiedConfig not validated, config server:"+remoteConfigServer+", namespace:"+namespace+", group:"+group+", file extension:"+fileType+", ext config file:"+extConfig);
            }
        }

        //循环请求通用配置文件和主配置文件，放到remoteConfiguration中，然后提取OAPserver的地址，在Spring启动前完成APM-agent的加载工作
        Random random = new Random();
        int i = random.nextInt(serverArr.length);
        try {
            HttpURLConnection extUrlHttpConnection = HttpUtil.getNormalHttpConnection(extUrl[i], 5000);
            HttpURLConnection configUrlHttpConnection = HttpUtil.getNormalHttpConnection(configUrl[i], 5000);

            byte[] extBytes = HttpUtil.readHttpURLConnection(extUrlHttpConnection);
            byte[] configBytes = HttpUtil.readHttpURLConnection(configUrlHttpConnection);
            String extStr = new String(extBytes, StandardCharsets.UTF_8);
            String configStr = new String(configBytes, StandardCharsets.UTF_8);
            JSONObject ext = JSON.parseObject(extStr);
            if(ext == null || ext.isEmpty()){
                ReedLogger.warn("ATTENTION!!! Group public configuration from remote["+extUrl[i]+"] is empty!");
                new EnderBusinessException("Application want to get configurations from remote, but it is empty!").printStackTrace();
            }else{
                String extContent = ext.getString("content");
                Map extMap = new Yaml().load(extContent);
                extMap.forEach((k,v)->remoteConfiguration.put(k.toString(), v.toString()));
                ReedLogger.debug("Group public configuration from remote is:"+ext.toJSONString());
            }
            JSONObject config = JSON.parseObject(configStr);
            if(config == null || config.isEmpty()){
                ReedLogger.warn("ATTENTION!!! Group application configuration from remote["+configUrl[i]+"] is empty!");
                new EnderBusinessException("Application want to get configurations from remote, but it is empty!").printStackTrace();
            }else{
                String configContent = config.getString("content");
                Map configMap = new Yaml().load(configContent);
                configMap.forEach((k,v)->remoteConfiguration.put(k.toString(), v.toString()));
                ReedLogger.debug("Group application configuration from remote is:"+config.toJSONString());
            }
//            String extContent = ext.getString("content");
//            String configContent = config.getString("content");
//            Map extMap = new Yaml().load(extContent);
//            Map configMap = new Yaml().load(configContent);
//            extMap.forEach((k,v)->remoteConfiguration.put(k.toString(), v.toString()));
//            configMap.forEach((k,v)->remoteConfiguration.put(k.toString(), v.toString()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new EnderRuntimeException("remote config server access failed! exception:"+e.getMessage());
        }
    }

    private void handleServiceRegister(String name){
        Class<? extends  ReedStarter> clz = this.getClass();
        EnableServiceRegister annotation = clz.getAnnotation(EnableServiceRegister.class);
        if(annotation == null){
            return;
        }
        Discoverable.DiscoverArg  discoverArg = new Discoverable.DiscoverArg();
        discoverArg.setServiceName(name);
//        String registerCenterUrl = ReedContext.getString(REED_SERVICE_REGISTER_CENTER);
        String registerCenterUrl = reedEnvMap.get(REED_SERVICE_REGISTER_CENTER);
        ReedLogger.debug("registerCenterUrl In SysEnv --->"+registerCenterUrl);
        if(StringUtil.isEmpty(registerCenterUrl) && StringUtil.isEmpty(annotation.server())){
            throw new RuntimeException("Either REED_SERVICE_REGISTER_CENTER or @EnableServiceRegister.server() can not be empty!");
        }
        String registerNacosNamespace = reedEnvMap.get(REED_SERVICE_REGISTER_NACOS_NAMESPACE);
        ReedLogger.debug("REED_SERVICE_REGISTER_NACOS_NAMESPACE In SysEnv --->"+registerNacosNamespace);
        if(StringUtil.isEmpty(registerNacosNamespace) && StringUtil.isEmpty(annotation.namespace())){
            throw new RuntimeException("Either REED_SERVICE_REGISTER_NACOS_NAMESPACE or @EnableServiceRegister.namespace() can not be empty!");
        }
        String registerNacosGroup = reedEnvMap.get(REED_SERVICE_REGISTER_NACOS_GROUP);
        ReedLogger.debug("REED_SERVICE_REGISTER_NACOS_GROUP In SysEnv --->"+registerNacosGroup);
        if(StringUtil.isEmpty(registerNacosNamespace) && StringUtil.isEmpty(annotation.group())){
            throw new RuntimeException("Either REED_SERVICE_REGISTER_NACOS_GROUP or @EnableServiceRegister.group() can not be empty!");
        }
        discoverArg.setServerUrl(StringUtil.isEmpty(annotation.server())?registerCenterUrl:annotation.server());
        ReedLogger.debug("registerCenterUrl--->"+discoverArg.getServerUrl());
        discoverArg.setNamespace(StringUtil.isEmpty(annotation.namespace())?registerNacosNamespace:annotation.namespace());
        ReedLogger.debug("register nacos namespace--->"+discoverArg.getNamespace());
        discoverArg.setGroup(StringUtil.isEmpty(annotation.group())?registerNacosGroup:annotation.group());
        ReedLogger.debug("register nacos group--->"+discoverArg.getGroup());
        discoverArg.setDiscoveryProduction(annotation.production());
        for(Class<?> clazz : internalInterfaces){
            if(clazz == Discoverable.class){
                Discoverable service = (Discoverable)this;
                service.register(discoverArg);
                return;
            }
        }
        throw new RuntimeException("If @EnableServiceRegister declared, you should implements Discoverable as well!");
    }

    @Deprecated
    private void handleServiceTrace(String name){
        Class<? extends  ReedStarter> clz = this.getClass();
        EnableServiceTrace annotation = clz.getAnnotation(EnableServiceTrace.class);
        if(annotation == null){
            return;
        }
        Traceable.TraceArg  traceArg = new Traceable.TraceArg();
        traceArg.setTraceServer(annotation.traceServer());
//        String traceCenterUrl = ReedContext.getString(REED_SERVICE_TRACE_CENTER);
        String traceCenterUrl = reedEnvMap.get(REED_SERVICE_TRACE_CENTER);
        ReedLogger.debug("traceCenterUrl In SysEnv --->"+traceCenterUrl);
        if(StringUtil.isEmpty(traceCenterUrl) && StringUtil.isEmpty(annotation.traceServer())){
            throw new RuntimeException("Either REED_SERVICE_TRACE_CENTER or @EnableServiceTrace.traceServer() can not be empty!");
        }
        traceArg.setTraceServer(StringUtil.isEmpty(annotation.traceServer())?traceCenterUrl:annotation.traceServer());
        ReedLogger.debug("traceCenterUrl--->"+traceArg.getTraceServer());
        traceArg.setPercentage(annotation.persentage());
        Traceable.TraceType traceType = annotation.traceType();
        traceArg.setTraceType(traceType);
        if(traceType.equals(Traceable.TraceType.KAFKA)){
            Traceable.TraceKafka traceKafka = new Traceable.TraceKafka();
//            String kafkaServerUrl = ReedContext.getString(REED_SERVICE_TRACE_KAFKA_SERVERS);
            String kafkaServerUrl = reedEnvMap.get(REED_SERVICE_TRACE_KAFKA_SERVERS);
            if(StringUtil.isEmpty(kafkaServerUrl) && StringUtil.isEmpty(annotation.kafkaServer())){
                throw new RuntimeException("Either REED_SERVICE_TRACE_KAFKA_SERVERS or @EnableServiceTrace.kafkaServer() can not be empty!");
            }else{
                traceKafka.setHosts(StringUtil.isEmpty(annotation.kafkaServer())?
                        parseKafkaHosts(kafkaServerUrl):
                            parseKafkaHosts(annotation.kafkaServer()));
            }
            traceKafka.setTopic(annotation.traceTopic());
            traceArg.setKafka(traceKafka);
        }

        for(Class<?> clazz : internalInterfaces){
            if(clazz == Discoverable.class){
                Traceable service = (Traceable)this;
                service.startServiceTrace(traceArg);
                return;
            }
        }
        throw new RuntimeException("If @EnableServiceTrace declared, you should implements Traceable as well!");
    }

//    private void handleAutoTranslate(){
//        Class<? extends  ReedStarter> clz = this.getClass();
//        ReedAutoTranslate annotation = clz.getAnnotation(ReedAutoTranslate.class);
//        if(annotation == null || !annotation.enable()){
//            return;
//        }
//        String reedLanguageService = ReedContext.getString(REED_LANGUAGE_SERVICE);
//        if(StringUtil.isEmpty(reedLanguageService) && StringUtil.isEmpty(annotation.language())){
//            throw new RuntimeException("When @ReedAutoTranslate Added, Either REED_LANGUAGE_SERVICE or" +
//                    " @ReedAutoTranslate.language() can not be empty!");
//        }
//        ReedContext.setAppVal(REED_LANGUAGE_SERVICE, StringUtil.isEmpty(annotation.language())?reedLanguageService:annotation.language());
//    }

    private void handleAdminClient(){
        Class<? extends  ReedStarter> clz = this.getClass();
        EnableAdminClient annotation = clz.getAnnotation(EnableAdminClient.class);
        if(annotation == null){
            return;
        }
        Clientable.Configration  configration = new Clientable.Configration();

//        String username = ReedContext.getString(REED_ADMIN_USERNAME);
        String username = reedEnvMap.get(REED_ADMIN_USERNAME);
        ReedLogger.debug("Admin Username In SysEnv --->"+username);
//        if(StringUtil.isEmpty(username) && StringUtil.isEmpty(annotation.username())){
//            throw new RuntimeException("Either REED_ADMIN_USERNAME or @EnableAdminClient.username() can not be empty!");
//        }
        configration.setUsername(StringUtil.isEmpty(annotation.username())?username:annotation.username());
        ReedLogger.debug("Admin Username --->"+configration.getUsername());

//        String password = ReedContext.getString(REED_ADMIN_PASSWORD);
        String password = reedEnvMap.get(REED_ADMIN_PASSWORD);
        ReedLogger.debug("Admin Password In SysEnv --->"+password);
//        if(StringUtil.isEmpty(password) && StringUtil.isEmpty(annotation.password())){
//            throw new RuntimeException("Either REED_ADMIN_PASSWORD or @EnableAdminClient.password() can not be empty!");
//        }
        configration.setPassword(StringUtil.isEmpty(annotation.password())?password:annotation.password());
        ReedLogger.debug("Admin Password --->"+configration.getPassword());

//        String serverUrl = ReedContext.getString(REED_ADMIN_SERVER_URL);
        String serverUrl = reedEnvMap.get(REED_ADMIN_SERVER_URL);
        ReedLogger.debug("Admin ServerUrl In SysEvn --->"+serverUrl);
        if(StringUtil.isEmpty(serverUrl) && StringUtil.isEmpty(annotation.server())){
            throw new RuntimeException("Either REED_ADMIN_SERVER_URL or @EnableAdminClient.server() can not be empty!");
        }
        configration.setServerUrl(StringUtil.isEmpty(annotation.server())?serverUrl:annotation.server());
        ReedLogger.debug("Admin ServerUrl --->"+configration.getServerUrl());
        EventCenter.addEventListener(LogAlarmEvent.class, LogAlarmHandler.getInstance(configration.getServerUrl()));
        for(Class<?> clazz : internalInterfaces){
            if(clazz == Clientable.class){
                Clientable service = (Clientable)this;
                service.config(configration);
                return;
            }
        }
        throw new RuntimeException("If @EnableAdminClient declared, you should implements Clientable as well!");
    }

    protected void handleReedUnifiedConfiguration(){
        Class<? extends  ReedStarter> clz = this.getClass();
        ReedUnifiedConfig annotation = clz.getAnnotation(ReedUnifiedConfig.class);
        if(annotation == null){
            return;
        }
        ReedLogger.debug("handling Reed Unified Configuration...");
        //this.putArgs("spring.cloud.nacos.config.enabled", "true");
        putArgs("ReedCipher{b56c0223134d4bb5a7ec4a5c814dab0aed9483ffc66a6cd249abcaf705f762ad5ef3aa6d915f1e43}",
                "ReedCipher{eadbe9eb68777e52}");

        String server = reedEnvMap.get(REED_UNIFIED_CONFIG_SERVER);
        ReedLogger.debug("Reed Unified Configuration Server --->"+server);
        if(StringUtil.isEmpty(server) && StringUtil.isEmpty(annotation.server())){
            throw new RuntimeException("Either REED_UNIFIED_CONFIG_SERVER or @ReedUnifiedConfig.server() can not be empty!");
        }
        putArgs("ReedCipher{b56c0223134d4bb5a7ec4a5c814dab0aed9483ffc66a6cd203caea7c2ab26a945406d96dfe0bfc92}",
                StringUtil.isEmpty(annotation.server())?server:annotation.server());

        String group = reedEnvMap.get(REED_UNIFIED_CONFIG_GROUP);
        ReedLogger.debug("Reed Unified Configuration group --->"+group);
        if(StringUtil.isEmpty(group) && StringUtil.isEmpty(annotation.group())){
            throw new RuntimeException("Either REED_UNIFIED_CONFIG_GROUP or @ReedUnifiedConfig.group() can not be empty!");
        }
        putArgs("ReedCipher{b56c0223134d4bb5a7ec4a5c814dab0aed9483ffc66a6cd291da76178df9a3c5}",
                StringUtil.isEmpty(annotation.group())?group:annotation.group());

        String namespace = reedEnvMap.get(REED_UNIFIED_CONFIG_NAMESPACE);
        ReedLogger.debug("Reed Unified Configuration namespace --->"+namespace);
        if(StringUtil.isEmpty(namespace) && StringUtil.isEmpty(annotation.namespace())){
            throw new RuntimeException("Either REED_UNIFIED_CONFIG_NAMESPACE or @ReedUnifiedConfig.namespace() can not be empty!");
        }
        putArgs("ReedCipher{b56c0223134d4bb5a7ec4a5c814dab0aed9483ffc66a6cd2d22226793a0c13ead3aced4fc5609dc8}",
                StringUtil.isEmpty(annotation.namespace())?namespace:annotation.namespace());

        //file-extension
        putArgs("ReedCipher{b56c0223134d4bb5a7ec4a5c814dab0aed9483ffc66a6cd24dcfc444d88eacaa311b7c0c8c17938cfeb959b7d4642fcb}",
                annotation.fileExtension());

        //auto refresh
        //this.putArgs("spring.cloud.bus.refresh.enabled",String.valueOf(annotation.autoRefresh()));
        putArgs("ReedCipher{b56c0223134d4bb57b5def6bbcd405c05da3143c5e11ca5094a2cd3b552ad948feb959b7d4642fcb}",
                String.valueOf(annotation.autoRefresh()));
        putArgs("ReedCipher{b56c0223134d4bb5a7ec4a5c814dab0aed9483ffc66a6cd2b72aa508341a3bc257942e3a4b4db36f5ef3aa6d915f1e43}",
                String.valueOf(annotation.autoRefresh()));
        //ext-config
        putArgs("ReedCipher{b56c0223134d4bb5a7ec4a5c814dab0aed9483ffc66a6cd2b99a2adc707e9cb04351c0521a6955effef8cd639596040a}", annotation.extConfig());
        putArgs("ReedCipher{b56c0223134d4bb5a7ec4a5c814dab0aed9483ffc66a6cd2b99a2adc707e9cb04351c0521a6955ef37336cf7f908eedb}", StringUtil.isEmpty(annotation.group())?group:annotation.group());
        putArgs("ReedCipher{b56c0223134d4bb5a7ec4a5c814dab0aed9483ffc66a6cd2b99a2adc707e9cb04351c0521a6955eff368cc565a2dd6d6}", String.valueOf(annotation.autoRefresh()));
    }

    private Set<HostColonPort> parseKafkaHosts(String kafkaHosts){
        String[] kafkaArray = kafkaHosts.split(",");
        Set<HostColonPort> kafkaSet = new HashSet<>();
        for(String str : kafkaArray){
            kafkaSet.add(HostColonPort.parse(str));
        }
        return kafkaSet;
    }


    protected static void putArgs(String key, String value){
        if(StringUtil.isEmpty(key) || StringUtil.isEmpty(value)){
            return;
        }

        if(StringUtil.isMatched(StringUtil.Reed_CIPHER, key)){
            key = StringUtil.decryptCiphertext(key, DESUtil.DEFAULT_SECURITY_CODE);
        }

        if(StringUtil.isMatched(StringUtil.Reed_CIPHER, value)){
            value = StringUtil.decryptCiphertext(value, DESUtil.DEFAULT_SECURITY_CODE);
        }

        System.setProperty(key, value);
    }

    private void checkFinal(Class<?> clz){
        if(!JavaUtil.isFinal(clz)){
            throw new RuntimeException("For Reed System Security, please add \"final\" modifier on class @ "+clz.getName());
        }
    }


    private void handleAgent(){
        Class<? extends  ReedStarter> clz = this.getClass();
        EnableAPMAnalysis annotation = clz.getAnnotation(EnableAPMAnalysis.class);
        if(annotation == null){
            return;
        }

//        ReedLogger.debug("checking system jvm configs...");
//        String classpath = ReedContext.getString("java.class.path");
//        ReedLogger.debug("System JVM Checked: "+(classpath.indexOf("tools.jar")==-1?"false":"true"));
//        String javaHome = ReedContext.getString("java.home");
//        String[] path = javaHome.split(ReedContext.getString("file.separator"));
//        if(StringUtil.isEmpty(javaHome) || path.length<=1){
//            throw new RuntimeException("Cannot find java home, make sure the system environment is right!");
//        }
//        String javaEnv = path[path.length-1];
//        if(javaEnv.toLowerCase().indexOf("jdk")!=-1){
//
//        }

//        check and release agents
        try{
            ReedLogger.debug("checking apm-agent files...");
            String agentDir = "apm-agent";
            String projhome = System.getProperty("user.home");
            String agentDirFullPath = projhome+File.separator+agentDir;
            if(FileUtil.exists(agentDirFullPath)){
                if(FileUtil.isFolder(agentDirFullPath)){
//                    String folderMd5 = FileUtil.getDirMd5(agentDirFullPath, "logs");
                    String agentMd5 = FileUtil.md5(new File(agentDirFullPath+File.separator+AGENT_FILE_NAME));
//                    if(!AGENT_DIR_MD5.equalsIgnoreCase(folderMd5)){
                    if(!AGENT_FILE_MD5.equalsIgnoreCase(agentMd5)){
                        ReedLogger.debug("found old or invalidate apm-agent, will override...");
                        FileUtil.rmdir(agentDirFullPath);
                        releaseAgents(agentDirFullPath+".zip");
                    }else{
                        ReedLogger.debug("apm-agents already exist!");
                    }
                }else{
                    ReedLogger.debug("did not found apm-agent folder, will clear the ENV. then release and deploy...");
                    FileUtil.rmfile(agentDirFullPath);
                    releaseAgents(agentDirFullPath+".zip");
                }

            }else{
                ReedLogger.debug("did not found apm-agent folder, release and deploy...");
                releaseAgents(agentDirFullPath+".zip");
            }
        }catch(Throwable throwable){
            throwable.printStackTrace();
            ReedLogger.error("Fail to load the apm-agent: " + throwable);
        }

//        String server = reedEnvMap.get(REED_APM_SERVER);
        //annotation>remote>local>env
        String serverFromRemote = remoteConfiguration.get(REED_APM_SERVER);
        String serverFromLocal = localConfiguration.get(REED_APM_SERVER);
        String serverFromEnv = System.getenv(REED_APM_SERVER);
        String server = StringUtil.isEmpty(serverFromRemote)?
                (StringUtil.isEmpty(serverFromLocal)?serverFromEnv:serverFromLocal)
                :serverFromRemote;
        if(StringUtil.isEmpty(server) && StringUtil.isEmpty(annotation.server())){
            throw new RuntimeException("Either REED_APM_SERVER or @EnableAPMAnalysis.server() can not be empty!");
        }
        server = StringUtil.isEmpty(server)?annotation.server():server;

        //for skywalking.agent.instance_name
        String podId = System.getenv("POD_ID");
        String sysIp = ReedContext.getString("os.ip", "UNKNOW_ENV");
        podId = StringUtil.isEmpty(podId)?sysIp:podId;

        EnableAPMAnalysis.APMLogType type = annotation.log();
        String apmLoggerAppender = type==EnableAPMAnalysis.APMLogType.FILE?"file":"console";

        ReedLogger.debug(EnderUtil.devInfo()+" - start loading apm agent[skywalking]...");
//        putArgs("SW_AGENT_COLLECTOR_BACKEND_SERVICES", "11.11.54.102:11800");
//        putArgs("skywalking.collector.backend_service", "11.11.54.102:11800");
        putArgs("skywalking.collector.backend_service", server);
//        putArgs("SW_AGENT_NAME", getModuleName());
        putArgs("skywalking.agent.service_name", getModuleName());
//        putArgs("SW_LOGGING_DIR", ReedContext.getString("user.dir")+File.separator+"rflog");
        putArgs("skywalking.logging.dir", ReedContext.getString("user.dir")+File.separator+"rflog");
//        putArgs("SW_LOGGING_FILE_NAME", getModuleName()+"-apm.log");
        putArgs("skywalking.logging.file_name", getModuleName()+"-apm.log");
//        putArgs("SW_LOGGING_LEVEL", "DEBUG");
        putArgs("skywalking.logging.level", annotation.logLevel());
        putArgs("skywalking.logging.pattern", "%timestamp [%thread] <%level> {%class} : %msg %throwable");
        putArgs("skywalking.logging.output", apmLoggerAppender);
//        putArgs("skywalking.logging.output", "console");
        putArgs("skywalking.agent.instance_name", getModuleName()+"@"+podId);
        try {
            synchronized (this){
                final Class<?> virtualMachineClass = getVirtualMachineClass();
                final Method attachMethod = virtualMachineClass.getMethod("attach", String.class);
                Object vmObj = attachMethod.invoke(virtualMachineClass, ReedContext.vmPID());
                if(vmObj == null){
                    throw new EnderRuntimeException("Error during attach to JVM! evidence:"+vmObj);
                }
                final Method loadAgentMethod = virtualMachineClass.getMethod("loadAgent", String.class);
                String projhome = System.getProperty("user.home");
                String folderName = "apm-agent";
                String agentMainJar  = "skywalking-agent-ender.jar";
                String agentFullPath = projhome+File.separator+folderName+File.separator+agentMainJar;
                ReedLogger.debug("loading apm-agent: "+agentFullPath);
                loadAgentMethod.invoke(vmObj, agentFullPath);
                final Method detachMethod = virtualMachineClass.getMethod("detach");
                detachMethod.invoke(vmObj);
//                if(obj instanceof VirtualMachine){
//                    VirtualMachine vm = (VirtualMachine)obj;
//                    String projhome = System.getProperty("user.home");
//                    String folderName = "apm-agent";
//                    String agentMainJar  = "skywalking-agent-ender.jar";
//                    String agentFullPath = projhome+File.separator+folderName+File.separator+agentMainJar;
//                    ReedLogger.debug("loading apm-agent: "+agentFullPath);
//                    vm.loadAgent(agentFullPath);
//                    vm.detach();
//                }else{
//                    throw new EnderRuntimeException("Error during attach to JVM! evidence:"+obj);
//                }
            }
//            VirtualMachine vm  = VirtualMachine.attach(ReedContext.vmPID());
//            vm.loadAgent("/Users/ender/Desktop/apache-skywalking-apm-bin/agent/skywalking-agent.jar");
//            String projhome = System.getProperty("user.home");
//            String folderName = "apm-agent";
//            String agentMainJar  = "skywalking-agent-ender.jar";
//            String agentFullPath = projhome+File.separator+folderName+File.separator+agentMainJar;
//            ReedLogger.debug("loading apm-agent: "+agentFullPath);
////            vm.loadAgent(agentFullPath, "logging.pattern=%timestamp [%thread] <%level> {%class} : %msg %throwable");
//            vm.loadAgent(agentFullPath);
//            vm.detach();
//        } catch (AttachNotSupportedException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (AgentLoadException e) {
//            e.printStackTrace();
//        } catch (AgentInitializationException e) {
//            e.printStackTrace();
        } catch(NoSuchMethodException e){
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void releaseAgents(String path){
        String agentPkgPath = "/org/reed/system/libs/apm-agent.zip";
        try {
            ReedLogger.debug("agentPkgPath="+agentPkgPath);
            URL url = ReedContext.class.getResource(agentPkgPath);
            InputStream in = url.openStream();
            OutputStream out = new FileOutputStream(path);
            int count = 0;
            byte[] buffer = new byte[256];
            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }
            in.close();
            out.close();
            ReedLogger.debug("released apm-agent.zip to "+path);
        } catch (IOException e) {
            ReedLogger.error("Exception when release apm-agent.zip");
            e.printStackTrace();
        }

        //unzip agent pkg
        try {
            releaseZipToFile(path, System.getProperty("user.home"));
        } catch (IOException e) {
            ReedLogger.error("Exception when unzip apm agents: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void releaseZipToFile(String sourceZip, String outFileName)throws IOException{
        ZipFile zfile=new ZipFile(sourceZip);
        ReedLogger.debug(zfile.getName());
        Enumeration zList=zfile.entries();
        ZipEntry ze=null;
        byte[] buf=new byte[1024];
        while(zList.hasMoreElements()){
            //从ZipFile中得到一个ZipEntry
            ze=(ZipEntry)zList.nextElement();
            if(ze.isDirectory()){
                continue;
            }
            //以ZipEntry为参数得到一个InputStream，并写到OutputStream中
            OutputStream os = new BufferedOutputStream(new FileOutputStream(getRealFileName(outFileName, ze.getName())));
            InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
            int readLen=0;
            while ((readLen=is.read(buf, 0, 1024))!=-1) {
                os.write(buf, 0, readLen);
            }
            is.close();
            os.close();
            ReedLogger.debug("Extracted: "+ze.getName());
        }
        zfile.close();
    }
    /**
     * 给定根目录，返回一个相对路径所对应的实际文件名.
     *
     * @param baseDir
     *            指定根目录
     * @param absFileName
     *            相对路径名，来自于ZipEntry中的name
     * @return java.io.File 实际的文件
     */
    private File getRealFileName(String baseDir, String absFileName) {
        String[] dirs = absFileName.split("/");
        //System.out.println(dirs.length);
        File ret = new File(baseDir);
        //System.out.println(ret);
        if (dirs.length > 1) {
            for (int i = 0; i < dirs.length - 1; i++) {
                ret = new File(ret, dirs[i]);
            }
        }
        if (!ret.exists()) {
            ret.mkdirs();
        }
        ret = new File(ret, dirs[dirs.length - 1]);
        return ret;
    }

    private Class<?> getVirtualMachineClass() throws EnderRuntimeException{
        if(JavaUtil.isJava5()){
            throw new EnderRuntimeException("JDK6 or later version is needed!" +
                    " ReedFramework works with JDK8 or later since 0.0.6-SNAPSHOT!");
        }
        final String virtualMachineClassName = "com.sun.tools.attach.VirtualMachine";
        try{
            return Class.forName(virtualMachineClassName);
        }catch(final ClassNotFoundException e){
            String javaHomeStr = System.getProperty("java_home");
            javaHomeStr = StringUtil.isEmpty(javaHomeStr)?System.getProperty("JAVA_HOME"):javaHomeStr;
            javaHomeStr = StringUtil.isEmpty(javaHomeStr)?System.getProperty("java.home"):javaHomeStr;
            File javaHome = new File(javaHomeStr);
            if("jre".equalsIgnoreCase(javaHome.getName())){
                javaHome = javaHome.getParentFile();
            }
            final String[] defaultToolsLocation = { "lib", "tools.jar", "lib" + File.separator + "tools.jar"};
            File tools = null;
            for (final String name : defaultToolsLocation) {
                tools = new File(javaHome, name);
            }
            if(tools == null || !tools.exists()){
                throw new EnderRuntimeException("Cannot find tools.jar within classpath," +
                        " please make sure JDK6(1.6) or later version installed" +
                        " and configured right(java home needed)! further exception:"+e);
            }
            try{
                final URL url = tools.toURI().toURL();
                final ClassLoader classLoader;
                final URL[] urls = {url};
                classLoader = URLClassLoader.newInstance(urls);
                return Class.forName(virtualMachineClassName, true, classLoader);
            }catch(MalformedURLException|ClassNotFoundException e1){
                throw new EnderRuntimeException("Exception throws when getting VirtualMachine class!" +
                        " further exception:"+e);
            }
        }
    }

}
