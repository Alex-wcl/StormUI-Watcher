package com.weibo.stormUI.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.weibo.stormUI.service.ClusterService;

import net.sf.json.JSONObject;

public class ClusterServlet extends HttpServlet{

	/**
	 * 
	 */
	
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
	
	/**
	 * 覆盖父类doGet方法，处理请求。
	 * 使用java的反射机制，动态的调用方法
	 */
	@Override
	public void doGet(HttpServletRequest request,
			HttpServletResponse response)throws IOException,ServletException{
		String requestURL = request.getRequestURL().toString();
		String methodName = requestURL.substring(requestURL.lastIndexOf("/")+1);
		Method method = null;
		int responseValue = 0;
		try {
			//获取请求的方法，不是执行！
			//getMethod中第一个参数是方法名，第二个参数是方法中的形参（多个为数组），如果没有则为null
			method = ClusterService.class.getMethod(methodName, null);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		
		try {
			//执行方法。
			//invoke是开始执行方法，第一个参数是指定执行哪一个对象中的方法，第二个参数是实参（数组）。
			responseValue = (Integer)method.invoke(clusterService, null);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		//将返回结果转换成json格式返回到客户端。
		String json = "{\"results\":\"" + methodName +"\"}";
		JSONObject jsonObj = JSONObject.fromObject(json);
		jsonObj.put("count", responseValue);
		PrintWriter out = response.getWriter();
		out.print(jsonObj);
		out.flush();
		out.close();
	}

	
}
