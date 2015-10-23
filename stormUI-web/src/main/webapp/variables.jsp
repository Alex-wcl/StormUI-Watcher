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
 	<link rel="stylesheet" type="text/css" href="css/style.css" />
		<script src="js/modernizr.custom.63321.js"></script>
		<style>	
			body {
				background: #7f9b4e url(images/bg2.jpg) no-repeat center top;
				-webkit-background-size: cover;
				-moz-background-size: cover;
				background-size: cover;
			}
		</style>
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
	<h1>查看/修改系统变量</h1>
	<form class="form-4" action="VariableServlet" method="post">
		<h3>DataProcessorSleepTime(毫秒)</h3><input type="text" id="DataProcessorSleepTime" name="DataProcessorSleepTime" value=<%=variables.getDataProcessorSleepTime() %> /><br>
		<h3>DataSourceServerIP</h3><input type="text" id="DataSourceServerIP" name="DataSourceServerIP" value=<%=variables.getDataSourceServerIP() %> /><br>
		<h3>DataSourceServerPort</h3><input type="text" name="DataSourceServerPort" value=<%=variables.getDataSourceServerPort() %> /><br>
		<h3>DataBaseServerIP</h3><input type="text" name="DataBaseServerIP" value=<%=variables.getDataBaseServerIP() %> /><br>
		<h3>DataBaseServerPort</h3><input type="text" name="DataBaseServerPort" value=<%=variables.getDataBaseServerPort() %> /><br>
		<h3>DataBaseUserName</h3><input type="text" name="DataBaseUserName" value=<%=variables.getDataBaseUserName() %> /><br>
		<h3>DataBasePassword</h3><input type="text" name="DataBasePassword" value=<%=variables.getDataBasePassword() %> /><br>
		<h3>DataBaseName</h3><input type="text" name="DataBaseName" value=<%=variables.getDataBaseName() %> /><br>
		<input type="submit" value="提交">
	</form>
	<%}else{
		request.getRequestDispatcher("/index.jsp").forward(request, response);
	%>
	<%} %>
	
	
</body>
</html>