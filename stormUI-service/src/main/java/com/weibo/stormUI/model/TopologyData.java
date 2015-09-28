package com.weibo.stormUI.model;

public class TopologyData {
	private String id;
	private String encodedId;
	private String name;
	private String status;
	private String uptime;
	private int tasksTotal;
	private int workersTotal;
	private int executorsTotal;
	
	
	@Override
	public String toString() {
		return "topologyId = " + id + ",encodedId = " + encodedId
				+ ",topolotyName = " + name + ",status = " + status
				+ ",uptime = " + uptime + ",tasksTotal = " + tasksTotal
				+ ",workersTotal = " + workersTotal + ",executorsTotal = " + executorsTotal;
	}
	
	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getEncodedId() {
		return encodedId;
	}
	public void setEncodedId(String encodedId) {
		this.encodedId = encodedId;
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
