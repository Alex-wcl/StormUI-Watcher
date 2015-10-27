package com.weibo.stormUI.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AlarmProperties {
   
    private boolean isAvailable;
    private int worker_alarm_times;
    private int topology_alarm_times;
    private int topology_alarm_interval;
    private int worker_alarm_interval;
    private Map topologyWorker = new HashMap();
    
    public AlarmProperties(){
        
    }
       
    public AlarmProperties(Map topologyWorker, boolean isAvailable, int worker_alarm_times, int topology_alarm_times,
                           int topology_alarm_interval,int worker_alarm_interval) {
                       super();
                       this.topologyWorker = topologyWorker;
                       this.isAvailable = isAvailable;
                       this.worker_alarm_times = worker_alarm_times;
                       this.topology_alarm_times = topology_alarm_times;
                       this.topology_alarm_interval = topology_alarm_interval;
                       this.worker_alarm_interval = worker_alarm_interval;
                   }
    
    
    @Override
    public String toString() {
        Iterator iter = topologyWorker.entrySet().iterator();
        StringBuffer sub = new StringBuffer();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            sub.append(key + " : " + value);
        }
        return "isAvailable : " + isAvailable
                + ",worker_alarm_times : " + worker_alarm_times
                + ",topology_alarm_times : " + topology_alarm_times
                + ",topology_alarm_interval : " + topology_alarm_interval
                + ",worker_alarm_interval : " + worker_alarm_interval
                + sub.toString();
    }
    
    public Map getTopologyWorker() {
        return topologyWorker;
    }
    public void setTopologyWorker(Map topologyWorker) {
        this.topologyWorker = topologyWorker;
    }
    public boolean isAvailable() {
        return isAvailable;
    }
    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
    public int getWorker_alarm_times() {
        return worker_alarm_times;
    }
    public void setWorker_alarm_times(int worker_alarm_times) {
        this.worker_alarm_times = worker_alarm_times;
    }
    public int getTopology_alarm_times() {
        return topology_alarm_times;
    }
    public void setTopology_alarm_times(int topology_alarm_times) {
        this.topology_alarm_times = topology_alarm_times;
    }
    public int getTopology_alarm_interval() {
        return topology_alarm_interval;
    }
    public void setTopology_alarm_interval(int topology_alarm_interval) {
        this.topology_alarm_interval = topology_alarm_interval;
    }

    public int getWorker_alarm_interval() {
        return worker_alarm_interval;
    }

    public void setWorker_alarm_interval(int worker_alarm_interval) {
        this.worker_alarm_interval = worker_alarm_interval;
    }
    
}
