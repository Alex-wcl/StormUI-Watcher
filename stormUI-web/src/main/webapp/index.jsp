<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
     <%
    	String msg = (String)request.getAttribute("msg");
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
    <head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
        <title>Custom Login Form Styling</title>
        <meta name="keywords" content="css3, login, form, custom, input, submit, button, html5, placeholder" />
        <meta name="author" content="Codrops" />
        <link rel="shortcut icon" href="../favicon.ico"> 
        <link rel="stylesheet" type="text/css" href="css/style.css" />
		<script src="js/modernizr.custom.63321.js"></script>
		<style>	
			body {
				background: #7f9b4e url(images/bg2.jpg) no-repeat center top;
				-webkit-background-size: cover;
				-moz-background-size: cover;
				background-size: cover;
			}
			.container > header h1,
			.container > header h2 {
				color: #fff;
				text-shadow: 0 1px 1px rgba(0,0,0,0.7);
			}
		</style>
		<script type="text/javascript">
			<%if(msg != null && msg.equals("username_error")){%>
				alert("用户名或密码错误！");
			<%}%> 
		</script>
    </head>
   
    <body>
        <div class="container">
			<div  style="margin-top:15%;">
				<section class="main">
					<form class="form-3" action="UserServlet" method="post">
					    <h1>请登录</h1>
					    <p>
					        <input type="text" name="username" placeholder="Username or email" required>
					    </p>
					    <p>
					        <input type="password" name='password' placeholder="Password" required> 
					    </p>
	
					    <p>
					        <input type="submit" name="submit" value="Continue">
					    </p>       
					</form>
				</section>
			</div>
        </div>
    </body>
</html>

