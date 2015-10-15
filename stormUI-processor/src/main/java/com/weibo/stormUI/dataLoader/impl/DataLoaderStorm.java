package com.weibo.stormUI.dataLoader.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.weibo.stormUI.dataLoader.DataLoader;
import com.weibo.stormUI.dataLoader.module.ModuleDataLoader;
import com.weibo.stormUI.model.ClusterData;
import com.weibo.stormUI.model.SpoutADNBolt;
import com.weibo.stormUI.util.StringHelper;
import com.weibo.stormUI.util.URLConnectionHelper;

import net.sf.json.JSONObject;

@Component
public class DataLoaderStorm<T> implements DataLoader<T>{
	private static final Logger log = LogManager.getLogger(DataLoaderStorm.class);
	private T datas;
	private String SERVER_IP;
	private String SERVER_PORT;
	private ModuleDataLoader moduleDataLoader;
	private List<String> topologyIds;
	private Map<String, Object> map = new HashMap<String, Object>();
	private List<T> elems;
	
	public DataLoaderStorm(){}
	public DataLoaderStorm(String serverIP, String serverPORT,T data,List<String> topologyIds) {
		this.SERVER_IP = serverIP;
		this.SERVER_PORT = serverPORT;
		this.datas = data;
		this.topologyIds = topologyIds;
		this.elems = new ArrayList<T>();
	}
	
	public List<T> nextData() {
		this.elems = new ArrayList<T>();
		String item = "";
		String data = null;
		String urlString = null;
		if(datas instanceof SpoutADNBolt){
			int size = topologyIds.size();
			if(size > 0){
				CountDownLatch latch = new CountDownLatch(size);
				for(int i = 0;i < topologyIds.size();i++){
					moduleDataLoader = new ModuleDataLoader(this,topologyIds.get(i),latch,i);
					moduleDataLoader.setName(topologyIds.get(i));
					moduleDataLoader.start();
				}
				try {
					latch.await();
				} catch (InterruptedException e) {
					log.catching(e);
				}
			}
			return elems;
		}
		
		try{
			item = datas.getClass().getName().substring(datas.getClass().getName().lastIndexOf(".")+1, datas.getClass().getName().indexOf("Data")).toLowerCase() + "/summary";
		}catch(Exception e){
			log.catching(e);
		}
		urlString = "http://" + SERVER_IP + ":" + SERVER_PORT + "/api/v1/" + item;
		data = URLConnectionHelper.URLConnection(urlString);
		String stringDatas = null;
		
		//cluster只有一个，只循环一次
		int count = 1;
		if(!(datas instanceof ClusterData)){
			stringDatas = data.substring(data.indexOf("[") + 1, data.lastIndexOf("]"));
			count = StringHelper.getSubtringCount(stringDatas, "{");
		}
		for (int i = 0; i < count; i++) {
			String t = null;
			JSONObject jsonObject = null;
			if(!(datas instanceof ClusterData)){
				t = stringDatas.substring(StringHelper.getSubtringIndex(stringDatas, "{", i + 1),
						StringHelper.getSubtringIndex(stringDatas, "}", i + 1) + 1);
				jsonObject = JSONObject.fromObject(t);
			}else{
				jsonObject = JSONObject.fromObject(data);
			}
			datas  = (T)jsonObject.toBean(jsonObject, datas.getClass());
			elems.add(datas);
			log.info(datas.getClass().getName() + i);
		}
		return elems;
	}
	
	
	
		public T getDatas() {
			return datas;
		}

		public void setDatas(T datas) {
			this.datas = datas;
		}

		public String getSERVER_IP() {
			return SERVER_IP;
		}
		public void setSERVER_IP(String sERVER_IP) {
			SERVER_IP = sERVER_IP;
		}
		public String getSERVER_PORT() {
			return SERVER_PORT;
		}
		public void setSERVER_PORT(String sERVER_PORT) {
			SERVER_PORT = sERVER_PORT;
		}

		public List<String> getTopologyIds() {
			return topologyIds;
		}

		public void setTopologyIds(List<String> topologyIds) {
			this.topologyIds = topologyIds;
		}

		public Map<String, Object> getMap() {
			return map;
		}

		public void setMap(Map<String, Object> map) {
			this.map = map;
		}

		public List<T> getElems() {
			return elems;
		}

		public void setElems(List<T> elems) {
			this.elems = elems;
		}

			
		
}
