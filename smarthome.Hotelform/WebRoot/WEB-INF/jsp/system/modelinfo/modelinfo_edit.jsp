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
			if($("#MODEL_ID").val()==""){
			$("#MODEL_ID").tips({
				side:3,
	            msg:'请输入MODEL_ID',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#MODEL_ID").focus();
			return false;
		}
		if($("#DEVICE_ADDRESS").val()==""){
			$("#DEVICE_ADDRESS").tips({
				side:3,
	            msg:'请输入DEVICE_ADDRESS',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#DEVICE_ADDRESS").focus();
			return false;
		}
		if($("#DEVICE_TYPE").val()==""){
			$("#DEVICE_TYPE").tips({
				side:3,
	            msg:'请输入DEVICE_TYPE',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#DEVICE_TYPE").focus();
			return false;
		}
		if($("#CONTROL_COMMAND").val()==""){
			$("#CONTROL_COMMAND").tips({
				side:3,
	            msg:'请输入CONTROL_COMMAND',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#CONTROL_COMMAND").focus();
			return false;
		}
		if($("#DELAY_VALUES").val()==""){
			$("#DELAY_VALUES").tips({
				side:3,
	            msg:'请输入DELAY_VALUES',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#DELAY_VALUES").focus();
			return false;
		}
		$("#Form").submit();
		$("#zhongxin").hide();
		$("#zhongxin2").show();
	}
	
</script>
	</head>
<body>
	<form action="modelinfo/${msg }.do" name="Form" id="Form" method="post">
		<input type="hidden" name="MODELINFO_ID" id="MODELINFO_ID" value="${pd.MODELINFO_ID}"/>
		<input type="hidden" name="MODEL_ID" id="MODEL_ID" value="${pd.MODEL_ID}" maxlength="32" placeholder="这里输入MODEL_ID" title="MODEL_ID"/>
		<input type="hidden" name="DEVICE_ADDRESS" id="DEVICE_ADDRESS" value="${pd.DEVICE_ADDRESS}" maxlength="32" placeholder="这里输入DEVICE_ADDRESS" title="DEVICE_ADDRESS"/>
		<input type="hidden" name="DEVICE_TYPE" id="DEVICE_TYPE" value="${pd.DEVICE_TYPE}" maxlength="32" placeholder="这里输入DEVICE_TYPE" title="DEVICE_TYPE"/>
		<div id="zhongxin">
		<table id="table_report" class="table table-striped table-bordered table-hover">
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">执行动作:</td>
				<td>
					<select name="CONTROL_COMMAND" class="form-control checkacc" id="CONTROL_COMMAND">
								<option value="100">开</option>
								<option value="0">关</option>
								<option value="50">停</option>
								<option value="1">电视开</option>		
								<option value="2">电视关</option>		
								<option value="11">空调开</option>		
								<option value="12">空调关</option>
								<option value="13">制冷16°</option>
								<option value="14">制冷17°</option>
								<option value="15">制冷18°</option>
								<option value="16">制冷18°</option>
								<option value="17">制冷20°</option>
								<option value="18">制冷21°</option>
								<option value="19">制冷22°</option>
								<option value="20">制冷23°</option>
								<option value="21">制冷24°</option>
								<option value="22">制冷25°</option>
								<option value="23">制冷26°</option>
								<option value="24">制冷27°</option>
								<option value="25">制冷28°</option>
								<option value="26">制冷29°</option>
								<option value="27">制冷30°</option>
								<option value="28">制热16°</option>
								<option value="29">制热17°</option>
								<option value="30">制热18°</option>
								<option value="31">制热19°</option>
								<option value="32">制热20°</option>
								<option value="33">制热21°</option>
								<option value="34">制热22°</option>
								<option value="35">制热23°</option>
								<option value="36">制热24°</option>
								<option value="37">制热25°</option>
								<option value="38">制热26°</option>
								<option value="39">制热27°</option>
								<option value="40">制热28°</option>
								<option value="41">制热29°</option>
								<option value="42">制热30°</option>
								
					</select>
				</td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">执行间隔:</td>
				<td>
					<select name="DELAY_VALUES" class="form-control checkacc" id="DELAY_VALUES">
								<option value="300">立即执行</option>
								<option value="500">延迟0.5秒</option>
								<option value="1000">延迟1秒</option>
								<option value="2000">延迟2秒</option>			
								<option value="3000">延迟3秒</option>			
								<option value="4000">延迟4秒</option>			
								<option value="10000">延迟10秒</option>			
								<option value="15000">延迟15秒</option>			
								<option value="30000">延迟30秒</option>			
					</select>
				</td>
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