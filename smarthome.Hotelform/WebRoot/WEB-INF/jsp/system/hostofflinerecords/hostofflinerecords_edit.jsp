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
			if($("#DEVICE_CODE").val()==""){
			$("#DEVICE_CODE").tips({
				side:3,
	            msg:'请输入主机序列号',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#DEVICE_CODE").focus();
			return false;
		}
		if($("#HOST_NUMBER").val()==""){
			$("#HOST_NUMBER").tips({
				side:3,
	            msg:'请输入主机编号',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#HOST_NUMBER").focus();
			return false;
		}
		if($("#OFFLINETIME").val()==""){
			$("#OFFLINETIME").tips({
				side:3,
	            msg:'请输入离线时间',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#OFFLINETIME").focus();
			return false;
		}
		$("#Form").submit();
		$("#zhongxin").hide();
		$("#zhongxin2").show();
	}
	
</script>
	</head>
<body>
	<form action="hostofflinerecords/${msg }.do" name="Form" id="Form" method="post">
		<input type="hidden" name="HOSTOFFLINERECORDS_ID" id="HOSTOFFLINERECORDS_ID" value="${pd.HOSTOFFLINERECORDS_ID}"/>
		<div id="zhongxin">
		<table id="table_report" class="table table-striped table-bordered table-hover">
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">主机序列号:</td>
				<td><input type="text" name="DEVICE_CODE" id="DEVICE_CODE" value="${pd.DEVICE_CODE}" maxlength="32" placeholder="这里输入主机序列号" title="主机序列号"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">主机编号:</td>
				<td><input type="text" name="HOST_NUMBER" id="HOST_NUMBER" value="${pd.HOST_NUMBER}" maxlength="32" placeholder="这里输入主机编号" title="主机编号"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">离线时间:</td>
				<td><input type="text" name="OFFLINETIME" id="OFFLINETIME" value="${pd.OFFLINETIME}" maxlength="32" placeholder="这里输入离线时间" title="离线时间"/></td>
			</tr>
			<tr>
				<td style="text-align: center;" colspan="10">
					<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
					<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
				</td>
			</tr>
		</table>
		</div>
		
		<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green">提交中...</h4></div>
		
	</form>
	
	
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