package com.weibo.stormUI.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.weibo.stormUI.po.Variables;
import com.weibo.stormUI.processor.DataProcessor;
import com.weibo.stormUI.service.ClusterService;

public class VariableServlet extends HttpServlet{
	private static final Logger log = LogManager.getLogger(VariableServlet.class);
	private static final long serialVersionUID = 2301587146057570485L;
	private Variables variables;
	
	/**
	 * 覆盖HttpServlet中方法，初始化参数
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		variables = Variables.getInstance();
	}
	
	
	/**
	 * doGet是查询并显示数据
	 */
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("variables", variables);
		req.getRequestDispatcher("/variables.jsp").forward(req, resp); 
	}
	/**
	 * doPost是修改并显示数据
	 */
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long DataProcessorSleepTime = Long.valueOf(req.getParameter("DataProcessorSleepTime"));
		String DataSourceServerIP = req.getParameter("DataSourceServerIP");
		String DataSourceServerPort = req.getParameter("DataSourceServerPort");
		String DataBaseServerIP = req.getParameter("DataBaseServerIP");
		String DataBaseServerPort = req.getParameter("DataBaseServerPort");
		String DataBaseUserName = req.getParameter("DataBaseUserName");
		String DataBasePassword = req.getParameter("DataBasePassword");
		String DataBaseName = req.getParameter("DataBaseName");
		variables.DataProcessorSleepTime = DataProcessorSleepTime;
		variables.DataSourceServerIP = DataSourceServerIP;
		variables.DataSourceServerPort = DataSourceServerPort;
		variables.DataBaseServerIP = DataBaseServerIP;
		variables.DataBaseServerPort = DataBaseServerPort;
		variables.DataBaseUserName = DataBaseUserName;
		variables.DataBasePassword = DataBasePassword;
		variables.DataBaseName = DataBaseName;
		req.setAttribute("variables", variables);
		req.setAttribute("status", "ok");
		req.getRequestDispatcher("/variables.jsp").forward(req, resp); 
	}
	
}
