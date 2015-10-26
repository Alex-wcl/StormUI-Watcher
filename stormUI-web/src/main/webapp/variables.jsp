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
	
	function submitme(){
		document.AlarmServlet.submit();
	}
</script>
</head>
<body>
	<%
	String username = (String)request.getSession().getAttribute("username");
	String password = (String)request.getSession().getAttribute("password");
	if(username != null && password != null){%>
	
	<form class="form-4" action="VariableServlet" method="post">
		<table style="width: 500px;float: left;">
		<caption align="top" style="margin-bottom: 15px;"><h1>查看/修改系统变量</h1></caption>
			<tr>
				<td><h3>DataProcessorSleepTime(毫秒)</h3></td>
				<td><input type="text" id="DataSourceServerIP" name="DataSourceServerIP" value=<%=variables.getDataSourceServerIP() %> /></td>
			</tr>
			
			<tr>
				<td><h3>DataSourceServerIP</h3></td>
				<td><input type="text" id="DataProcessorSleepTime" name="DataProcessorSleepTime" value=<%=variables.getDataProcessorSleepTime() %> /></td>
			</tr>
			
			<tr>
				<td><h3>DataSourceServerPort</h3></td>
				<td><input type="text" name="DataSourceServerPort" value=<%=variables.getDataSourceServerPort() %> /></td>
			</tr>
			
			<tr>
				<td><h3>DataBaseServerIP</h3></td>
				<td><input type="text" name="DataBaseServerIP" value=<%=variables.getDataBaseServerIP() %> /></td>
			</tr>
			
			<tr>
				<td><h3>DataBaseServerPort</h3></td>
				<td><input type="text" name="DataBaseServerPort" value=<%=variables.getDataBaseServerPort() %> /></td>
			</tr>
			
			<tr>
				<td><h3>DataBaseUserName</h3></td>
				<td><input type="text" name="DataBaseUserName" value=<%=variables.getDataBaseUserName() %> /></td>
			</tr>
			
			<tr>
				<td><h3>DataBasePassword</h3></td>
				<td><input type="text" name="DataBasePassword" value=<%=variables.getDataBasePassword() %> /></td>
			</tr>
			
			<tr>
				<td><h3>DataBaseName</h3></td>
				<td><input type="text" name="DataBaseName" value=<%=variables.getDataBaseName() %> /></td>
			</tr>
			<tr>
				<td><input type="submit" value="提交" style="width: 300px;" ></td>
				<td><input type="button" value="查看报警参数" onclick="javascript:submitme();" style="width: 300px;" ></td>
			</tr>
		</table>
		
	</form>
	<%}else{
		request.getRequestDispatcher("/index.jsp").forward(request, response);
	%>
	<%} %>
	<form name="AlarmServlet" action="AlarmServlet" method="get"></form>
</body>
</html>