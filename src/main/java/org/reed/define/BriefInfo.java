/**
 * IdeaProject @ org.reed.define/BriefInfo.java
 */
package org.reed.define;

import com.alibaba.fastjson2.JSONObject;
import org.reed.utils.StringUtil;

import java.lang.reflect.Field;

/**
 * @author chenxiwen
 * @createTime 2018年09月24日 下午7:00
 * @description
 */
public class BriefInfo {
    private static final BriefInfo ourInstance = new BriefInfo();

    public static BriefInfo getInstance() {
        return ourInstance;
    }

    private BriefInfo() {
    }

    public static String briefJsonString(Object obj) throws IllegalAccessException{
        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("{");
        for(Field field : fields){
            field.setAccessible(true);
            Brief brief = field.getAnnotation(Brief.class);
            if(brief != null){
                strBuf.append("\""+(StringUtil.isEmpty(brief.alias())?field.getName():brief.alias())+"\":");
                strBuf.append(field.get(obj).toString());
                strBuf.append(",");
            }
        }
        strBuf.append("}");
        String briefInfo = strBuf.toString().replaceAll(",}", "}");
        return briefInfo;
    }

    public static <T> T briefObj(T t) throws IllegalAccessException, InstantiationException, NoSuchFieldException{
        Class clazz = t.getClass();
        T proxy = (T)clazz.newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields){
            field.setAccessible(true);
            Brief brief = field.getAnnotation(Brief.class);
            if(brief != null || field.getType().isPrimitive()){//java中的基本类型也需要复制过去
                Field proxyField = proxy.getClass().getDeclaredField(field.getName());
                proxyField.setAccessible(true);
                proxyField.set(proxy, field.get(t));
            }
        }
        return proxy;
    }
}
