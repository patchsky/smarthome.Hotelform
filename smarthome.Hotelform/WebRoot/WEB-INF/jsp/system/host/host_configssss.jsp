<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<base href="<%=basePath%>">
		<meta charset="utf-8" />
		<title></title>
		<meta name="description" content="overview & stats" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
		<link href="static/css/bootstrap.min.css" rel="stylesheet" />
		<script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>
		<script type="text/javascript" src="static/js/jquery.tips.js"></script>
		

	</head>
<body>
	
	<h1>${pd.network}</h1>
	<h1>${pd.channel}</h1>
	<div id="zhongxin">
		<form action="http://www.baidu.com"  method="post">
		<table border="1">
			<tr>
				<td>网络号</td>
				<td>ssss</td>
			</tr>
			<tr>
				<td>ssss</td>
				<td>ssss</td>
			</tr>
		</table>
		<input type="text" name="NETWORK" id="NETWORK" value="${pd.network}"/>
		<input type="submit"  value="写入"/>
	</form>	
	</div>
		<script type="text/javascript">
		$(top.hangge());
		
		
		</script>
</body>
</html>