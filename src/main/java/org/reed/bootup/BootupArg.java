/**
 * IdeaProject @ org.reed.bootup/BootupArg.java
 */
package org.reed.bootup;

/**
 * @author chenxiwen
 * @createTime 2019年07月15日 上午10:49
 * @description
 */
@Deprecated
public enum BootupArg {
    //是否开启注册发现
    ServiceRegistrationDiscovery,
    //是否开启健康监控
    ServiceHealthManageable,
    //是否需要命令行管理套件
    ConsoleManageable,
}
