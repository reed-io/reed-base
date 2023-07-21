package org.reed.system;

import org.reed.utils.StringUtil;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.software.os.NetworkParams;
import oshi.software.os.OperatingSystem;

import java.util.Arrays;
import java.util.List;

public class OshiTest {

    public static void main(String[] args) {
        SystemInfo systemInfo = new SystemInfo();
        System.out.println(systemInfo);
        HardwareAbstractionLayer hal = systemInfo.getHardware();
        OperatingSystem os = systemInfo.getOperatingSystem();
        System.out.println(os);

        NetworkParams networkParams = os.getNetworkParams();
        System.out.println(networkParams);

        List<NetworkIF> networkIFs = hal.getNetworkIFs();
        networkIFs.stream().filter(networkIF -> networkIF.getMTU()>0 && networkIF.getIPv4addr().length>0 && StringUtil.isMacAddress(networkIF.getMacaddr()))
                .forEach(networkIF -> {
            System.out.println(networkIF.toString());
            System.out.println(networkIF.getName());
            System.out.println(networkIF.getDisplayName());
            Arrays.stream(networkIF.getIPv4addr()).forEach(s -> System.out.println("ipv4:"+s));
            Arrays.stream(networkIF.getSubnetMasks()).forEach(aShort -> System.out.println(aShort));
            Arrays.stream(networkIF.getIPv6addr()).forEach(s -> System.out.println("ipv6:"+s));
            System.out.println("mac:"+networkIF.getMacaddr());
            System.out.println("---------------------------------------------");
        });


        System.out.println(hal.getMemory());
        long totalMem = hal.getMemory().getTotal();
        System.out.println(totalMem);
        long available = hal.getMemory().getAvailable();
        System.out.println(available);
        System.out.println(hal.getMemory().getVirtualMemory());
        System.out.println("---------------------------------------------");


        System.out.println("processor: "+hal.getProcessor());
        System.out.println("processorId: "+hal.getProcessor().getProcessorIdentifier().getProcessorID());
        System.out.println("logical processors 0: "+hal.getProcessor().getLogicalProcessors().get(0).toString());
        System.out.println("processor name: "+hal.getProcessor().getProcessorIdentifier().getName());
        System.out.println("processor frequency: "+hal.getProcessor().getProcessorIdentifier().getVendorFreq());
        System.out.println("processor split(\"\")[0]: "+hal.getProcessor().getProcessorIdentifier().toString().split(" ")[0]);
        System.out.println("arch: " + (hal.getProcessor().getProcessorIdentifier().isCpu64bit()?"x64":"x86"));
//        System.out.println("processor split(\"\")[2]: "+hal.getProcessor().getProcessorIdentifier().getName().split(" ")[2]);
//        System.out.println("processor split(\"\")[5]: "+hal.getProcessor().getProcessorIdentifier().getName().split(" ")[5]);

        hal.getProcessor().getLogicalProcessors().forEach(logicalProcessor -> {
            System.out.println(logicalProcessor.toString());
            System.out.println("nunaNode:"+logicalProcessor.getNumaNode());
            System.out.println("physicalPackageNumber:"+logicalProcessor.getPhysicalPackageNumber());
            System.out.println("physicalProcessorNumber:"+logicalProcessor.getPhysicalProcessorNumber());
            System.out.println("processorGroup:"+logicalProcessor.getProcessorGroup());
            System.out.println("processorNumber:"+logicalProcessor.getProcessorNumber());
            System.out.println(hal.getProcessor().getMaxFreq());
            System.out.println("-------------next processor-------------");
        });


        System.out.println("---------------------------------------------");

        hal.getDiskStores().forEach(hwDiskStore -> {
            System.out.println(hwDiskStore);
            System.out.println("name:"+hwDiskStore.getName());
            System.out.println("serial:"+hwDiskStore.getSerial());
            System.out.println("size:"+hwDiskStore.getSize());
            System.out.println("partitions:"+hwDiskStore.getPartitions());
            System.out.println("readBytes:"+hwDiskStore.getReadBytes());
            System.out.println("writeBytes:"+hwDiskStore.getWriteBytes());
        });

        System.out.println("---------------------------------------------");
        System.out.println(os.getFileSystem());

        os.getFileSystem().getFileStores().forEach(osFileStore -> {
            System.out.println(osFileStore.getName());
            System.out.println(osFileStore.getTotalSpace());
            System.out.println(osFileStore.getFreeSpace());
            System.out.println(osFileStore.getType());
            System.out.println(osFileStore.getMount());
            System.out.println(osFileStore.getUUID());
            System.out.println(osFileStore.toString());
        });
        System.out.println("---------------------------------------------");

        hal.getUsbDevices(true).forEach(usbDevice -> System.out.println("usbdevice:"+usbDevice));
    }
}
