package com.weibo.stormUI.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.weibo.stormUI.dataLoader.DataLoader;
import com.weibo.stormUI.dataLoader.impl.DataLoaderStorm;
import com.weibo.stormUI.dataPersistencer.DataPersistencer;
import com.weibo.stormUI.processor.DataProcessor;

public class DataLoadListener implements ServletContextListener {

	public void contextInitialized(ServletContextEvent servletContextEvent) {
		System.out.println("正在开启DataLoader线程...");
		ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext());
		try {
			DataPersistencer DataPersistencer = (DataPersistencer)applicationContext.getBean(DataPersistencer.class);
			DataLoader dataLoader = (DataLoader)applicationContext.getBean(DataLoaderStorm.class);
			Runnable dataProcessor = new DataProcessor(1000*5,dataLoader,DataPersistencer);
			Thread thread = new Thread(dataProcessor);
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
