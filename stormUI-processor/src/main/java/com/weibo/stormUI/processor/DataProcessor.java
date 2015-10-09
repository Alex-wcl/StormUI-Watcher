package com.weibo.stormUI.processor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.HttpRequestHandlerServlet;
import org.springframework.web.context.support.WebApplicationContextUtils;

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
	private String server_IP;
	private String server_PORT;
	private List<TopologyData> datas;
	private Variables variables;
	public DataProcessor(){
		datas = new ArrayList<TopologyData>();
		variables = Variables.getInstance();
		server_IP = variables.DataSourceServerIP;
		server_PORT = variables.DataSourceServerPort;
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
		influxDBUtil.setUp(variables.DataBaseServerIP,variables.DataBaseServerPort,variables.DataBaseUserName,variables.DataBasePassword);
		influxDBUtil.createDB(variables.DataBaseName);
		while(true){
			Stopwatch watch = Stopwatch.createStarted();
			List<SpoutADNBolt> spoutADNBoltData = boltDataLoader.nextData();
			List<SupervisorData> supervisorData = supervisorDataLoader.nextData();
			List<ClusterData> clusterData = clusterDataLoader.nextData();
			List<TopologyData> topologyData = topologyDataLoader.nextData();
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
	}


	public static void main(String[] args) throws IOException, InterruptedException {
		// 开启线程
		Runnable run = new DataProcessor();
		Thread thread = new Thread(run);
		thread.setName("meng");
		thread.start();
		
	}


}
