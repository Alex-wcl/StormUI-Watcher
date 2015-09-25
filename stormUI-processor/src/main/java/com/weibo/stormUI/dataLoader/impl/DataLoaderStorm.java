package com.weibo.stormUI.dataLoader.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.common.base.Stopwatch;
import com.weibo.stormUI.dataLoader.DataLoader;
import com.weibo.stormUI.dataLoader.module.ModuleDataLoader;
import com.weibo.stormUI.model.BoltData;
import com.weibo.stormUI.model.ClusterData;
import com.weibo.stormUI.model.SpoutData;
import com.weibo.stormUI.model.SupervisorData;
import com.weibo.stormUI.model.TopologyData;
import com.weibo.stormUI.util.GlobalVariable;
import com.weibo.stormUI.util.StringHelper;
import com.weibo.stormUI.util.URLConnectionHelper;

import net.sf.json.JSONObject;

@Component
public class DataLoaderStorm<T> implements DataLoader<T>{
	private static final Logger log = LogManager.getLogger(DataLoaderStorm.class);
	private ClusterData clusterData;
	private List<TopologyData> topologyDatas;
	private List<SupervisorData> supervisorDatas;
	private T datas;
	private List<BoltData> blotDatas;
	private List<SpoutData> spoutDatas;
	private String SERVER_IP;
	private String SERVER_PORT;
	private ModuleDataLoader moduleDataLoader;
	
	public DataLoaderStorm(String serverIP, String serverPORT) {
		this.SERVER_IP = serverIP;
		this.SERVER_PORT = serverPORT;
	}
	
	/**
	 * map中的结构：
	 * 		key   				value				value type
	 * 	clusterData			clusterData			ClusterData
	 * 	topologyDatas  		topologyDatas		List<TopologyData>
	 * 	supervisorDatas		supervisorDatas		List<SupervisorData>
	 * 	'topologyID01'		mapSpoutAndBolt		Map<String, Object>
	 * 	'topologyID02'		mapSpoutAndBolt		Map<String, Object>
	 * 	.....
	 * 
	 * 	其中：mapSpoutAndBolt的结构为
	 * 	spoutDatas 			spoutDatas			List<SpoutData>
	 * 	blotDatas			blotDatas			List<BoltData>
	 */
	public T nextData() {
		// 从storm中load数据
		try {
			// 获取cluster数据
			clusterData = loadClusterSummary(SERVER_IP, SERVER_PORT);
			synchronized (datas) {
				datas.put("clusterData", clusterData);
			}
			// 获取topology summary数据
			List<TopologyData> topologyTmp;
			topologyTmp = loadTopologySummary(SERVER_IP, SERVER_PORT);
			if(topologyDatas == null && topologyTmp != null){
				 topologyDatas = topologyTmp;
			}
			synchronized (datas) {
				datas.put("topologyDatas", topologyTmp);
			}
			// 获取Supervisor summary数据
			supervisorDatas = loadSupervisorSummary(SERVER_IP, SERVER_PORT);
			synchronized (datas) {
				datas.put("supervisorDatas", supervisorDatas);
			}
			// 获取bolts和spout数据
			// 因为可能会有很多topology，所以需要查询多次
			int topologySize = topologyDatas.size();
			for (int i = 0; i < topologySize; i++) {
				String topologyId = topologyDatas.get(i).getTopologyId(); 
				moduleDataLoader = new ModuleDataLoader(this,topologyId);
				moduleDataLoader.start();
			}
			return datas;
		} catch (IOException e) {
			log.catching(e);
			return null;
		}
	}
	
	
	
	/**
	 * public T nextData() {
		// 从storm中load数据
		try {
			// 获取cluster数据
			clusterData = loadClusterSummary(SERVER_IP, SERVER_PORT);
			synchronized (datas) {
				datas.put("clusterData", clusterData);
			}
			// 获取topology summary数据
			List<TopologyData> topologyTmp;
			topologyTmp = loadTopologySummary(SERVER_IP, SERVER_PORT);
			if(topologyDatas == null && topologyTmp != null){
				 topologyDatas = topologyTmp;
			}
			synchronized (datas) {
				datas.put("topologyDatas", topologyTmp);
			}
			// 获取Supervisor summary数据
			supervisorDatas = loadSupervisorSummary(SERVER_IP, SERVER_PORT);
			synchronized (datas) {
				datas.put("supervisorDatas", supervisorDatas);
			}
			// 获取bolts和spout数据
			// 因为可能会有很多topology，所以需要查询多次
			int topologySize = topologyDatas.size();
			for (int i = 0; i < topologySize; i++) {
				String topologyId = topologyDatas.get(i).getTopologyId(); 
				moduleDataLoader = new ModuleDataLoader(this,topologyId);
				moduleDataLoader.start();
			}
			return datas;
		} catch (IOException e) {
			log.catching(e);
			return null;
		}
	}
	 * @param clusterIP
	 * @param port
	 * @return
	 * @throws IOException
	 */
	
	
	// 获取cluster信息，只有一条数据
		public ClusterData loadClusterSummary(String clusterIP, String port) throws IOException {
			String urlString = "http://" + clusterIP + ":" + port + "/api/v1/cluster/summary";
			String data = URLConnectionHelper.URLConnection(urlString);
			JSONObject jsonObject = JSONObject.fromObject(data);
			ClusterData clusterData = new ClusterData();
			clusterData.setStormVersion(jsonObject.getString("stormVersion"));
			clusterData.setNimbusUptime(jsonObject.getString("nimbusUptime"));
			clusterData.setSupervisors(jsonObject.getInt("supervisors"));
			clusterData.setSlotsTotal(jsonObject.getInt("slotsTotal"));
			clusterData.setSlotsUsed(jsonObject.getInt("slotsUsed"));
			clusterData.setSlotsFree(jsonObject.getInt("slotsFree"));
			clusterData.setExecutorsTotal(jsonObject.getInt("executorsTotal"));
			clusterData.setTasksTotal(jsonObject.getInt("tasksTotal"));
			return clusterData;
		}

