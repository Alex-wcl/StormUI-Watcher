package com.weibo.stormUI.processor;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.weibo.stormUI.dataLoader.DataLoader;
import com.weibo.stormUI.dataPersistencer.DataPersistencer;

@Component
public class DataProcessor implements Runnable {
	private long SLEEPTIME;
	private DataLoader dataLoader;
	private DataPersistencer dataPersistencer;
	
	public DataProcessor(){
		
	}
	public DataProcessor(long sleepTime, DataLoader dataLoader,DataPersistencer dataPersistencer){
		this.SLEEPTIME = sleepTime;
		this.dataLoader = dataLoader;
		this.dataPersistencer = dataPersistencer;
	}
	public void run() {
		while (true) {
			long sTime = System.currentTimeMillis();
			Map<String,Object> nextData = dataLoader.nextData();
			long cTime = System.currentTimeMillis();
			dataPersistencer.saveData(nextData);
			long eTime  = System.currentTimeMillis();
//			try {
//				System.out.println("开始休眠！");
//				Thread.currentThread().sleep(SLEEPTIME);
//				System.out.println("结束休眠！");
//			} catch (InterruptedException e) {
//				//日志系统
//				e.printStackTrace();
//			}
		}

	}


	public static void main(String[] args) throws IOException {
		// 测试
		// DataLoader dataLoader = new DataLoader();
		// dataLoader.loadTopologySummary(CLUSTER_SERVER_IP,CLUSTER_SERVER_PORT);

		// 开启线程
//		Runnable run = new DataProcessor();
//		Thread thread = new Thread(run);
//		thread.start();
	}


	public DataLoader getDataLoader() {
		return dataLoader;
	}


	public void setDataLoader(DataLoader dataLoader) {
		this.dataLoader = dataLoader;
	}


	public long getSLEEPTIME() {
		return SLEEPTIME;
	}


	public void setSLEEPTIME(long sLEEPTIME) {
		SLEEPTIME = sLEEPTIME;
	}


	public DataPersistencer getDataPersistencer() {
		return dataPersistencer;
	}

	public void setDataPersistencer(DataPersistencer dataPersistencer) {
		this.dataPersistencer = dataPersistencer;
	}
}
