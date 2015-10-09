<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
<h2>请登录! </h2>
	<form action="UserServlet" method="post">
		用户名：<input type="text" name="username" /><br>
		密码：<input type="password" name="password" /><br>
		<input type="submit" value="登录">
	</form>
	<a href="http://10.77.108.126:3000/">storm监控</a>
	<!-- //先避免登录，测试！<br>
	<a href="VariableServlet">查看/修改变量</a> -->
</body>
</html>
