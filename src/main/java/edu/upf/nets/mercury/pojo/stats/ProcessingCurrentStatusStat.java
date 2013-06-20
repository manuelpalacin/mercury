package edu.upf.nets.mercury.pojo.stats;

import java.util.Date;

public class ProcessingCurrentStatusStat {
	
	private String incompleted;
	private String completed;
	private String processing;
	private String pending;
	private String error;
	private Date timeStamp;
	
	public String getIncompleted() {
		return incompleted;
	}
	public void setIncompleted(String incompleted) {
		this.incompleted = incompleted;
	}
	public String getCompleted() {
		return completed;
	}
	public void setCompleted(String completed) {
		this.completed = completed;
	}
	public String getProcessing() {
		return processing;
	}
	public void setProcessing(String processing) {
		this.processing = processing;
	}
	public String getPending() {
		return pending;
	}
	public void setPending(String pending) {
		this.pending = pending;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public Date getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	

}
