package com.weibo.stormUI.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.weibo.stormUI.processor.DataLoader;

public class DataLoadListener implements ServletContextListener {

	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("正在开启DataLoader线程...");
		try {
			Runnable dataLoader = new DataLoader();
			Thread thread = new Thread(dataLoader);
			thread.start();
			System.out.println("DataLoader线程启动成功！");
		} catch (Exception e) {
			System.out.println("DataLoader线程启动失败！");
			e.printStackTrace();
		}
	}
	
	
	public void contextDestroyed(ServletContextEvent arg0) {

	}

}
