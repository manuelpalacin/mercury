package edu.upf.nets.mercury.monitoring;

public interface MonitoringServer {
		
	public double getCpuUsage();
	
	public double getSystemMemoryUsage();
	
	public double getJVMMemoryUsage();

}
