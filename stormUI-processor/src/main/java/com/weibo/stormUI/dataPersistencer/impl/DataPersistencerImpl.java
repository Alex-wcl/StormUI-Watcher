package com.weibo.stormUI.dataPersistencer.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDB.LogLevel;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.stereotype.Component;

import com.weibo.stormUI.dataLoader.module.ModuleDataLoader;
import com.weibo.stormUI.dataPersistencer.DataPersistencer;
import com.weibo.stormUI.model.BoltData;
import com.weibo.stormUI.model.ClusterData;
import com.weibo.stormUI.model.SpoutADNBolt;
import com.weibo.stormUI.model.SpoutData;
import com.weibo.stormUI.model.SupervisorData;
import com.weibo.stormUI.model.TopologyData;

@Component
public class DataPersistencerImpl<T> implements DataPersistencer<T> {
	private Logger log = LogManager.getLogger(DataPersistencerImpl.class);
	private static InfluxDB INFLUXDB;
	private static String DBNAME;
	private static String IP;
	private static String PORT;
	
	
	public InfluxDB setUp(String ip, String port, String userNameOFInfluxDB, String passwd) {
		if(INFLUXDB == null){
			synchronized (this) {
				if(INFLUXDB == null){
					IP = ip;
					PORT = port;
					INFLUXDB = InfluxDBFactory.connect("http://" + IP + ":" + PORT, userNameOFInfluxDB, passwd);
					INFLUXDB.setLogLevel(LogLevel.NONE);
					
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
							log.info("数据库已经存在!" + "dbName : " + dbName);
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
	public <T> boolean insertData(T object) {
		Point point = null;
		if(object != null){
			//如果保存的是ClusterData数据
			if(object instanceof ClusterData){
				ClusterData tmp = (ClusterData)object;
				point = Point.measurement("cluster")
						.field("nimbusUptime", tmp.getNimbusUptime()).field("supervisors", tmp.getSupervisors())
						.field("slotsTotal", tmp.getSlotsTotal()).field("slotsUsed", tmp.getSlotsUsed())
						.field("slotsFree", tmp.getSlotsFree()).field("executorsTotal", tmp.getExecutorsTotal())
						.field("tasksTotal", tmp.getTasksTotal()).time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
						.tag("stormVersion",tmp.getStormVersion())
						.build();
			}
			//如果保存的是BlotData数据
			if(object instanceof SpoutADNBolt ){
				SpoutADNBolt spoutADNBolt = (SpoutADNBolt)object;
				List<BoltData> listBoltData = spoutADNBolt.getBoltDatas();
				List<SpoutData> listSpoutData = spoutADNBolt.getSpoutDatas();
				for(BoltData tmp : listBoltData){
					point = Point.measurement("bolt")
							.field("executors", tmp.getExecutors()).field("tasks", tmp.getTasks())
							.field("emitted", tmp.getEmitted()).field("transferred", tmp.getTransferred())
							.field("capacity", tmp.getCapacity()).field("executeLatency", tmp.getExecuteLatency())
							.field("executed", tmp.getExecuted()).field("processLatency", tmp.getProcessLatency())
							.field("acked", tmp.getAcked()).field("failed", tmp.getFailed())
							.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
							.tag("topologyId",tmp.getTopologyId())
							.tag("boltId", tmp.getBoltId())
							.build();
					INFLUXDB.write(DBNAME, "default", point);
				}
				for(SpoutData tmp : listSpoutData){
					point = Point.measurement("spout").field("executors", tmp.getExecutors())
							.field("emitted", tmp.getEmitted())
							.field("completeLatency", tmp.getCompleteLatency()).field("transferred", tmp.getTransferred())
							.field("acked", tmp.getAcked()).field("tasks", tmp.getTasks())
							.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
							.tag("topologyId", tmp.getTopologyId())
							.tag("spoutId", tmp.getSpoutId())
							.build();
					INFLUXDB.write(DBNAME, "default", point);
				}
				return true;
			}
			
			//如果保存的是SupervisorData数据
			if(object instanceof SupervisorData){
				SupervisorData tmp = (SupervisorData)object;
				point = Point.measurement("supervisor").field("uptime", tmp.getUptime())
						.field("slotsTotal", tmp.getSlotsTotal()).field("slotsUsed", tmp.getSlotsUsed())
						.tag("supervisorId", tmp.getId()).tag("host", tmp.getHost())
						.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
						.build();
			}
			//如果保存的是TopologyData
			if(object instanceof TopologyData){
				TopologyData tmp = (TopologyData)object;
				point = Point.measurement("topology").field("uptime", tmp.getUptime())
						.field("tasksTotal", tmp.getTasksTotal())
						.field("workersTotal", tmp.getWorkersTotal())
						.field("executorsTotal", tmp.getExecutorsTotal())
						.tag("topologyId", tmp.getId())
						.tag("topolotyName",tmp.getName())
						.tag("status", tmp.getStatus())
						.tag("encodedId", tmp.getEncodedId())
						.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
						.build();
			}
			INFLUXDB.write(DBNAME, "default", point);
			return true;
		}
		return false;
	}
	
	//插入多条记录
	public boolean saveData(List<T> objects) {
		if(objects != null){
			for(T tmp : objects){
				insertData(tmp);
			}
		}
		return true;
	}
	/**
	 * version 1 
	 * 每一次weibo-camera-message-processor上线id都会改变，而前端显示时会根据其id唯一判断并显示，
	 * 这样会导致出现很多weibo-camera-message-processor，因此将先后上线的weibo-camera-message-processor都设置为相同的id
	 * version 2
	 * 将所有的topology都显示出来，不要截取
	 * @param topologyId
	 * @return
	 */
	/*public String invertTopologyId(String topologyId){
		int size = topologyId.length();
		if(size >=30 && (topologyId.substring(0, 30)).equals("weibo-camera-message-processor")){
			return topologyId.substring(0, 30);
		}
		return topologyId;
	}*/


}
