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
		if($("#GUEST_ROOM").val()==""){
			$("#GUEST_ROOM").tips({
				side:3,
	            msg:'请输入客房号',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#GUEST_ROOM").focus();
			return false;
		}
		if($("#DEVICE_TYPE").val()==""){
			$("#DEVICE_TYPE").tips({
				side:3,
	            msg:'请输入设备类型',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#DEVICE_TYPE").focus();
			return false;
		}
		if($("#NICK_NAME").val()==""){
			$("#NICK_NAME").tips({
				side:3,
	            msg:'请输入设备名称',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#DEVICE_TYPE").focus();
			return false;
		}
		if($("#DEVICE_ADDRESS").val()==""){
			$("#DEVICE_ADDRESS").tips({
				side:3,
	            msg:'请输入设备地址码',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#DEVICE_ADDRESS").focus();
			return false;
		}
		$("#Form").submit();
		$("#zhongxin").hide();
		$("#zhongxin2").show();
	}
	
</script>
	</head>
<body>
	<form action="hostdevice/${msg}.do" name="Form" id="Form" method="post">
		
		<input type="hidden" name="HOSTDEVICE_ID" id="HOSTDEVICE_ID" value="${pd.HOSTDEVICE_ID}"/>
		<div id="zhongxin">
		<table id="table_report" class="table table-striped table-bordered table-hover">
			<tr>
				<td style="width:90px;text-align: right;padding-top: 13px;">主机序列号:</td>
				<td><input type="text" name="DEVICE_CODE" id="DEVICE_CODE" value="${pd.DEVICE_CODE}" maxlength="32" placeholder="这里输入主机序列号" title="DEVICE_CODE" readonly="readonly"/></td>
			</tr>
			<tr>
				<td style="width:90px;text-align: right;padding-top: 13px;">客房号:</td>
				<td><input type="text" name="GUEST_ROOM" id="GUEST_ROOM" value="${pd.GUEST_ROOM}" maxlength="32" placeholder="这里输入客房号" title="客房号" /></td>
			</tr>
			<tr>
				<td style="width:90px;text-align: right;padding-top: 13px;">设备类型:</td>
				<td>
					<c:if test="${pd.DEVICE_TYPE=='1'}">
						<img src="deviceImage/icon_shuangxiangdeng.png" style="height: 30px; width: 30px" />
						<input type="hidden" name="DEVICE_TYPE" id="DEVICE_TYPE" value="${pd.DEVICE_TYPE}" maxlength="32" placeholder="这里输入设备类型" title="设备类型" readonly="readonly"/>
					</c:if>
					<c:if test="${pd.DEVICE_TYPE=='2'}">
						<img src="deviceImage/icon_chunalian.png" style="height: 30px;width: 30px"/>
						<input type="hidden" name="DEVICE_TYPE" id="DEVICE_TYPE" value="${pd.DEVICE_TYPE}" maxlength="32" placeholder="这里输入设备类型" title="设备类型" readonly="readonly"/>
					</c:if>
					<c:if test="${pd.DEVICE_TYPE=='98'}">
						<img src="deviceImage/icon_dianshi.png" style="height: 25px;width: 40px"/>
						<input type="hidden" name="DEVICE_TYPE" id="DEVICE_TYPE" value="${pd.DEVICE_TYPE}" maxlength="32" placeholder="这里输入设备类型" title="设备类型" readonly="readonly"/>
					</c:if>
					
					<c:if test="${pd.DEVICE_TYPE=='99'}">
						<img src="deviceImage/icon_guaji.png" style="height: 25px;width: 40px"/>
						<input type="hidden" name="DEVICE_TYPE" id="DEVICE_TYPE" value="${pd.DEVICE_TYPE}" maxlength="32" placeholder="这里输入设备类型" title="设备类型" readonly="readonly"/>
					</c:if>
					
					<c:if test="${pd.DEVICE_TYPE=='1114'}">
						<img src="deviceImage/icon_kaiguandeng.png" style="height: 30px;width: 30px"/>
						<input type="hidden" name="DEVICE_TYPE" id="DEVICE_TYPE" value="${pd.DEVICE_TYPE}" maxlength="32" placeholder="这里输入设备类型" title="设备类型" readonly="readonly"/>
					</c:if>
					
					<c:if test="${pd.DEVICE_TYPE=='3114'}">
						<img src="deviceImage/icon_chunalian.png" style="height: 30px;width: 30px"/>
						<input type="hidden" name="DEVICE_TYPE" id="DEVICE_TYPE" value="${pd.DEVICE_TYPE}" maxlength="32" placeholder="这里输入设备类型" title="设备类型" readonly="readonly"/>
					</c:if>
					
				</td>
			</tr>
			<tr>
				<td style="width:90px;text-align: right;padding-top: 13px;">设备名称:</td>
				<td><input type="text" name="NICK_NAME" id="NICK_NAME" value="${pd.NICK_NAME}" maxlength="32" placeholder="这里输入设备名称" title="设备名称"/></td>
			</tr>
			<tr>
				<td style="width:90px;text-align: right;padding-top: 13px;">设备地址码:</td>
				<td><input type="text" name="DEVICE_ADDRESS" id="DEVICE_ADDRESS" value="${pd.DEVICE_ADDRESS}" maxlength="32" placeholder="这里输入设备地址码" title="设备地址码" readonly="readonly"/></td>
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