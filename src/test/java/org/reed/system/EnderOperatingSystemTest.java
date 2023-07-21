/**
 * E5Projects#org.reed.system/EnderOperatingSystemTest.java
 */
package org.reed.system;

import org.reed.system.EnderOperatingSystem.NetworkConfig;

/**
 * @author chenxiwen
 * @date 2020-12-08 14:40:22
 */
public class EnderOperatingSystemTest {

    public static void main(String[] args) {
        NetworkConfig networkConfig = EnderOperatingSystem.getInstance().getMainNetworkConfig();
        System.out.println(networkConfig);
        System.out.println(EnderOperatingSystem.getInstance().getDiskSN());
        System.out.println(EnderOperatingSystem.getInstance().getCpu());
        System.out.println(EnderOperatingSystem.getInstance().getCpu().getFrequencyStr());
        System.out.println(EnderOperatingSystem.getInstance().getSystemID());
        System.out.println(EnderOperatingSystem.getInstance().getMemory());
        System.out.println(EnderOperatingSystem.getInstance().getMemory().getFreeMemStr());
        System.out.println(EnderOperatingSystem.getInstance().getFreeDiskSpace());
    }
}
