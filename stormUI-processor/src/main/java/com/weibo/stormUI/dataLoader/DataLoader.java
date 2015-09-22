package com.weibo.stormUI.dataLoader;

import java.util.Map;

import org.springframework.stereotype.Component;

public class DataLoader {
	public String SERVER_IP;
	public String SERVER_PORT;
	
	public DataLoader(){}
	public DataLoader(String serverIP,String serverPORT){
		this.SERVER_IP = serverIP;
		this.SERVER_PORT = serverPORT;
	}
	public Map<String,Object> nextData(){
		return null;
	}
}
