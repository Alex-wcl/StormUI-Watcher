package com.weibo.stormUI.model;

public class SupervisorData {
	private String supervisorId;
	private String host;
	private String uptime;
	private int slotsTotal;
	private int slotsUsed;
	
	@Override
	public String toString() {
		return "supervisorId = " + supervisorId + ",host = " + host
				+ ",uptime = " + uptime + ",slotsTotal = " + slotsTotal
				+ ",slotsUsed = " + slotsUsed ;
	}
	
	public String getSupervisorId() {
		return supervisorId;
	}
	public void setSupervisorId(String supervisorId) {
		this.supervisorId = supervisorId;
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

	
}
