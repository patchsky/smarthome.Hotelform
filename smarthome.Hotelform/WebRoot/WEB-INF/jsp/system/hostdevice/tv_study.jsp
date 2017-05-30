<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
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
<link href="static/css/bootstrap-responsive.min.css" rel="stylesheet" />
<link rel="stylesheet" href="static/css/font-awesome.min.css" />
<!-- 下拉框 -->
<link rel="stylesheet" href="static/css/chosen.css" />

<link rel="stylesheet" href="static/css/ace.min.css" />
<link rel="stylesheet" href="static/css/ace-responsive.min.css" />
<link rel="stylesheet" href="static/css/ace-skins.min.css" />

<link rel="stylesheet" href="static/css/datepicker.css" />
<!-- 日期框 -->
<script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>
<script type="text/javascript" src="static/js/jquery.tips.js"></script>
<script type="text/javascript">
	
	
	//保存
	function save(){
		$("#Form").submit();
		$("#zhongxin").hide();
		$("#zhongxin2").show();
	}
	
	//保存
	function config(){
		$("#Forms").submit();
		$("#zhongxin").hide();
		$("#zhongxin2").show();
	}
	
	//保存
	function configs(){
		if($("input#NETWORK1").val()==""||$("input#NETWORK1").val()==null){
			$("#NETWORK1").tips({
				side:3,
	            msg:'请输入终端网络号',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#NETWORK1").focus();
			return false;
		}else if($("input#CHANNEL1").val()==""||$("input#CHANNEL1").val()==null){
			$("#CHANNEL1").tips({
				side:3,
	            msg:'请输入终端信道号',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#CHANNEL1").focus();
			return false;
		}else{
			var n;
			if($("input#NETWORK1").val()==""||$("input#NETWORK1").val()==null){
				n = 1;
			}else{
				n = $("input#NETWORK1").val();
			}
			var c;
			if($("input#CHANNEL1").val()==""||$("input#CHANNEL1").val()==null){
				c = 1;
			}else{
				c = $("input#CHANNEL1").val();
			}
			window.location.href='<%=basePath%>
	host/writeTerminal.do?DEVICE_CODE='
					+ $("input#DEVICE_CODE").val()
					+ '&NETWORK='
					+ n
					+ '&CHANNEL=' + c;
			$("#zhongxin").hide();
			$("#zhongxin2").show();
		}

	}
</script>
<style>


#page_container {
	border: 0px;
	width: 250px;
	height: 250px;
	background-image: url('imgae/1.png');
}

#banner {
	width: 98px;
	height: 68px;
	margin-left: 76px;
	padding-top: 8px;
}

#left {
	width: 90px;
	height: 60px;
	margin-left: 7px;
	margin-top: 3px;
	float: left;
}

#center {
	width: 51px;
	margin-top: 23px;
	float: left;
}

#right {
	float: right;
	width: 93px;
	height: 60px;
	margin-top: 3px;
	margin-right: 5px;
}

#bottom {
	clear: both;
	width: 98px;
	height: 80px;
	margin-left: 76px;
	margin-top: 73px;
}

