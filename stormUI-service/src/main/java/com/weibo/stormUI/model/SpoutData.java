package com.weibo.stormUI.model;

public class SpoutData {
	private String executors;
	private String emitted;
	private String completeLatency;
	private String transferred;
	private String acked;
	private String errorPort;
	private String spoutId;
	private String tasks;
	private String topologyId;
	
	@Override
	public String toString() {
		return "executors = " + executors + ",emitted = " + emitted
				+ ",completeLatency = " + completeLatency
				+ ",transferred = " + transferred
				+ ",acked = " + acked + ",errorPort = " + errorPort
				+ ",spoutId = " + spoutId
				+ ",tasks = " + tasks 
				+ ",errorWorkerLogLink = " 
				+ ",topologyId = " + topologyId;
	}

	
	
	public String getTopologyId() {
		return topologyId;
	}



	public void setTopologyId(String topologyId) {
		this.topologyId = topologyId;
	}



	public String getExecutors() {
		return executors;
	}

	public void setExecutors(String executors) {
		this.executors = executors;
	}

	public String getEmitted() {
		return emitted;
	}

	public void setEmitted(String emitted) {
		this.emitted = emitted;
	}

	

	public String getCompleteLatency() {
		return completeLatency;
	}

	public void setCompleteLatency(String completeLatency) {
		this.completeLatency = completeLatency;
	}

	public String getTransferred() {
		return transferred;
	}

	public void setTransferred(String transferred) {
		this.transferred = transferred;
	}

	public String getAcked() {
		return acked;
	}

	public void setAcked(String acked) {
		this.acked = acked;
	}

	public String getErrorPort() {
		return errorPort;
	}

	public void setErrorPort(String errorPort) {
		this.errorPort = errorPort;
	}

	public String getSpoutId() {
		return spoutId;
	}

	public void setSpoutId(String spoutId) {
		this.spoutId = spoutId;
	}

	public String getTasks() {
		return tasks;
	}

	public void setTasks(String tasks) {
		this.tasks = tasks;
	}

}
