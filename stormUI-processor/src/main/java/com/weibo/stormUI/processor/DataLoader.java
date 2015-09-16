package com.weibo.stormUI.processor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.weibo.stormUI.model.ClusterData;
import com.weibo.stormUI.model.SupervisorData;
import com.weibo.stormUI.model.TopologyData;
import com.weibo.stormUI.util.InfluxDBUtil;

import net.sf.json.JSONObject;

public class DataLoader implements Runnable{

	//influxdb服务器的ip地址
	private static String INFLUXDB_SERVER_IP = "10.77.108.126";
	//influxdb提供的端口号，默认为8086
	private static int INFLUXDB_SERVER_PORT = 8086;
	//cluster集群的ip地址
	private static String CLUSTER_SERVER_IP = "10.77.108.127";
	//cluster集群端口号
	private static int CLUSTER_SERVER_PORT = 8090;
	//线程休眠时间（毫秒）
	private static long SLEEPTIME = 10000;
	//influxdb的用户名和密码，默认为root，root
	private static String USERNAME_INFLUXDB = "meng";
	private static String PASSWD_INFLUXDB = "meng";
	//要创建的influxdb数据库的名称
	private static String INFLUXDBNAME = "storm";
	
	public void run() {
		InfluxDBUtil<ClusterData> clusterInfluxDB = new InfluxDBUtil<ClusterData>();
		clusterInfluxDB.setUp(INFLUXDB_SERVER_IP, INFLUXDB_SERVER_PORT,USERNAME_INFLUXDB, PASSWD_INFLUXDB);
		clusterInfluxDB.createDB(INFLUXDBNAME);
		ClusterData clusterData = null;
		//从storm中load数据
		while(true){
			try {
				clusterData = loadClusterSummary(CLUSTER_SERVER_IP,CLUSTER_SERVER_PORT);
				clusterInfluxDB.insertData(clusterData);
				//clusterInfluxDB.getData();
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
	
	
	public static void main(String[] args) throws IOException {
//		DataLoader dataLoader = new DataLoader();
//		ClusterData clusterData = dataLoader.loadClusterSummary(CLUSTER_IP,PORT);
		Runnable run = new DataLoader();
		Thread thread = new Thread(run);
		thread.start();
	}
	

	
	//获取cluster信息，只有一条数据
	 public ClusterData loadClusterSummary(String clusterIP,int port) throws IOException { 
		 String urlString = "http://" + clusterIP + ":" + port + "/api/v1/cluster/summary";
		 System.out.println("请求的url是：" + urlString);
		 URL url = new URL(urlString); 
         //打开到此 URL 的连接并返回一个用于从该连接读入的 InputStream。 
		 URLConnection uc = url.openConnection();  
         BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream())); 
         StringBuffer json = new StringBuffer();
         String data = null;  
         while ((data = in.readLine()) != null) { 
        	 json.append(data);  
         } 
         in.close(); 
         System.out.println("返回的数据是： " + json);
         data = json.toString();
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
	     System.out.println("转换之后的数据：" + clusterData.toString());
         return clusterData;
	 } 
	 
	 //获取Topology的数据，是一个list，表示多个TopologyData。
	 public List<TopologyData> loadTopologySummary(String clusterIP,int port) throws IOException {
		 String urlString = "http://" + clusterIP + ":" + port + "/api/v1/topology/summary";
		 System.out.println("请求的url是：" + urlString);
		 URL url = new URL(urlString); 
         //打开到此 URL 的连接并返回一个用于从该连接读入的 InputStream。 
		 URLConnection uc = url.openConnection();  
         BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream())); 
         StringBuffer json = new StringBuffer();
         String data = null;  
         while ((data = in.readLine()) != null) { 
        	 json.append(data);  
         } 
         in.close(); 
         data = json.toString();
         System.out.println("返回的数据是： " + json);
         String topologies = data.substring(data.indexOf("[")+1, data.lastIndexOf("]"));
         int count = getSubtringCount(topologies,"{");
         System.out.println(count);
         JSONObject jsonObject = null;
         TopologyData topologyData = new TopologyData();
         List<TopologyData> topologyDatas = new ArrayList<TopologyData>();
         for(int i = 0;i < count;i++){
        	 String t = topologies.substring(getSubtringIndex(topologies,"{",i+1),getSubtringIndex(topologies,"}",i+1)+1);
        	 jsonObject = JSONObject.fromObject(t);
        	 topologyData.setTopologyId(jsonObject.getString("id"));
        	 topologyData.setEncodedId(jsonObject.getString("encodedId"));
        	 topologyData.setTopolotyName(jsonObject.getString("name"));
        	 topologyData.setStatus(jsonObject.getString("status"));
        	 topologyData.setUptime(jsonObject.getString("uptime"));
        	 topologyData.setTasksTotal(jsonObject.getInt("tasksTotal"));
        	 topologyData.setWorkersTotal(jsonObject.getInt("workersTotal"));
        	 topologyData.setExecutorsTotal(jsonObject.getInt("executorsTotal"));
        	 System.out.println(topologyData.toString());
        	 topologyDatas.add(topologyData);
         }
         return topologyDatas;
	 }
	 
	 //返回所有的Supervisors。
	 public List<SupervisorData> loadSupervisorSummary(String clusterIP,int port) throws IOException {
		 String urlString = "http://" + clusterIP + ":" + port + "/api/v1/supervisor/summary";
		 System.out.println("请求的url是：" + urlString);
		 URL url = new URL(urlString); 
         //打开到此 URL 的连接并返回一个用于从该连接读入的 InputStream。 
		 URLConnection uc = url.openConnection();  
         BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream())); 
         StringBuffer json = new StringBuffer();
         String data = null;  
         while ((data = in.readLine()) != null) { 
        	 json.append(data);  
         } 
         in.close(); 
         data = json.toString();
         System.out.println("返回的数据是： " + json);
         
         
         String supervisors = data.substring(data.indexOf("[")+1, data.lastIndexOf("]"));
         int count = getSubtringCount(supervisors,"{");
         System.out.println(count);
         JSONObject jsonObject = null;
         SupervisorData supervisorData = new SupervisorData();
         List<SupervisorData> supervisorDatas = new ArrayList<SupervisorData>();
         for(int i = 0;i < count;i++){
        	 String t = supervisors.substring(getSubtringIndex(supervisors,"{",i+1),getSubtringIndex(supervisors,"}",i+1)+1);
        	 jsonObject = JSONObject.fromObject(t);
        	 supervisorData.setSupervisorId(jsonObject.getString("id"));
        	 supervisorData.setHost(jsonObject.getString("host"));
        	 supervisorData.setUptime(jsonObject.getString("uptime"));
        	 supervisorData.setSlotsTotal(jsonObject.getInt("slotsTotal"));
        	 supervisorData.setSlotsUsed(jsonObject.getInt("slotsUsed"));
        	 System.out.println(supervisorData.toString());
        	 supervisorDatas.add(supervisorData);
         }
         return supervisorDatas;
	 }
	 
	 
	 
		
		//获取spouts信息
		
		
		//获取slots信息
	 
	 
	 //查询一个字符串中特定字符串的个数
	 private int getSubtringCount(String string ,String subString){
		 String s = String.copyValueOf(string.toCharArray());
		 String ToFind = subString;
		 int index = 0;
		 int count = 0;
		  while (index != -1) {
		   index = s.indexOf(ToFind);
		   if(index == -1){
			   break;
		   }
		   s = s.substring(index + ToFind.length());
		   count++;

		  }
		  return count;
		 
	 }
	 
	 //查询一个字符串中特定字符串第n次出现的索引
	 private  int getSubtringIndex(String _string ,String subString,int n){
		String string = String.copyValueOf(_string.toCharArray());
	    Matcher slashMatcher = Pattern.compile("\\" + subString).matcher(string);
	    int mIdx = 0;
	    while(slashMatcher.find()) {
	       mIdx++;
	       //当"/"符号第三次出现的位置
	       if(mIdx == n){
	          break;
	       }
	    }
	    return slashMatcher.start();
	 }
	 
	//从storm中load数据
	
	//解析数据
	
	//向influxDB中插入数据
	
}
