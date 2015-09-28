package com.weibo.stormUI.model;

public class BoltData {
	private String boltId;
	private String executors;
	private String tasks;
	private String emitted;
	private String transferred;
	private String capacity;
	private String executeLatency;
	private String executed;
	private String processLatency;
	private String acked;
	private String failed;
	private String topologyId;
	private String errorLapsedSecs;
	
	@Override
	public String toString() {
		return "boltId = " + boltId + ",executors = " + executors
				+ ",tasks = " + tasks + ",emitted = " + emitted
				+ ",transferred = " + transferred
				+ ",capacity = " + capacity + ",executeLatency = " + executeLatency
				+ ",executed = " + executed
				+ ",processLatency = " + processLatency + ",acked = " + acked
				+ ",failed = " + failed
				+ ",topologyId = " + topologyId;
	}


	public String getErrorLapsedSecs() {
		return errorLapsedSecs;
	}

	public void setErrorLapsedSecs(String errorLapsedSecs) {
		this.errorLapsedSecs = errorLapsedSecs;
	}


	public String getBoltId() {
		return boltId;
	}

	public void setBoltId(String boltId) {
		this.boltId = boltId;
	}

	public String getExecutors() {
		return executors;
	}

	public void setExecutors(String executors) {
		this.executors = executors;
	}

	public String getTasks() {
		return tasks;
	}

	public void setTasks(String tasks) {
		this.tasks = tasks;
	}

	public String getEmitted() {
		return emitted;
	}

	public void setEmitted(String emitted) {
		this.emitted = emitted;
	}

	public String getTransferred() {
		return transferred;
	}

	public void setTransferred(String transferred) {
		this.transferred = transferred;
	}

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}

	public String getExecuteLatency() {
		return executeLatency;
	}

	public void setExecuteLatency(String executeLatency) {
		this.executeLatency = executeLatency;
	}

	public String getExecuted() {
		return executed;
	}

	public void setExecuted(String executed) {
		this.executed = executed;
	}

	public String getProcessLatency() {
		return processLatency;
	}

	public void setProcessLatency(String processLatency) {
		this.processLatency = processLatency;
	}

	public String getAcked() {
		return acked;
	}

	public void setAcked(String acked) {
		this.acked = acked;
	}

	public String getFailed() {
		return failed;
	}

	public void setFailed(String failed) {
		this.failed = failed;
	}


	public String getTopologyId() {
		return topologyId;
	}

	public void setTopologyId(String topologyId) {
		this.topologyId = topologyId;
	}
	

	
}
