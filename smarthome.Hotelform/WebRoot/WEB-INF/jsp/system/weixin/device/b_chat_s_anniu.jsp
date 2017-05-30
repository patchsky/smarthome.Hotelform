<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>去微信蓝牙调起</title>
<script src="http://libs.baidu.com/jquery/2.0.0/jquery.js"></script>   
<script src="http://res.wx.qq.com/open/js/jweixin-1.1.0.js"> </script>  
</head>
<body>
<h2 style="color: white;background-color: green;text-align: center;background-position: center;">蓝牙设备</h2>  
  <div class="page">  
    <div class="bd spacing">  
        <div class="weui_cells weui_cells_form">  
                  
            <div class="weui_cell">  
                <div class="weui_cell_hd"><label class="weui_label" style="width: auto;">当前设备: </label></div>  
                <div class="weui_cell_bd weui_cell_primary">  
                   <label id="lbdeviceid" class="weui_label" style="width: auto;"></label>  
                </div>  
            </div>  
            <div class="weui_cell">  
                <div class="weui_cell_hd"><label class="weui_label" style="width: auto;">状态信息: </label></div>  
                <div class="weui_cell_bd weui_cell_primary">  
                    <label id="lbInfo" class="weui_label" style="width: auto;"></label>  
                </div>  
            </div>   
            <div class="weui_cell" >  
                <div class="weui_cell_hd"><label class="weui_label">日志:  </label></div>  
                <div class="weui_cell_bd weui_cell_primary">  
                    <textarea id="logtext" class="weui_textarea" placeholder="日志" rows="5"></textarea>  
                </div>  
            </div>  
    
        </div>  
   
        <div class="weui_btn_area weui">  
               
            <button class="weui_btn weui_btn weui_btn_warn" id="CallGetWXrefresh">获取设备</button><br>  
    
        </div>  
    
    </div>  
   
    <div class="weui_dialog_alert" id="Mydialog" style="display: none;">  
    <div class="weui_mask"></div>  
    <div class="weui_dialog">  
        <div class="weui_dialog_hd" id="dialogTitle"><strong class="weui_dialog_title">着急啦</strong></div>  
        <div class="weui_dialog_bd" id="dialogContent">亲,使用本功能,请先打开手机蓝牙！</div>  
        <div class="weui_dialog_ft">  
            <a href="#" class="weui_btn_dialog primary">确定</a>  
        </div>  
    </div>  
    </div>  
       
       
    <!--BEGIN toast-->  
    <div id="toast" style="display: none;">  
        <div class="weui_mask_transparent"></div>  
        <div class="weui_toast">  
            <i class="weui_icon_toast"></i>  
            <p class="weui_toast_content" id="toast_msg">已完成</p>  
        </div>  
    </div>  
    <!--end toast-->  
   
    <!-- loading toast -->  
    <div id="loadingToast" class="weui_loading_toast" style="display:none;">  
        <div class="weui_mask_transparent"></div>  
        <div class="weui_toast">  
            <div class="weui_loading">  
                <div class="weui_loading_leaf weui_loading_leaf_0"></div>  
                <div class="weui_loading_leaf weui_loading_leaf_1"></div>  
                <div class="weui_loading_leaf weui_loading_leaf_2"></div>  
                <div class="weui_loading_leaf weui_loading_leaf_3"></div>  
                <div class="weui_loading_leaf weui_loading_leaf_4"></div>  
                <div class="weui_loading_leaf weui_loading_leaf_5"></div>  
                <div class="weui_loading_leaf weui_loading_leaf_6"></div>  
                <div class="weui_loading_leaf weui_loading_leaf_7"></div>  
                <div class="weui_loading_leaf weui_loading_leaf_8"></div>  
                <div class="weui_loading_leaf weui_loading_leaf_9"></div>  
                <div class="weui_loading_leaf weui_loading_leaf_10"></div>  
                <div class="weui_loading_leaf weui_loading_leaf_11"></div>  
            </div>  
            <p class="weui_toast_content" id="loading_toast_msg">数据加载中</p>  
        </div>  
    </div>  
    <!-- End loading toast -->  
       
    <!--BEGIN dialog1-->  
    <div class="weui_dialog_confirm" id="dialog1" style="display: none;">  
        <div class="weui_mask"></div>  
        <div class="weui_dialog">  
            <div class="weui_dialog_hd"><strong class="weui_dialog_title">弹窗标题</strong></div>  
            <div class="weui_dialog_bd">自定义弹窗内容，居左对齐显示，告知需要确认的信息等</div>  
            <div class="weui_dialog_ft">  
                <a href="javascript:;" class="weui_btn_dialog default" id="qxBtn">取消</a>  
                <a href="javascript:;" class="weui_btn_dialog primary" id="okBtn">确定</a>  
            </div>  
        </div>  
    </div>  
    <!--END dialog1-->  
    <!--BEGIN dialog2-->  
    <div class="weui_dialog_alert" id="dialog2" style="display: none;">  
        <div class="weui_mask"></div>  
        <div class="weui_dialog">  
            <div class="weui_dialog_hd"><strong class="weui_dialog_title">弹窗标题</strong></div>  
            <div class="weui_dialog_bd">弹窗内容，告知当前页面信息等</div>  
            <div class="weui_dialog_ft">  
                <a href="javascript:;" class="weui_btn_dialog primary">确定</a>  
            </div>  
        </div>  
    </div>  
    <!--END dialog2-->  
