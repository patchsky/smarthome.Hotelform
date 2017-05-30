package com.kincony.controller.weixin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.marker.weixin.DefaultSession;
import org.marker.weixin.HandleMessageAdapter;
import org.marker.weixin.MySecurity;
import org.marker.weixin.msg.Data4Item;
import org.marker.weixin.msg.Msg4Event;
import org.marker.weixin.msg.Msg4Image;
import org.marker.weixin.msg.Msg4ImageText;
import org.marker.weixin.msg.Msg4Link;
import org.marker.weixin.msg.Msg4Location;
import org.marker.weixin.msg.Msg4Text;
import org.marker.weixin.msg.Msg4Video;
import org.marker.weixin.msg.Msg4Voice;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;







import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.kincony.controller.base.BaseController;
import com.kincony.entity.system.User;
import com.kincony.service.system.user.UserService;
import com.kincony.service.weixin.command.CommandService;
import com.kincony.service.weixin.imgmsg.ImgmsgService;
import com.kincony.service.weixin.textmsg.TextmsgService;
import com.kincony.util.AppUtil;
import com.kincony.util.Const;
import com.kincony.util.DateUtil;
import com.kincony.util.HttpUtil;
import com.kincony.util.PageData;
import com.kincony.util.Tools;
import com.kincony.util.WeixinUtil;
import com.kincony.utils.AES;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * 
* 类名称：WeixinController.java
* 类描述： 微信公共平台开发 
* @author LiFusheng
* 作者单位： 
* 联系方式：
* 创建时间：2016年10月
* @version 1.0
 */
@Controller
@RequestMapping(value="/weixin")
public class WeixinController extends BaseController{
	
	@Resource(name="textmsgService")
	private TextmsgService textmsgService;
	@Resource(name="commandService")
	private CommandService commandService;
	@Resource(name="imgmsgService")
	private ImgmsgService imgmsgService;
	@Resource(name="userService")
	private UserService userService;
	
	
	
	
	/**
	 * 设备授权接口
	 */
	@RequestMapping(value="/authorizeDevice" ,produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object authorizeDevice()throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String access_token = readTxtFile("c:/access_token.txt");//更换为自己的access_token
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			//00000D052021
	        String deviceId=pd.get("deviceId").toString();//调用生成二维码接口返回的设备id
	        System.err.println("deviceId "+deviceId);
	        String mac="00000D052021";//设备mac地址，可询问提供硬件设备的厂商人员
	        String params="{\"device_num\":\"1\",\"device_list\":[{"
	                   +"\"id\":\""+deviceId+"\","
	                   +"\"mac\":\""+mac+"\","
	                    +"\"connect_protocol\":\"3\","
	                    +"\"auth_key\":\"\","
	                    +"\"close_strategy\":\"1\","
	                    +"\"conn_strategy\":\"1\","
	                    +"\"crypt_method\":\"0\","
	                    +"\"auth_ver\":\"0\","
	                    +"\"manu_mac_pos\":\"-1\","
	                    +"\"ser_mac_pos\":\"-2\","
	                    +"\"ble_simple_protocol\": \"0\""
	                    + "}],"
	                    +"\"op_type\":\"1\""
	                   + "}";
	                    
	        String s=HttpUtil.sendPost("https://api.weixin.qq.com/device/authorize_device?access_token="+access_token, params);
	        System.out.println("返回："+s);
			map.put("result", s);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}			
		
