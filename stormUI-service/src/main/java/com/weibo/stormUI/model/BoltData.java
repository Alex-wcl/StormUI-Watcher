package com.weibo.stormUI.model;

public class BoltData {
	private String boltId;
	private int executors;
	private int tasks;
	private int emitted;
	private int transferred;
	private double capacity;
	private double executeLatency;
	private int executed;
	private double processLatency;
	private int acked;
	private int failed;
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

    public String getBoltId() {
        return boltId;
    }

    public void setBoltId(String boltId) {
        this.boltId = boltId;
    }

    public int getExecutors() {
        return executors;
    }

    public void setExecutors(int executors) {
        this.executors = executors;
    }

    public int getTasks() {
        return tasks;
    }

    public void setTasks(int tasks) {
        this.tasks = tasks;
    }

    public int getEmitted() {
        return emitted;
    }

    public void setEmitted(int emitted) {
        this.emitted = emitted;
    }

    public int getTransferred() {
        return transferred;
    }

    public void setTransferred(int transferred) {
        this.transferred = transferred;
    }

    public double getCapacity() {
        return capacity;
    }

  

    public double getExecuteLatency() {
        return executeLatency;
    }

    public void setExecuteLatency(double executeLatency) {
        this.executeLatency = executeLatency;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public void setExecuteLatency(int executeLatency) {
        this.executeLatency = executeLatency;
    }

    public int getExecuted() {
        return executed;
    }

    public void setExecuted(int executed) {
        this.executed = executed;
    }

    public double getProcessLatency() {
        return processLatency;
    }

    public void setProcessLatency(double processLatency) {
        this.processLatency = processLatency;
    }

    public int getAcked() {
        return acked;
    }

    public void setAcked(int acked) {
        this.acked = acked;
    }

    public int getFailed() {
        return failed;
    }

    public void setFailed(int failed) {
        this.failed = failed;
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