</div>  
   
<div id="myparams"  style="display: none;">  
 <span id="timestamp">${timestamp }</span>  
 <span id="nonceStr">${nonceStr }</span>  
 <span id="signature">${signature }</span>  
 <span id="appId">${appId }</span>  
    
</div>
<script type="text/javascript">
	jQuery(document).ready(function(){  
	  //初始化库   
	 loadXMLDoc();  
	 //初始化库结束  
	 //点击获取设备按钮的函数 开始  
	 $("#CallGetWXrefresh").on("click",function(e){    
	      
	     //1. 打开微信设备   
	     my_openWXDeviceLib();  
	     //2. 获取设备信息  
	     my_getWXDeviceInfos();  
	 });  
	 //点击获取设备按钮的函数 结束   
	     
	 }); 
	
	function loadXMLDoc()  
	{  
	    var appId =jQuery("#appId").text();  
	    var timestamp=jQuery("#timestamp").text();  
	    var nonceStr =jQuery("#nonceStr").text();  
	    var signature=jQuery("#signature").text();  
	    wx.config({  
	             beta: true,  
	              debug: true,// 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。  
	              appId: appId,   
	              timestamp: timestamp,  
	              nonceStr: nonceStr,  
	              signature: signature,  
	              jsApiList: [  
	                'openWXDeviceLib',  
	                'closeWXDeviceLib',  
	                'getWXDeviceInfos',  
	                'getWXDeviceBindTicket',  
	                'getWXDeviceUnbindTicket',  
	                'startScanWXDevice',  
	                'stopScanWXDevice',  
	                'connectWXDevice',  
	                'disconnectWXDevice',  
	                'sendDataToWXDevice',  
	                'onWXDeviceBindStateChange',  
	                'onWXDeviceStateChange',  
	                'onScanWXDeviceResult',  
	                'onReceiveDataFromWXDevice',  
	                'onWXDeviceBluetoothStateChange',  
	              ]  
	          });  
	             alert("初始化库结束");  
	}  
	
	
	function my_openWXDeviceLib(){  
		   var x=0;   
		   WeixinJSBridge.invoke('openWXDeviceLib', {},   
		   function(res){  
		       mlog("打开设备返回："+res.err_msg);
		       alert(res.err_msg);
		      if(res.err_msg=='openWXDeviceLib:ok')  
		        {  
		          if(res.bluetoothState=='off')  
		            {      
		              showdialog("太着急啦","亲,使用前请先打开手机蓝牙！");    
		              $("#lbInfo").innerHTML="1.请打开手机蓝牙";  
		              $("#lbInfo").css({color:"red"});  
		              x=1;  
		              isOver();  
		            };  
		          if(res.bluetoothState=='unauthorized')  
		            {  
		              showdialog("出错啦","亲,请授权微信蓝牙功能并打开蓝牙！");      
		              $("#lbInfo").html("1.请授权蓝牙功能");  
		              $("#lbInfo").css({color:"red"});  
		              x=1;  
		              isOver();  
		            };   
		          if(res.bluetoothState=='on')  
		            {  
		              //showdialog("太着急啦","亲,请查看您的设备是否打开！");     
		              $("#lbInfo").html("1.蓝牙已打开,未找到设备");  
		              $("#lbInfo").css({color:"red"});  
		              //$("#lbInfo").attr(("style", "background-color:#000");  
		              x=0;  
		              //isOver();  
		            };        
		        }  
		      else  
		        {  
		          $("#lbInfo").html("1.微信蓝牙打开失败");  
		          x=1;   
		          showdialog("微信蓝牙状态","亲,请授权微信蓝牙功能并打开蓝牙！");     
		        }  
		    });  
		   return x;  //0表示成功 1表示失败  
		}  
	
	
	function my_getWXDeviceInfos(){  
	      
	    WeixinJSBridge.invoke('getWXDeviceInfos', {}, function(res){  
	        var len=res.deviceInfos.length;  //绑定设备总数量  
	        for(i=0; i<=len-1;i++)  
	         {  
	           //alert(i + ' ' + res.deviceInfos[i].deviceId + ' ' +res.deviceInfos[i].state);   
	           if(res.deviceInfos[i].state==="connected")  
	            {  
	              $("#lbdeviceid").html(res.deviceInfos[i].deviceId);   
	              C_DEVICEID = res.deviceInfos[i].deviceId;  
	              $("#lbInfo").html("2.设备已成功连接");  
	              $("#lbInfo").css({color:"green"});  
	                
	              break;     
	            }    
	         }  
	              
	    });   
	  return;      
	}  
	
	
	//打印日志  
	function mlog(m){  
	    var log=$('#logtext').val();  
	    //log=log+m;  
	    log = m;  
	    $('#logtext').val(log);  
	}  
</script>
</body>
</html>