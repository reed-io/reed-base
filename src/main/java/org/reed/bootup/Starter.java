/**
 * IdeaProject @ org.reed.bootup/Starter.java
 */
package org.reed.bootup;

import org.reed.define.BaseErrorCode;
import org.reed.define.CodeDescTranslator;
//import org.reed.log.Logger;
import org.reed.log.ReedLogger;
import org.reed.system.ReedContext;
import org.reed.system.SysEngine;
import org.reed.utils.BitUtil;
import org.reed.utils.StringUtil;

import java.util.List;
import java.util.Set;

/**
 * @author chenxiwen
 * @createTime 2018年11月08日 上午11:01
 * @description
 */
@Deprecated
public class Starter {
    public static final String DEFAULT="DEFAULT";
    public static final String PROJ_NAME_ENV_KEY = "PROJ_ID";

//    protected static InheritableThreadLocal<Integer> tl = new InheritableThreadLocal();
//    static{
//        tl.set(0);
//    }

    private static int sysArgs = 0;

    private Starter(){}

    public static int letsGo(Startable proj){
        String projName = initProjectName(proj);
        System.out.println("PROJID="+projName);
        System.out.println(System.getProperty(PROJ_NAME_ENV_KEY));
        initLogger(projName);
        initMsgDic(projName);
        initBootupArgs(proj.bootArgs());
        return sysArgs;
    }

    public static String defaultModuleName() {
        return ReedContext.getString("proj.name") == null ?
                ReedContext.getString("proj_name") == null ?
                        ReedContext.getString("ProjectName") == null ?
                                DEFAULT :
                                ReedContext.getString("ProjectName") :
                        ReedContext.getString("proj_name") :
                ReedContext.getString("proj.name");
    }


    protected static String initProjectName(Startable proj){
        String projName = proj.getModuleName();
        String modelName = StringUtil.isEmpty(projName)?defaultModuleName():projName;
        System.out.println("模块【"+modelName+"】开始启动");
        System.setProperty(PROJ_NAME_ENV_KEY, modelName);
        return projName;
    }

    protected static void initLogger(String modelName){
        if(ReedLogger.init(modelName)){
            ReedLogger.info("\t模块"+modelName+"加载ReedLogger成功！");
        }else{
            throw new RuntimeException("\t模块"+modelName+"加载ReedLogger失败！");
        }
//        if(Logger.init(modelName)){
//            Logger.info("\t模块"+modelName+"加载Logger成功！");
//        }else{
//            throw new RuntimeException("\t模块"+modelName+"加载Logger失败！");
//        }
    }

    protected static void initMsgDic(String modelName){
        ReedLogger.info("\t模块"+modelName+"开始加载CodeDescTranslator字典");
        List<Class<? extends BaseErrorCode>> errCodeClassList = SysEngine.realizeClassWithType(BaseErrorCode.class, null);
        for(Class<? extends BaseErrorCode> clz : errCodeClassList){
            CodeDescTranslator.init(clz);
        }
    }

    protected static void initBootupArgs(Set<BootupArg> args){
        if(args == null || args.size() == 0){
            return;
        }
//        if(args.contains(BootupArg.ServiceRegistrationDiscovery)){
//            tl.set(BitUtil.setBit(tl.get(), 0));
//        }
//        if (args.contains(BootupArg.ServiceHealthManageable)){
//            tl.set(BitUtil.setBit(tl.get(), 1));
//        }
//        if (args.contains(BootupArg.ConsoleManageable)){
//            tl.set(BitUtil.setBit(tl.get(), 2));
//        }

        if(args.contains(BootupArg.ServiceRegistrationDiscovery)){
            sysArgs = BitUtil.setBit(sysArgs, 0);
        }
        if (args.contains(BootupArg.ServiceHealthManageable)){
            sysArgs = BitUtil.setBit(sysArgs, 1);
        }
        if (args.contains(BootupArg.ConsoleManageable)){
            sysArgs = BitUtil.setBit(sysArgs, 2);
        }

    }

}
