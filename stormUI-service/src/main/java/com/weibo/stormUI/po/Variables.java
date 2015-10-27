package com.weibo.stormUI.po;

import org.springframework.stereotype.Component;

@Component
public class Variables {
	//处理机休眠时间
	private long DataProcessorSleepTime = 1000 * 20;
	//数据源
	private String DataSourceServerIP = "10.77.128.101";
	private String DataSourceServerPort = "8080";
	//数据库http://10.77.108.126:8083/
//	private String DataBaseServerIP = "10.75.0.24";
	private String DataBaseServerIP = "10.77.108.126";
	private String DataBaseServerPort = "8086";
	private String DataBaseUserName = "root";
	private String DataBasePassword = "root";
	private String DataBaseName = "storm";
	
	private static final Variables variables = new Variables();
	private Variables(){}
	public static Variables getInstance(){
	    return variables;
	}
	
	public String getString(){
		return "DataProcessorSleepTime = " + DataProcessorSleepTime + "DataSourceServerIP = " + DataSourceServerIP
				+ "DataSourceServerPort = " + DataSourceServerPort + "DataBaseServerIP = " + DataBaseServerIP
				+ "DataBaseServerPort = " + DataBaseServerPort + "DataBaseUserName = " + DataBaseUserName 
				+ "DataBasePassword = " + DataBasePassword + "DataBaseName = " + DataBaseName;
	}
    public long getDataProcessorSleepTime() {
        return DataProcessorSleepTime;
    }
    public void setDataProcessorSleepTime(long dataProcessorSleepTime) {
        DataProcessorSleepTime = dataProcessorSleepTime;
    }
    public String getDataSourceServerIP() {
        return DataSourceServerIP;
    }
    public void setDataSourceServerIP(String dataSourceServerIP) {
        DataSourceServerIP = dataSourceServerIP;
    }
    public String getDataSourceServerPort() {
        return DataSourceServerPort;
    }
    public void setDataSourceServerPort(String dataSourceServerPort) {
        DataSourceServerPort = dataSourceServerPort;
    }
    public String getDataBaseServerIP() {
        return DataBaseServerIP;
    }
    public void setDataBaseServerIP(String dataBaseServerIP) {
        DataBaseServerIP = dataBaseServerIP;
    }
    public String getDataBaseServerPort() {
        return DataBaseServerPort;
    }
    public void setDataBaseServerPort(String dataBaseServerPort) {
        DataBaseServerPort = dataBaseServerPort;
    }
    public String getDataBaseUserName() {
        return DataBaseUserName;
    }
    public void setDataBaseUserName(String dataBaseUserName) {
        DataBaseUserName = dataBaseUserName;
    }
    public String getDataBasePassword() {
        return DataBasePassword;
    }
    public void setDataBasePassword(String dataBasePassword) {
        DataBasePassword = dataBasePassword;
    }
    public String getDataBaseName() {
        return DataBaseName;
    }
    public void setDataBaseName(String dataBaseName) {
        DataBaseName = dataBaseName;
    }
	
	
}
