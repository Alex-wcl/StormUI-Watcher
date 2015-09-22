package com.weibo.stormUI.util;

public class GlobalVariable {
	/**
	 * 定义cluster的id，因为目前只有一个集群，不用每次都进行网络传输。
	 */
	private static String CLUSTER_ID;
	/**
	 * influxdb服务器的ip地址
	 */
	private static String INFLUXDB_SERVER_IP = "10.77.108.126";
	/**
	 * influxdb提供的端口号，默认为8086
	 */
	private static int INFLUXDB_SERVER_PORT = 8086;
	/**
	 * 被监控cluster集群的ip地址。
	 */
	private static String CLUSTER_SERVER_IP = "10.77.128.101";
	/**
	 * 被监控cluster集群端口号
	 */
	private static int CLUSTER_SERVER_PORT = 8080;
	/**
	 * 线程休眠时间（毫秒）
	 */
	private static long SLEEPTIME = 1000 * 5;
	/**
	 * influxdb的用户名和密码，默认为root，root
	 */
	private static String USERNAME_INFLUXDB = "root";
	private static String PASSWD_INFLUXDB = "root";
	/**
	 * 要创建的influxdb数据库的名称
	 */
	private static String INFLUXDBNAME = "storm";
	
	public static String getINFLUXDB_SERVER_IP() {
		return INFLUXDB_SERVER_IP;
	}
	public static synchronized void setINFLUXDB_SERVER_IP(String iNFLUXDB_SERVER_IP) {
		INFLUXDB_SERVER_IP = iNFLUXDB_SERVER_IP;
	}
	public static int getINFLUXDB_SERVER_PORT() {
		return INFLUXDB_SERVER_PORT;
	}
	public static synchronized void setINFLUXDB_SERVER_PORT(int iNFLUXDB_SERVER_PORT) {
		INFLUXDB_SERVER_PORT = iNFLUXDB_SERVER_PORT;
	}
	public static String getCLUSTER_SERVER_IP() {
		return CLUSTER_SERVER_IP;
	}
	public static synchronized void setCLUSTER_SERVER_IP(String cLUSTER_SERVER_IP) {
		CLUSTER_SERVER_IP = cLUSTER_SERVER_IP;
	}
	public static int getCLUSTER_SERVER_PORT() {
		return CLUSTER_SERVER_PORT;
	}
	public static synchronized void setCLUSTER_SERVER_PORT(int cLUSTER_SERVER_PORT) {
		CLUSTER_SERVER_PORT = cLUSTER_SERVER_PORT;
	}
	public static long getSLEEPTIME() {
		return SLEEPTIME;
	}
	public static synchronized void setSLEEPTIME(long sLEEPTIME) {
		SLEEPTIME = sLEEPTIME;
	}
	public static String getUSERNAME_INFLUXDB() {
		return USERNAME_INFLUXDB;
	}
	public static synchronized void setUSERNAME_INFLUXDB(String uSERNAME_INFLUXDB) {
		USERNAME_INFLUXDB = uSERNAME_INFLUXDB;
	}
	public static String getPASSWD_INFLUXDB() {
		return PASSWD_INFLUXDB;
	}
	public static synchronized void setPASSWD_INFLUXDB(String pASSWD_INFLUXDB) {
		PASSWD_INFLUXDB = pASSWD_INFLUXDB;
	}
	public static String getINFLUXDBNAME() {
		return INFLUXDBNAME;
	}
	public static synchronized void setINFLUXDBNAME(String iNFLUXDBNAME) {
		INFLUXDBNAME = iNFLUXDBNAME;
	}
	public static String getCLUSTER_ID() {
		return CLUSTER_ID;
	}
	public static synchronized void setCLUSTER_ID(String cLUSTER_ID) {
		CLUSTER_ID = cLUSTER_ID;
	}

}
