/**
 * base/org.reed.bootup/NativeBootup.java
 */
package org.reed.bootup;

import java.util.List;

import org.reed.define.BaseErrorCode;
import org.reed.define.CodeDescTranslator;
//import org.reed.log.Logger;
import org.reed.log.ReedLogger;
import org.reed.system.ReedContext;
import org.reed.system.SysEngine;
import org.reed.utils.StringUtil;

/**
 * @author chenxiwen
 * @date 2017年8月16日下午2:30:03
 */
public abstract class NativeBootup {
    
    public void start(String[] args){
        String modelName = StringUtil.isEmpty(getModuleName())?defaultModuleName():getModuleName();
        System.out.println("模块【"+modelName+"】开始启动");
        //日志
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
        //字典
        ReedLogger.info("\t模块"+modelName+"开始加载CodeDescTranslator字典");
        List<Class<? extends BaseErrorCode>> errCodeClassList = SysEngine.realizeClassWithType(BaseErrorCode.class, null);
        for(Class<? extends BaseErrorCode> clz : errCodeClassList){
            CodeDescTranslator.init(clz);
        }
        //启动项目其他框架
        bootup(args);
    }

    public abstract void beforeStart();

    public abstract void afterStart();

    public abstract void bootup(String[] args);
    
    public abstract String getModuleName();

    protected final String defaultModuleName(){
        return ReedContext.getString("proj.name") == null ?
                ReedContext.getString("proj_name") == null ?
                        ReedContext.getString("ProjectName") == null ?
                                ReedContext.getString("ProjectName", "DEFAULT") :
                                ReedContext.getString("ProjectName", "DEFAULT") :
                        ReedContext.getString("proj_name") :
                ReedContext.getString("proj.name");
    }

}
