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
	<base href="<%=basePath%>"><!-- jsp文件头和头部 -->
	<%@ include file="../../system/admin/top.jsp"%> 
	<link rel="stylesheet" href="css/bootstrap.min.css">
  <link rel="stylesheet" href="css/custom.css">
  <link rel="stylesheet" href="css/iosOverlay.css">

  <link rel="stylesheet" href="css/prettify.css">
  <script src="js/modernizr-2.0.6.min.js"></script>
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
			<form action="hostdevice/lists.do" method="post" name="Form" id="Form">
			<table>
				<tr>
					<td>
						<span class="input-icon">
							<c:choose>
								<c:when test="${not empty allUser}">
									<select name="GUEST_ROOM" class="form-control checkacc" id="GUEST_ROOM" onclick="search();">
										<c:if test="${pd.GUEST_ROOM == ''}">
											<option value="">请选择</option>
										</c:if>
										<c:if test="${pd.GUEST_ROOM != ''}">
											<option value="${pd.GUEST_ROOM}">${pd.GUEST_ROOM}</option>
											<option value="">请选择</option>
										</c:if>
										
										<c:forEach items="${allUser}" var="var" varStatus="vs">
											<option value="${var.USERNAME}">${var.USERNAME}</option>
												
										</c:forEach>
									</select>
									
								</c:when>
								<c:otherwise>
									<select name="GUEST_ROOM" class="form-control checkacc" id="GUEST_ROOM">
										<option value="">没有用户</option>
									</select>
								</c:otherwise>
							</c:choose>
							<!-- <input autocomplete="off" id="nav-search-input" type="text" name="GUEST_ROOM" value="" placeholder="这里输入关键词" /> -->
						</span>
					</td>
					
					<td style="vertical-align:top;"><button class="btn btn-mini btn-light" onclick="search();"  title="检索"><i id="nav-search-icon" class="icon-search"></i></button></td>
					<c:if test="${QX.cha == 1 }">
					<td style="vertical-align:top;"><a class="btn btn-mini btn-light" onclick="toExcel();" title="导出到EXCEL"><i id="nav-search-icon" class="icon-download-alt"></i></a></td>
					</c:if>
					<c:if test="${QX.add == 1 }"><td style="vertical-align:top;"><a class="btn btn-mini btn-light" onclick="fromExcel();" title="从EXCEL导入"><i id="nav-search-icon" class="icon-cloud-upload"></i></a></td></c:if>
				</tr>
			</table>
			<!-- 检索  -->
			<table  id="table_report"  class="table table-striped table-bordered table-hover" style="width:100%;margin-top: 10px;">
				
				<thead>
					<tr>
						<th class="center">
						<label><input type="checkbox" id="zcheckbox" /><span class="lbl"></span></label>
						</th>
						<th class="center">序号</th>
						<th class="center">主机序列号</th>
						<th class="center">客房号</th>
						<th class="center">设备类型</th>
						<th class="center">设备名称</th>
						<th class="center">设备地址码</th>
						<th class="center">操作</th>
						<th class="center">操作</th>
					</tr>
				</thead>
										
				<tbody>
					
				<!-- 开始循环 -->	
				<c:choose>
					<c:when test="${not empty varList}">
						<c:if test="${QX.cha == 1 }">
						<c:forEach items="${varList}" var="var" varStatus="vs">
							<tr>
								<td style="vertical-align:middle;text-align: center;width: 30px;">
									<label><input type='checkbox' name='ids' value="${var.HOSTDEVICE_ID}" /><span class="lbl"></span></label>
								</td>
								<td style="vertical-align:middle;text-align: center;">${vs.index+1}</td>
								<!-- 主机序列号-->
								<td style="vertical-align:middle;text-align: center;">${var.DEVICE_CODE}</td>
								<!-- 客房号-->
								<td style="vertical-align:middle;text-align: center;">${var.GUEST_ROOM}</td>
								
								<!-- 设备类型 -->
								<td style="vertical-align:middle;text-align: center;">
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
								<td style="vertical-align:middle;text-align: center;">${var.NICK_NAME}</td>
								<!-- 设备地址码 -->
								<td style="vertical-align:middle;text-align: center;">${var.DEVICE_ADDRESS}</td>
								<!-- 设备操作-->
								<td style="vertical-align:middle;text-align: center;" > 
									<c:if test="${var.DEVICE_TYPE=='1'}">
															
										<input type="checkbox" id="${var.DEVICE_ADDRESS}" class="ace-switch ace-switch-3" onclick="zigbeeOpen('${var.DEVICE_ADDRESS}','${var.GUEST_ROOM}','${var.DEVICE_CODE}','${var.DEVICE_TYPE}');" <c:if test="${var.STATE==1}">checked="checked"</c:if>/><span class="lbl"></span>
															
									</c:if> 
									<c:if test="${var.DEVICE_TYPE=='2'}">						
										<%-- <a href="<%=basePath%>hostdevice/sadasdas.do?deviceAddress=${var.DEVICE_ADDRESS}&GUEST_ROOM=${var.GUEST_ROOM}&deviceCode=${var.DEVICE_CODE}&command=100&commandType=2"><img  src="uploadFiles/open.png"  style="height: 23px;width: 23px;"/></a> --%>
										<%-- <a href="<%=basePath%>hostdevice/command.do?deviceAddress=${var.DEVICE_ADDRESS}&GUEST_ROOM=${var.GUEST_ROOM}&deviceCode=${var.DEVICE_CODE}&command=50&commandType=2"><img height="37" src="uploadFiles/lefe.png"/></a> --%>
										<%-- <a href="<%=basePath%>hostdevice/sadasdas.do?deviceAddress=${var.DEVICE_ADDRESS}&GUEST_ROOM=${var.GUEST_ROOM}&deviceCode=${var.DEVICE_CODE}&command=0&commandType=2"><img  src="uploadFiles/cloc.png" style="height: 23px;width: 23px;"/></a> --%>
										<form method="post">
																<input type="range" name="range" min="0" max="100"
																	step="1" value="" style="width: 100px;" />
																<output name="result"> </output>
															</form>
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
																        localStorage ? (localStorage.rangeValue = range.value) : alert("数据保存到了数据库或是其他什么地方。");
																    }, false);
																
																    // 滑动时显示选择的值
																    range.addEventListener("change", function() {
																        result.value = range.value;
																        var s = range.value;
																        
																        window.location.href='<%=basePath%>hostdevice/sadasdas.do?command='+range.value+'&deviceAddress='+'${var.DEVICE_ADDRESS}'+'&GUEST_ROOM='+'${var.GUEST_ROOM}'+'&deviceCode='+'${var.DEVICE_CODE}'+'&commandType=2';
																        <%-- window.location.href='<%=basePath%>hostdevice/command.do?deviceAddress=${var.DEVICE_ADDRESS}&GUEST_ROOM=${var.GUEST_ROOM}&deviceCode=${var.DEVICE_CODE}&command=100&commandType=2'; --%>
																    }, false);
																
																})();       
    														</script>
									</c:if>
									<c:if test="${var.DEVICE_TYPE=='98'}">
										<c:if test="${QX.add == 1}">
											<a style="cursor:pointer;" title="编辑" onclick="tvStudy('${var.DEVICE_ADDRESS}','${var.GUEST_ROOM}','${var.DEVICE_CODE}');" class="tooltip-success" data-rel="tooltip" title="" data-placement="left">学习界面</a>
											<a style="cursor:pointer;" title="编辑" onclick="tvCommand('${var.DEVICE_ADDRESS}','${var.GUEST_ROOM}','${var.DEVICE_CODE}');" class="tooltip-success" data-rel="tooltip" title="" data-placement="left">控制界面</a>
										</c:if>
										<c:if test="${QX.add != 1}">
											<a style="cursor:pointer;" title="编辑" onclick="tvCommand('${var.DEVICE_ADDRESS}','${var.GUEST_ROOM}','${var.DEVICE_CODE}');" class="tooltip-success" data-rel="tooltip" title="" data-placement="left">控制界面</a>
										</c:if>
									</c:if>
									
									<c:if test="${var.DEVICE_TYPE=='99'}">
											<c:if test="${QX.add == 1}">
												<a style="cursor:pointer;" title="编辑" onclick="airStudy('${var.DEVICE_ADDRESS}','${var.GUEST_ROOM}','${var.DEVICE_CODE}');" class="tooltip-success" data-rel="tooltip" title="" data-placement="left">学习界面</a>
												<a style="cursor:pointer;" title="编辑" onclick="airCommand('${var.DEVICE_ADDRESS}','${var.GUEST_ROOM}','${var.DEVICE_CODE}');" class="tooltip-success" data-rel="tooltip" title="" data-placement="left">控制界面</a>
											</c:if>
											<c:if test="${QX.add != 1}">
												<a style="cursor:pointer;" title="编辑" onclick="airCommand('${var.DEVICE_ADDRESS}','${var.GUEST_ROOM}','${var.DEVICE_CODE}');" class="tooltip-success" data-rel="tooltip" title="" data-placement="left">控制界面</a>
											</c:if>
									</c:if>
									
									<c:if test="${var.DEVICE_TYPE=='1114'}">
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
								<td style="width: 30px;" class="center">
									<c:if test="${QX.edit != 1 && QX.del != 1 }">
										<span class="label label-large label-grey arrowed-in-right arrowed-in"><i class="icon-lock" title="无权限"></i></span>
										</c:if>
										
											<c:if test="${QX.edit == 1 }">
											<a style="cursor:pointer;" title="编辑" onclick="edit('${var.HOSTDEVICE_ID}');" class="tooltip-success" data-rel="tooltip" title="" data-placement="left"><span class="green"><i class="icon-edit"></i></span></a>
											</c:if>
											<c:if test="${QX.del == 1 }">
											<a style="cursor:pointer;" title="删除" onclick="del('${var.HOSTDEVICE_ID}');" class="tooltip-error" data-rel="tooltip" title="" data-placement="left"><span class="red"><i class="icon-trash"></i></span> </a>
											</c:if>
									
								</td>
							</tr>
						</c:forEach>
						</c:if>
						<c:if test="${QX.cha == 0 }">
							<tr>
								<td colspan="100" class="center">您无权查看</td>
							</tr>
						</c:if>
					</c:when>
					<c:otherwise>
						<tr class="main_info">
							<td colspan="100" class="center" >没有相关数据</td>
						</tr>
					</c:otherwise>
				</c:choose>
					
				</tbody>
			</table>
			
			
		<div class="page-header position-relative">
		<table style="width:100%;">
			<tr>
				<td style="vertical-align:top;">
					<%-- <c:if test="${QX.add == 1 }">
					<a class="btn btn-small btn-success" onclick="add();">新增</a>
					</c:if> --%>
					<c:if test="${QX.del == 1 }">
					<a class="btn btn-small btn-danger" onclick="makeAll('确定要删除选中的数据吗?');" title="批量删除" ><i class='icon-trash'></i></a>
					</c:if>
				</td>
				<td style="vertical-align:top;"><div class="pagination" style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}</div></td>
			</tr>
		</table>
		</div>
		</form>
	</div>
 
 
 
 
	<!-- PAGE CONTENT ENDS HERE -->
  </div><!--/row-->
	
