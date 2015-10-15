package com.weibo.stormUI.processor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import com.weibo.stormUI.po.Variables;

@Component
public class DataProcessor implements Runnable {
	private static final Logger log = LogManager.getLogger(DataProcessor.class);
	private List<String> topologyIds;
	private Variables variables;
	public DataProcessor(){
		variables = Variables.getInstance();
	}
	public void run() {
		try{
			//初始化topologyIds
			DataLoader<TopologyData> topologyDataLoader = new DataLoaderStorm<TopologyData>(variables.DataSourceServerIP,variables.DataSourceServerPort,new TopologyData(),topologyIds);
			DataLoader<SupervisorData> supervisorDataLoader = new DataLoaderStorm<SupervisorData>(variables.DataSourceServerIP,variables.DataSourceServerPort,new SupervisorData(),topologyIds);
			DataLoader<ClusterData> clusterDataLoader = new DataLoaderStorm<ClusterData>(variables.DataSourceServerIP,variables.DataSourceServerPort,new ClusterData(),topologyIds);
			DataPersistencerImpl influxDBUtil = new DataPersistencerImpl();
			influxDBUtil.setUp(variables.DataBaseServerIP,variables.DataBaseServerPort,variables.DataBaseUserName,variables.DataBasePassword);
			influxDBUtil.createDB(variables.DataBaseName);
			while(true){
				Stopwatch watch = Stopwatch.createStarted();
				List<TopologyData> topologyData = topologyDataLoader.nextData();
				//为了避免topologyId可能会变的情况，将每一次查询的topologyId重新保存。
				topologyIds = new ArrayList<String>();
				for(TopologyData tmp : topologyData){
					topologyIds.add(tmp.getId());
				}
				DataLoader<SpoutADNBolt> boltDataLoader = new DataLoaderStorm<SpoutADNBolt>(variables.DataSourceServerIP,variables.DataSourceServerPort,new SpoutADNBolt(),topologyIds);
				List<SpoutADNBolt> spoutADNBoltData = boltDataLoader.nextData();
				List<SupervisorData> supervisorData = supervisorDataLoader.nextData();
				List<ClusterData> clusterData = clusterDataLoader.nextData();
				log.info("loadDataTime = " + watch);
				watch.reset();
				watch.start();
				influxDBUtil.saveData(spoutADNBoltData);
				influxDBUtil.saveData(supervisorData);
				influxDBUtil.saveData(clusterData);
				influxDBUtil.saveData(topologyData);
				log.info("insertDataTime = " + watch);
				try {
					Thread.sleep(variables.DataProcessorSleepTime);
				} catch (InterruptedException e) {
					log.catching(e);
				}
			}
		}catch(Exception e){
			log.catching(e);
		}
	}


	public static void main(String[] args) throws IOException, InterruptedException {
		// 开启线程
		Runnable run = new DataProcessor();
		Thread thread = new Thread(run);
		thread.setName("meng");
		thread.start();
		
	}


}
