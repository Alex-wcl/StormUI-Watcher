package com.weibo.stormUI.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.weibo.stormUI.service.ClusterService;

public class VariablesServlet extends HttpServlet{
	private static final long serialVersionUID = 2301587146057570485L;
	private ClusterService clusterService;
	//定义全局ServletConfig变量，接收初始化参数config
	private ServletConfig servletConfig;
	
	/**
	 * 覆盖HttpServlet中方法，初始化参数
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		servletConfig = config;
		ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletConfig.getServletContext());
		clusterService = applicationContext.getBean(ClusterService.class);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
	}
}
