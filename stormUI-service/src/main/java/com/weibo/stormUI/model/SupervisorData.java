package com.weibo.stormUI.model;

public class SupervisorData {
	private String supervisors;
	private String id;
	private String host;
	private String uptime;
	private int slotsTotal;
	private int slotsUsed;
	
	@Override
	public String toString() {
		return "id = " + id + ",host = " + host
				+ ",uptime = " + uptime + ",slotsTotal = " + slotsTotal
				+ ",slotsUsed = " + slotsUsed ;
	}
	
	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getUptime() {
		return uptime;
	}
	public void setUptime(String uptime) {
		this.uptime = uptime;
	}
	public int getSlotsTotal() {
		return slotsTotal;
	}
	public void setSlotsTotal(int slotsTotal) {
		this.slotsTotal = slotsTotal;
	}
	public int getSlotsUsed() {
		return slotsUsed;
	}
	public void setSlotsUsed(int slotsUsed) {
		this.slotsUsed = slotsUsed;
	}

	public String getSupervisors() {
		return supervisors;
	}

	public void setSupervisors(String supervisors) {
		this.supervisors = supervisors;
	}

	
}
