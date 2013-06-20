package edu.upf.nets.mercury.monitoring;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;


@Component
public class MonitoringServerImpl implements MonitoringServer{

	private static final Logger log = Logger.getLogger(MonitoringServerImpl.class.getName());
    private long lastSystemTime      = 0;
    private long lastProcessCpuTime  = 0;
	
	
	@Override
	public double getCpuUsage() {
		
		if ( lastSystemTime == 0 ){
            baselineCounters();
        }

        long systemTime     = System.nanoTime();
        long processCpuTime = 0;

        if ( ManagementFactory.getOperatingSystemMXBean() instanceof OperatingSystemMXBean ) {
            processCpuTime = ( (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean() ).getProcessCpuTime();
        }

        double cpuUsage = (double) ( processCpuTime - lastProcessCpuTime ) / ( systemTime - lastSystemTime );
        lastSystemTime     = systemTime;
        lastProcessCpuTime = processCpuTime;

        return cpuUsage / ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();
	}

	@Override
	public double getSystemMemoryUsage() {
		long freeSystemMemory = ( (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean() ).getFreePhysicalMemorySize();
		long totalSystemMemory = ( (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean() ).getTotalPhysicalMemorySize();
		return (double) (totalSystemMemory-freeSystemMemory) / totalSystemMemory ;
	}

	@Override
	public double getJVMMemoryUsage() {
		long freeJVMMemory =  Runtime.getRuntime().freeMemory();
		long totalJVMMemory =  Runtime.getRuntime().totalMemory();
		return (double) (totalJVMMemory-freeJVMMemory) / totalJVMMemory ;
	}
	
	
    private void baselineCounters(){
        lastSystemTime = System.nanoTime();

        if ( ManagementFactory.getOperatingSystemMXBean() instanceof OperatingSystemMXBean ){
            lastProcessCpuTime = ( (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean() ).getProcessCpuTime();
        }
    }

}
