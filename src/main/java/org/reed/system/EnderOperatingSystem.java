/**
 * E5Projects#org.reed.system/EnderOperatingSystem.java
 */
package org.reed.system;

import org.reed.utils.CollectionUtil;
import org.reed.utils.StringUtil;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.NetworkParams;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author chenxiwen
 * @date 2020-12-07 16:42:33
 */
public final class EnderOperatingSystem {

    private final String ENCRYPT_KEY = "ender123";

    private static final EnderOperatingSystem instance;

    static{
         instance = new EnderOperatingSystem();
    }

    private final CPU cpu;

    private final Memory memory;

    private List<Disk> disks;

    private List<FileSystem> fileSystems;

    private List<NetworkConfig> networkConfigs;

    private SystemInfo systemInfo;

    private EnderOperatingSystem() {
        if(systemInfo == null){
            systemInfo = new SystemInfo();
        }
        if(disks == null){
            disks = new ArrayList<>();
        }
        if(fileSystems == null){
            fileSystems = new ArrayList<>();
        }
        if(networkConfigs == null){
            networkConfigs = new ArrayList<>();
        }
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
        CentralProcessor processor = hardware.getProcessor();
        String[] processorName = processor.getProcessorIdentifier().getName().split(" ");
        this.cpu = new CPU(processor.getProcessorIdentifier().getProcessorID().trim(), processorName[0],
                processor.getProcessorIdentifier().isCpu64bit()?"x64":"x86", processorName.length>2?processorName[2]:processorName[processorName.length-1],
                processor.getMaxFreq(), processor.getPhysicalProcessorCount(),
                processor.getLogicalProcessorCount());
        GlobalMemory memory = hardware.getMemory();
        this.memory = new Memory(memory.getTotal(), memory.getTotal()-memory.getAvailable(), memory.getAvailable(),
                memory.getVirtualMemory().getSwapTotal(),
                memory.getVirtualMemory().getSwapTotal()-memory.getVirtualMemory().getSwapUsed(),
                memory.getVirtualMemory().getSwapUsed(), memory.getVirtualMemory().getVirtualMax(),
                memory.getVirtualMemory().getVirtualMax()-memory.getVirtualMemory().getVirtualInUse(),
                memory.getVirtualMemory().getVirtualInUse());
        hardware.getDiskStores().forEach(hwDiskStore -> disks.add
                (new Disk(hwDiskStore.getSerial().trim(), hwDiskStore.getSize(), hwDiskStore.getModel())));
        operatingSystem.getFileSystem().getFileStores().forEach(osFileStore ->  fileSystems.add(
                new FileSystem(osFileStore.getUUID(),osFileStore.getMount(), osFileStore.getLabel(),
                        osFileStore.getTotalSpace(), osFileStore.getFreeSpace(), osFileStore.getType())));
        NetworkParams networkParams = operatingSystem.getNetworkParams();
        hardware.getNetworkIFs(true).forEach(networkIF -> networkConfigs.add(
                new NetworkConfig(networkIF.getName(), networkIF.getDisplayName(), networkParams.getHostName(),
                        networkParams.getDomainName(), networkIF.getMacaddr(), networkIF.getIPv4addr().length>0?networkIF.getIPv4addr()[0]:null,
                        networkParams.getIpv4DefaultGateway(), networkIF.getSubnetMasks().length>0?networkIF.getSubnetMasks()[0]:0,
                        networkIF.getIPv6addr().length>0?networkIF.getIPv6addr()[0]:null, networkParams.getIpv6DefaultGateway(),
                        networkIF.getPrefixLengths().length>0?networkIF.getPrefixLengths()[0]:0,
                        networkParams.getDnsServers().length>0?networkParams.getDnsServers()[0]:null,
                        networkParams.getDnsServers().length>1?networkParams.getDnsServers()[1]:null, networkIF.getMTU())
        ));
        networkConfigs.sort(new Comparator<NetworkConfig>() {
            @Override
            public int compare(NetworkConfig o1, NetworkConfig o2) {
                final boolean isO1Ipv4Empty = StringUtil.isEmpty(o1.getIpv4());
                final boolean isO2Ipv4Empty = StringUtil.isEmpty(o2.getIpv4());
                final boolean isO1MacAddress = StringUtil.isMacAddress(o1.getMacAddress());
                final boolean isO2MacAddress = StringUtil.isMacAddress(o2.getMacAddress());
                if(isO1MacAddress && isO2MacAddress){
                    if(!isO1Ipv4Empty&&!isO2Ipv4Empty){
                        return o2.weight-o1.weight;
                    }
                    if(isO1Ipv4Empty){
                        if(isO2Ipv4Empty){
                            return o2.weight-o1.weight;
                        }else{
                            return 1;
                        }
                    }else{
                        if(isO2Ipv4Empty){
                            return -1;
                        }else{
                            return o2.weight-o1.weight;
                        }
                    }
                }else{
                    if(!isO1MacAddress){
                        if(!isO2MacAddress){
                            return o2.weight-o1.weight;
                        }else{
                            return 1;
                        }
                    }else{
                        if(isO2Ipv4Empty){
                            return -1;
                        }else{
                            return o2.weight-o1.weight;
                        }
                    }
                }
            }
        });
//        System.out.println(networkConfigs);
    }


