package com.weibo.stormUI.processor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.common.base.Stopwatch;
import com.weibo.stormUI.dataLoader.DataLoader;
import com.weibo.stormUI.dataLoader.impl.DataLoaderStorm;
import com.weibo.stormUI.dataPersistencer.DataPersistencer;
import com.weibo.stormUI.dataPersistencer.impl.DataPersistencerImpl;

@Component
public class DataProcessor implements Runnable {
	private static final Logger log = LogManager.getLogger(DataProcessor.class);
	private long SLEEPTIME;
	private Map<String ,Object> datas;
	private DataLoader dataLoader;
	private DataPersistencer<DataPersistencerImpl> dataPersistencer;
	public DataProcessor(){
		
	}
	public DataProcessor(long sleepTime, DataLoader dataLoader,DataPersistencer dataPersistencer){
		this.SLEEPTIME = sleepTime;
		this.dataLoader = dataLoader;
		this.dataPersistencer = dataPersistencer;
	}
	public void run() {
		while(true){
			Stopwatch watch = Stopwatch.createStarted();
			dataLoader.nextData();
			log.info("dataloader execute time = " + watch.stop());
			log.info("dataLoaderThread started");
			dataPersistencer.saveData();
			log.info("dataPersistencerThread started");
			try {
				Thread.sleep(SLEEPTIME);
			} catch (InterruptedException e) {
				log.catching(e);
			}
		}
	}


	public static void main(String[] args) throws IOException {
		// 开启线程
		Map<String ,Object> datas = new HashMap<String ,Object>();
		DataLoader<Map> dataLoader = new DataLoaderStorm("10.77.108.127","8090",datas);
		DataPersistencer<DataPersistencerImpl> dataPersistencer = new DataPersistencerImpl("10.77.108.126","8086","root","root","storm",datas);
		Runnable run = new DataProcessor(1000 * 10,dataLoader,dataPersistencer);
		Thread thread = new Thread(run);
		thread.start();
	}


}
