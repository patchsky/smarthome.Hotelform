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
		if($("#GUEST_ROOM").val()==""){
			$("#GUEST_ROOM").tips({
				side:3,
	            msg:'请选择客房',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#GUEST_ROOM").focus();
			return false;
		}
		if($("#NAME").val()==""){
			$("#NAME").tips({
				side:3,
	            msg:'请输入情景模式昵称',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#NAME").focus();
			return false;
		}
		if($("#ICO").val()==""){
			$("#ICO").tips({
				side:3,
	            msg:'请选择图标',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#ICO").focus();
			return false;
		}
		$("#Form").submit();
		$("#zhongxin").hide();
		$("#zhongxin2").show();
	}
	
</script>

<style type="text/css">
		td{width: 10%;}
	</style>
	</head>
<body>
	<script>
function a(o){
img1.src="../"+o.value;
}

</script>
	<form action="model/${msg }.do" name="Form" id="Form" method="post">
		<input type="hidden" name="MODEL_ID" id="MODEL_ID" value="${pd.MODEL_ID}"/>
		<div id="zhongxin">
		<table id="table_report" class="table table-striped table-bordered table-hover">
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">客房:</td>
				<td>
					<%-- <input type="text" name="GUEST_ROOM" id="GUEST_ROOM" value="${pd.GUEST_ROOM}" maxlength="32" placeholder="这里输入GUEST_ROOM" title="GUEST_ROOM"/> --%>
					<c:choose>
						<c:when test="${not empty allUser}">
						
							<c:if test="${msg=='save'}">
								<select name="GUEST_ROOM" class="form-control checkacc" id="GUEST_ROOM">
									<c:forEach items="${allUser}" var="var" varStatus="vs">
										<option value="${var.USERNAME}">${var.USERNAME}</option>
											
									</c:forEach>
								</select>
							</c:if>
							<c:if test="${msg=='edit'}">
								<select name="GUEST_ROOM" disabled="disabled" class="form-control checkacc" id="GUEST_ROOM" >
									<c:forEach items="${allUser}" var="var" varStatus="vs">
										<option value="${var.USERNAME}">${var.USERNAME}</option>
											
									</c:forEach>
								</select>
							</c:if>
						</c:when>
						<c:otherwise>
							<select name="GUEST_ROOM" class="form-control checkacc" id="GUEST_ROOM">
								<option value="">没有用户</option>
							</select>
						</c:otherwise>
					</c:choose>
					
				</td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">情景模式昵称:</td>
				<td><input type="text" name="NAME" id="NAME" value="${pd.NAME}" maxlength="32" placeholder="情景模式昵称" title="NAME"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">图标:</td>
				<td>
					<select name="ICO" class="form-control checkacc" id="ICO" onchange=a(this)>
								<option value="contextualModel/goHome.png">图标1</option>
								<option value="contextualModel/leaveHome.png">图标2</option>
								<option value="contextualModel/dining.png">图标3</option>
					</select>
					<img id="img1" src="">
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