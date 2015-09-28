package com.weibo.stormUI.processor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.common.base.Stopwatch;
import com.weibo.stormUI.dataLoader.DataLoader;
import com.weibo.stormUI.dataLoader.impl.DataLoaderStorm;
import com.weibo.stormUI.dataPersistencer.impl.DataPersistencerImpl;
import com.weibo.stormUI.model.ClusterData;
import com.weibo.stormUI.model.SpoutADNBolt;
import com.weibo.stormUI.model.SupervisorData;
import com.weibo.stormUI.model.TopologyData;

@Component
public class DataProcessor implements Runnable {
	private static final Logger log = LogManager.getLogger(DataProcessor.class);
	private long SLEEPTIME = 1000 * 20;
	private List<String> topologyIds;
	private String server_IP = "10.77.128.101";
	private String server_PORT = "8080";
//	private String server_IP = "10.77.108.127";
//	private String server_PORT = "8090";
	private List<TopologyData> datas;
	public DataProcessor(){
		datas = new ArrayList<TopologyData>();
	}
	public void run() {
		//初始化topologyIds
		DataLoader<TopologyData> topologyDataLoader = new DataLoaderStorm<TopologyData>(server_IP,server_PORT,new TopologyData(),topologyIds);
		datas = topologyDataLoader.nextData();
		if(topologyIds == null){
			topologyIds = new ArrayList<String>();
			for(TopologyData tmp : datas){
				topologyIds.add(tmp.getId());
			}
		}
		DataLoader<SpoutADNBolt> boltDataLoader = new DataLoaderStorm<SpoutADNBolt>(server_IP,server_PORT,new SpoutADNBolt(),topologyIds);
		DataLoader<SupervisorData> supervisorDataLoader = new DataLoaderStorm<SupervisorData>(server_IP,server_PORT,new SupervisorData(),topologyIds);
		DataLoader<ClusterData> clusterDataLoader = new DataLoaderStorm<ClusterData>(server_IP,server_PORT,new ClusterData(),topologyIds);
		DataPersistencerImpl influxDBUtil = new DataPersistencerImpl();
		influxDBUtil.setUp("10.77.108.126","8086","root","root");
		influxDBUtil.createDB("storm");
		while(true){
			Stopwatch watch = Stopwatch.createStarted();
			List<SpoutADNBolt> spoutADNBoltData = boltDataLoader.nextData();
			List<SupervisorData> supervisorData = supervisorDataLoader.nextData();
			List<ClusterData> clusterData = clusterDataLoader.nextData();
			List<TopologyData> topologyData = topologyDataLoader.nextData();
			log.info("time = " + watch);
			influxDBUtil.saveData(spoutADNBoltData);
			influxDBUtil.saveData(supervisorData);
			influxDBUtil.saveData(clusterData);
			influxDBUtil.saveData(topologyData);
			log.info("insertDataTime = " + watch);
			try {
				Thread.sleep(SLEEPTIME);
			} catch (InterruptedException e) {
				log.catching(e);
			}
		}
	}


	public static void main(String[] args) throws IOException {
		// 开启线程
		Runnable run = new DataProcessor();
		Thread thread = new Thread(run);
		thread.start();
	}


}
