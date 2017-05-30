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
	<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=1;" name="viewport" />
	<link rel="stylesheet" href="static/js/lc_switch.css">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" >  
	</head>
<body style="width: 100%;height: 100%">

<div class="container-fluid" id="main-container">


<div id="page-content" class="clearfix">
						
  <div class="row-fluid">

	<div class="row-fluid">
	
			<!-- 检索  -->
			<!-- <form action="hostdevice/list.do" method="post" name="Form" id="Form"> -->
			
			<table >
				<tr>
					<td>
						<%-- <script type="text/javascript">
							var a='<%=session.getAttribute("ssssd")%>';
							//或者用var a="${sessionScope.userName}"
							alert(a);
	
						</script> --%>
						<%-- <span class="input-icon">
							<input autocomplete="off" id="nav-search-input" type="hidden"  name="GUEST_ROOM" value="<%=session.getAttribute("ssssd")%>" placeholder="这里输入关键词" readonly="readonly" style="width: 168px;"/>
							
						</span> --%>
					</td>
					
					<!-- <td style="vertical-align:top;margin-top: 50px;"><button class="btn btn-mini btn-light" onclick="search();"  title="检索"><i id="nav-search-icon" class="icon-search"></i></button></td> -->
					<%-- <c:if test="${QX.cha == 1 }">
					<td style="vertical-align:top;"><a class="btn btn-mini btn-light" onclick="toExcel();" title="导出到EXCEL"><i id="nav-search-icon" class="icon-download-alt"></i></a></td>
					</c:if> --%>
					
					
				</tr>
			</table>
			<!-- 检索  -->
		
			
			<table  id="table_report"  class="table table-striped table-bordered table-hover" style="width:100%;margin-top: 10px;">
				
				<thead>
					<tr>
						<th class="center">序号</th>
						<th class="center">客房号</th>
						<th class="center">设备类型</th>
						<th class="center">设备名称</th>
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
								<td class='center' >${vs.index+1}</td>
								<!-- 客房号-->
								<td class='center'>${var.GUEST_ROOM}</td>
								<!-- 设备类型 -->
								<td class='center'>
									<c:if test="${var.DEVICE_TYPE=='1'}"><img src="deviceImage/icon_kaiguandeng.png" style="height: 30px;width: 30px"/></c:if>
									<c:if test="${var.DEVICE_TYPE=='2'}"><img src="deviceImage/icon_chunalian.png" style="height: 30px;width: 30px"/></c:if>
									<c:if test="${var.DEVICE_TYPE=='1114'}"><img src="deviceImage/icon_kaiguandeng.png" style="height: 30px;width: 30px"/></c:if>
								</td>
								<!-- 设备名称 -->
								<td class='center'>${var.NICK_NAME}</td>
								<!-- 设备操作-->
								<td class='center' > 
									<c:if test="${var.DEVICE_TYPE=='1'}">						
										<a href="<%=basePath%>hostdevice/command.do?deviceAddress=${var.DEVICE_ADDRESS}&GUEST_ROOM=${var.GUEST_ROOM}&deviceCode=${var.DEVICE_CODE}&command=1&commandType=1" ><img  src="uploadFiles/close_pressed.png"  style="height: 23px;width: 23px;"/></a>
										
										<a href="<%=basePath%>hostdevice/command.do?deviceAddress=${var.DEVICE_ADDRESS}&GUEST_ROOM=${var.GUEST_ROOM}&deviceCode=${var.DEVICE_CODE}&command=0&commandType=1"><img  src="uploadFiles/close.png" style="height: 23px;width: 23px;"/></a>
									</c:if>
									<c:if test="${var.DEVICE_TYPE=='2'}">						
										<a href="<%=basePath%>hostdevice/command.do?deviceAddress=${var.DEVICE_ADDRESS}&GUEST_ROOM=${var.GUEST_ROOM}&deviceCode=${var.DEVICE_CODE}&command=100&commandType=2"><img  src="uploadFiles/open.png"  style="height: 23px;width: 23px;"/></a>
										<%-- <a href="<%=basePath%>hostdevice/command.do?deviceAddress=${var.DEVICE_ADDRESS}&GUEST_ROOM=${var.GUEST_ROOM}&deviceCode=${var.DEVICE_CODE}&command=50&commandType=2"><img height="37" src="uploadFiles/lefe.png"/></a> --%>
										<a href="<%=basePath%>hostdevice/command.do?deviceAddress=${var.DEVICE_ADDRESS}&GUEST_ROOM=${var.GUEST_ROOM}&deviceCode=${var.DEVICE_CODE}&command=0&commandType=2"><img  src="uploadFiles/cloc.png" style="height: 23px;width: 23px;"/></a>
									</c:if>
									<c:if test="${var.DEVICE_TYPE=='1114'}">
										<a href="<%=basePath%>hostdevice/command.do?deviceAddress=${var.DEVICE_ADDRESS}&GUEST_ROOM=${var.GUEST_ROOM}&deviceCode=${var.DEVICE_CODE}&command=100&commandType=${var.DEVICE_TYPE}"><img  src="uploadFiles/close_pressed.png" style="height: 23px;width: 23px;"/></a>
										<a href="<%=basePath%>hostdevice/command.do?deviceAddress=${var.DEVICE_ADDRESS}&GUEST_ROOM=${var.GUEST_ROOM}&deviceCode=${var.DEVICE_CODE}&command=0&commandType=${var.DEVICE_TYPE}"><img  src="uploadFiles/close.png" style="height: 23px;width: 23px;" /></a>
									
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
					<%-- <c:if test="${QX.del == 1 }">
					<a class="btn btn-small btn-danger" onclick="makeAll('确定要删除选中的数据吗?');" title="批量删除" ><i class='icon-trash'></i></a>
					</c:if> --%>
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
		<script src="static/js/jquery.js"></script>
		<script src="static/js/lc_switch.js" type="text/javascript"></script>

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