    public static EnderOperatingSystem getInstance(){
        return instance;
    }

    public CPU getCpu() {
        return cpu;
    }

    public Memory getMemory() {
        return memory;
    }

    public List<Disk> getDisks() {
        return disks;
    }

    public List<FileSystem> getFileSystems() {
        return fileSystems;
    }

    public List<NetworkConfig> getNetworkConfigs() {
        return networkConfigs;
    }

    public SystemInfo getSystemInfo() {
        return systemInfo;
    }

    public long getFreeDiskSpace(){
        long freeSize = 0l;
        for (FileSystem fileSystem:
             this.fileSystems) {
            freeSize+=fileSystem.getFreeSize();
        }
        return freeSize;
    }

    public long getDiskSpace(){
        long size = 0l;
        for (FileSystem fileSystem:
             this.fileSystems) {
            size+=fileSystem.getTotalSize();
        }
        return size;
    }

    public String getFreeDiskSpaceStr(){
        return FormatUtil.formatBytes(getFreeDiskSpace());
    }

    public String getDiskSN(){
        String result = "";
        for (Disk disk:
             this.disks) {
            result+=disk.getSerialNumber();
        }
        return result;
    }

    public String getSystemID(){
        try {
            return StringUtil.encrypt2CipherText(this.cpu.getCpuSN()+"@"+getDiskSN(), ENCRYPT_KEY);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException("System do not support UTF-8 encoding!");
        }
    }

    public long getSystemUpTime(){
        return systemInfo.getOperatingSystem().getSystemUptime();
    }

    public long getCurrentFreeMem(){
        return systemInfo.getHardware().getMemory().getAvailable();
    }

    public NetworkConfig getMainNetworkConfig(){
        return this.networkConfigs.size()>0?this.networkConfigs.get(0):null;
    }

    protected final class CPU{
        private String cpuSN;
        // Inter/AMD
        private String brand;

        // x86/x64/arm
        private String architecture;

        // i5-6100u, ryzen-3400g, etc...
        private String model;

        //max frequency of this processor
        private long frequency;

        private int coreCount;

        private int threadCount;

        private CPU() {
        }

