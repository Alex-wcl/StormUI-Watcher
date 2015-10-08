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
</body>
</html>