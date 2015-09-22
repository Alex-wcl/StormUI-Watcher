package com.weibo.stormUI.dataPersistencer.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.weibo.stormUI.dataPersistencer.DataPersistencer;
import com.weibo.stormUI.model.BoltData;
import com.weibo.stormUI.model.ClusterData;
import com.weibo.stormUI.model.SpoutData;
import com.weibo.stormUI.model.SupervisorData;
import com.weibo.stormUI.model.TopologyData;
import com.weibo.stormUI.util.InfluxDBUtil;

@Component
public class DataPersistencerImpl extends DataPersistencer{
	// 初始化五个数据操作类
	InfluxDBUtil<ClusterData> clusterInfluxDB = new InfluxDBUtil<ClusterData>();
	InfluxDBUtil<TopologyData> topologyInfluxDB = new InfluxDBUtil<TopologyData>();
	InfluxDBUtil<BoltData> boltInfluxDB = new InfluxDBUtil<BoltData>();
	InfluxDBUtil<SpoutData> spoutInfluxDB = new InfluxDBUtil<SpoutData>();
	InfluxDBUtil<SupervisorData> supervisorInfluxDB = new InfluxDBUtil<SupervisorData>();
	public DataPersistencerImpl(){
		super("10.77.108.126","8086","root","root","storm");
		clusterInfluxDB.setUp(SERVER_IP, SERVER_PORT, DB_USERNAME, DB_PASSWORD);
		clusterInfluxDB.createDB(INFLUXDBNAME);
	}
	public DataPersistencerImpl(String serverIP, String serverPORT, String DBUserName, String DBPassword,
			String DBName) {
		super(serverIP, serverPORT, DBUserName, DBPassword, DBName);
		// 初始化数据连接。只要一次就行了，因为成员变量公用。
		clusterInfluxDB.setUp(SERVER_IP, SERVER_PORT, DB_USERNAME, DB_PASSWORD);
		clusterInfluxDB.createDB(INFLUXDBNAME);
	}
	
	
	@Override
	public boolean saveData(Map<String, Object> map) {
		Map<String, Object> data = map;
		if(data != null){
			ClusterData clusterData = (ClusterData)map.get("clusterData");
			if(clusterData != null){
				clusterInfluxDB.insertData(clusterData);
			}
			List<TopologyData> topologyDatas = (List<TopologyData>)map.get("topologyDatas");
			if(topologyDatas != null){
				topologyInfluxDB.insertDatas(topologyDatas);
			}
			List<SupervisorData> supervisorDatas = (List<SupervisorData>)map.get("supervisorDatas");
			if(supervisorDatas != null){
				supervisorInfluxDB.insertDatas(supervisorDatas);
			}
			
			int topologySize = topologyDatas.size();
			for(int i = 0;i < topologySize;i++){
				Map<String,Object> spoutsANDBolts = (Map<String, Object>)map.get(topologyDatas.get(i).getTopologyId());
				List<BoltData> boltDatas = (List<BoltData>) spoutsANDBolts.get("boltDatas");
				List<SpoutData> spoutDatas = (List<SpoutData>) spoutsANDBolts.get("spoutDatas");
				boltInfluxDB.insertDatas(boltDatas);
				spoutInfluxDB.insertDatas(spoutDatas);
			}
			
			return true;
		}else{
			return false;
		}
	}
	
	
}