#zhong {
	background-color: gray;
	
}
#zhong1 {
	background-color: gray;
	height: 90px;
}
</style>
</head>
<body style="background-color: gray;">

	<div id="zhong">
		<div>

			<a
				href="<%=basePath%>hostdevice/tv_study.do?DEVICE_ADDRESS=${pd.DEVICE_ADDRESS}&GUEST_ROOM=${pd.GUEST_ROOM}&DEVICE_CODE=${pd.DEVICE_CODE}&VALUES=1">
				<img src="uploadFiles/close_pressed.png"
				style="height: 60px; width: 60px;" />
			</a> <a
				href="<%=basePath%>hostdevice/tv_study.do?DEVICE_ADDRESS=${pd.DEVICE_ADDRESS}&GUEST_ROOM=${pd.GUEST_ROOM}&DEVICE_CODE=${pd.DEVICE_CODE}&VALUES=2">
				<img src="uploadFiles/close.png"
				style="height: height: 60px; width: 60px; padding-left: 10px; float: right;" />
			</a>

		</div>

		<div id="page_container">
			<div id="banner">
				<a
					href="<%=basePath%>hostdevice/tv_study.do?DEVICE_ADDRESS=${pd.DEVICE_ADDRESS}&GUEST_ROOM=${pd.GUEST_ROOM}&DEVICE_CODE=${pd.DEVICE_CODE}&VALUES=3">
					<img src="imgae/on.png"
					style="width: 99px; height: 92px; display: table-cell; text-align: right; vertical-align: middle; clear: both;" />
				</a>
			</div>
			<div id="left">
				<a
					href="<%=basePath%>hostdevice/tv_study.do?DEVICE_ADDRESS=${pd.DEVICE_ADDRESS}&GUEST_ROOM=${pd.GUEST_ROOM}&DEVICE_CODE=${pd.DEVICE_CODE}&VALUES=5">
					<img src="imgae/left.png"
					style="width: 99px; height: 92px; float: left;" />
				</a>
			</div>
			<div id="center">
				<a href="<%=basePath%>hostdevice/tv_study.do?DEVICE_ADDRESS=${pd.DEVICE_ADDRESS}&GUEST_ROOM=${pd.GUEST_ROOM}&DEVICE_CODE=${pd.DEVICE_CODE}&VALUES=7">
					<img src="imgae/in_center.png" style="width: 100px; height: 50px; padding-left: 3px;" />
				</a>
			</div>
			<div id="right">
				<a
					href="<%=basePath%>hostdevice/tv_study.do?DEVICE_ADDRESS=${pd.DEVICE_ADDRESS}&GUEST_ROOM=${pd.GUEST_ROOM}&DEVICE_CODE=${pd.DEVICE_CODE}&VALUES=6">
					<img src="imgae/right.png"
					style="width: 99px; height: 92px; float: right;" />
				</a>
			</div>
			<div id="bottom">
				<a
					href="<%=basePath%>hostdevice/tv_study.do?DEVICE_ADDRESS=${pd.DEVICE_ADDRESS}&GUEST_ROOM=${pd.GUEST_ROOM}&DEVICE_CODE=${pd.DEVICE_CODE}&VALUES=4">
					<img src="imgae/Under.png"
					style="width: 99px; height: 92px; display: table-cell; text-align: right; vertical-align: middle; float: inherit;" />
				</a>
			</div>
			
		</div>
	</div>
	<div id="zhong1">
		<%-- ${pd.DEVICE_ADDRESS} --%>
		<a href="<%=basePath%>hostdevice/tv_study.do?DEVICE_ADDRESS=${pd.DEVICE_ADDRESS}&GUEST_ROOM=${pd.GUEST_ROOM}&DEVICE_CODE=${pd.DEVICE_CODE}&VALUES=8">
			<img src="imgae/menu.png" style="width: 80px; height: 30px; float: left;margin-top: 15px;"  />
		</a>
		
		<a href="<%=basePath%>hostdevice/tv_study.do?DEVICE_ADDRESS=${pd.DEVICE_ADDRESS}&GUEST_ROOM=${pd.GUEST_ROOM}&DEVICE_CODE=${pd.DEVICE_CODE}&VALUES=9" >
			<img src="imgae/home.png" style="width: 80px; height: 30px; margin-left: 5px;margin-top: 15px;" />
		</a>
			
		
		<a href="<%=basePath%>hostdevice/tv_study.do?DEVICE_ADDRESS=${pd.DEVICE_ADDRESS}&GUEST_ROOM=${pd.GUEST_ROOM}&DEVICE_CODE=${pd.DEVICE_CODE}&VALUES=10" style="margin-left: 2px;">
			<img src="imgae/return.png" style="width: 80px; height: 30px; float:right; margin-top: 15px;"  />
		</a>
	</div>
	<div id="zhongxin" ></div>
	<!-- 引入 -->
	<script type="text/javascript">
		window.jQuery
				|| document
						.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");
	</script>
	<script src="static/js/bootstrap.min.js"></script>
	<script src="static/js/ace-elements.min.js"></script>
	<script src="static/js/ace.min.js"></script>
	<script type="text/javascript" src="static/js/chosen.jquery.min.js"></script>
	<!-- 下拉框 -->
	<script type="text/javascript"
		src="static/js/bootstrap-datepicker.min.js"></script>
	<!-- 日期框 -->
	<script type="text/javascript">
		$(top.hangge());
		$(function() {

			//单选框
			$(".chzn-select").chosen();
			$(".chzn-select-deselect").chosen({
				allow_single_deselect : true
			});

			//日期框
			$('.date-picker').datepicker();

		});
	</script>
</body>
</html>