        public CPU(String cpuSN, String brand, String architecture, String model, long frequency, int coreCount, int threadCount) {
            this.cpuSN = cpuSN;
            this.brand = brand;
            this.architecture = architecture;
            this.model = model;
            this.frequency = frequency;
            this.coreCount = coreCount;
            this.threadCount = threadCount;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getArchitecture() {
            return architecture;
        }

        public void setArchitecture(String architecture) {
            this.architecture = architecture;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public int getCoreCount() {
            return coreCount;
        }

        public void setCoreCount(int coreCount) {
            this.coreCount = coreCount;
        }

        public int getThreadCount() {
            return threadCount;
        }

        public void setThreadCount(int threadCount) {
            this.threadCount = threadCount;
        }

        public long getFrequency() {
            return frequency;
        }

        public String getFrequencyStr() {
            return FormatUtil.formatHertz(frequency);
        }

        public void setFrequency(long frequency) {
            this.frequency = frequency;
        }

        public String getCpuSN() {
            return cpuSN;
        }

        public void setCpuSN(String cpuSN) {
            this.cpuSN = cpuSN;
        }

        @Override
        public String toString() {
            return "CPU{" +
                    "cpuSN='" + cpuSN + '\'' +
                    ", brand='" + brand + '\'' +
                    ", architecture='" + architecture + '\'' +
                    ", model='" + model + '\'' +
                    ", frequency=" + frequency +
                    ", coreCount=" + coreCount +
                    ", threadCount=" + threadCount +
                    '}';
        }
    }


    protected final class Memory {

        private long totalMem;

        private long usedMem;

        private long freeMem;

        private long swapTotal;

        private long swapFree;

        private long swapUsed;

        private long virtualTotal;

        private long virtualUsed;

        private long virtualFree;

        private Memory() {
        }

        public Memory(long totalMem, long usedMem, long freeMem, long swapTotal, long swapFree, long swapUsed, long virtualTotal, long virtualFree, long virtualUsed) {
            this.totalMem = totalMem;
            this.usedMem = usedMem;
            this.freeMem = freeMem;
            this.swapTotal = swapTotal;
            this.swapFree = swapFree;
            this.swapUsed = swapUsed;
            this.virtualTotal = virtualTotal;
            this.virtualUsed = virtualUsed;
            this.virtualFree = virtualFree;
        }

        public long getTotalMem() {
            return totalMem;
        }

        public String getTotalMemStr(){
            return FormatUtil.formatBytes(totalMem);
        }

        public void setTotalMem(long totalMem) {
            this.totalMem = totalMem;
        }

        public long getUsedMem() {
            return usedMem;
        }

        public String getUsedMemStr(){
            return FormatUtil.formatBytes(usedMem);
        }

        public void setUsedMem(long usedMem) {
            this.usedMem = usedMem;
        }

        public long getFreeMem() {
            return freeMem;
        }

        public String getFreeMemStr(){
            return FormatUtil.formatBytes(freeMem);
        }

        public void setFreeMem(long freeMem) {
            this.freeMem = freeMem;
        }

        public long getSwapTotal() {
            return swapTotal;
        }

        public void setSwapTotal(long swapTotal) {
            this.swapTotal = swapTotal;
        }

        public long getSwapFree() {
            return swapFree;
        }

        public void setSwapFree(long swapFree) {
            this.swapFree = swapFree;
        }

        public long getSwapUsed() {
            return swapUsed;
        }

        public void setSwapUsed(long swapUsed) {
            this.swapUsed = swapUsed;
        }

        public long getVirtualTotal() {
            return virtualTotal;
        }

        public void setVirtualTotal(long virtualTotal) {
            this.virtualTotal = virtualTotal;
        }

        public long getVirtualUsed() {
            return virtualUsed;
        }

        public void setVirtualUsed(long virtualUsed) {
            this.virtualUsed = virtualUsed;
        }

        public long getVirtualFree() {
            return virtualFree;
        }

        public void setVirtualFree(long virtualFree) {
            this.virtualFree = virtualFree;
        }

        @Override
        public String toString() {
            return "Memory{" +
                    "totalMem=" + totalMem +
                    ", usedMem=" + usedMem +
                    ", freeMem=" + freeMem +
                    ", swapTotal=" + swapTotal +
                    ", swapFree=" + swapFree +
                    ", swapUsed=" + swapUsed +
                    ", virtualTotal=" + virtualTotal +
                    ", virtualUsed=" + virtualUsed +
                    ", virtualFree=" + virtualFree +
                    '}';
        }
    }


    protected final class Disk{

        private String serialNumber;

        private long size;

        private String model;

        private Disk() {
        }

        public Disk(String serialNumber, long size, String model) {
            this.serialNumber = serialNumber;
            this.size = size;
            this.model = model;
        }

        public String getSerialNumber() {
            return serialNumber;
        }

        public void setSerialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        @Override
        public String toString() {
            return "Disk{" +
                    "serialNumber='" + serialNumber + '\'' +
                    ", size=" + size +
                    ", model='" + model + '\'' +
                    '}';
        }
    }

    protected final class FileSystem{
        private String volumeId;

        private String mount;

        private String name;

        private long totalSize;

        private long freeSize;

        private String type;

        private FileSystem() {
        }

        public FileSystem(String volumeId, String mount, String name, long totalSize, long freeSize, String type) {
            this.volumeId = volumeId;
            this.mount = mount;
            this.name = name;
            this.totalSize = totalSize;
            this.freeSize = freeSize;
            this.type = type;
        }

        public String getVolumeId() {
            return volumeId;
        }

        public void setVolumeId(String volumeId) {
            this.volumeId = volumeId;
        }

        public String getMount() {
            return mount;
        }

        public void setMount(String mount) {
            this.mount = mount;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getTotalSize() {
            return totalSize;
        }

        public void setTotalSize(long totalSize) {
            this.totalSize = totalSize;
        }

        public long getFreeSize() {
            return freeSize;
        }

        public void setFreeSize(long freeSize) {
            this.freeSize = freeSize;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return "FileSystem{" +
                    "volumeId='" + volumeId + '\'' +
                    ", mount='" + mount + '\'' +
                    ", name='" + name + '\'' +
                    ", totalSize=" + totalSize +
                    ", freeSize=" + freeSize +
                    ", type='" + type + '\'' +
                    '}';
        }
    }

    protected final class NetworkConfig{
        private String name;
        private String model;
        private String hostName;
        private String domainName;
        private String macAddress;
        private String ipv4;
        private String ipv4Gateway;
        private short subnetMask;
        private String ipv6;
        private String ipv6Gateway;
        private short prefixLength;
        private String mainDNS;
        private String backupDNS;

        private int weight;

        private NetworkConfig() {
        }

        public NetworkConfig(String name, String model, String hostName, String domainName, String macAddress, String ipv4, String ipv4Gateway, short subnetMask, String ipv6, String ipv6Gateway, short prefixLength, String mainDNS, String backupDNS, int weight) {
            this.name = name;
            this.model = model;
            this.hostName = hostName;
            this.domainName = domainName;
            this.macAddress = macAddress;
            this.ipv4 = ipv4;
            this.ipv4Gateway = ipv4Gateway;
            this.subnetMask = subnetMask;
            this.ipv6 = ipv6;
            this.ipv6Gateway = ipv6Gateway;
            this.prefixLength = prefixLength;
            this.mainDNS = mainDNS;
            this.backupDNS = backupDNS;
            this.weight = weight;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getHostName() {
            return hostName;
        }

        public void setHostName(String hostName) {
            this.hostName = hostName;
        }

        public String getDomainName() {
            return domainName;
        }

        public void setDomainName(String domainName) {
            this.domainName = domainName;
        }

        public String getMacAddress() {
            return macAddress;
        }

        public void setMacAddress(String macAddress) {
            this.macAddress = macAddress;
        }

        public String getIpv4() {
            return ipv4;
        }

        public void setIpv4(String ipv4) {
            this.ipv4 = ipv4;
        }

        public String getIpv4Gateway() {
            return ipv4Gateway;
        }

        public void setIpv4Gateway(String ipv4Gateway) {
            this.ipv4Gateway = ipv4Gateway;
        }

        public short getSubnetMask() {
            return subnetMask;
        }

        public void setSubnetMask(short subnetMask) {
            this.subnetMask = subnetMask;
        }

        public String getIpv6() {
            return ipv6;
        }

        public void setIpv6(String ipv6) {
            this.ipv6 = ipv6;
        }

        public String getIpv6Gateway() {
            return ipv6Gateway;
        }

        public void setIpv6Gateway(String ipv6Gateway) {
            this.ipv6Gateway = ipv6Gateway;
        }

        public short getPrefixLength() {
            return prefixLength;
        }

        public void setPrefixLength(short prefixLength) {
            this.prefixLength = prefixLength;
        }

        public String getMainDNS() {
            return mainDNS;
        }

        public void setMainDNS(String mainDNS) {
            this.mainDNS = mainDNS;
        }

        public String getBackupDNS() {
            return backupDNS;
        }

        public void setBackupDNS(String backupDNS) {
            this.backupDNS = backupDNS;
        }

        public long getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        @Override
        public String toString() {
            return "NetworkConfig{" +
                    "name='" + name + '\'' +
                    ", model='" + model + '\'' +
                    ", hostName='" + hostName + '\'' +
                    ", domainName='" + domainName + '\'' +
                    ", macAddress='" + macAddress + '\'' +
                    ", ipv4='" + ipv4 + '\'' +
                    ", ipv4Gateway='" + ipv4Gateway + '\'' +
                    ", subnetMask=" + subnetMask +
                    ", ipv6='" + ipv6 + '\'' +
                    ", ipv6Gateway='" + ipv6Gateway + '\'' +
                    ", prefixLength=" + prefixLength +
                    ", mainDNS='" + mainDNS + '\'' +
                    ", backupDNS='" + backupDNS + '\'' +
                    ", weight=" + weight +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "EnderOperatingSystem{" +
                "cpu=" + cpu +
                ", memory=" + memory +
                ", disks=" + CollectionUtil.toString(disks) +
                ", fileSystems=" + CollectionUtil.toString(fileSystems) +
                ", networkConfigs=" + CollectionUtil.toString(networkConfigs) +
                '}';
    }
}
