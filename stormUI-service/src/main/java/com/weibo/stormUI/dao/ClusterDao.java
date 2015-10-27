package com.weibo.stormUI.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.weibo.stormUI.util.GlobalVariable;

@Component
public class ClusterDao {
	
	
	public int usedSlots(String string){
		System.out.println("执行ClusterService.usedSlots（）方法！");
		return 0;
	}
	public int freeSlots(String string){
		System.out.println("执行ClusterService.freeSlots（）方法！");
		return 0;
	}
	public int totalSlots(String string){
		System.out.println("执行ClusterService.totalSlots（）方法！");
		return 0;
	}
	public int executorCount(String string){
		System.out.println("执行ClusterService.executorCount（）方法！");
		return 0;
	}
	public int taskCount(String string){
		System.out.println("执行taskCount（）方法！");
		return 0;
	}
}
