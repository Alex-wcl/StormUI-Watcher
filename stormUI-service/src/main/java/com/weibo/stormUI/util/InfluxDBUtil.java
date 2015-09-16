package com.weibo.stormUI.util;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDB.LogLevel;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;


import com.weibo.stormUI.model.BlotData;
import com.weibo.stormUI.model.ClusterData;
import com.weibo.stormUI.model.SpoutData;
import com.weibo.stormUI.model.SupervisorData;
import com.weibo.stormUI.model.TopologyData;


public class InfluxDBUtil<T> {
	
	private static InfluxDB INFLUXDB;
	private static String DBNAME;
	private static String IP;
	private static int PORT;
	
	
	public InfluxDB setUp(String ip, int port, String userNameOFInfluxDB, String passwd) {
		if(INFLUXDB == null){
			synchronized (this) {
				if(INFLUXDB == null){
					IP = ip;
					PORT = port;
					INFLUXDB = InfluxDBFactory.connect("http://" + IP + ":" + PORT, userNameOFInfluxDB, passwd);
					INFLUXDB.setLogLevel(LogLevel.FULL);
					
				}
			}
		}
		return INFLUXDB;
	}

	public boolean createDB(String dbName) {

		if(DBNAME == null && dbName != null){
			synchronized(this){
				if(DBNAME == null){
					List<String> dbs = INFLUXDB.describeDatabases();
					for(String tmp : dbs){
						if(tmp != null && tmp.equals(dbName)){
							DBNAME = dbName;
							INFLUXDB.enableBatch(20000000, 600, TimeUnit.MILLISECONDS);
							System.out.println("数据库已经存在！");
							return true;
						}
					}
					INFLUXDB.createDatabase(dbName);
					DBNAME = dbName;
					INFLUXDB.enableBatch(20000000, 600, TimeUnit.MILLISECONDS);
				}
			}
		}

		return true;
	}

	//插入一条数据
	public boolean insertData(T object) {
		Point point = null;
		if(object != null){
			//如果保存的是ClusterData数据
			if(object.getClass().equals(ClusterData.class)){
				ClusterData tmp = (ClusterData)object;
				point = Point.measurement("cluster")
						.field("nimbusUptime", tmp.getNimbusUptime()).field("supervisors", tmp.getSupervisors())
						.field("slotsTotal", tmp.getSlotsTotal()).field("slotsUsed", tmp.getSlotsUsed())
						.field("slotsFree", tmp.getSlotsFree()).field("executorsTotal", tmp.getExecutorsTotal())
						.field("tasksTotal", tmp.getTasksTotal())
						.tag("stormVersion",tmp.getStormVersion())
						.build();
			}
			//如果保存的是BlotData数据
			if(object.getClass().equals(BlotData.class)){
				BlotData tmp = (BlotData)object;
				point = Point.measurement("blot")
						.field("executors", tmp.getExecutors()).field("tasks", tmp.getTasks())
						.field("emitted", tmp.getEmitted()).field("transferred", tmp.getTransferred())
						.field("capacity", tmp.getCapacity()).field("executeLatency", tmp.getExecuteLatency())
						.field("executed", tmp.getExecuted()).field("processLatency", tmp.getProcessLatency())
						.field("acked", tmp.getAcked()).field("failed", tmp.getFailed())
//						.tag("errorHost", tmp.getErrorHost())
//						.tag("errorPort", tmp.getErrorPort())
						.tag("boltId", tmp.getBoltId())
						.build();
			}
			
			//如果保存的是SpoutData数据
			if(object.getClass().equals(SpoutData.class)){
				SpoutData tmp = (SpoutData)object;
				point = Point.measurement("spout").field("executors", tmp.getExecutors())
						.field("emitted", tmp.getEmitted()).field("errorLapsedSecs", tmp.getErrorLapsedSecs())
						.field("completeLatency", tmp.getCompleteLatency()).field("transferred", tmp.getTransferred())
						.field("acked", tmp.getAcked())	
						.field("tasks", tmp.getTasks())
//						.tag("errorHost", tmp.getErrorHost())
//						.tag("lastError", tmp.getLastError())
//						.tag("errorWorkerLogLink", tmp.getErrorWorkerLogLink())
//						.tag("failed", tmp.getFailed())
						.tag("spoutId", tmp.getSpoutId())
						.build();
				System.out.println("spoutId = " + tmp.getSpoutId());
			}
			
			//如果保存的是SupervisorData数据
			if(object.getClass().equals(SupervisorData.class)){
				SupervisorData tmp = (SupervisorData)object;
				point = Point.measurement("supervisor").field("uptime", tmp.getUptime())
						.field("slotsTotal", tmp.getSlotsTotal()).field("slotsUsed", tmp.getSlotsUsed())
						.tag("supervisorId", tmp.getSupervisorId()).tag("host", tmp.getHost())
						.build();
			}
			//如果保存的是TopologyData
			if(object.getClass().equals(TopologyData.class)){
				TopologyData tmp = (TopologyData)object;
				point = Point.measurement("topology").field("uptime", tmp.getUptime())
						.field("tasksTotal", tmp.getTasksTotal()).field("workersTotal", tmp.getWorkersTotal())
						.field("executorsTotal", tmp.getExecutorsTotal())
						.tag("topologyId", tmp.getTopologyId()).tag("topolotyName",tmp.getTopolotyName())
						.tag("status", tmp.getStatus()).tag("encodedId", tmp.getEncodedId())
						.build();
			}
			INFLUXDB.write(DBNAME, "default", point);
			return true;
		}
		return false;
	}
	
	//插入多条记录
	public boolean insertDatas(List<T> objects) {
		if(objects != null){
			int size = objects.size();
			for(int i = 0;i < size;i++ ){
				insertData(objects.get(i));
			}
		}
		return true;
	}
	
	public void getData(){
		String queryCommand = "select stormVersion from cluster";
		Query query = new Query(queryCommand, DBNAME);
		QueryResult result = INFLUXDB.query(query);
		List lists = result.getResults();
		System.out.println("开始打印数据！");
		for(int i = 0;i < lists.size();i++){
			System.out.println(lists.get(i));
		}
		System.out.println("打印结束！");
	}

}
