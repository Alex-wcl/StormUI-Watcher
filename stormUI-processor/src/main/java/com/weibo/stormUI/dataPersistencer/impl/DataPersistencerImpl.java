package com.weibo.stormUI.dataPersistencer.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.weibo.stormUI.dataPersistencer.DataPersistencer;
import com.weibo.stormUI.model.BoltData;
import com.weibo.stormUI.model.ClusterData;
import com.weibo.stormUI.model.SpoutData;
import com.weibo.stormUI.model.SupervisorData;
import com.weibo.stormUI.model.TopologyData;
import com.weibo.stormUI.util.GlobalVariable;
import com.weibo.stormUI.util.InfluxDBUtil;

@Component
public class DataPersistencerImpl implements DataPersistencer{
	
	private static final Logger log = LogManager.getLogger(DataPersistencerImpl.class);
	// 初始化五个数据操作类
	InfluxDBUtil<ClusterData> clusterInfluxDB = new InfluxDBUtil<ClusterData>();
	InfluxDBUtil<TopologyData> topologyInfluxDB = new InfluxDBUtil<TopologyData>();
	InfluxDBUtil<BoltData> boltInfluxDB = new InfluxDBUtil<BoltData>();
	InfluxDBUtil<SpoutData> spoutInfluxDB = new InfluxDBUtil<SpoutData>();
	InfluxDBUtil<SupervisorData> supervisorInfluxDB = new InfluxDBUtil<SupervisorData>();
	// 持久化服务器的ip地址
	public  String SERVER_IP;
	// 服务器提供的端口号
	public String SERVER_PORT;
	// 数据库用户名和密码
	public String DB_USERNAME;
	public String DB_PASSWORD;
	// 要创建的数据库的名称
	public String INFLUXDBNAME;
	
	public Map<String ,Object> datas;
	
	public List<String> topologyIds = new ArrayList<String>();
	
	
//	public DataPersistencerImpl(){
//		super("10.77.108.126","8086","root","root","storm");
//		clusterInfluxDB.setUp(SERVER_IP, SERVER_PORT, DB_USERNAME, DB_PASSWORD);
//		clusterInfluxDB.createDB(INFLUXDBNAME);
//	}
	public DataPersistencerImpl(String serverIP, String serverPORT, String DBUserName, String DBPassword,
			String DBName,Map<String ,Object> datasMap) {
		this.SERVER_IP = serverIP;
		this.SERVER_PORT = serverPORT;
		this.DB_USERNAME = DBUserName;
		this.DB_PASSWORD = DBPassword;
		this.INFLUXDBNAME = DBName;
		this.datas = datasMap;
		// 初始化数据连接。只要一次就行了，因为成员变量公用。
		clusterInfluxDB.setUp(SERVER_IP, SERVER_PORT, DB_USERNAME, DB_PASSWORD);
		clusterInfluxDB.createDB(INFLUXDBNAME);
		topologyIds.add("weibo-camera-message-processor-online-1-25-7448-92-1442976290");
		topologyIds.add("nc-push-storm-1-0-23-3241-67-1442297146");
		topologyIds.add("groupchat-storm-offline-3-0-47-7000-71-1442309535");
		topologyIds.add("groupchat-storm-online-3-0-47-26441-70-1442309452");
		topologyIds.add("msgs-router-20150906-39-1441507512");
		topologyIds.add("webim_storm-1-0-9-28290-72-1442309603");
	}
	
	
	public boolean saveData() {
		log.info("datas insert starting...");
		if(datas != null){
			ClusterData clusterData = (ClusterData)datas.get("clusterData");
			if(clusterData != null){
				log.info("clusterData insert starting");
				clusterInfluxDB.insertData(clusterData);
				synchronized (datas) {
					datas.remove("clusterData");
				}
				log.info("clusterData insert successfully");
			}
			List<TopologyData> topologyDatas = (List<TopologyData>)datas.get("topologyDatas");
			if(topologyDatas != null){
				log.info("topologyDatas insert starting");
				topologyInfluxDB.insertDatas(topologyDatas);
				synchronized (datas) {
					datas.remove("topologyDatas");
				}
				log.info("topologyDatas insert successfully");
			}
			List<SupervisorData> supervisorDatas = (List<SupervisorData>)datas.get("supervisorDatas");
			if(supervisorDatas != null){
				log.info("supervisorDatas insert starting");
				supervisorInfluxDB.insertDatas(supervisorDatas);
				synchronized (datas) {
					datas.remove("supervisorDatas");
				}
				log.info("supervisorDatas insert successfully");
			}
			
			int topologySize = topologyIds.size();
			for(int i = 0;i < topologySize;i++){
				Map<String,Object> spoutsANDBolts = (Map<String, Object>)datas.get(topologyIds.get(i));
				if(spoutsANDBolts != null){
					log.info("spoutsANDBolts insert starting");
					List<BoltData> boltDatas = (List<BoltData>) spoutsANDBolts.get("boltDatas");
					List<SpoutData> spoutDatas = (List<SpoutData>) spoutsANDBolts.get("spoutDatas");
					boltInfluxDB.insertDatas(boltDatas);
					synchronized (datas) {
						datas.remove("boltDatas");
					}
					log.info("boltDatas insert successfully");
					spoutInfluxDB.insertDatas(spoutDatas);
					synchronized (datas) {
						datas.remove("spoutDatas");
					}
					log.info("spoutDatas insert successfully");
				}
			}
			log.info("datas insert end");
			return true;
		}else{
			log.info("datas is null");
			return false;
		}
	}


}
