package com.weibo.stormUI.dataLoader.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.weibo.stormUI.dataLoader.DataLoader;
import com.weibo.stormUI.model.BoltData;
import com.weibo.stormUI.model.ClusterData;
import com.weibo.stormUI.model.SpoutData;
import com.weibo.stormUI.model.SupervisorData;
import com.weibo.stormUI.model.TopologyData;

import net.sf.json.JSONObject;

@Component
public class DataLoaderStorm extends DataLoader {

	private ClusterData clusterData;
	private List<TopologyData> topologyDatas;
	private List<SupervisorData> supervisorDatas;
	private Map<String, Object> map;
	private List<BoltData> blotDatas;
	private List<SpoutData> spoutDatas;
	
	public DataLoaderStorm(){
		super("10.77.128.101", "8080");
		map = new HashMap<String, Object>();
	}
	public DataLoaderStorm(String serverIP, String serverPORT) {
		super(serverIP, serverPORT);
		map = new HashMap<String, Object>();
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
	public Map<String, Object> nextData() {
		// 从storm中load数据
		try {
			// 获取cluster数据
			clusterData = loadClusterSummary(SERVER_IP, SERVER_PORT);
			map.put("clusterData", clusterData);
			// 获取topology summary数据
			topologyDatas = loadTopologySummary(SERVER_IP, SERVER_PORT);
			map.put("topologyDatas", topologyDatas);
			// 获取Supervisor summary数据
			supervisorDatas = loadSupervisorSummary(SERVER_IP, SERVER_PORT);
			map.put("supervisorDatas", supervisorDatas);
			// 获取bolts和spout数据
			// 因为可能会有很多topology，所以需要查询多次
			int topologySize = topologyDatas.size();
			for (int i = 0; i < topologySize; i++) {
				Map<String, Object> tmp = loadTopologyInfo(SERVER_IP, SERVER_PORT, topologyDatas.get(i).getTopologyId());
				map.put(topologyDatas.get(i).getTopologyId(), tmp);
			}
			
			return map;
		} catch (IOException e) {
			//加入日志系统
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	// 获取cluster信息，只有一条数据
		public ClusterData loadClusterSummary(String clusterIP, String port) throws IOException {
			String urlString = "http://" + clusterIP + ":" + port + "/api/v1/cluster/summary";
			String data = URLConnectionHelper(urlString);
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
			String data = URLConnectionHelper(urlString);
			String topologies = data.substring(data.indexOf("[") + 1, data.lastIndexOf("]"));
			int count = getSubtringCount(topologies, "{");
			List<TopologyData> topologyDatas = new ArrayList<TopologyData>();
			for (int i = 0; i < count; i++) {
				TopologyData topologyData = new TopologyData();
				String t = topologies.substring(getSubtringIndex(topologies, "{", i + 1),
						getSubtringIndex(topologies, "}", i + 1) + 1);
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
			String data = URLConnectionHelper(urlString);
			String supervisors = data.substring(data.indexOf("[") + 1, data.lastIndexOf("]"));
			int count = getSubtringCount(supervisors, "{");
			List<SupervisorData> supervisorDatas = new ArrayList<SupervisorData>();
			for (int i = 0; i < count; i++) {
				String t = supervisors.substring(getSubtringIndex(supervisors, "{", i + 1),
						getSubtringIndex(supervisors, "}", i + 1) + 1);
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


		// 获取spout和bolts信息，map中有两条记录，key为spoutDatas和blotDatas，value都是list
		public Map<String,Object> loadTopologyInfo(String clusterIP, String port, String topologyId) throws IOException {
			String urlString = "http://" + clusterIP + ":" + port + "/api/v1/topology/" + topologyId;
			String result = URLConnectionHelper(urlString);
			// 解析spout数据
			// substringOfSpouts:Spout部分的数据。查找[]中间的部分
			String substringOfSpouts = result.substring(result.indexOf("\"spouts\"") + 9,
					result.indexOf("]", result.indexOf("spouts")));
			// 查询有多少条数据
			int countOFLeftBrace = getSubtringCount(substringOfSpouts, "{");
			List<SpoutData> spoutDatas = new ArrayList<SpoutData>();
			// 返回的数据中有很多条spout，逐个解析。
			for (int i = 0; i < countOFLeftBrace; i++) {
				String tmp = substringOfSpouts.substring(getSubtringIndex(substringOfSpouts, "{", i + 1),
						getSubtringIndex(substringOfSpouts, "}", i + 1) + 1);
				JSONObject jsonObject = JSONObject.fromObject(tmp);
				SpoutData spoutData = new SpoutData();
				spoutData.setExecutors(jsonObject.getString("executors"));
				spoutData.setEmitted(jsonObject.getString("emitted"));
				spoutData.setCompleteLatency(jsonObject.getString("completeLatency"));
				spoutData.setTransferred(jsonObject.getString("transferred"));
				spoutData.setAcked(jsonObject.getString("acked"));
				spoutData.setErrorPort(jsonObject.getString("errorPort"));
				spoutData.setSpoutId(jsonObject.getString("spoutId"));
				spoutData.setTasks(jsonObject.getString("tasks"));
				spoutData.setTopologyId(topologyId);
				spoutDatas.add(spoutData);
			}
			// 解析bolts数据
			// substringOfBlots部分的数据。查找[]中间的部分
			String substringOfBlots = result.substring(result.indexOf("\"bolts\"") + 9,
					result.indexOf("}]", result.indexOf("bolts")) + 2);
			// 查询数据条数
			countOFLeftBrace = getSubtringCount(substringOfBlots, "{");
			List<BoltData> blotDatas = new ArrayList<BoltData>();
			// 逐条解析
			for (int i = 0; i < countOFLeftBrace; i++) {
				String tmp = substringOfBlots.substring(getSubtringIndex(substringOfBlots, "{", i + 1),
						getSubtringIndex(substringOfBlots, "}", i + 1) + 1);
				JSONObject jsonObject = JSONObject.fromObject(tmp);
				BoltData blotData = new BoltData();
				blotData.setExecutors(jsonObject.getString("executors"));
				blotData.setEmitted(jsonObject.getString("emitted"));
				blotData.setBoltId(jsonObject.getString("encodedBoltId"));
				blotData.setTasks(jsonObject.getString("tasks"));
				blotData.setTransferred(jsonObject.getString("transferred"));
				blotData.setCapacity(jsonObject.getString("capacity"));
				blotData.setExecuteLatency(jsonObject.getString("executeLatency"));
				blotData.setExecuted(jsonObject.getString("executed"));
				blotData.setProcessLatency(jsonObject.getString("processLatency"));
				blotData.setAcked(jsonObject.getString("acked"));
				blotData.setFailed(jsonObject.getString("failed"));
				blotData.setTopologyId(topologyId);
				blotDatas.add(blotData);
			}
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("spoutDatas", spoutDatas);
			map.put("blotDatas", blotDatas);
			return map;
		}

		// 根据URL请求数据， 并返回result
		public String URLConnectionHelper(String urlString) throws IOException {
			URL url = new URL(urlString);
			// 打开到此 URL 的连接并返回一个用于从该连接读入的 InputStream。
			URLConnection uc = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			StringBuffer json = new StringBuffer();
			String data = null;
			while ((data = in.readLine()) != null) {
				json.append(data);
			}
			in.close();
			data = json.toString();
			return data;
		}

		// 查询一个字符串中特定字符串的个数
		private int getSubtringCount(String string, String subString) {
			String s = String.copyValueOf(string.toCharArray());
			String ToFind = subString;
			int index = 0;
			int count = 0;
			while (index != -1) {
				index = s.indexOf(ToFind);
				if (index == -1) {
					break;
				}
				s = s.substring(index + ToFind.length());
				count++;

			}
			return count;

		}

		// 查询一个字符串中特定字符串第n次出现的索引
		private int getSubtringIndex(String _string, String subString, int n) {
			String string = String.copyValueOf(_string.toCharArray());
			Matcher slashMatcher = Pattern.compile("\\" + subString).matcher(string);
			int mIdx = 0;
			while (slashMatcher.find()) {
				mIdx++;
				// 当"/"符号第n次出现的位置
				if (mIdx == n) {
					break;
				}
			}
			return slashMatcher.start();
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

		public Map<String, Object> getMap() {
			return map;
		}

		public void setMap(Map<String, Object> map) {
			this.map = map;
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
