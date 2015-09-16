package com.weibo.stormUI.util;

public class GlobalVariable {
	/**
	 * 定义cluster的id，因为目前只有一个集群，不用每次都进行网络传输。
	 */
	private static String CLUSTER_ID;
	
	public static String getCLUSTER_ID() {
		return CLUSTER_ID;
	}
	synchronized public static void setCLUSTER_ID(String cLUSTER_ID) {
		CLUSTER_ID = cLUSTER_ID;
	}

}
