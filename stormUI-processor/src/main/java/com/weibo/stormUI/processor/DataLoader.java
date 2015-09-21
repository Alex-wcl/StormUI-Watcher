package com.weibo.stormUI.processor;

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

import com.weibo.stormUI.model.BoltData;
import com.weibo.stormUI.model.ClusterData;
import com.weibo.stormUI.model.SpoutData;
import com.weibo.stormUI.model.SupervisorData;
import com.weibo.stormUI.model.TopologyData;
import com.weibo.stormUI.util.InfluxDBUtil;

import net.sf.json.JSONObject;

public class DataLoader implements Runnable {

	// influxdb服务器的ip地址
	private static String INFLUXDB_SERVER_IP = "10.77.108.126";
	// influxdb提供的端口号，默认为8086
	private static int INFLUXDB_SERVER_PORT = 8086;
	// cluster集群的ip地址
	// 一开始监控该IP：10.77.108.127，port：8090
	private static String CLUSTER_SERVER_IP = "10.77.128.101";
	// cluster集群端口号
	private static int CLUSTER_SERVER_PORT = 8080;
	// 线程休眠时间（毫秒）
	private static long SLEEPTIME = 1000 * 5;
	// influxdb的用户名和密码，默认为root，root
	private static String USERNAME_INFLUXDB = "root";
	private static String PASSWD_INFLUXDB = "root";
	// 要创建的influxdb数据库的名称
	private static String INFLUXDBNAME = "storm";

	public void run() {

		// 初始化五个数据操作类
		InfluxDBUtil<ClusterData> clusterInfluxDB = new InfluxDBUtil<ClusterData>();
		InfluxDBUtil<TopologyData> topologyInfluxDB = new InfluxDBUtil<TopologyData>();
		InfluxDBUtil<BoltData> blotInfluxDB = new InfluxDBUtil<BoltData>();
		InfluxDBUtil<SpoutData> spoutInfluxDB = new InfluxDBUtil<SpoutData>();
		InfluxDBUtil<SupervisorData> supervisorInfluxDB = new InfluxDBUtil<SupervisorData>();
		// 初始化数据连接。只要一次就行了，因为成员变量公用。
		clusterInfluxDB.setUp(INFLUXDB_SERVER_IP, INFLUXDB_SERVER_PORT, USERNAME_INFLUXDB, PASSWD_INFLUXDB);
		clusterInfluxDB.createDB(INFLUXDBNAME);
		// 定义局部引用
		ClusterData clusterData = null;
		List<TopologyData> topologyDatas = null;
		List<SupervisorData> supervisorDatas = null;
		Map map = null;
		List<BoltData> blotDatas = null;
		List<SpoutData> spoutDatas = null;
		// 从storm中load数据
		while (true) {
			try {

				// 获取cluster数据，并保存
				clusterData = loadClusterSummary(CLUSTER_SERVER_IP, CLUSTER_SERVER_PORT);
				clusterInfluxDB.insertData(clusterData);

				// 获取topology summary数据并保存
				topologyDatas = loadTopologySummary(CLUSTER_SERVER_IP, CLUSTER_SERVER_PORT);
				topologyInfluxDB.insertDatas(topologyDatas);

				// 获取Supervisor summary数据并保存
				supervisorDatas = loadSupervisorSummary(CLUSTER_SERVER_IP, CLUSTER_SERVER_PORT);
				supervisorInfluxDB.insertDatas(supervisorDatas);

				// 获取bolts和spout数据并保存
				// 因为可能会有很多topology，所以需要查询多次
				int topologySize = topologyDatas.size();
				for (int i = 0; i < topologySize; i++) {
					map = loadTopologyInfo(CLUSTER_SERVER_IP, CLUSTER_SERVER_PORT,
							topologyDatas.get(i).getTopologyId());
					blotDatas = (List<BoltData>) map.get("blotDatas");
					spoutDatas = (List<SpoutData>) map.get("spoutDatas");
					blotInfluxDB.insertDatas(blotDatas);
					spoutInfluxDB.insertDatas(spoutDatas);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				System.out.println("线程休眠中....");
				Thread.currentThread().sleep(SLEEPTIME);
				System.out.println("结束休眠！");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	// 获取cluster信息，只有一条数据
	public ClusterData loadClusterSummary(String clusterIP, int port) throws IOException {
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
	public List<TopologyData> loadTopologySummary(String clusterIP, int port) throws IOException {
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
	public List<SupervisorData> loadSupervisorSummary(String clusterIP, int port) throws IOException {
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

	public static void main(String[] args) throws IOException {
		// 测试
		// DataLoader dataLoader = new DataLoader();
		// dataLoader.loadTopologySummary(CLUSTER_SERVER_IP,CLUSTER_SERVER_PORT);

		// 开启线程
		Runnable run = new DataLoader();
		Thread thread = new Thread(run);
		thread.start();
	}

	// 获取spout和bolts信息，map中有两条记录，key为spoutDatas和blotDatas，value都是list
	public Map loadTopologyInfo(String clusterIP, int port, String topologyId) throws IOException {
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
		Map map = new HashMap();
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

}
