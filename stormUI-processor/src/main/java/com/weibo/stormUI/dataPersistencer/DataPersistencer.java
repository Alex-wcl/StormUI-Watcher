package com.weibo.stormUI.dataPersistencer;

import java.util.Map;

import org.springframework.stereotype.Component;

public class DataPersistencer {
	// 持久化服务器的ip地址
	public  String SERVER_IP;
	// 服务器提供的端口号
	public String SERVER_PORT;
	// 数据库用户名和密码
	public String DB_USERNAME;
	public String DB_PASSWORD;
	// 要创建的数据库的名称
	public String INFLUXDBNAME;
	
	public DataPersistencer(){}
	public DataPersistencer(String serverIP,String serverPORT,String DBUserName,String DBPassword,String DBName){
		this.SERVER_IP = serverIP;
		this.SERVER_PORT = serverPORT;
		this.DB_USERNAME = DBUserName;
		this.DB_PASSWORD = DBPassword;
		this.INFLUXDBNAME = DBName;
	}
	
	public boolean saveData(Map<String,Object> map){
		return false;
	}
	
}