		return AppUtil.returnObject(new PageData(), map);
	}
	
	/**
	 * 调用接口获取设备deviceId和二维码
	 */
	@RequestMapping(value="/getDeviceId" ,produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getDeviceId()throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String access_token = readTxtFile("c:/access_token.txt");
		String product_id="26832";
		String deviceId = HttpUtil.sendGet("https://api.weixin.qq.com/device/getqrcode?access_token="+access_token+"&product_id="+product_id, "utf-8", 30000);
		map.put("result", deviceId);
		return AppUtil.returnObject(new PageData(), map);
	}
	
	/**
	 * 去微信蓝牙调起
	 */
	@RequestMapping(value="/toWeiXinBluetooth")
	public ModelAndView toWeiXinBluetooth(){
		logBefore(logger, "去微信蓝牙调起");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			String access_token = readTxtFile("c:/access_token.txt");
			String ticket = WeixinUtil.getJsapiTicket(access_token);
			
			HttpServletRequest req = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
			String basePath = req.getScheme()+"://"+req.getServerName()+":"+req.getServerPort(); 
			Map<String,String> map = WeixinUtil.sign(ticket, basePath+"/"+"smarthome.Hotelform"+"+/"+"weixin/toWeiXinBluetooth.do");
			for (String key : map.keySet()) {
				System.err.println(key+"="+map.get(key));
			}
			String appId="wx2cf8cb957b32464c";//应用id 
			mv.setViewName("system/weixin/device/b_chat_s_anniu");
			mv.addObject("timestamp", map.get("timestamp"));
			mv.addObject("nonceStr", map.get("nonceStr"));
			mv.addObject("signature", map.get("signature"));
			mv.addObject("appId", appId);
			System.err.println(ticket);
			System.err.println(map.get("timestamp"));
			System.err.println(map.get("nonceStr"));
			System.err.println(map.get("signature"));
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}						
		return mv;
	}
	
	
	/**
	 * 请求登录，验证用户
	 */
	@RequestMapping(value="/login_login" ,produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object login()throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		PageData pd = new PageData();
		pd = this.getPageData();
		String errInfo = "";
		
		//shiro管理的session
		Subject currentUser = SecurityUtils.getSubject();  
		Session session = currentUser.getSession();
		String USERNAME = pd.get("USERNAME").toString();
		String PASSWORD  = pd.get("PWD").toString();
		pd.put("PHONE", USERNAME);
		String passwd = AES.encrypt(PASSWORD);
		/*String decrypt = AES.decrypt(passwd);
		System.err.println(passwd);
		System.err.println(decrypt);*/
		pd.put("PASSWORD", passwd);
		pd = userService.getUserByNameAndPwd(pd);
		if(pd != null){
			if(pd.get("ROLE_ID").equals("1")||pd.get("ROLE_ID").equals("2ebd852e43c54c7eaf450f9af0189932")){
				pd.put("LAST_LOGIN",DateUtil.getTime().toString());
				userService.updateLastLogin(pd);
				User user = new User();
				user.setUSER_ID(pd.getString("USER_ID"));
				user.setUSERNAME(pd.getString("USERNAME"));
				user.setPASSWORD(pd.getString("PASSWORD"));
				user.setNAME(pd.getString("NAME"));
				user.setRIGHTS(pd.getString("RIGHTS"));
				user.setROLE_ID(pd.getString("ROLE_ID"));
				user.setLAST_LOGIN(pd.getString("LAST_LOGIN"));
				user.setIP(pd.getString("IP"));
				user.setSTATUS(pd.getString("STATUS"));
				session.setAttribute(Const.SESSION_USER, user);
				session.setAttribute("ssssd", pd.getString("USERNAME"));
				session.setAttribute("ROLE_ID", pd.get("ROLE_ID"));
				session.removeAttribute(Const.SESSION_SECURITY_CODE);
				
				//shiro加入身份验证
				Subject subject = SecurityUtils.getSubject(); 
			    UsernamePasswordToken token = new UsernamePasswordToken(USERNAME, PASSWORD); 
			    try { 
			        subject.login(token); 
			    } catch (AuthenticationException e) { 
			    	errInfo = "身份验证失败！";
			    }
			}else{
				String ruzhu = pd.get("STARTTIME").toString();//入住时间戳
				Long STARTTIME = Long.valueOf(ruzhu);
				System.err.println("入住时间戳 "+STARTTIME);
				String tuifan = pd.get("ENDTIME").toString();
				Long ENDTIME = Long.valueOf(tuifan);
				System.err.println("退房时间戳 "+ENDTIME);
				SimpleDateFormat formats =  new SimpleDateFormat("yyyy-MM-dd");
				Date SYSTEM_STARTTIME = formats.parse(formats.format(new Date())); 
				long time = SYSTEM_STARTTIME.getTime();
				System.err.println("系统时间戳 "+time);
				if(time>STARTTIME&&time>ENDTIME){
					errInfo = "sdsadas";
				}else if(time<STARTTIME&&time<ENDTIME){
					errInfo = "sdsadas";
				}else{
					pd.put("LAST_LOGIN",DateUtil.getTime().toString());
					userService.updateLastLogin(pd);
					User user = new User();
					user.setUSER_ID(pd.getString("USER_ID"));
					user.setUSERNAME(pd.getString("USERNAME"));
					user.setPASSWORD(pd.getString("PASSWORD"));
					user.setNAME(pd.getString("NAME"));
					user.setRIGHTS(pd.getString("RIGHTS"));
					user.setROLE_ID(pd.getString("ROLE_ID"));
					user.setLAST_LOGIN(pd.getString("LAST_LOGIN"));
					user.setIP(pd.getString("IP"));
					user.setSTATUS(pd.getString("STATUS"));
					session.setAttribute(Const.SESSION_USER, user);
					session.setAttribute("ssssd", pd.getString("USERNAME"));
					session.setAttribute("ROLE_ID", pd.get("ROLE_ID"));
					session.removeAttribute(Const.SESSION_SECURITY_CODE);
					
					//shiro加入身份验证
					Subject subject = SecurityUtils.getSubject(); 
				    UsernamePasswordToken token = new UsernamePasswordToken(USERNAME, PASSWORD); 
				    try { 
				        subject.login(token); 
				    } catch (AuthenticationException e) { 
				    	errInfo = "身份验证失败！";
				    }
				}
			}
			
		    
		}else{
			errInfo = "usererror"; 				//用户名或密码有误
		}
		if(Tools.isEmpty(errInfo)){
			errInfo = "success";					//验证成功
		}
		map.put("result", errInfo);
		return AppUtil.returnObject(new PageData(), map);
	}
	
	/**
	 * 请求登录，验证用户
	 */
	@RequestMapping(value="/login_login2" ,produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object login1()throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		PageData pd = new PageData();
		pd = this.getPageData();
		String errInfo = "";
		
		//shiro管理的session
		Subject currentUser = SecurityUtils.getSubject();  
		Session session = currentUser.getSession();
		String USERNAME = pd.get("USERNAME").toString();
		String PASSWORD  = pd.get("PWD").toString();
		pd.put("PHONE", USERNAME);
		/*String passwd = AES.encrypt(PASSWORD);*/
		/*String decrypt = AES.decrypt(passwd);
		System.err.println(passwd);
		System.err.println(decrypt);*/
		pd.put("PASSWORD", PASSWORD);
		pd = userService.getUserByNameAndPwd(pd);
		if(pd != null){
			if(pd.get("ROLE_ID").equals("1")||pd.get("ROLE_ID").equals("2ebd852e43c54c7eaf450f9af0189932")){
				pd.put("LAST_LOGIN",DateUtil.getTime().toString());
				userService.updateLastLogin(pd);
				User user = new User();
				user.setUSER_ID(pd.getString("USER_ID"));
				user.setUSERNAME(pd.getString("USERNAME"));
				user.setPASSWORD(pd.getString("PASSWORD"));
				user.setNAME(pd.getString("NAME"));
				user.setRIGHTS(pd.getString("RIGHTS"));
				user.setROLE_ID(pd.getString("ROLE_ID"));
				user.setLAST_LOGIN(pd.getString("LAST_LOGIN"));
				user.setIP(pd.getString("IP"));
				user.setSTATUS(pd.getString("STATUS"));
				session.setAttribute(Const.SESSION_USER, user);
				session.setAttribute("ssssd", pd.getString("USERNAME"));
				session.setAttribute("ROLE_ID", pd.get("ROLE_ID"));
				session.removeAttribute(Const.SESSION_SECURITY_CODE);
				
				//shiro加入身份验证
				Subject subject = SecurityUtils.getSubject(); 
			    UsernamePasswordToken token = new UsernamePasswordToken(USERNAME, PASSWORD); 
			    try { 
			        subject.login(token); 
			    } catch (AuthenticationException e) { 
			    	errInfo = "身份验证失败！";
			    }
			}else{
				String ruzhu = pd.get("STARTTIME").toString();//入住时间戳
				Long STARTTIME = Long.valueOf(ruzhu);
				System.err.println("入住时间戳 "+STARTTIME);
				String tuifan = pd.get("ENDTIME").toString();
				Long ENDTIME = Long.valueOf(tuifan);
				System.err.println("退房时间戳 "+ENDTIME);
				SimpleDateFormat formats =  new SimpleDateFormat("yyyy-MM-dd");
				Date SYSTEM_STARTTIME = formats.parse(formats.format(new Date())); 
				long time = SYSTEM_STARTTIME.getTime();
				System.err.println("系统时间戳 "+time);
				if(time>STARTTIME&&time>ENDTIME){
					errInfo = "sdsadas";
				}else if(time<STARTTIME&&time<ENDTIME){
					errInfo = "sdsadas";
				}else{
					pd.put("LAST_LOGIN",DateUtil.getTime().toString());
					userService.updateLastLogin(pd);
					User user = new User();
					user.setUSER_ID(pd.getString("USER_ID"));
					user.setUSERNAME(pd.getString("USERNAME"));
					user.setPASSWORD(pd.getString("PASSWORD"));
					user.setNAME(pd.getString("NAME"));
					user.setRIGHTS(pd.getString("RIGHTS"));
					user.setROLE_ID(pd.getString("ROLE_ID"));
					user.setLAST_LOGIN(pd.getString("LAST_LOGIN"));
					user.setIP(pd.getString("IP"));
					user.setSTATUS(pd.getString("STATUS"));
					session.setAttribute(Const.SESSION_USER, user);
					session.setAttribute("ssssd", pd.getString("USERNAME"));
					session.setAttribute("ROLE_ID", pd.get("ROLE_ID"));
					session.removeAttribute(Const.SESSION_SECURITY_CODE);
					
					//shiro加入身份验证
					Subject subject = SecurityUtils.getSubject(); 
				    UsernamePasswordToken token = new UsernamePasswordToken(USERNAME, PASSWORD); 
				    try { 
				        subject.login(token); 
				    } catch (AuthenticationException e) { 
				    	errInfo = "身份验证失败！";
				    }
				}
			}
			
		    
		}else{
			errInfo = "usererror"; 				//用户名或密码有误
		}
		if(Tools.isEmpty(errInfo)){
			errInfo = "success";					//验证成功
		}
		map.put("result", errInfo);
		return AppUtil.returnObject(new PageData(), map);
	}
	
	
	/**
	 * 去d登陆
	 */
	@RequestMapping(value="/toLoginsds")
	public ModelAndView toLogin(){
		logBefore(logger, "去登陆界面");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			System.err.println(pd.get("PHONE"));
			System.err.println(pd.get("PWD"));
			pd.put("SYSNAME", Tools.readTxtFile(Const.SYSNAME)); //读取系统名称
			mv.setViewName("system/login/login");
			
			mv.addObject("pd", pd);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}						
		return mv;
	}
	
	/**
	 * 去d登陆
	 */
	@RequestMapping(value="/toLoginsds2")
	public ModelAndView toLogin2(){
		logBefore(logger, "去登陆界面");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			System.err.println(pd.get("PHONE"));
			System.err.println();
			pd.put("PHONE", pd.get("PHONE"));
			pd.put("PWD", pd.get("PWD").toString().replaceAll(" ", "+"));
			pd.put("SYSNAME", Tools.readTxtFile(Const.SYSNAME)); //读取系统名称
			mv.setViewName("system/login/login2");
			
			mv.addObject("pd", pd);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}						
		return mv;
	}
	
	/**
	 * 接口验证,总入口
	 * @param out
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	 @RequestMapping(value="/index")
	 public void index(
			 PrintWriter out,
			 HttpServletRequest request,
			 HttpServletResponse response
			 ) throws Exception{     
		 logBefore(logger, "微信接口");
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			String signature = pd.getString("signature");		//微信加密签名
			String timestamp = pd.getString("timestamp");		//时间戳
			String nonce	 = pd.getString("nonce");			//随机数
			String echostr 	 = pd.getString("echostr");			//字符串

			if(null != signature && null != timestamp && null != nonce && null != echostr){/* 接口验证  */
				logBefore(logger, "进入身份验证");
			    List<String> list = new ArrayList<String>(3) { 
				    private static final long serialVersionUID = 2621444383666420433L; 
				    public String toString() {  // 重写toString方法，得到三个参数的拼接字符串
				               return this.get(0) + this.get(1) + this.get(2); 
				           } 
				         }; 
				   list.add(Tools.readTxtFile(Const.WEIXIN)); 		//读取Token(令牌)
				   list.add(timestamp); 
				   list.add(nonce); 
				   Collections.sort(list);							// 排序 
				   String tmpStr = new MySecurity().encode(list.toString(), 
				    MySecurity.SHA_1);								// SHA-1加密 
				   
				    if (signature.equals(tmpStr)) { 
				           out.write(echostr);						// 请求验证成功，返回随机码 
				     }else{ 
				           out.write(""); 
			       } 
				out.flush();
				out.close(); 
			}else{/* 消息处理  */
				logBefore(logger, "进入消息处理");
				response.reset();
				sendMsg(request,response);
			}
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
	}
	 /**
	  * 处理微信服务器发过来的各种消息，包括：文本、图片、地理位置、音乐等等 
	  * @param request
	  * @param response
	  * @throws Exception
	  */
	 public void sendMsg(HttpServletRequest request, HttpServletResponse response) throws Exception{ 

         InputStream is = request.getInputStream(); 
         OutputStream os = response.getOutputStream(); 

         final DefaultSession session = DefaultSession.newInstance(); 
         session.addOnHandleMessageListener(new HandleMessageAdapter(){ 
        	 
        	/**
        	 * 事件
        	 */
        	@Override
        	public void onEventMsg(Msg4Event msg) {
        		/** msg.getEvent()
        		 * unsubscribe：取消关注 ; subscribe：关注
        		 */
        		logger.info(msg.getEvent());
        		if("subscribe".equals(msg.getEvent())){
        			returnMSg(msg,null,"关注");
        		}else if("CLICK".equals(msg.getEvent())){
        			returnMSg(msg,null,"关注");
        		}
        	}
        	
        	 /**
        	  * 收到的文本消息
        	  */
        	 @Override 
             public void onTextMsg(Msg4Text msg) { 
                returnMSg(null,msg,msg.getContent().trim());
             }
        	 
        	 @Override
        	public void onImageMsg(Msg4Image msg) {
        		// TODO Auto-generated method stub
        		super.onImageMsg(msg);
        	}
        	 
        	 @Override
        	public void onLocationMsg(Msg4Location msg) {
        		// TODO Auto-generated method stub
        		super.onLocationMsg(msg);
        	}
        	 
        	@Override
        	public void onLinkMsg(Msg4Link msg) {
        		// TODO Auto-generated method stub
        		super.onLinkMsg(msg);
        	}
        	
        	@Override
        	public void onVideoMsg(Msg4Video msg) {
        		// TODO Auto-generated method stub
        		super.onVideoMsg(msg);
        	}
        	
        	@Override
        	public void onVoiceMsg(Msg4Voice msg) {
        		// TODO Auto-generated method stub
        		super.onVoiceMsg(msg);
        	}
        	
        	@Override
        	public void onErrorMsg(int errorCode) {
        		// TODO Auto-generated method stub
        		super.onErrorMsg(errorCode);
        	}
        	
        	/**
        	 * 返回消息
        	 * @param emsg
        	 * @param tmsg
        	 * @param getmsg
        	 */
        	public void returnMSg(Msg4Event emsg, Msg4Text tmsg, String getmsg){
        		 PageData msgpd;
                 PageData pd = new PageData();
                 String toUserName,fromUserName,createTime;
                 if(null == emsg){
                	 toUserName = tmsg.getToUserName();
                	 fromUserName = tmsg.getFromUserName();
                	 createTime = tmsg.getCreateTime();
                 }else{
                	 toUserName = emsg.getToUserName();
                	 fromUserName = emsg.getFromUserName();
                	 createTime = emsg.getCreateTime();
                 }
                 pd.put("KEYWORD", getmsg);
                 try {
 						msgpd = textmsgService.findByKw(pd);
 						if(null != msgpd){
 							 Msg4Text rmsg = new Msg4Text(); 
 		                     rmsg.setFromUserName(toUserName); 
 		                     rmsg.setToUserName(fromUserName); 
 		                     //rmsg.setFuncFlag("0"); 
 		                     rmsg.setContent(msgpd.getString("CONTENT")); //回复文字消息
 		                     session.callback(rmsg); 
 						}else{
 							msgpd = imgmsgService.findByKw(pd);
 							if(null != msgpd){
 								 Msg4ImageText mit = new Msg4ImageText(); 
 				                 mit.setFromUserName(toUserName); 
 				                 mit.setToUserName(fromUserName);  
 				                 mit.setCreateTime(createTime);  
 								 //回复图文消息
 				                 if(null != msgpd.getString("TITLE1") && null != msgpd.getString("IMGURL1")){
 				                	 Data4Item d1 = new Data4Item(msgpd.getString("TITLE1"),msgpd.getString("DESCRIPTION1"),msgpd.getString("IMGURL1"),msgpd.getString("TOURL1"));  
 				                	 mit.addItem(d1);
 				                	 
 				                	 if(null != msgpd.getString("TITLE2") && null != msgpd.getString("IMGURL2") && !"".equals(msgpd.getString("TITLE2").trim()) && !"".equals(msgpd.getString("IMGURL2").trim())){
 					                	 Data4Item d2 = new Data4Item(msgpd.getString("TITLE2"),msgpd.getString("DESCRIPTION2"),msgpd.getString("IMGURL2"),msgpd.getString("TOURL2"));  
 					                	 mit.addItem(d2);
 					                 }
 				                	 if(null != msgpd.getString("TITLE3") && null != msgpd.getString("IMGURL3") && !"".equals(msgpd.getString("TITLE3").trim()) && !"".equals(msgpd.getString("IMGURL3").trim())){
 					                	 Data4Item d3 = new Data4Item(msgpd.getString("TITLE3"),msgpd.getString("DESCRIPTION3"),msgpd.getString("IMGURL3"),msgpd.getString("TOURL3"));  
 					                	 mit.addItem(d3);
 					                 }
 				                	 if(null != msgpd.getString("TITLE4") && null != msgpd.getString("IMGURL4") && !"".equals(msgpd.getString("TITLE4").trim()) && !"".equals(msgpd.getString("IMGURL4").trim())){
 					                	 Data4Item d4 = new Data4Item(msgpd.getString("TITLE4"),msgpd.getString("DESCRIPTION4"),msgpd.getString("IMGURL4"),msgpd.getString("TOURL4"));  
 					                	 mit.addItem(d4);
 					                 }
 				                	 if(null != msgpd.getString("TITLE5") && null != msgpd.getString("IMGURL5") && !"".equals(msgpd.getString("TITLE5").trim()) && !"".equals(msgpd.getString("IMGURL5").trim())){
 					                	 Data4Item d5 = new Data4Item(msgpd.getString("TITLE5"),msgpd.getString("DESCRIPTION5"),msgpd.getString("IMGURL5"),msgpd.getString("TOURL5"));  
 					                	 mit.addItem(d5);
 					                 }
 				                	 if(null != msgpd.getString("TITLE6") && null != msgpd.getString("IMGURL6") && !"".equals(msgpd.getString("TITLE6").trim()) && !"".equals(msgpd.getString("IMGURL6").trim())){
 					                	 Data4Item d6 = new Data4Item(msgpd.getString("TITLE6"),msgpd.getString("DESCRIPTION6"),msgpd.getString("IMGURL6"),msgpd.getString("TOURL6"));  
 					                	 mit.addItem(d6);
 					                 }
 				                	 if(null != msgpd.getString("TITLE7") && null != msgpd.getString("IMGURL7") && !"".equals(msgpd.getString("TITLE7").trim()) && !"".equals(msgpd.getString("IMGURL7").trim())){
 					                	 Data4Item d7 = new Data4Item(msgpd.getString("TITLE7"),msgpd.getString("DESCRIPTION7"),msgpd.getString("IMGURL7"),msgpd.getString("TOURL7"));  
 					                	 mit.addItem(d7);
 					                 }
 				                	 if(null != msgpd.getString("TITLE8") && null != msgpd.getString("IMGURL8") && !"".equals(msgpd.getString("TITLE8").trim()) && !"".equals(msgpd.getString("IMGURL8").trim())){
 					                	 Data4Item d8 = new Data4Item(msgpd.getString("TITLE8"),msgpd.getString("DESCRIPTION8"),msgpd.getString("IMGURL8"),msgpd.getString("TOURL8"));  
 					                	 mit.addItem(d8);
 					                 }
 				                 }
 				                 //mit.setFuncFlag("0");   
 				                 session.callback(mit); 
 							}else{
 								msgpd = commandService.findByKw(pd);
 								if(null != msgpd){
 			             			Runtime runtime = Runtime.getRuntime(); 
 			             			runtime.exec(msgpd.getString("COMMANDCODE"));
 								}else{
 									 Msg4Text rmsg = new Msg4Text(); 
 				                     rmsg.setFromUserName(toUserName); 
 				                     rmsg.setToUserName(fromUserName); 
 				                     rmsg.setContent("无匹配结果");
 				                     session.callback(rmsg); 
 								}
 							}
 						}
 				} catch (Exception e1) {
 					logBefore(logger, "匹配错误");
 				}
        	}
        	
        }); 

         /*必须调用这两个方法   如果不调用close方法，将会出现响应数据串到其它Servlet中。*/ 
         session.process(is, os);	//处理微信消息  
         session.close();			//关闭Session 
     } 

	//================================================获取关注列表==============================================================
	public final static String gz_url="https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=";
	//获取access_token
	@RequestMapping(value="/getGz")
	public void getGz(PrintWriter out) {
		logBefore(logger, "获取关注列表");
		try{
			String access_token = readTxtFile("c:/access_token.txt");
			
			System.out.println(access_token+"============");
			
			String requestUrl=gz_url.replace("ACCESS_TOKEN", access_token);
			
			System.out.println(requestUrl+"============");
			
			JSONObject jsonObject = httpRequst(requestUrl, "GET", null);
			System.out.println(jsonObject);
			//System.out.println(jsonObject.getString("total")+"============");
			
			/*PrintWriter pw;
			try {
				pw = new PrintWriter( new FileWriter( "e:/gz.txt" ) );
				pw.print(jsonObject.getString("total"));
		        pw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			out.write("success");
			out.close();*/
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
	}
	
	
	public final static String Menu_url="https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=";
	//获取access_token
	@RequestMapping(value="/getMenu")
	public void getMUAN_url(PrintWriter out) {
		logBefore(logger, "");
		try{
			String access_token = readTxtFile("c:/access_token.txt");
			
			System.out.println(access_token+"============");
			
			String requestUrl=gz_url.replace("ACCESS_TOKEN", access_token);
			
			System.out.println(requestUrl+"============");
			
			JSONObject jsonObject = httpRequst(requestUrl, "GET", null);
			System.out.println(jsonObject);
			//System.out.println(jsonObject.getString("total")+"============");
			
			/*PrintWriter pw;
			try {
				pw = new PrintWriter( new FileWriter( "e:/gz.txt" ) );
				pw.print(jsonObject.getString("total"));
		        pw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			out.write("success");
			out.close();*/
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
	}
	
	
	//读取文件
	public String readTxtFile(String filePath) {
		try {
			String encoding = "utf-8";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(
				new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					//System.out.println(lineTxt);
					return lineTxt;
				}
				read.close();
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		return "";
	}
	
	//================================================获取access_token==============================================================
	public final static String access_token_url="https://api.weixin.qq.com/cgi-bin/token?" +
			"grant_type=client_credential&appid=APPID&secret=APPSECRET";
	//获取access_token
	@RequestMapping(value="/getAt")
	public void getAt(PrintWriter out) {
		logBefore(logger, "获取access_token");
		try{
			String appid = "wx2cf8cb957b32464c";
			String appsecret = "38a07942520d93458183234aba0c21aa";
			
			String requestUrl=access_token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
			JSONObject jsonObject=httpRequst(requestUrl, "GET", null);
			
			//System.out.println(jsonObject.getString("access_token")+"============");
			
			PrintWriter pw;
			try {
				pw = new PrintWriter( new FileWriter( "c:/access_token.txt" ) );
				pw.print(jsonObject.getString("access_token"));
		        pw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			out.write("success");
			out.close();
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
	}
	
	public JSONObject httpRequst(String requestUrl,String requetMethod,String outputStr){
		JSONObject jsonobject=null;
		StringBuffer buffer=new StringBuffer();
		try
		{
			//创建SSLContext对象，并使用我们指定的新人管理器初始化
			TrustManager[] tm={new MyX509TrustManager()};
			SSLContext sslcontext=SSLContext.getInstance("SSL","SunJSSE");
			sslcontext.init(null, tm, new java.security.SecureRandom());
			//从上述SSLContext对象中得到SSLSocktFactory对象
			SSLSocketFactory ssf=sslcontext.getSocketFactory();
			
			URL url=new URL(requestUrl);
			HttpsURLConnection httpUrlConn=(HttpsURLConnection)url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);
			
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			//设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requetMethod);
			
			if("GET".equalsIgnoreCase(requetMethod))
				httpUrlConn.connect();
			
			//当有数据需要提交时
			if(null!=outputStr)
			{
			OutputStream outputStream=httpUrlConn.getOutputStream();
			//注意编码格式，防止中文乱码
			outputStream.write(outputStr.getBytes("UTF-8"));
			outputStream.close();
			}
			
			//将返回的输入流转换成字符串
			InputStream inputStream=httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader=new InputStreamReader(inputStream,"utf-8");
			BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
			
			
			String str=null;
			while((str = bufferedReader.readLine()) !=null)
			{ 
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			//释放资源
			inputStream.close();
			inputStream=null;
			httpUrlConn.disconnect();
			jsonobject=JSONObject.fromObject(buffer.toString());
		}
		catch (ConnectException ce) {
			// TODO: handle exception
		}
		catch (Exception e) {  
		}
		return jsonobject;
	}
	//================================================获取access_token==============================================================
}


//================================================获取access_token==============================================================
 class MyX509TrustManager implements X509TrustManager
{

	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		// TODO Auto-generated method stub
		
	}

	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		// TODO Auto-generated method stub
		
	}

	public X509Certificate[] getAcceptedIssuers() {
		// TODO Auto-generated method stub
		return null;
	}
}
//================================================获取access_token==============================================================