</div><!--/#page-content-->
</div><!--/.fluid-container#main-container-->
		
		
		
		<!-- 引入 -->
		<script type="text/javascript">window.jQuery || document.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
		<script src="static/js/bootstrap.min.js"></script>
		<script src="static/js/ace-elements.min.js"></script>
		<script src="static/js/ace.min.js"></script>
		
		<script type="text/javascript" src="static/js/chosen.jquery.min.js"></script><!-- 下拉框 -->
		<script type="text/javascript" src="static/js/bootstrap-datepicker.min.js"></script><!-- 日期框 -->
		<script type="text/javascript" src="static/js/bootbox.min.js"></script><!-- 确认窗口 -->
		<!-- 引入 -->
		<script type="text/javascript" src="static/js/jquery.tips.js"></script><!--提示框-->
		
		
		<script type="text/javascript">
		//打开上传excel页面
		function fromExcel(){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="EXCEL 导入到数据库";
			 diag.URL = '<%=basePath%>hostdevice/goUploadExcel.do';
			 diag.Width = 300;
			 diag.Height = 150;
			 diag.CancelEvent = function(){ //关闭事件
				 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
					 if('${page.currentPage}' == '0'){
						 top.jzts();
						 setTimeout("self.location.reload()",100);
					 }else{
						 nextPage(${page.currentPage});
					 }
				}
				diag.close();
			 };
			 diag.show();
		}
		</script>
		
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
		
		
		//电视剧控制界面
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
		
		//电视剧控制界面
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
							alert(result.message); 
						}else{
							/* alert("2"); */
						}
					}
				});
				<%--  window.location.href='<%=basePath%>hostdevice/command.do?deviceAddress='+deviceAddress+'&GUEST_ROOM='+GUEST_ROOM+'&deviceCode='+deviceCode+'&commandType='+commandType+'&command='+0; --%>
			}
			   
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

