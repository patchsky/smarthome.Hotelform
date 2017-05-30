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
<title>${pd.SYSNAME}</title>
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=yes">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" >
<link rel="stylesheet" href="<%=basePath %>loginCss/style.css">

</head>
<body>
    <div class="cont" >
		 <div class="demo">
			  <div class="login" id="loginbox">
			  		
				  <div class="login__check"><a href="<%=basePath %>"><img src="../loginCss/(zhaoxi.net).png" alt="Logo" style="width: 15rem"/></a></div>
			      <div class="login__form">
			        <div class="login__row">
			          <svg class="login__icon name svg-icon" viewBox="0 0 20 20">
			            <path d="M0,20 a10,8 0 0,1 20,0z M10,0 a4,4 0 0,1 0,8 a4,4 0 0,1 0,-8" />
			          </svg>
			          <input type="text" class="login__input name" placeholder="Username" id="usename" value="${pd.PHONE}" />
			        </div>
			        <div class="login__row">
			          <svg class="login__icon pass svg-icon" viewBox="0 0 20 20">
			            <path d="M0,20 20,20 20,8 0,8z M10,13 10,16z M4,8 a6,8 0 0,1 12,0" />
			          </svg>
			          <input type="password" class="login__input pass" placeholder="Password" id="password" value="${pd.PWD}"/>
			        </div>
			         <input type="hidden" class="login__input pass" placeholder="Password" id="passwordss" value="${pd.PWD}"/>
			        <button type="button" class="login__submit" onclick="check();">登&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;录</button>
			        <p class="login__signup">Copyright © FH 2016</p>
			        
			        <!-- <p class="login__signups">杭州空灵智能科技有限公司</p> -->
			      </div>
		     </div>
	  	 </div>
	</div>
   	
	<script src="<%=basePath %>static/js/jquery-1.7.2.js"></script>
	<script type="text/javascript" src="<%=basePath %>static/js/jquery.tips.js"></script>
	<script type="text/javascript" src="<%=basePath %>static/js/jquery.cookie.js"></script>
</body>

<script type="text/javascript">

	
	
	function check() {
		
		if ($("#usename").val() == "") {
			$("#usename").tips({
				side : 1,
				msg : "请输入手机号",
				bg : '#FF5080',
				time : 1
			});
				
			return false;
		}else if ($("#password").val() == "") {
			$("#password").tips({
				side : 1,
				msg : "请输入密码",
				bg : '#FF5080',
				time : 1
			});
			
			return false;
		}else{
			var loginname = $("#usename").val();
			var password = $("#password").val();
			$.ajax({
				type: "POST",
				url: '<%=basePath%>weixin/login_login.do?',
		    	data: {'USERNAME' : loginname,'PWD' : password},
				dataType:'json',
				cache: false,
				success: function(data){
					if("success" == data.result){
						
						saveCookie();
						window.location.href="<%=basePath%>main/index";
					}else if("usererror" == data.result){
						$("#usename").tips({
							side : 1,
							msg : "手机号或密码有误",
							bg : '#FF5080',
							time : 1
						});
						$("#usename").focus();
					}else if("sdsadas" == data.result){
						$("#usename").tips({
							side : 1,
							msg : "该房间不再您入住时间内,请联系前台",
							bg : '#FF5080',
							time : 3
						});
						$("#usename").focus();
					
					}else{
						alert("缺少参数");
					}
				}
			});
		} 
	}
	
	
	function saveCookie() {
		if ($("#saveid").attr("checked")) {
			$.cookie('loginname', $("#usename").val(), {
				expires : 7
			});
			$.cookie('password', $("#password").val(), {
				expires : 7
			});
		}
	}
</script>
</html>