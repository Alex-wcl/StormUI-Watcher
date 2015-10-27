package com.weibo.stormUI.model;

public class SpoutData {
	private double executors;
	private int emitted;
	private double completeLatency;
	private int transferred;
	private int acked;
	private String errorPort;
	private String spoutId;
	private double tasks;
	private String topologyId;
	private String errorLapsedSecs;
	
	
	/**
	 * "executors":1,
	 * "emitted":1133200,
	 * "errorLapsedSecs":null,
	 * "completeLatency":"0.000",
	 * "transferred":1133200,
	 * "acked":1133180,
	 * "errorPort":"",
	 * "spoutId":"grouptag_spout",
	 * "tasks":1,
	 * "errorHost":"",
	 * "lastError":"",
	 * "errorWorkerLogLink":"http://:8000/log?file=worker-.log",
	 * "failed":0,
	 * "encodedSpoutId":
	 * "grouptag_spout"
	 */
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


    public double getExecutors() {
        return executors;
    }


    public void setExecutors(double executors) {
        this.executors = executors;
    }


    public int getEmitted() {
        return emitted;
    }


    public void setEmitted(int emitted) {
        this.emitted = emitted;
    }


    public double getCompleteLatency() {
        return completeLatency;
    }


    public void setCompleteLatency(double completeLatency) {
        this.completeLatency = completeLatency;
    }


    public int getTransferred() {
        return transferred;
    }


    public void setTransferred(int transferred) {
        this.transferred = transferred;
    }


    public int getAcked() {
        return acked;
    }


    public void setAcked(int acked) {
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


    public double getTasks() {
        return tasks;
    }


    public void setTasks(double tasks) {
        this.tasks = tasks;
    }


    public String getTopologyId() {
        return topologyId;
    }


    public void setTopologyId(String topologyId) {
        this.topologyId = topologyId;
    }


    public String getErrorLapsedSecs() {
        return errorLapsedSecs;
    }


    public void setErrorLapsedSecs(String errorLapsedSecs) {
        this.errorLapsedSecs = errorLapsedSecs;
    }

	
	
	
}
