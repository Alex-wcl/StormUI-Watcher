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
<style type="text/css">
	.text{
		width: 50px;
	}	
</style>
<script type="text/javascript">
	<%if(status != null && status.equals("ok")){%>
		alert("修改成功！");
	<%}%>
</script>
</head>
<body>
	<form action="AlarmServlet" method="post">
		<table>
			<tr>
				<td>是否报警(true/false)</td>
				<td><input type="text" name="isAvailable" value=<%=alarmProperties.isAvailable() %> class="text"/></td>
			</tr>
			<tr>
				<td>worker改变之后报警次数(次)</td>
				<td><input type="text" name="worker_alarm_times" value=<%=alarmProperties.getWorker_alarm_times() %> class="text" /></td>
			</tr>
			<tr>
				<td>worker改变之后报警时间间隔（秒）</td>
				<td><input type="text" name="worker_alarm_interval" value=<%=alarmProperties.getWorker_alarm_interval() %> class="text" /></td>
			</tr>
			
			<tr>
				<td>topology已死，报警次数（次）</td>
				<td><input type="text" name="topology_alarm_times" value=<%=alarmProperties.getTopology_alarm_times()%> class="text" /></td>
			</tr>
			<tr>
				<td>topology已死，报警时间间隔（秒）</td>
				<td><input type="text" name="topology_alarm_interval" value=<%=alarmProperties.getTopology_alarm_interval()%> class="text"/></td>
			</tr>
			<tr>
				<td>topology_worker阈值</td>
			</tr>
			
			<%if(topology != null){ 
		    Iterator iter = topology.entrySet().iterator();
		    while (iter.hasNext()) {
		      Map.Entry entry = (Map.Entry) iter.next(); 
		      String key = (String)entry.getKey();
		      String value = (String)entry.getValue();
			%>
			<tr>
				<td><input type="text" readonly="readonly" id="topologyid" name="topologyid" value=<%=key%> style="width: 250px;"/> :</td>
				<td><input type="text" id="worker" name="worker" value=<%=value%> class="text"/></td>
			</tr>
			<%} }%>
			
		</table>
		<input type="submit" value="提交"/>
	</form>
</body>
</html>