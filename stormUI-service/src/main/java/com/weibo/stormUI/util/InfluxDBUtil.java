package com.weibo.stormUI.util;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDB.LogLevel;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

import com.weibo.stormUI.model.ClusterData;

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
		System.out.println("IP : " + IP);
		System.out.println("PORT : " + PORT);
		System.out.println("url : " + "http://" + IP + ":" + PORT);
		return INFLUXDB;
	}

	public boolean createDB(String dbName) {
		if(DBNAME == null){
			synchronized(this){
				if(DBNAME == null){
					INFLUXDB.createDatabase(dbName);
					DBNAME = dbName;
					INFLUXDB.enableBatch(20000000, 600, TimeUnit.MILLISECONDS);
				}
			}
		}
		System.out.println("DBNAME : " + DBNAME);
		return true;
	}

	//插入一条数据
	public boolean insertData(T object) {
		Point point = null;
		if(object.getClass().equals(ClusterData.class)){
			ClusterData tmp = (ClusterData)object;
			point = Point.measurement("cluster").field("stormVersion", tmp.getStormVersion())
					.field("nimbusUptime", tmp.getNimbusUptime()).field("supervisors", tmp.getSupervisors())
					.field("slotsTotal", tmp.getSlotsTotal()).field("slotsUsed", tmp.getSlotsUsed())
					.field("slotsFree", tmp.getSlotsFree()).field("executorsTotal", tmp.getExecutorsTotal())
					.field("tasksTotal", tmp.getTasksTotal()).tag("module", "cluster").build();
		}
		INFLUXDB.write(DBNAME, "default", point);
		return true;
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
