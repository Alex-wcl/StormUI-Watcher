package com.weibo.stormUI.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.DocumentException;

import com.weibo.stormUI.model.AlarmProperties;
import com.weibo.stormUI.util.XmlHelper;

public class AlarmServlet extends HttpServlet{
	private static final Logger log = LogManager.getLogger(AlarmServlet.class);
	private static final long serialVersionUID = 2301587146057570485L;
	private AlarmProperties alarmProperties;
	
	/**
	 * 覆盖HttpServlet中方法，初始化参数
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
	    alarmProperties = new AlarmProperties();
	}
	
	
	/**
	 * doGet是查询并显示数据
	 */
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    try {
	        alarmProperties = XmlHelper.getAlarmProperties(Thread.currentThread().getContextClassLoader().getResource("alarmProperties.xml"));
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	    
	    req.setAttribute("alarmProperties", alarmProperties);
		req.getRequestDispatcher("/alarmProperties.jsp").forward(req, resp); 
	}
	/**
	 * doPost是修改并显示数据
	 */
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    boolean isAvailable = false;
	    if(req.getParameter("isAvailable").equals("true")){
	        isAvailable = true;
	    }
		int worker_alarm_times = Integer.valueOf(req.getParameter("worker_alarm_times"));
		int worker_alarm_interval = Integer.valueOf(req.getParameter("worker_alarm_interval"));
		int topology_alarm_times = Integer.valueOf(req.getParameter("topology_alarm_times"));
		int topology_alarm_interval = Integer.valueOf(req.getParameter("topology_alarm_interval"));
		String[] topologyIds = req.getParameterValues("topologyid");
		String[] workers = req.getParameterValues("worker");
		Map topologyMap = new HashMap();
		for(int i = 0;i<topologyIds.length;i++){
		    topologyMap.put(topologyIds[i], workers[i]);
		}
		AlarmProperties AlarmPropertiesTmp = new AlarmProperties(topologyMap,isAvailable,worker_alarm_times,topology_alarm_times,topology_alarm_interval,worker_alarm_interval);
		System.out.println(Thread.currentThread().getContextClassLoader().getResource("alarmProperties.xml"));
		boolean msg = XmlHelper.updateAndSaveAlarmProperties(AlarmPropertiesTmp,Thread.currentThread().getContextClassLoader().getResource("alarmProperties.xml"));
        if(msg){
            req.setAttribute("status", "ok");  
        }else{
            req.setAttribute("status", "error");
        }
		req.setAttribute("alarmProperties", AlarmPropertiesTmp);
		
		System.out.println(msg);
		req.getRequestDispatcher("/alarmProperties.jsp").forward(req, resp); 
	}
	
}
