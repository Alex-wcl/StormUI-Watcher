package com.weibo.stormUI.model;

public class TopologyData {
	private String topologyId;
	private String encodedId;
	private String topolotyName;
	private String status;
	private String uptime;
	private int tasksTotal;
	private int workersTotal;
	private int executorsTotal;
	
	@Override
	public String toString() {
		return "topologyId = " + topologyId + ",encodedId = " + encodedId
				+ ",topolotyName = " + topolotyName + ",status = " + status
				+ ",uptime = " + uptime + ",tasksTotal = " + tasksTotal
				+ ",workersTotal = " + workersTotal + ",executorsTotal = " + executorsTotal;
	}
	
	public String getTopologyId() {
		return topologyId;
	}
	public void setTopologyId(String topologyId) {
		this.topologyId = topologyId;
	}
	public String getEncodedId() {
		return encodedId;
	}
	public void setEncodedId(String encodedId) {
		this.encodedId = encodedId;
	}
	public String getTopolotyName() {
		return topolotyName;
	}
	public void setTopolotyName(String topolotyName) {
		this.topolotyName = topolotyName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUptime() {
		return uptime;
	}
	public void setUptime(String uptime) {
		this.uptime = uptime;
	}

	public int getTasksTotal() {
		return tasksTotal;
	}

	public void setTasksTotal(int tasksTotal) {
		this.tasksTotal = tasksTotal;
	}

	public int getWorkersTotal() {
		return workersTotal;
	}

	public void setWorkersTotal(int workersTotal) {
		this.workersTotal = workersTotal;
	}

	public int getExecutorsTotal() {
		return executorsTotal;
	}

	public void setExecutorsTotal(int executorsTotal) {
		this.executorsTotal = executorsTotal;
	}
	
}
