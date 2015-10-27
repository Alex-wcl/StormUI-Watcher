package com.weibo.stormUI.processor;

import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import com.google.common.base.Stopwatch;
import com.weibo.stormUI.dataLoader.DataLoader;
import com.weibo.stormUI.dataLoader.impl.DataLoaderStorm;
import com.weibo.stormUI.dataPersistencer.impl.DataPersistencerImpl;
import com.weibo.stormUI.model.AlarmProperties;
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


            /*alarm module write by Yang LinQing*/
            URL alarmUrl = Thread.currentThread().getContextClassLoader().getResource("alarmProperties.xml");
            //System.out.println(Thread.currentThread().getContextClassLoader().getResource(""));
            //URL alarmUrl = new URL("/home/luka107/sina/project/watcher/stormUI-web/src/main/resources/alarmProperties.xml");

            if(alarmUrl == null) {
                log.info("null !!!  boy");
            }

            AlarmProperties alarmProperties = XmlHelper.getAlarmProperties(alarmUrl);

            Map topologyWorker = alarmProperties.getTopologyWorker();
            Set<String> topologyKeys = topologyWorker.keySet();

            Yaml yaml = new Yaml();
            URL yamlUrl = Thread.currentThread().getContextClassLoader().getResource("EmailConfig.yaml");

            //URL yamlUrl = new URL("/home/luka107/sina/project/watcher/stormUI-web/src/main/resources/EmailConfig.yaml");
            Map<String, List<String>> emailMap = (Map<String, List<String>>)yaml.load(new FileInputStream(yamlUrl.getFile()));
            List<String> emailList = emailMap.get("emails");

            System.out.println("TopologySize: " + topologyData.size());

            String content = "";
            String subject = "Storm Cluster Monitor Alert";
            String receiver = "";

            if(alarmProperties.isAvailable()) {

                for(int i = 0 ; i < emailList.size() ; i++) {
                    receiver += "," + emailList.get(i);
                }
                receiver = receiver.substring(1);
                Map<String, Integer> topologyNumMap = new HashMap<String, Integer>();

                for(int i = 0 ; i < topologyData.size() ; i++) {
                    TopologyData tempTopologyData = topologyData.get(i);
                    String topologyName = tempTopologyData.getName();
                    boolean isMatch = false;

                    log.info("topologyName: " + topologyName);

                    for(String topologyKey : topologyKeys) {
                        int containJudge = topologyName.indexOf(topologyKey);
                        if(containJudge < 0) continue;
                        else {
                            isMatch = true;

                            int workerNum = tempTopologyData.getWorkersTotal();
                            if(!topologyNumMap.containsKey(topologyKey)) {
                                topologyNumMap.put(topologyKey, workerNum);
                            }
                            else {
                                workerNum += topologyNumMap.get(topologyKey);
                                topologyNumMap.put(topologyKey, workerNum);
                            }

                        }
                    }
                    //we need revise the configuration file, adding the new topology
                    if(!isMatch) {
                        log.info("Add!!!!");
                        content = "we have new topology, need to revise the configuration and add it";
                        alert(receiver, subject, content);
                    }

                }

                for(String topologyKey : topologyKeys) {
                    if(!topologyNumMap.containsKey(topologyKey)) {
                        System.out.println("topologyKey: " + topologyKey);
                        continue;
                    }
                    int workerNum = topologyNumMap.get(topologyKey);
                    if(workerNum != Integer.valueOf(topologyWorker.get(topologyKey).toString())) { //alarm

                        content = topologyKey + "'s worker number is wrong!";

                        alert(receiver, subject, content);
                    }
                }

                //judge whether the configurations have some topologies that were already died.
                for(String topologyKey : topologyKeys) {
                    boolean isMatch = false;
                    for(int i = 0 ; i < topologyData.size() ; i++) {
                        TopologyData tempTopologyData = topologyData.get(i);
                        String topologyName = tempTopologyData.getName();
                        int containJudge = topologyName.indexOf(topologyKey);
                        if(containJudge < 0) continue;
                        else {
                            isMatch = true;
                            break;
                        }
                    }
                    //we need revise the configuration file, deleting the died topology.
                    if(!isMatch) {
                        log.info("Delete ~!!!!");
                        content = "we have old topology, need to revise the configuration and delete it";
                        alert(receiver, subject, content);
                    }
                }
            }


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


    private static void alert(String receiver, String subject, String content) {

        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost("http://10.75.8.32:8090/ajax/alert/sendMail.html");

            ArrayList<NameValuePair> nameValueList = new ArrayList<NameValuePair>();



            nameValueList.add(new BasicNameValuePair("mailto",receiver));
            nameValueList.add(new BasicNameValuePair("msgto",receiver));
            nameValueList.add(new BasicNameValuePair("sv","微博平台"));
            nameValueList.add(new BasicNameValuePair("service","通讯产品"));
            nameValueList.add(new BasicNameValuePair("object","ELK"));
            nameValueList.add(new BasicNameValuePair("subject", subject));
            nameValueList.add(new BasicNameValuePair("content", content));



            httpPost.setEntity(new UrlEncodedFormEntity(nameValueList, HTTP.UTF_8));
            httpPost.getParams().setParameter("http.protocol.content-charset",HTTP.UTF_8);


            HttpResponse response = httpClient.execute(httpPost);

            log.info(EntityUtils.toString(response.getEntity()));
        } catch(Exception e) {
            log.error("alert error!" + receiver + " " + subject + " " + content, e);
        }
    }

    public static void main(String[] args) {
        Runnable run = new DataProcessor();
        Thread thread = new Thread(run);
        thread.start();
    }
}
