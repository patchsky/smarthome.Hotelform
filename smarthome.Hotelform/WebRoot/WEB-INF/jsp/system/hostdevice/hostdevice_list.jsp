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
<!-- jsp文件头和头部 -->
<%@ include file="../../system/admin/top.jsp"%>
<meta
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=1;"
	name="viewport" />
<link rel="stylesheet" href="css/bootstrap.min.css">
  <link rel="stylesheet" href="css/custom.css">
  <link rel="stylesheet" href="css/iosOverlay.css">
<link rel="stylesheet" href="css/style.css" media="screen" type="text/css" />
  <link rel="stylesheet" href="css/prettify.css">
  <script src="js/modernizr-2.0.6.min.js"></script>
  
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
	
	<style type="text/css">
		.img:hover{border-radius:50%}
		.img{border-radius:50%}
		#Methods1 dl{margin:20px 0 0 20px; float:left; }  
		#Methods1 dt{width:50px; margin:10px; height:50px; background:#FFF;}  
		#Methods1 dd{width:50px; margin:0 0 10px 10px; height:30px;  background:#FFFFFF; text-align:center;} 
	</style>
	<link rel="stylesheet" href="style.css">
	<link rel="stylesheet" type="text/css" href="csss/style.css" media="screen" />
</head>
<body style="width: 100%; height: 100%">

 

  <script src="js/jquery.min.js"></script>
  <script src="js/iosOverlay.js"></script>
  <script src="js/spin.min.js"></script>
  <script src="js/prettify.js"></script>
  <script src="js/custom.js"></script>
	<div class="container-fluid" id="main-container">


		<div id="page-content" class="clearfix">

			<div class="row-fluid">

				<div class="row-fluid">

					<!-- 检索  -->
					<!-- <form action="hostdevice/list.do" method="post" name="Form" id="Form"> -->
					<form action="hostdevice/list.do" method="post" name="Form"
						id="Form">
						
						
						<section class="slider-container">
								<ul id="slider" class="slider-wrapper">
									<li class="slide-current">
										<img src="images/1.jpg" alt="Slider Imagen 1">
										
									</li>
							
									<li>
										<img src="images/2.jpg" alt="Slider Imagen 1">
									</li>
							
									<li>
										<img src="images/3.jpg" alt="Slider Imagen 1">
									</li>
							
									<li>
										<img src="images/4.jpg" alt="Slider Imagen 1">
									</li>
								</ul>
						</section>



						<script src="lunb/jquery.min.js"></script>
						<script src="lunb/main.js"></script>
					
						
						<span style="display: block; font-size: 2rem">当前房间:${pd.GUEST_ROOM}</span>
						<!-- 开始循环 -->
						<c:choose>
							<c:when test="${not empty model}">
								<c:forEach items="${model}" var="var" varStatus="vs">
									<div id="Methods1">
											<dl>  
  													<dt><img src="${var.ICO}" class="img" id="loading" style="width:50px; height:50px;" onclick="modelCommand('${var.MODEL_ID}');"/></dt>  
										    	<dd>${var.NAME}</dd>  
										    </dl>  
										</div>
								</c:forEach>
								
							</c:when>
							<c:otherwise>
								该客房没有情景模式列表
							</c:otherwise>
						</c:choose>
						<table id="table_report"
							class="table table-striped table-bordered table-hover"
							style="margin-top: 10px; ">

							<thead>
								<tr>
									<th style="width: 15%;text-align: center;">类型</th>
									<th style="width: 23%;text-align: center;">名称</th>
									<!-- <th style="width: 15%;text-align: center;">状态</th> -->
									<th style="width: 40%;text-align: center;">操作</th>

								</tr>
							</thead>

							<tbody>

								<!-- 开始循环 -->
								<c:choose>
									<c:when test="${not empty varList}">
										<c:forEach items="${varList}" var="var" varStatus="vs">
												<tr>
													<%-- <td class='center'>${vs.index+1}</td> --%>
													<!-- 客房号-->
													<%-- <td class='center'>${var.GUEST_ROOM}</td> --%>
													<!-- 设备类型 -->
													<td  style="text-align: center;" >
														<c:if test="${var.DEVICE_TYPE=='1'}">
															<img src="deviceImage/icon_shuangxiangdeng.png" style="height: 40px; width: 40px" />
														</c:if> 
														<c:if test="${var.DEVICE_TYPE=='2'}">
															<img src="deviceImage/icon_chunalian.png" style="height: height: 40px; width: 40px" />
														</c:if>
														<c:if test="${var.DEVICE_TYPE=='98'}">
															<img src="deviceImage/icon_dianshi.png" style="height: 40px; width: 40px" />
														</c:if>
														<c:if test="${var.DEVICE_TYPE=='99'}">
															<img src="deviceImage/icon_guaji.png" style="height: 40px; width: 40px" />
														</c:if> 
														<c:if test="${var.DEVICE_TYPE=='1114'}">
															<img src="deviceImage/icon_kaiguandeng.png" style="height: 40px; width: 40px" />
														</c:if>
														<c:if test="${var.DEVICE_TYPE=='3114'}">
															<img src="deviceImage/icon_chunalian.png" style="height: 40px; width: 40px" />
														</c:if>
														
													</td>
													<!-- 设备名称 -->
													<td  style="vertical-align:middle;text-align: center;">${var.NICK_NAME}</td>
													<!-- 设备状态 -->
													<%-- <td  style="text-align: center;">
														<c:if test="${var.STATE==0}">关</c:if>
														<c:if test="${var.STATE==1}">开</c:if>
														<c:if test="${var.STATE=='2'}">无</c:if>
													</td> --%>
													<!-- 设备操作-->
													<td  style="vertical-align:middle;text-align: center;">
														<c:if test="${var.DEVICE_TYPE=='1'}">
															
															<input type="checkbox" id="${var.DEVICE_ADDRESS}" class="ace-switch ace-switch-3" onclick="zigbeeOpen('${var.DEVICE_ADDRESS}','${var.GUEST_ROOM}','${var.DEVICE_CODE}','${var.DEVICE_TYPE}');" <c:if test="${var.STATE==1}">checked="checked"</c:if>/><span class="lbl"></span>
															
														</c:if> 
														<%-- <c:if test="${var.DEVICE_TYPE=='2'}">
															<input id="loading" type="range" name="range" min="0" max="100"
																	step="1" value="" style="width: 96px;" />
																<output name="result"> </output>
															<script type="text/javascript">
																	
																        (function() {
																        	
																    var f = document.forms[0],
																        range = f['range'],
																        result = f['result'],
																        cachedRangeValue = localStorage.rangeValue ? localStorage.rangeValue : 100; 
																    // 设置初始值
																    // 无论是否本地存储了，都设置值为5
																    range.value = cachedRangeValue;
																    result.value = cachedRangeValue;
																
																    // 当用户选择了个值，更新本地存储
																    range.addEventListener("mouseup", function() {
																        /* alert("你选择的值是：" + range.value + ". 我现在正在用本地存储保存此值。在现代浏览器上刷新并检测。"); */
																        
																        window.location.href='<%=basePath%>hostdevice/command.do?command='+range.value+'&deviceAddress='+'${var.DEVICE_ADDRESS}'+'&GUEST_ROOM='+'${var.GUEST_ROOM}'+'&deviceCode='+'${var.DEVICE_CODE}'+'&commandType=2';
																        localStorage ? (localStorage.rangeValue = range.value) : alert("数据保存到了数据库或是其他什么地方。");
																    }, false);
																
																    // 滑动时显示选择的值
																    range.addEventListener("change", function() {
																        result.value = range.value;
																        var s = range.value;
																        
																        window.location.href='<%=basePath%>hostdevice/command.do?command='+range.value+'&deviceAddress='+'${var.DEVICE_ADDRESS}'+'&GUEST_ROOM='+'${var.GUEST_ROOM}'+'&deviceCode='+'${var.DEVICE_CODE}'+'&commandType=2';
																        window.location.href='<%=basePath%>hostdevice/command.do?deviceAddress=${var.DEVICE_ADDRESS}&GUEST_ROOM=${var.GUEST_ROOM}&deviceCode=${var.DEVICE_CODE}&command=100&commandType=2';
																    }, false);
																
																})();
																        
																        
																
    														</script>

														</c:if> --%>
														<c:if test="${var.DEVICE_TYPE=='98'}">
															<c:if test="${QX.add == 1}">
																<a style="cursor:pointer;display: block;" title="编辑" onclick="tvStudy('${var.DEVICE_ADDRESS}','${var.GUEST_ROOM}','${var.DEVICE_CODE}');" class="tooltip-success" data-rel="tooltip" title="" data-placement="left">学习界面</a>
																<a style="cursor:pointer;display: block;" title="编辑" onclick="tvCommand('${var.DEVICE_ADDRESS}','${var.GUEST_ROOM}','${var.DEVICE_CODE}');" class="tooltip-success" data-rel="tooltip" title="" data-placement="left">控制界面</a>
															</c:if>
															<c:if test="${QX.add != 1}">
																<a style="cursor:pointer;display: block;" title="编辑" onclick="tvCommand('${var.DEVICE_ADDRESS}','${var.GUEST_ROOM}','${var.DEVICE_CODE}');" class="tooltip-success" data-rel="tooltip" title="" data-placement="left">控制界面</a>
															</c:if>
														</c:if>
														
														<c:if test="${var.DEVICE_TYPE=='99'}">
															<c:if test="${QX.add == 1}">
																<a style="cursor:pointer;display: block;" title="编辑" onclick="airStudy('${var.DEVICE_ADDRESS}','${var.GUEST_ROOM}','${var.DEVICE_CODE}');" class="tooltip-success" data-rel="tooltip" title="" data-placement="left">学习界面</a>
																<a style="cursor:pointer;display: block;" title="编辑" onclick="airCommand('${var.DEVICE_ADDRESS}','${var.GUEST_ROOM}','${var.DEVICE_CODE}');" class="tooltip-success" data-rel="tooltip" title="" data-placement="left">控制界面</a>
															</c:if>
															<c:if test="${QX.add != 1}">
																<a style="cursor:pointer;display: block;" title="编辑" onclick="airCommand('${var.DEVICE_ADDRESS}','${var.GUEST_ROOM}','${var.DEVICE_CODE}');" class="tooltip-success" data-rel="tooltip" title="" data-placement="left">控制界面</a>
															</c:if>
														</c:if>
														
														<c:if test="${var.DEVICE_TYPE=='1114'}">
															<%-- <button id="loading" class="btn" onclick="lampOpen('${var.DEVICE_ADDRESS}','${var.GUEST_ROOM}','${var.DEVICE_CODE}','${var.DEVICE_TYPE}');" >加载等待</button> --%>
															
															<span id="loading"  onclick="lampOpen('${var.DEVICE_ADDRESS}','${var.GUEST_ROOM}','${var.DEVICE_CODE}','${var.DEVICE_TYPE}');" style="float: left;"><img
																src="uploadFiles/close_pressed.png"
																style="height: 40px; width: 40px;" /></span>
															<span id="loading"  onclick="lampGuan('${var.DEVICE_ADDRESS}','${var.GUEST_ROOM}','${var.DEVICE_CODE}','${var.DEVICE_TYPE}');" style="float: right;"><img
																src="uploadFiles/close.png"
																style="height: 40px; width: 40px;" /></span>
																
															
														</c:if>
														
														<c:if test="${var.DEVICE_TYPE=='3114'}">
															<input type="button" id="loading" value="开" onclick="opens('${var.DEVICE_ADDRESS}','${var.GUEST_ROOM}','${var.DEVICE_CODE}','${var.DEVICE_TYPE}');"style="float: left;">
															<input type="button" id="loading" value="关" onclick="guan('${var.DEVICE_ADDRESS}','${var.GUEST_ROOM}','${var.DEVICE_CODE}','${var.DEVICE_TYPE}');" style="vertical-align:middle;text-align: center;">
															<input type="button" id="loading" value="停" onclick="stop('${var.DEVICE_ADDRESS}','${var.GUEST_ROOM}','${var.DEVICE_CODE}','${var.DEVICE_TYPE}');" style="float: right;">
														</c:if>
														
														
														</td>

												</tr>
											</c:forEach>
										<%-- <c:if test="${QX.cha == 1 }">
											
										</c:if>
										<c:if test="${QX.cha == 0 }">
											<tr>
												<td colspan="100" class="center">您无权查看</td>
											</tr>
										</c:if> --%>
									</c:when>
									<c:otherwise>
										<tr class="main_info">
											<td colspan="100" class="center">没有相关数据</td>
										</tr>
									</c:otherwise>
								</c:choose>

							</tbody>
						</table>


						<div class="page-header position-relative">
							<table style="width: 100%;">
								<tr>
									
									<td style="vertical-align:top;"><div class="pagination" style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}</div></td>
								</tr>
							</table>
						</div>
					</form>
				</div>




				<!-- PAGE CONTENT ENDS HERE -->
			</div>
			<!--/row-->

		</div>
		<!--/#page-content-->
	</div>
	<!--/.fluid-container#main-container-->



	<!-- 引入 -->
	<script src="static/js/jquery.js"></script>
	<script src="static/js/lc_switch.js" type="text/javascript"></script>

	<script type="text/javascript">window.jQuery || document.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
	<script src="static/js/bootstrap.min.js"></script>
	<script src="static/js/ace-elements.min.js"></script>
	<script src="static/js/ace.min.js"></script>

	<script type="text/javascript" src="static/js/chosen.jquery.min.js"></script>
	<!-- 下拉框 -->
	<script type="text/javascript"
		src="static/js/bootstrap-datepicker.min.js"></script>
	<!-- 日期框 -->
	<script type="text/javascript" src="static/js/bootbox.min.js"></script>
	<!-- 确认窗口 -->
	<!-- 引入 -->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
	<!--提示框-->
	
	<!-- zDialog -->
	<script type="text/javascript" src="static/zDialog/zDrag.js"></script>

	<script type="text/javascript" src="static/zDialog/zDialog.js"></script>

	<!-- 轮播 -->
	<script type="text/javascript" src="jss/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="jss/jquery.banner.revolution.min.js"></script>
