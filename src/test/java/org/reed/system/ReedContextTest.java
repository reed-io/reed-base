/**
 * E5Projects @ org.reed.system/ReedContextTest.java
 */
package org.reed.system;

import org.reed.bootup.ReedStarter;

/**
 * @author chenxiwen
 * @createTime 2019年12月12日 下午3:51
 * @description
 */
public class ReedContextTest {
    public static void main(String[] args){
//        for(Object env:System.getProperties().keySet()){
//            System.out.println(env+":"+System.getProperty(env.toString()));
//        }
        System.out.println(System.getenv(ReedStarter.REED_SERVICE_REGISTER_CENTER));
        String s = ReedContext.getString(ReedStarter.REED_SERVICE_REGISTER_CENTER);
        System.out.println(s);
    }
}