		// 获取Topology的数据，是一个list，表示多个TopologyData。
		public List<TopologyData> loadTopologySummary(String clusterIP, String port) throws IOException {
			String urlString = "http://" + clusterIP + ":" + port + "/api/v1/topology/summary";
			String data = URLConnectionHelper.URLConnection(urlString);
			String topologies = data.substring(data.indexOf("[") + 1, data.lastIndexOf("]"));
			int count = StringHelper.getSubtringCount(topologies, "{");
			List<TopologyData> topologyDatas = new ArrayList<TopologyData>();
			for (int i = 0; i < count; i++) {
				TopologyData topologyData = new TopologyData();
				String t = topologies.substring(StringHelper.getSubtringIndex(topologies, "{", i + 1),
						StringHelper.getSubtringIndex(topologies, "}", i + 1) + 1);
				JSONObject jsonObject = JSONObject.fromObject(t);
				topologyData.setTopologyId(jsonObject.getString("id"));
				topologyData.setEncodedId(jsonObject.getString("encodedId"));
				topologyData.setTopolotyName(jsonObject.getString("name"));
				topologyData.setStatus(jsonObject.getString("status"));
				topologyData.setUptime(jsonObject.getString("uptime"));
				topologyData.setTasksTotal(jsonObject.getInt("tasksTotal"));
				topologyData.setWorkersTotal(jsonObject.getInt("workersTotal"));
				topologyData.setExecutorsTotal(jsonObject.getInt("executorsTotal"));
				topologyDatas.add(topologyData);
			}
			return topologyDatas;
		}

		// 返回所有的Supervisors。
		public List<SupervisorData> loadSupervisorSummary(String clusterIP, String port) throws IOException {
			String urlString = "http://" + clusterIP + ":" + port + "/api/v1/supervisor/summary";
			String data = URLConnectionHelper.URLConnection(urlString);
			String supervisors = data.substring(data.indexOf("[") + 1, data.lastIndexOf("]"));
			int count = StringHelper.getSubtringCount(supervisors, "{");
			List<SupervisorData> supervisorDatas = new ArrayList<SupervisorData>();
			for (int i = 0; i < count; i++) {
				String t = supervisors.substring(StringHelper.getSubtringIndex(supervisors, "{", i + 1),
						StringHelper.getSubtringIndex(supervisors, "}", i + 1) + 1);
				JSONObject jsonObject = JSONObject.fromObject(t);
				SupervisorData supervisorData = new SupervisorData();
				supervisorData.setSupervisorId(jsonObject.getString("id"));
				supervisorData.setHost(jsonObject.getString("host"));
				supervisorData.setUptime(jsonObject.getString("uptime"));
				supervisorData.setSlotsTotal(jsonObject.getInt("slotsTotal"));
				supervisorData.setSlotsUsed(jsonObject.getInt("slotsUsed"));
				supervisorDatas.add(supervisorData);
			}
			return supervisorDatas;
		}


		public ClusterData getClusterData() {
			return clusterData;
		}

		public void setClusterData(ClusterData clusterData) {
			this.clusterData = clusterData;
		}

		public List<TopologyData> getTopologyDatas() {
			return topologyDatas;
		}

		public void setTopologyDatas(List<TopologyData> topologyDatas) {
			this.topologyDatas = topologyDatas;
		}

		public List<SupervisorData> getSupervisorDatas() {
			return supervisorDatas;
		}

		public void setSupervisorDatas(List<SupervisorData> supervisorDatas) {
			this.supervisorDatas = supervisorDatas;
		}

		

		public Map<String, Object> getDatas() {
			return datas;
		}

		public synchronized void setDatas(Map<String, Object> datas) {
			this.datas = datas;
		}


		public String getSERVER_IP() {
			return SERVER_IP;
		}
		public void setSERVER_IP(String sERVER_IP) {
			SERVER_IP = sERVER_IP;
		}
		public String getSERVER_PORT() {
			return SERVER_PORT;
		}
		public void setSERVER_PORT(String sERVER_PORT) {
			SERVER_PORT = sERVER_PORT;
		}
		public List<BoltData> getBlotDatas() {
			return blotDatas;
		}

		public void setBlotDatas(List<BoltData> blotDatas) {
			this.blotDatas = blotDatas;
		}

		public List<SpoutData> getSpoutDatas() {
			return spoutDatas;
		}

		public void setSpoutDatas(List<SpoutData> spoutDatas) {
			this.spoutDatas = spoutDatas;
		}

		
		
}
