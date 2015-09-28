package com.weibo.stormUI.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.weibo.stormUI.processor.DataProcessor;

public class DataLoadListener implements ServletContextListener {
	private static final Logger log = LogManager.getLogger(DataLoadListener.class);
	private Thread thread; 
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		log.info("初始化DataLoadListener...");
		ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext());
		try {
			Runnable dataProcessor = applicationContext.getBean(DataProcessor.class);
			thread = new Thread(dataProcessor);
			thread.setName("data-processor");
			thread.start();
			log.info("DataLoader线程启动成功！");
		} catch (Exception e) {
			System.out.println("DataLoader线程启动失败！");
			e.printStackTrace();
		}
	}
	
	
	@SuppressWarnings("deprecation")
	public void contextDestroyed(ServletContextEvent arg0) {
		log.info("DataLoader销毁！");
		thread.stop();
	}

}
