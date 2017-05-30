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

<link rel="stylesheet" href="static/jquery_ui/jquery-ui.css">
<script src="static/jquery_ui/jquery-1.10.2.js"></script>
<script src="static/jquery_ui/jquery-ui.js"></script>
<script>
    $(function () {
        $("#refrigeration").slider({ max: 27, min: 12, step: 1, change: function (e, ui) { $('#loading').val(ui.value); }, value: parseInt($('#loading').val(),10)||0/*初始化值，不设置默认为0*/ });
        var v = $("#refrigeration").slider('value'), opt = $("#refrigeration").slider('option');
        if(v == "12"){
        	$( "#loading" ).html("还没调温度");
        }
        
        $("#refrigeration_1").click(function () {
            var v = $("#refrigeration").slider('value'), opt = $("#refrigeration").slider('option');
            v = eval(v + this.value + opt.step);
            if (v < opt.min) v = opt.min; else if (v > opt.max) v = opt.max;

            $("#refrigeration").slider('value', v);
            var temperature;
            if(v=="13"){
            	temperature = "16°";
            }else if(v=="14"){
            	temperature = "17°";
            }else if(v=="15"){
            	temperature = "18°";
            }else if(v=="16"){
            	temperature = "19°";
            }else if(v=="17"){
            	temperature = "20°";
            }else if(v=="18"){
            	temperature = "21°";
            }else if(v=="19"){
            	temperature = "22°";
            }else if(v=="20"){
            	temperature = "23°";
            }else if(v=="21"){
            	temperature = "24°";
            }else if(v=="22"){
            	temperature = "25°";
            }else if(v=="23"){
            	temperature = "26°";
            }else if(v=="24"){
            	temperature = "27°";
            }else if(v=="25"){
            	temperature = "28°";
            }else if(v=="26"){
            	temperature = "29°";
            }else if(v=="27"){
            	temperature = "30°";            	
            }
            $( "#loading" ).html(temperature);
            
        });
        
        $("#refrigeration_2").click(function () {
            var v = $("#refrigeration").slider('value'), opt = $("#refrigeration").slider('option');
            v = eval(v + this.value + opt.step);
            if (v < opt.min) v = opt.min; else if (v > opt.max) v = opt.max;

            $("#refrigeration").slider('value', v);
            var temperature;
        	if(v=="12"){
        		temperature = "还没调温度";
        	}else if(v=="13"){
        		temperature = "16°";
            }else if(v=="14"){
            	temperature = "17°";
            }else if(v=="15"){
            	temperature = "18°";
            }else if(v=="16"){
            	temperature = "19°";
            }else if(v=="17"){
            	temperature = "20°";
            }else if(v=="18"){
            	temperature = "21°";
            }else if(v=="19"){
            	temperature = "22°";
            }else if(v=="20"){
            	temperature = "23°";
            }else if(v=="21"){
            	temperature = "24°";
            }else if(v=="22"){
            	temperature = "25°";
            }else if(v=="23"){
            	temperature = "26°";
            }else if(v=="24"){
            	temperature = "27°";
            }else if(v=="25"){
            	temperature = "28°";
            }else if(v=="26"){
            	temperature = "29°";
            }else if(v=="27"){
            	temperature = "30°";            	
            }
            $( "#loading" ).html(temperature);

        });
        
        
        $("#heating").slider({ max: 42, min: 27, step: 1, change: function (e, ui) { $('#loadings').val(ui.value); }, value: parseInt($('#loading').val(),10)||0/*初始化值，不设置默认为0*/ });
        var v = $("#heating").slider('value'), opt = $("#heating").slider('option');
        if(v == "27"){
        	$( "#loadings" ).html("还没调温度");
        }
        
        $("#heating_1").click(function () {
            var v = $("#heating").slider('value'), opt = $("#heating").slider('option');
            v = eval(v + this.value + opt.step);
            if (v < opt.min) v = opt.min; else if (v > opt.max) v = opt.max;

            $("#heating").slider('value', v);
            var temperature;
            if(v=="28"){
            	temperature = "16°";
            }else if(v=="29"){
            	temperature = "17°";
            }else if(v=="30"){
            	temperature = "18°";
            }else if(v=="31"){
            	temperature = "19°";
            }else if(v=="32"){
            	temperature = "20°";
            }else if(v=="33"){
            	temperature = "21°";
            }else if(v=="34"){
            	temperature = "22°";
            }else if(v=="35"){
            	temperature = "23°";
            }else if(v=="36"){
            	temperature = "24°";
            }else if(v=="37"){
            	temperature = "25°";
            }else if(v=="38"){
            	temperature = "26°";
            }else if(v=="39"){
            	temperature = "27°";
            }else if(v=="40"){
            	temperature = "28°";
            }else if(v=="41"){
            	temperature = "29°";
            }else if(v=="42"){
            	temperature = "30°";            	
            }
            $( "#loadings" ).html(temperature);
            
        });
        
        $("#heating_2").click(function () {
            var v = $("#heating").slider('value'), opt = $("#heating").slider('option');
            v = eval(v + this.value + opt.step);
            if (v < opt.min) v = opt.min; else if (v > opt.max) v = opt.max;

            $("#heating").slider('value', v);
            var temperature;
        	if(v=="27"){
        		temperature = "还没调温度";
        	}else if(v=="28"){
        		temperature = "16°";
            }else if(v=="29"){
            	temperature = "17°";
            }else if(v=="30"){
            	temperature = "18°";
            }else if(v=="31"){
            	temperature = "19°";
            }else if(v=="32"){
            	temperature = "20°";
            }else if(v=="33"){
            	temperature = "21°";
            }else if(v=="34"){
            	temperature = "22°";
            }else if(v=="35"){
            	temperature = "23°";
            }else if(v=="36"){
            	temperature = "24°";
            }else if(v=="37"){
            	temperature = "25°";
            }else if(v=="38"){
            	temperature = "26°";
            }else if(v=="39"){
            	temperature = "27°";
            }else if(v=="40"){
            	temperature = "28°";
            }else if(v=="41"){
            	temperature = "29°";
            }else if(v=="42"){
            	temperature = "30°";            	
            }
            $( "#loadings" ).html(temperature);

        });
    });
    function refrigeration_study(){
    	if($("#loading").val()==12){
    		alert("还没调温度,请先调下温度在控制");
    	}else{
    		window.location.href='<%=basePath%>hostdevice/air_command.do?VALUES='+$("#loading").val()+'&GUEST_ROOM='+'${pd.GUEST_ROOM}'+'&DEVICE_CODE='+'${pd.DEVICE_CODE}'+'&DEVICE_ADDRESS='+'${pd.DEVICE_ADDRESS}';
    	}
    }
    
    
    function heating_study(){
    	if($("#loadings").val()==27){
    		alert("还没调温度,请先调下温度在控制");
    	}else{
    		window.location.href='<%=basePath%>hostdevice/air_command.do?VALUES='+$("#loadings").val()+'&GUEST_ROOM='+'${pd.GUEST_ROOM}'+'&DEVICE_CODE='+'${pd.DEVICE_CODE}'+'&DEVICE_ADDRESS='+'${pd.DEVICE_ADDRESS}';
    	}
    }
