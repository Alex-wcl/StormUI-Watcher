<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.weibo.stormUI.po.*" %>
<%
		Variables variables = (Variables)request.getAttribute("variables");
		String status = (String)request.getAttribute("status");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看/修改系统变量</title>

<script type="text/javascript">
	<%if(status != null && status.equals("ok")){%>
		alert("修改成功！");
	<%}%>
</script>
</head>
<body>
	<%
	String username = (String)request.getSession().getAttribute("username");
	String password = (String)request.getSession().getAttribute("password");
	if(username != null && password != null){%>
	<h2>查看/修改系统变量</h2>
	<form action="VariableServlet" method="post">
		<h3>DataProcessorSleepTime(毫秒)</h3><input type="text" id="DataProcessorSleepTime" name="DataProcessorSleepTime" value=<%=variables.DataProcessorSleepTime %> /><br>
		<h3>DataSourceServerIP</h3><input type="text" id="DataSourceServerIP" name="DataSourceServerIP" value=<%=variables.DataSourceServerIP %> /><br>
		<h3>DataSourceServerPort</h3><input type="text" name="DataSourceServerPort" value=<%=variables.DataSourceServerPort %> /><br>
		<h3>DataBaseServerIP</h3><input type="text" name="DataBaseServerIP" value=<%=variables.DataBaseServerIP %> /><br>
		<h3>DataBaseServerPort</h3><input type="text" name="DataBaseServerPort" value=<%=variables.DataBaseServerPort %> /><br>
		<h3>DataBaseUserName</h3><input type="text" name="DataBaseUserName" value=<%=variables.DataBaseUserName %> /><br>
		<h3>DataBasePassword</h3><input type="text" name="DataBasePassword" value=<%=variables.DataBasePassword %> /><br>
		<h3>DataBaseName</h3><input type="text" name="DataBaseName" value=<%=variables.DataBaseName %> /><br>
		<input type="submit" value="提交">
	</form>
	<%}else{
		request.getRequestDispatcher("/index.jsp").forward(request, response);
	%>
	<%} %>
	
	<%-- <%
	String username = (String)request.getSession().getAttribute("username");
	String password = (String)request.getSession().getAttribute("password");
	if(username != null && password != null){%>
	<h1>欢迎您，<%=username %></h1>
	<a href="http://10.75.0.24:10135/">Muses</a>:在线数据查询系统<br>
	<a href="http://10.210.230.32">私信dashboard</a>:私信、长微博、实体库、多媒体监控<br>
	<a href="http://10.13.81.28/dashboard#groupchat-28">群聊dashboard</a>：群聊监控<br>
	<a href="http://10.75.28.180:8082/index.html#/dashboard/elasticsearch/msgs-router">乒乓私信dashboard</a>：乒乓私信监控<br>
	<a href="http://172.16.234.135/">webim监控后台  </a><br>
	<a href="http://10.79.40.213:9090/index.py">TFS集群监控和运维系统</a><br>
	<a href="http://10.13.1.136:9001/group_chat_debug/sitemap.php">群聊基本操作工具 </a><br>
	<a href="http://10.75.0.24:13211/groupchat-tool/">群聊后台工具 </a><br>
	<a href=" http://10.73.14.122:9001/">私信系统保护hystrix实时监控</a><br>
	<a href="http://10.210.230.32">通知中心接口监控</a><br>
	<a href="http://10.73.17.25:3000">DSP KAFKA监控</a><br>
	<a href="http://10.77.128.101:8080/index.html">storm UI</a><br>
	<%}else{
		request.getRequestDispatcher("/index.jsp").forward(request, response);
	%>
	<%} %> --%>
</body>
</html>