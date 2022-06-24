/**
 * IdeaProject @ org.reed.bootup/Startable.java
 */
package org.reed.bootup;

import java.util.Set;

/**
 * @author chenxiwen
 * @createTime 2018年11月08日 上午11:01
 * @description
 */
public interface Startable {
    /**
     *S
     * @return 定义模块名称
     */
    String getModuleName();

    /**
     *
     * @return 框架启动参数集合
     */
    Set<BootupArg> bootArgs();
}
