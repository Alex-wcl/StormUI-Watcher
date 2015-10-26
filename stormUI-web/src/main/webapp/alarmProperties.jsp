<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@page import="com.weibo.stormUI.model.AlarmProperties" %>
    <%@page import="java.util.*" %>
<% 
	AlarmProperties alarmProperties = (AlarmProperties)request.getAttribute("alarmProperties");
	String status = (String)request.getAttribute("status");
	Map topology = null;
	if(alarmProperties != null){
	    topology = alarmProperties.getTopologyWorker();
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>show alarm properties</title>
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
	<form class="form-4" action="AlarmServlet" method="post">
		<table>
			<caption align="top" style="margin-bottom: 15px;"><h1>报警参数</h1></caption>
			<tr>
				<td><h3>是否报警(true/false)</h3></td>
				<td><input type="text" name="isAvailable" value=<%=alarmProperties.isAvailable() %> class="text"/></td>
			</tr>
			<tr>
				<td><h3>worker改变之后报警次数(次)</h3></td>
				<td><input type="text" name="worker_alarm_times" value=<%=alarmProperties.getWorker_alarm_times() %> class="text" /></td>
			</tr>
			<tr>
				<td><h3>worker改变之后报警时间间隔（秒）</h3></td>
				<td><input type="text" name="worker_alarm_interval" value=<%=alarmProperties.getWorker_alarm_interval() %> class="text" /></td>
			</tr>
			
			<tr>
				<td><h3>topology已死，报警次数（次）</h3></td>
				<td><input type="text" name="topology_alarm_times" value=<%=alarmProperties.getTopology_alarm_times()%> class="text" /></td>
			</tr>
			<tr>
				<td><h3>topology已死，报警时间间隔（秒）</h3></td>
				<td><input type="text" name="topology_alarm_interval" value=<%=alarmProperties.getTopology_alarm_interval()%> class="text"/></td>
			</tr>
			<tr>
				<td><h3>topology_worker阈值</h3></td>
			</tr>
			
			<%if(topology != null){ 
		    Iterator iter = topology.entrySet().iterator();
		    while (iter.hasNext()) {
		      Map.Entry entry = (Map.Entry) iter.next(); 
		      String key = (String)entry.getKey();
		      String value = (String)entry.getValue();
			%>
			<tr>
				<td><input type="text" readonly="readonly" id="topologyid" name="topologyid" value=<%=key%> style="width: 320px;"/> </td>
				<td><input type="text" id="worker" name="worker" value=<%=value%> class="text"/></td>
			</tr>
			<%} }%>
			<tr>
				<td><input type="button" value="返回" onclick="javascript:history.go(-1);" style="width: 300px;"/></td>
                <td><input type="submit" value="提交" style="width: 300px;"/></td>
            </tr>
		</table>
		
	</form>
	<%}else{
		request.getRequestDispatcher("/index.jsp").forward(request, response);
	%>
	<%} %>
</body>
</html>