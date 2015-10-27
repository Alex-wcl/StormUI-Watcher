package com.weibo.stormUI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.weibo.stormUI.dao.ClusterDao;
import com.weibo.stormUI.util.GlobalVariable;

@Component
public class ClusterService {
	
	@Autowired
	private ClusterDao clusterDao;
	
	public int usedSlots(){
		System.out.println("执行ClusterService.usedSlots（）方法！");
		int count = clusterDao.usedSlots(GlobalVariable.getCLUSTER_ID());
		return count;
	}
	public int freeSlots(){
		System.out.println("执行ClusterService.freeSlots（）方法！");
		int count = clusterDao.freeSlots(GlobalVariable.getCLUSTER_ID());
		return count;
	}
	public int totalSlots(){
		System.out.println("执行ClusterService.totalSlots（）方法！");
		int count = clusterDao.totalSlots(GlobalVariable.getCLUSTER_ID());
		return count;
	}
	public int executorCount(){
		System.out.println("执行ClusterService.executorCount（）方法！");
		int count = clusterDao.executorCount(GlobalVariable.getCLUSTER_ID());
		return count;
	}
	public int taskCount(){
		System.out.println("执行taskCount（）方法！");
		int count = clusterDao.taskCount(GlobalVariable.getCLUSTER_ID());
		return count;
	}
}
