package com.weibo.stormUI.processor;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.common.base.Stopwatch;
import com.weibo.stormUI.dataLoader.DataLoader;
import com.weibo.stormUI.dataLoader.impl.DataLoaderStorm;
import com.weibo.stormUI.dataPersistencer.impl.DataPersistencerImpl;
import com.weibo.stormUI.model.ClusterData;
import com.weibo.stormUI.model.SpoutADNBolt;
import com.weibo.stormUI.model.SupervisorData;
import com.weibo.stormUI.model.TopologyData;
import com.weibo.stormUI.po.Variables;
import com.weibo.stormUI.util.XmlHelper;

@Component
public class DataProcessor implements Runnable {
    private static final Logger log = LogManager.getLogger(DataProcessor.class);
    private List<String> topologyIds;
    private DataLoader<TopologyData> topologyDataLoader;
    private DataLoader<SupervisorData> supervisorDataLoader;
    private DataLoader<ClusterData> clusterDataLoader;
    private DataPersistencerImpl influxDB;
    private Timer timer;
    private Variables variables;
    private Stopwatch watch;
    private Stopwatch globalWatch;

    public void run() {
        try {
            setup();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    runTask();
                }
            };
            timer.schedule(task, 0, variables.getDataProcessorSleepTime());
        } catch (Throwable e) {
            log.catching(e);
        }
    }

    /**
     * 初始化参数
     */
    private void setup() {
        variables = Variables.getInstance();
        timer = new Timer();
        topologyDataLoader = new DataLoaderStorm<TopologyData>(variables.getDataSourceServerIP(), variables.getDataSourceServerPort(),
                new TopologyData(), topologyIds);
        supervisorDataLoader = new DataLoaderStorm<SupervisorData>(variables.getDataSourceServerIP(), variables.getDataSourceServerPort(),
                new SupervisorData(), topologyIds);
        clusterDataLoader = new DataLoaderStorm<ClusterData>(variables.getDataSourceServerIP(), variables.getDataSourceServerPort(),
                new ClusterData(), topologyIds);
        
        //初始化保存数据类
        influxDB = new DataPersistencerImpl();
        influxDB.setUp(variables.getDataBaseServerIP(), variables.getDataBaseServerPort(), variables.getDataBaseUserName(),
            variables.getDataBasePassword());
        influxDB.createDB(variables.getDataBaseName());
    }

    /**
     * 开始任务：定时取数据，并保存数据
     */
    private void runTask() {
        try {
            watch = Stopwatch.createStarted();
            globalWatch = Stopwatch.createStarted();
            //从远端收集数据
            List<TopologyData> topologyData = topologyDataLoader.nextData();
            log.info("loadTopologyDataTime ： " + watch);
            // 为了避免topologyId可能会变的情况，将每一次查询的topologyId重新保存。
            topologyIds = new ArrayList<String>();
            for (TopologyData topologyDataTmp : topologyData) {
                topologyIds.add(topologyDataTmp.getId());
            }
            
            DataLoader<SpoutADNBolt> boltDataLoader = new DataLoaderStorm<SpoutADNBolt>(variables.getDataSourceServerIP(),
                    variables.getDataSourceServerPort(), new SpoutADNBolt(), topologyIds);
            watch.reset();
            watch.start();
            List<SpoutADNBolt> spoutADNBoltData = boltDataLoader.nextData();
            log.info("loadSpoutADNBoltDataTime ： " + watch);
            watch.reset();
            watch.start();
            List<SupervisorData> supervisorData = supervisorDataLoader.nextData();
            log.info("loadSupervisorDataTime ： " + watch);
            watch.reset();
            watch.start();
            List<ClusterData> clusterData = clusterDataLoader.nextData();
            log.info("loadClusterDataTime ： " + watch);
            watch.reset();
            
            //保存数据到本地
            watch.start();
            influxDB.saveData(spoutADNBoltData);
            influxDB.saveData(supervisorData);
            influxDB.saveData(clusterData);
            influxDB.saveData(topologyData);
            log.info("insertDataTime ： " + watch);
            log.info("总耗时 ： " + globalWatch);
        } catch (Throwable t) {
            log.catching(t);
        }
    }
    
    
    public static void main(String[] args) {
        Runnable run = new DataProcessor();
        Thread thread = new Thread(run);
        thread.start();
    }
}