<script type="text/javascript" src="jss/banner.js"></script>



	<script type="text/javascript">
		
		$(top.hangge());
		
		//检索
		function search(){
			top.jzts();
			$("#Form").submit();
		}
		
		//新增
		function add(){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="新增";
			 diag.URL = '<%=basePath%>hostdevice/goAdd.do';
			 diag.Width = 450;
			 diag.Height = 355;
			 diag.CancelEvent = function(){ //关闭事件
				 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
					 if('${page.currentPage}' == '0'){
						 top.jzts();
						 setTimeout("self.location=self.location",100);
					 }else{
						 nextPage(${page.currentPage});
					 }
				}
				diag.close();
			 };
			 diag.show();
		}
		
		//删除
		function del(Id){
			bootbox.confirm("确定要删除吗?", function(result) {
				if(result) {
					top.jzts();
					var url = "<%=basePath%>hostdevice/delete.do?HOSTDEVICE_ID="+Id+"&tm="+new Date().getTime();
					$.get(url,function(data){
						nextPage(${page.currentPage});
					});
				}
			});
		}
		
		//修改
		function edit(Id){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="编辑";
			 diag.URL = '<%=basePath%>hostdevice/goEdit.do?HOSTDEVICE_ID='+Id;
			 diag.Width = 450;
			 diag.Height = 355;
			 diag.CancelEvent = function(){ //关闭事件
				 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
					 nextPage(${page.currentPage});
				}
				diag.close();
			 };
			 diag.show();
		}
		
		
		//电视机控制界面
		function tvCommand(DEVICE_ADDRESS,GUEST_ROOM,DEVICE_CODE){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="电视机控制界面";
			 diag.URL = '<%=basePath%>hostdevice/goTvCommand.do?DEVICE_ADDRESS='+DEVICE_ADDRESS+'&GUEST_ROOM='+GUEST_ROOM+'&DEVICE_CODE='+DEVICE_CODE;
			 diag.Width = 250;
			 diag.Height = 400;
			 diag.CancelEvent = function(){ //关闭事件
				 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
					 nextPage(${page.currentPage});
				}
				diag.close();
			 };
			 diag.show();
		}
		
		//电视机学习界面
		function tvStudy(DEVICE_ADDRESS,GUEST_ROOM,DEVICE_CODE){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="电视机学习界面";
			 diag.URL = '<%=basePath%>hostdevice/goTvStudy.do?DEVICE_ADDRESS='+DEVICE_ADDRESS+'&GUEST_ROOM='+GUEST_ROOM+'&DEVICE_CODE='+DEVICE_CODE;
			 diag.Width = 250;
			 diag.Height = 400;
			 diag.CancelEvent = function(){ //关闭事件
				 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
					 nextPage(${page.currentPage});
				}
				diag.close();
			 };
			 diag.show();
		}
		
		
		//空调控制界面
		function airCommand(DEVICE_ADDRESS,GUEST_ROOM,DEVICE_CODE){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="空调控制界面";
			 diag.URL = '<%=basePath%>hostdevice/goAirCommand.do?DEVICE_ADDRESS='+DEVICE_ADDRESS+'&GUEST_ROOM='+GUEST_ROOM+'&DEVICE_CODE='+DEVICE_CODE;
			 diag.Width = 300;
			 diag.Height = 400;
			 diag.CancelEvent = function(){ //关闭事件
				 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
					 nextPage(${page.currentPage});
				}
				diag.close();
			 };
			 diag.show();
		}
		
		//空调学习界面
		function airStudy(DEVICE_ADDRESS,GUEST_ROOM,DEVICE_CODE){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="空调学习界面";
			 diag.URL = '<%=basePath%>hostdevice/goAirStudy.do?DEVICE_ADDRESS='+DEVICE_ADDRESS+'&GUEST_ROOM='+GUEST_ROOM+'&DEVICE_CODE='+DEVICE_CODE;
			 diag.Width = 250;
			 diag.Height = 400;
			 diag.CancelEvent = function(){ //关闭事件
				 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
					 nextPage(${page.currentPage});
				}
				diag.close();
			 };
			 diag.show();
		}
		
		//zigbee灯开
		function zigbeeOpen(deviceAddress,GUEST_ROOM,deviceCode,commandType){
			
			var checkbox = document.getElementById(deviceAddress);//
			if(checkbox.checked){
				$.ajax({
					url : '<%=basePath%>hostdevice/command.do',
					type : 'get',
					data : {'deviceAddress' : deviceAddress,'GUEST_ROOM' : GUEST_ROOM,'deviceCode' : deviceCode,'commandType' : commandType,'command' : '1'},
					success : function(result){
						if(result.success == '0' ){
							alert(result.message);
						}else{
							/* alert("2"); */
						}
					}
				});
				 <%-- window.location.href='<%=basePath%>hostdevice/command.do?deviceAddress='+deviceAddress+'&GUEST_ROOM='+GUEST_ROOM+'&deviceCode='+deviceCode+'&commandType='+commandType+'&command='+1; --%>
			}else{
				$.ajax({
					url : '<%=basePath%>hostdevice/command.do',
					type : 'get',
					data : {'deviceAddress' : deviceAddress,'GUEST_ROOM' : GUEST_ROOM,'deviceCode' : deviceCode,'commandType' : commandType,'command' : '0'},
					success : function(result){
						if(result.success == '0' ){
							Dialog.alert(result.message);
						}else{
							/* alert("2"); */
						}
					}
				});
				<%--  window.location.href='<%=basePath%>hostdevice/command.do?deviceAddress='+deviceAddress+'&GUEST_ROOM='+GUEST_ROOM+'&deviceCode='+deviceCode+'&commandType='+commandType+'&command='+0; --%>
			}
			   
		}
		
		//情景模式控制
		function modelCommand(modelId){
			$.ajax({
				url : '<%=basePath%>hostdevice/modelCommand.do',
				type : 'get',
				data : {'modelId' : modelId},
				success : function(result){
					if(result.message == '该情景模式没有动作'){
						alert("该情景模式没有动作");
					}else{
						/* alert("2"); */
					}
				}
			});
			 <%--  window.location.href='<%=basePath%>hostdevice/modelCommand.do?modelId='+modelId;   --%>
		}
		
		//zigbee灯关
		function zigbeeGuan(deviceAddress,GUEST_ROOM,deviceCode,commandType){
			
			  window.location.href='<%=basePath%>hostdevice/command.do?deviceAddress='+deviceAddress+'&GUEST_ROOM='+GUEST_ROOM+'&deviceCode='+deviceCode+'&commandType='+commandType+'&command='+0;  
		}
		
		//射频灯开
		function lampOpen(deviceAddress,GUEST_ROOM,deviceCode,commandType){
			$.ajax({
				url : '<%=basePath%>hostdevice/command.do?',
				type : 'get',
				data : {'deviceAddress' : deviceAddress,'GUEST_ROOM' : GUEST_ROOM,'deviceCode' : deviceCode,'commandType' : commandType,'command' : '100'},
				success : function(result){
					if(result.success == '0' ){
						alert(result.message); 
					}else{
						/* alert("2"); */
					}
				}
			});
			
		}
		
		//射频灯关
		function lampGuan(deviceAddress,GUEST_ROOM,deviceCode,commandType){
			$.ajax({
				url : '<%=basePath%>hostdevice/command.do?',
				type : 'get',
				data : {'deviceAddress' : deviceAddress,'GUEST_ROOM' : GUEST_ROOM,'deviceCode' : deviceCode,'commandType' : commandType,'command' : '0'},
				success : function(result){
					if(result.success == '0' ){
						alert(result.message); 
					}else{
						/* alert("2"); */
					}
				}
			});
		}
		//射频窗帘开
		function opens(deviceAddress,GUEST_ROOM,deviceCode,commandType){
			$.ajax({
				url : '<%=basePath%>hostdevice/command.do?',
				type : 'get',
				data : {'deviceAddress' : deviceAddress,'GUEST_ROOM' : GUEST_ROOM,'deviceCode' : deviceCode,'commandType' : commandType,'command' : '100'},
				success : function(result){
					if(result.success == '0' ){
						alert(result.message); 
					}else{
						/* alert("2"); */
					}
				}
			});
			
			 <%-- window.location.href='<%=basePath%>hostdevice/command.do?deviceAddress='+deviceAddress+'&GUEST_ROOM='+GUEST_ROOM+'&deviceCode='+deviceCode+'&commandType='+commandType+'&command='+100;   --%>
			
		}
		//射频窗帘关
		function guan(deviceAddress,GUEST_ROOM,deviceCode,commandType){
			$.ajax({
				url : '<%=basePath%>hostdevice/command.do?',
				type : 'get',
				data : {'deviceAddress' : deviceAddress,'GUEST_ROOM' : GUEST_ROOM,'deviceCode' : deviceCode,'commandType' : commandType,'command' : '0'},
				success : function(result){
					if(result.success == '0' ){
						alert(result.message); 
					}else{
						/* alert("2"); */
					}
				}
			});
		}
		//射频窗帘停
		function stop(deviceAddress,GUEST_ROOM,deviceCode,commandType){
			$.ajax({
				url : '<%=basePath%>hostdevice/command.do?',
				type : 'get',
				data : {'deviceAddress' : deviceAddress,'GUEST_ROOM' : GUEST_ROOM,'deviceCode' : deviceCode,'commandType' : commandType,'command' : '50'},
				success : function(result){
					if(result.success == '0' ){
						alert(result.message); 
					}else{
						/* alert("2"); */
					}
				}
			});
		}
		
		
		</script>

	<script type="text/javascript">
		
		$(function() {
			
			//下拉框
			$(".chzn-select").chosen(); 
			$(".chzn-select-deselect").chosen({allow_single_deselect:true}); 
			
			//日期框
			$('.date-picker').datepicker();
			
			//复选框
			$('table th input:checkbox').on('click' , function(){
				var that = this;
				$(this).closest('table').find('tr > td:first-child input:checkbox')
				.each(function(){
					this.checked = that.checked;
					$(this).closest('tr').toggleClass('selected');
				});
					
			});
			
		});
		
		
		//批量操作
		function makeAll(msg){
			bootbox.confirm(msg, function(result) {
				if(result) {
					var str = '';
					for(var i=0;i < document.getElementsByName('ids').length;i++)
					{
						  if(document.getElementsByName('ids')[i].checked){
						  	if(str=='') str += document.getElementsByName('ids')[i].value;
						  	else str += ',' + document.getElementsByName('ids')[i].value;
						  }
					}
					if(str==''){
						bootbox.dialog("您没有选择任何内容!", 
							[
							  {
								"label" : "关闭",
								"class" : "btn-small btn-success",
								"callback": function() {
									//Example.show("great success");
									}
								}
							 ]
						);
						
						$("#zcheckbox").tips({
							side:3,
				            msg:'点这里全选',
				            bg:'#AE81FF',
				            time:8
				        });
						
						return;
					}else{
						if(msg == '确定要删除选中的数据吗?'){
							top.jzts();
							$.ajax({
								type: "POST",
								url: '<%=basePath%>hostdevice/deleteAll.do?tm='+new Date().getTime(),
						    	data: {DATA_IDS:str},
								dataType:'json',
								//beforeSend: validateData,
								cache: false,
								success: function(data){
									 $.each(data.list, function(i, list){
											nextPage(${page.currentPage});
									 });
								}
							});
						}
					}
				}
			});
		}
		
		//导出excel
		function toExcel(){
			window.location.href='<%=basePath%>hostdevice/excel.do';
		}
		</script>

</body>
</html>

