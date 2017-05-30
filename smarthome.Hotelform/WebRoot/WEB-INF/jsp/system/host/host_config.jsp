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
		<link href="static/css/bootstrap-responsive.min.css" rel="stylesheet" />
		<link rel="stylesheet" href="static/css/font-awesome.min.css" />
		<!-- 下拉框 -->
		<link rel="stylesheet" href="static/css/chosen.css" />
		
		<link rel="stylesheet" href="static/css/ace.min.css" />
		<link rel="stylesheet" href="static/css/ace-responsive.min.css" />
		<link rel="stylesheet" href="static/css/ace-skins.min.css" />
		
		<link rel="stylesheet" href="static/css/datepicker.css" /><!-- 日期框 -->
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
			window.location.href='<%=basePath%>host/writeTerminal.do?DEVICE_CODE='+$("input#DEVICE_CODE").val()+'&NETWORK='+n+'&CHANNEL='+c;
			$("#zhongxin").hide();
			$("#zhongxin2").show();	
		}
		
		
		
		
	}
	
</script>
		
	</head>
<body>
	<div id="zhongxin">
	<form action="host/write.do" name="Form" id="Form" method="post">
		<!-- <div id="zhongxin">
		
		</div> -->
		
		<input type="hidden" name="DEVICE_CODE" id="DEVICE_CODE" value="${pd.deviceCode}"/>
		<table id="table_report" class="table table-striped table-bordered table-hover">
			<tr>
				<td class="center" style="padding-top: 13px;">网络号</td>
				<td class="center" style="padding-top: 13px;">信道号</td>
			</tr>
			
			<tr>
				<td class="center"><input type="text" name="NETWORK" id="NETWORK" value="${pd.network}" style="width: 50px; text-align: center;"/></td>
				<td class="center"><input type="text" name="CHANNEL" id="CHANNEL" value="${pd.channel}" style="width: 50px; text-align: center;"/></td>
			</tr>
			<tr>
				<td style="text-align: center;" colspan="10">
					<a class="btn btn-mini btn-primary" onclick="save();">写入</a>
					<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
				</td>
			</tr>
		</table>
		
	</form>
	<form action="host/configTerminal.do" name="Forms" id="Forms" method="post">
		<!-- <div id="zhongxin">
						
		</div> -->
		<input type="hidden" name="DEVICE_CODE" id="DEVICE_CODE" value="${pd.deviceCode}"/>
		<table id="table_report" class="table table-striped table-bordered table-hover">
			<tr >
				<td class="center" style="padding-top: 13px;">终端网络号</td>
				<td class="center" style="padding-top: 13px;">终端信道号</td>
			</tr>
			
			<tr>
				<td class="center"><input type="text" name="NETWORK" id="NETWORK1" style="width: 50px; text-align: center;"/></td>
				<td class="center"><input type="text" name="CHANNEL" id="CHANNEL1" style="width: 50px; text-align: center;"/></td>
			</tr>
			<tr>
				<td style="text-align: center;" colspan="10">
						<a class="btn btn-mini btn-primary" onclick="config();">终端配置</a>
						<a class="btn btn-mini btn-primary" onclick="configs();">写入配置</a>
				</td>
			</tr>
			<tr>
				<th style="text-align: center;color: blue;" colspan="10">
						网络号和信道是主机和终端配件之间成功通信的前提条件，我公司出厂的所有终端配件，网络号默认为8192，信道默认为25
				</th>
			</tr>
		</table>
			
			
			
		<!-- <div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green">提交中...</h4></div> -->
		
	</form>
	</div>
	<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green">提交中...</h4></div>
		<!-- 引入 -->
		<script type="text/javascript">window.jQuery || document.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
		<script src="static/js/bootstrap.min.js"></script>
		<script src="static/js/ace-elements.min.js"></script>
		<script src="static/js/ace.min.js"></script>
		<script type="text/javascript" src="static/js/chosen.jquery.min.js"></script><!-- 下拉框 -->
		<script type="text/javascript" src="static/js/bootstrap-datepicker.min.js"></script><!-- 日期框 -->
		<script type="text/javascript">
		$(top.hangge());
		$(function() {
			
			//单选框
			$(".chzn-select").chosen(); 
			$(".chzn-select-deselect").chosen({allow_single_deselect:true}); 
			
			//日期框
			$('.date-picker').datepicker();
			
		});
		
		</script>
</body>
</html>