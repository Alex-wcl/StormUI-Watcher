package com.weibo.stormUI.po;

import org.springframework.stereotype.Component;

@Component
public class Variables {
	//处理机休眠时间
	public static long DataProcessorSleepTime = 1000 * 20;
	//数据源
	public static String DataSourceServerIP = "10.77.128.101";
	public static String DataSourceServerPort = "8080";
	//数据库
	public static String DataBaseServerIP = "10.77.108.126";
	public static String DataBaseServerPort = "8086";
	public static String DataBaseUserName = "root";
	public static String DataBasePassword = "root";
	public static String DataBaseName = "storm";
	
	private static Variables variables = new Variables();
	public static Variables getInstance(){
		return variables;
	}
	public static String getString(){
		return "DataProcessorSleepTime = " + DataProcessorSleepTime + "DataSourceServerIP = " + DataSourceServerIP
				+ "DataSourceServerPort = " + DataSourceServerPort + "DataBaseServerIP = " + DataBaseServerIP
				+ "DataBaseServerPort = " + DataBaseServerPort + "DataBaseUserName = " + DataBaseUserName 
				+ "DataBasePassword = " + DataBasePassword + "DataBaseName = " + DataBaseName;
	}
}
