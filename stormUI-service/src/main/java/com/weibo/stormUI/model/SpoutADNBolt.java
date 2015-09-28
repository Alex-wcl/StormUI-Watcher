package com.weibo.stormUI.model;

import java.util.List;

public class SpoutADNBolt {
	private List<BoltData> boltDatas;
	private List<SpoutData> spoutDatas;
	
	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		for(BoltData tmp : boltDatas){
			result.append(tmp.toString());
		}
		for(SpoutData tmp : spoutDatas){
			result.append(tmp.toString());
		}
		return result.toString();
	}
	public List<BoltData> getBoltDatas() {
		return boltDatas;
	}
	public void setBoltDatas(List<BoltData> boltDatas) {
		this.boltDatas = boltDatas;
	}
	public List<SpoutData> getSpoutDatas() {
		return spoutDatas;
	}
	public void setSpoutDatas(List<SpoutData> spoutDatas) {
		this.spoutDatas = spoutDatas;
	}
	
	
}
