<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="loginStyle/a.css" />
	
</head>
<body>
	<header>
	    <div class="text">
	        <a><em>加入收藏</em>|<span>联系我们</span></a>
	    </div>
	</header>
<div id="content">
    <img src="loginImg/jian.png" />
    <h1></h1>
    <form method="get">
        <input  id="usename" type="text" value="用户名"/><br/>
        <input  id="password" type="password" value="........"/>
        <div class="bt clear">
            <input class="check fl" type="checkbox" value=""/>
            <span class="fl">记住密码</span>
            <em class="fl">忘记密码？</em>
            <input id="submit" type="submit" value="登录" />
        </div>
    </form>
</div>
<div id="footer">
    <p><span></span></p>
</div>
</body>
</html>