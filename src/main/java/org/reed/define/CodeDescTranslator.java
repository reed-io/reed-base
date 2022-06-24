/**
 * base/org.reed.define/CodeDescTranslator.java
 */
package org.reed.define;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.reed.log.ReedLogger;
import org.reed.utils.EnderUtil;
import org.reed.utils.StringUtil;

/**
 * @author chenxiwen
 * @date 2017年8月16日上午10:42:10
 */
public class CodeDescTranslator {
    static final HashMap<String,String> errCodeMap=new HashMap<String,String>();
    static final HashMap<String,String> brandInfoMap=new HashMap<String,String>();
    
    static{
        brandInfoMap.put("HOTLINE", "010-56702070");
        brandInfoMap.put("ADDRESS", "北京市昌平区沙河镇西沙桥西中国石油科技园A29地块3层");
        brandInfoMap.put("AUTHOR", "中油瑞飞研究院 移动研发部");
    }
    
    private CodeDescTranslator(){
        
    }
    
    public static void init(Class<? extends BaseErrorCode> errCodeClass){
        try{
            for(Field field:errCodeClass.getDeclaredFields()){
              if(field.isAnnotationPresent(CodeDescTag.class)&&Modifier.isStatic(field.getModifiers())){
                Object vals=field.get(null);
                String code=null;
                CodeDescTag codeDesctag=field.getAnnotation(CodeDescTag.class);
                String desc=codeDesctag.desc();
                String[] apps=codeDesctag.append();
                for(int idx=0;idx<apps.length;idx++){
                  desc+=brandInfoMap.get(apps[idx]);
                }
                if(vals.getClass().equals(String.class)){
                  code=vals.toString();
                }
                if(vals.getClass().equals(int.class)||vals.getClass().equals(Integer.class)){
                  code=Integer.toHexString(Integer.parseInt(vals.toString()));
                }
                if(errCodeMap.containsKey(code)){
                  String vdesc=errCodeMap.get(code);
                  StringBuffer buffer=new StringBuffer("编码重复定义:");
                  buffer.append("[").append(vals).append(":").append(desc).append("]->");
                  buffer.append("[").append(vals).append(":").append(vdesc).append("]");
                  throw new RuntimeException(buffer.toString());
                }
                errCodeMap.put(code,desc);
              }
            }
            ReedLogger.info("编码解析类("+errCodeClass.getCanonicalName()+")成功添加到系统解析器CodeDescTranslator字典....");
          }
          catch(SecurityException e){
              e.printStackTrace();
              ReedLogger.error(EnderUtil.getDevInfo()+" - "+e.getMessage(), e);
          }
          catch(IllegalArgumentException e){
              e.printStackTrace();
              ReedLogger.error(EnderUtil.getDevInfo()+" - "+e.getMessage(), e);
          }
          catch(IllegalAccessException e){
              e.printStackTrace();
              ReedLogger.error(EnderUtil.getDevInfo()+" - "+e.getMessage(), e);
          }
    }
    
    public static String explain(int errorCode){
        return explain(errorCode, null, null);
    }
    
    public static String explain(int errorCode, Throwable thrown){
        return explain(errorCode, null, "e:"+thrown.getMessage());
    }
    
    public static <T extends Object>String explain(int errorCode,T t,String...args){
        String codeStr = Integer.toHexString(errorCode);
        String result = errCodeMap.get(codeStr);
        result = StringUtil.isEmpty(result)?"未知编码！":result;
        boolean ifirst=true;
        int start=-1;
        while((start=result.indexOf("$"))!=-1){
          int end=result.indexOf("}");
          String str=result.substring(start+2,end);
          Map<String,String> defs=new HashMap<String,String>();
          if(args!=null&&ifirst){
            ifirst=false;
            for(int idx=0;idx<args.length;idx++){
              String[] propkey=StringUtil.toArray(args[idx],":");
              try{
                defs.put(propkey[0],propkey[1]);
              }
              catch(Exception ex){
                defs.put(propkey[0],"");
              }
            }
            for(String key:defs.keySet()){
              if(result.indexOf("${"+key+"}")!=-1){
                String objval=null;
                try{
                  Field field=EnderUtil.getFieldByName(key,t.getClass());
                  field.setAccessible(true);
                  objval=field.get(t).toString();
                }
                catch(Exception ex){
                  objval=defs.get(key);
                }
                result=StringUtil.replace(result,"${"+key+"}",objval);
              }
            }
          }
          if(result.indexOf("${"+str+"}")!=-1){
            String objval=null;
            try{
              Field field=EnderUtil.getFieldByName(str,t.getClass());
              field.setAccessible(true);
              objval=field.get(t).toString();
            }
            catch(Exception ex){
              objval="";
            }
            result=StringUtil.replace(result,"${"+str+"}",objval);
          }
        }
        return result;
      }
    
}
