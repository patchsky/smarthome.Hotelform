/**
  * 文件说明
  * @Description:扩展说明
  * @Copyright: 2015 dreamtech.com.cn Inc. All right reserved
  * @Version: V6.0
  */
package com.kincony.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


public class SendMsgUtil {
	
	
	
	/**
	 * 发送短信消息
	  * @return
	  * @return String
	 * @throws UnsupportedEncodingException 
	  * @Author: LiFusheng

	 */
	@SuppressWarnings("deprecation")
	public static String sendMsg(String mobile,String content) throws UnsupportedEncodingException {
		Map<String, String> params = new HashMap<String, String>();
		//手机号码
		params.put("mobile", mobile);
		//将短信内容进行URLEncoder编码
		params.put("nr", URLEncoder.encode(content, "UTF-8"));
		String string = null;
		String contents = null;
		/*String contents = null;*/
		String encode = URLEncoder.encode(content);
		for (String key  : params.keySet()) {
			if("mobile".equals(key)){
				string = params.get(key);
			}
			
			if("nr".equals(key)){
				contents = params.get(key);
			}
		}
		//短信接口URL提交地址
		String url = "http://222.73.117.158/msg/HttpBatchSendSM?account=Jkdz888&mobile="+mobile+"&pswd=Hificat882&needstatus=1&msg="+contents;
		return HttpRequestUtil.getRequest(url, mobile);
	}
	
	/**
	 * 随机生成6位随机验证码
	  * @return
	  * @return String
	  * @Author: LiFusheng

	 */
	public  String createRandomVcode(){
		//验证码
		String vcode = "";
		for (int i = 0; i < 6; i++) {
			vcode = vcode + (int)(Math.random() * 9);
		}
		return vcode;
	}
	
	/**
	 * 随机生成6位随机验证码
	  * @return
	  * @return String
	  * @Author: LiFusheng

	 */
	public  String createRandom(){
		//验证码
		String vcode = "";
		for (int i = 0; i < 8; i++) {
			vcode = vcode + (int)(Math.random() * 9);
		}
		return vcode;
	}
	
	/**
	 * 随机生成3位随机验证码
	  * @return
	  * @return String
	  * @Author: LiFusheng

	 */
	public  String createRandomVcodes(){
		//验证码
		String vcode = "";
		for (int i = 0; i < 3; i++) {
			vcode = vcode + (int)(Math.random() * 9);
		}
		return vcode;
	}
	
	/***
	 * 
	 * 测试
	 * @param args
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args) throws UnsupportedEncodingException {
//		System.out.println(SendMsgUtil.createRandomVcode());
//		System.out.println("&ecb=12".substring(1));
		SendMsgUtil s = new SendMsgUtil();
		s.createRandomVcode();
		System.out.println(sendMsg("15105873889", "尊敬的用户，您的验证码为" + s));
		
	}
	
	
}