</script>
<style>


#zhong {
	width:100%
	height: 220px;
	background-color: gray;
	
}
#zhong1 {
	background-color: gray;
	height: 150px;
}


#refrigeration{margin:0px 10px; width:89%;height:13px;margin-left: 15px; display: none;}
#loading{margin:0px 10px;margin-top: 60px; display:block; text-align: center; font-size:20px;color:black;font-family: 楷体,SimKai;}
#refrigeration_1{width: 70px;height:35px;float: left;background-color: #808080;border:0px #ff0000 solid; background-image: url("deviceImage/in_up2.png");}
#refrigeration_2{width: 70px;height:35px; float: right;background-color: #808080; border:0px #ff0000 solid; background-image: url("deviceImage/in_down2.png");}

#heating{margin:0px 10px; width:89%;height:13px;margin-left: 15px; display: none;}
#loadings{margin:0px 10px;display:block; text-align: center; font-size:20px;color:black;font-family: 楷体,SimKai;}
#heating_1{width: 70px;height:35px;float: left;background-color: #808080;border:0px #ff0000 solid; background-image: url("deviceImage/in_up2.png");}
#heating_2{width: 70px;height:35px; float: right;background-color: #808080; border:0px #ff0000 solid; background-image: url("deviceImage/in_down2.png");}
</style>
</head>
<body style="background-color: gray;">
<script src="js/iosOverlay.js"></script>
  <script src="js/spin.min.js"></script>
  <script src="js/prettify.js"></script>
  <script src="js/custom.js"></script>
	<div id="zhong">
		<div>
		
			<a href="<%=basePath%>hostdevice/air_command.do?DEVICE_ADDRESS=${pd.DEVICE_ADDRESS}&GUEST_ROOM=${pd.GUEST_ROOM}&DEVICE_CODE=${pd.DEVICE_CODE}&VALUES=11">
				<img src="uploadFiles/close_pressed.png" style="height: 60px; width: 60px;" />
			</a> 
			<a href="<%=basePath%>hostdevice/air_command.do?DEVICE_ADDRESS=${pd.DEVICE_ADDRESS}&GUEST_ROOM=${pd.GUEST_ROOM}&DEVICE_CODE=${pd.DEVICE_CODE}&VALUES=12">
				<img src="uploadFiles/close.png" style="height: height: 60px; width: 60px; padding-left: 10px; float: right;" />
			</a>
			
			
		</div>
		<div>
			 <span ></span>
			 <div id="refrigeration"></div>
			<div id="sd"></div>
			 <div>
			 	<span id="loading" onclick="refrigeration_study()"></span>	
			 <br>
			 	<input type="button" value="+" id="refrigeration_1"/>制冷<input type="button" value="-" id="refrigeration_2" />
			 </div>
			<br /><br>
			<input id="loading" type="hidden" value="12" readonly="readonly" />
			
		</div>
	</div>
	
	
	<div id="zhong1">
		 <span></span>
			 <div id="heating"></div>
			 <div>
			 	<span id="loadings" onclick="heating_study()"></span>	
			 <br>
			 	<input type="button" value="+" id="heating_1"/>制热<input type="button" value="-" id="heating_2" />
			 </div>
			<br /><br>
			<input id="loadings" type="hidden" value="27" readonly="readonly" />
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
		
		
		//空调开
		function airOpen(deviceAddress,GUEST_ROOM,deviceCode,commandType){
			  window.location.href='<%=basePath%>hostdevice/command.do?deviceAddress='+deviceAddress+'&GUEST_ROOM='+GUEST_ROOM+'&deviceCode='+deviceCode+'&commandType='+commandType+'&command='+100;  
		}
		
	</script>
</body>
</html>