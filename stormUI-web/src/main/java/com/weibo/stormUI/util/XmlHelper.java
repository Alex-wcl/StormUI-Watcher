package com.weibo.stormUI.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.weibo.stormUI.model.AlarmProperties;
import com.weibo.stormUI.model.User;

public class XmlHelper {
    private static SAXReader reader = new SAXReader();

    public static List<User> getUsers(URL userXmlURL) throws DocumentException {
        Document document = reader.read(userXmlURL);
        Element root = document.getRootElement();
        List<Element> childElements = root.elements();
        List<User> users = new ArrayList<User>();

        for (Element child : childElements) {
            User user = new User();
            user.setName(child.elementText("username"));
            user.setPassword(child.elementText("password"));
            users.add(user);
        }
        return users;
    }

    /**
     * 读alarmProperties.xml
     * 
     * @return 所有的参数
     * @throws DocumentException
     */
    public static AlarmProperties getAlarmProperties(URL AlarmPropertiesXMLUrl) throws DocumentException {
        Document document = reader.read(AlarmPropertiesXMLUrl);
        Element root = document.getRootElement();
        AlarmProperties alarmProperties = new AlarmProperties();
        if (root.element("isAvailable").attribute("value").getText().equals("true")) {
            alarmProperties.setAvailable(true);
        }
        alarmProperties.setWorker_alarm_times(Integer.valueOf(root.element("worker_alarm_times").attribute("value").getText()));
        alarmProperties.setWorker_alarm_interval(Integer.valueOf(root.element("worker_alarm_interval").attribute("value").getText()));
        alarmProperties.setTopology_alarm_times(Integer.valueOf(root.element("topology_alarm_times").attribute("value").getText()));
        alarmProperties.setTopology_alarm_interval(Integer.valueOf(root.element("topology_alarm_interval").attribute("value").getText()));
        Element topologies = root.element("topologies");
        List<Element> topology = topologies.elements();
        for (Element child : topology) {
            alarmProperties.getTopologyWorker().put(child.attribute("topologyId").getText(), child.attribute("worker").getText());
        }
        return alarmProperties;
    }

    /**
     * 删除topology节点
     * 
     * @param key 被删除的topology的id
     * @return
     */
    public static boolean deleteAlarmProperties(String key) {
        return true;
    }

    /**
     * 更新或添加节点参数值（非topology），如果有则更新，没有则添加
     * 
     * @param name 节点名称
     * @param value 节点值
     * @return
     */
    public static boolean updateAndSaveAlarmProperties(String name, String value) {
        return true;
    }

    /**
     * 更新或添加两层节点topology的值，如果有则更新，没有则添加。
     * 
     * @param type topology
     * @param name topology的id
     * @param value topology的值
     * @return
     */
    public static boolean updateAndSaveAlarmProperties(String type, String name, String value) {
        return true;
    }

    /**
     * 更新整个alarmProperties
     * 
     * @param alarmProperties
     * @return
     * @throws DocumentException
     */
    public static boolean updateAndSaveAlarmProperties(AlarmProperties alarmProperties, URL AlarmPropertiesXMLUrl) {
        Document document = null;
        try {
            document = reader.read(AlarmPropertiesXMLUrl);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Element root = document.getRootElement();
        
        //不存在则为空
        Element isAvailable = root.element("isAvailable");
        if (alarmProperties.isAvailable()) {
            isAvailable.setAttributeValue("value", "true");
        } else {
            isAvailable.setAttributeValue("value", "false");
        }
        
        root.element("topology_alarm_interval").setAttributeValue("value", String.valueOf(alarmProperties.getTopology_alarm_interval()));;
        root.element("topology_alarm_times").setAttributeValue("value", String.valueOf(alarmProperties.getTopology_alarm_times()));;
        root.element("worker_alarm_times").setAttributeValue("value", String.valueOf(alarmProperties.getWorker_alarm_times()));
        root.element("worker_alarm_interval").setAttributeValue("value", String.valueOf(alarmProperties.getWorker_alarm_interval()));;
        
        
        Element topologies = root.element("topologies");
        root.remove(topologies);
        topologies = root.addElement("topologies");
        Iterator iter = alarmProperties.getTopologyWorker().entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            Element topology = topologies.addElement("topology");
            topology.setAttributeValue("topologyId", key);
            topology.setAttributeValue("worker", value);
        }
        //写入到文件
        writeTOFile(AlarmPropertiesXMLUrl,document);
        return true;
    }
    
    private static boolean writeTOFile(URL AlarmPropertiesXMLUrl,Document document){
        /** 将document中的内容写入文件中 */
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");

        XMLWriter writer = null;
        try {
            writer = new XMLWriter(new FileOutputStream(new File(AlarmPropertiesXMLUrl.getFile())), format);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            writer.write(document);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return true;
    }



}
