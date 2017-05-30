package com.kincony.server.helper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.httpclient.HttpClient;
import org.jboss.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.kincony.controller.base.BaseController;
import com.kincony.server.bean.DockUser;
import com.kincony.server.packets.ErrorPacket;
import com.kincony.server.packets.InPacket;
import com.kincony.server.packets.OutPacket;
import com.kincony.server.packets.in.AlarmPacket;
import com.kincony.server.packets.in.CDataPacket;
import com.kincony.server.packets.in.DDataPacket;
import com.kincony.server.packets.in.LoginPacket;
import com.kincony.server.packets.in.QueryPacket;
import com.kincony.server.packets.in.ReportPacket;
import com.kincony.server.packets.in.SendDDataReplyPacket;
import com.kincony.server.packets.in.alarm.Alarm0140Packet;
import com.kincony.server.packets.in.alarm.Alarm0150Packet;
import com.kincony.server.packets.in.alarm.Alarm0240Packet;
import com.kincony.server.packets.in.report.Report0150Packet;
import com.kincony.server.packets.in.report.Report0140Packet;
import com.kincony.server.packets.in.report.Report0240Packet;
import com.kincony.server.packets.out.DataReplyPacket;
import com.kincony.server.packets.out.KeepAlivePacket;
import com.kincony.server.packets.out.LoginReplyPacket;
import com.kincony.server.packets.out.QueryReplyPacket;
import com.kincony.server.packets.out.SendDDataPacket;
import com.kincony.server.support.PacketProcessor;
import com.kincony.server.support.Protocol;
import com.kincony.server.util.NettyUtils;
import com.kincony.server.util.StaticUtil;
import com.kincony.service.system.host.HostService;
import com.kincony.service.system.hostdevice.HostDeviceService;
import com.kincony.service.system.hostofflinerecords.HostOfflineRecordsService;
import com.kincony.service.system.resendverification.ResendVerificationService;
import com.kincony.util.DateUtil;
import com.kincony.util.PageData;
import com.kincony.util.UuidUtil;


public class PacketProcessHelper{

	private PacketProcessHelper packetProcessHelper;

	private Map<String, Integer> user_num = new HashMap<String, Integer>();
	
	private String message;
	private String s;
	private int count;
	@Resource(name="hostService")
	private HostService hostService;
	@Resource(name="hostofflinerecordsService")
	private HostOfflineRecordsService hostofflinerecordsService;
	@Resource(name="hostdeviceService")
	private HostDeviceService hostdeviceService;
	@Resource(name="resendverificationService")
	private ResendVerificationService resendverificationService;
	private PageData resendVerification;
	private Map<String, Object>map;
	
	private String prompt;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	

	

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public PacketProcessHelper getPacketProcessHelper() {
		return packetProcessHelper;
	}

	public void setPacketProcessHelper(PacketProcessHelper packetProcessHelper) {
		this.packetProcessHelper = packetProcessHelper;
	}

	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	

	
	
	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}



	private static Logger logger = LoggerFactory.getLogger(PacketProcessHelper.class);

	private PacketProcessor packetProcessor;

	private UserManager userManager;

	// private ArrayList<Action> actions;

	

	

	
	
	public PacketProcessHelper() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PacketProcessHelper(PacketProcessor packetProcessor) {
		this.packetProcessor = packetProcessor;
		this.userManager = packetProcessor.getUserManager();
	}

	private String userCode;
	private String deviceCode;

	/**
	 * 设备注册
	 * 
	 * @param in
	 * @throws Exception 
	 */
	public void processLoginSuccess(InPacket in) throws Exception {
		logger.info("开始处理设备登录");

		LoginPacket packet = (LoginPacket) in;
		packet.getDevData();

		int ret = 0;

		String hostName = packet.getHostName();
		int port = packet.getPort();

		String deviceCode = in.getDevId();
		DockUser user = userManager.getUser(deviceCode);
		logger.info("" + (user == null));
		
		if (user == null) {
			PageData findByDeviceCode = hostService.findByDeviceCode(deviceCode);
			System.err.println(findByDeviceCode);
			if (findByDeviceCode!=null) {
				// 注册在线用户
				userManager.addUser(deviceCode, new Date(), hostName, port);
				packetProcessor.addSocketAddress(deviceCode, new String[] {
						hostName, "" + port });
				ret = 1;
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("DEVICE_CODE", deviceCode);
				map.put("STATUS","1");
				hostService.editStatus(map);
			} else {
				ret = 0;
			}
		} else {
			ret = 1;
			user.setStatus(1);

			/*boProcessService.doLogin(deviceCode, hostName);*/
			userManager.addUser(deviceCode, new Date(), hostName, port);
			packetProcessor.addSocketAddress(deviceCode, new String[] {
					hostName, "" + port });
		}

		logger.info("注册设备序列号:" + deviceCode + " 设备注册! ip " + hostName + " 状态 "
				+ ret);

		LoginReplyPacket reply = new LoginReplyPacket(deviceCode, ret);
		reply.setHostName(hostName);
		reply.setPort(port);
		packetProcessor.sendStrategy(reply);
	}

	/**
	 * 设备离线
	 * 
	 * @param userId
	 * @param channel
	 */
	public void processLogout(String userId, Channel channel) {
		// String ip = NettyUtils.getIp(channel);
		logger.info(userId + " 设备注册! 离线");
		userManager.logoutUser(userId, new Date(), "");
	}

	public void processQuerySuccess(InPacket in) throws Exception {
		logger.info("开始处理设备查询");
		QueryPacket packet = (QueryPacket) in;

		String deviceCode = packet.getDevId();

		String hostName = packet.getHostName();
		int port = packet.getPort();

		int ret = 0; // 未注册

		DockUser user = userManager.getUser(deviceCode);

		if (user != null) {
			ret = user.getStatus();
		} else {
			// 数据库中查询
			PageData findByDeviceCode = hostService.findByDeviceCode(deviceCode);
			System.err.println(findByDeviceCode);
			if(findByDeviceCode==null){
				ret = 0; // 未注册
			}else{
				ret = 1;
			}
		}

		logger.info(" 查询设备状态 " + "devId " + deviceCode + " 状态 " + ret);

		QueryReplyPacket reply = new QueryReplyPacket(packet.getDevId(), ret);
		reply.setHostName(hostName);
		reply.setPort(port);
		packetProcessor.sendStrategy(reply);
	}

	/**
	 * 服务器主动发给设备
	 * 
	 * @param devId
	 * @param devType
	 *            设备类型
	 * @param frameType
	 *            帧类型
	 * @param frameBody
	 *            帧内容
	 */
	public void processSendDDatas(String devId, byte[] frameBody) {
		logger.info("发送 " + devId + " processSendDData");
		SendDDataPacket packet = new SendDDataPacket(devId);
		/*
		 * packet.setFrameType(frameType); packet.setDevType(devType);
		 */

		packet.setFrameBody(frameBody);
		packet.setFrameLen(frameBody.length);

		String[] address = packetProcessor.getSocketAddress(devId);

		if (address == null) {
			logger.error(devId + " 设备IP 不存在");
			message = devId + " 设备IP 不存在";
			return;
		}

		packet.setHostName(address[0]);
		packet.setPort(Integer.valueOf(address[1]));

		packetProcessor.sends(packet);

	}

	
	/**
	 * 服务器主动发给设备
	 * 
	 * @param devId
	 * @param devType
	 *            设备类型
	 * @param frameType
	 *            帧类型
	 * @param frameBody
	 *            帧内容
	 */
	public void processSendDDatas(String devId, byte[] frameBody,String userCode) {
		logger.info("发送 " + devId + " processSendDData");
		SendDDataPacket packet = new SendDDataPacket(devId);
		/*
		 * packet.setFrameType(frameType); packet.setDevType(devType);
		 */

		packet.setFrameBody(frameBody);
		packet.setFrameLen(frameBody.length);
		
		String[] address = packetProcessor.getSocketAddress(devId);

		if (address == null) {
			logger.error(devId + " 设备IP 不存在");
			message = devId + " 设备IP 不存在";
			return;
		}

		packet.setHostName(userCode);
		packet.setPort(Integer.valueOf(address[1]));

		packetProcessor.sends(packet);

	}
	
	/**
	 * 服务器主动发给设备
	 * 
	 * @param devId
	 * @param devType
	 *            设备类型
	 * @param frameType
	 *            帧类型
	 * @param frameBody
	 *            帧内容
	 */
	public void processSendDData(String devId, byte[] frameBody) {
		logger.info("发送 " + devId + " processSendDData");
		SendDDataPacket packet = new SendDDataPacket(devId);
		/*
		 * packet.setFrameType(frameType); packet.setDevType(devType);
		 */

		packet.setFrameBody(frameBody);
		packet.setFrameLen(frameBody.length);

		String[] address = packetProcessor.getSocketAddress(devId);

		if (address == null) {
			logger.error(devId + " 设备不在线");
			message = devId + " 设备不在线";
			return;
		}

		packet.setHostName(address[0]);
		packet.setPort(Integer.valueOf(address[1]));

		packetProcessor.send(packet);

	}

	/**
	 * 服务器主动发给设备
	 * 
	 * @param devId
	 * @param devType
	 *            设备类型
	 * @param frameType
	 *            帧类型
	 * @param frameBody
	 *            帧内容
	 */
	public void processSend0Packet(String devId, char devType, byte frameType) {
		SendDDataPacket packet = new SendDDataPacket(devId);
		int req = OutPacket.getNextSeq();
		logger.info("发送清0命令 " + devId + " rand=" + req);

		packet.setFrameType(frameType);
		packet.setDevType(devType);
		byte[] b = new byte[] { (byte) req };
		packet.setFrameBody(b);
		packet.setFrameLen(1);

		String[] address = packetProcessor.getSocketAddress(devId);

		if (address == null) {
			logger.error(devId + " 设备IP 不存在");
			return;
		}

		packet.setHostName(address[0]);
		packet.setPort(Integer.valueOf(address[1]));

		packetProcessor.send(packet);
	}

	public void processSendDDataSuccess(InPacket in) {
		SendDDataReplyPacket packet = (SendDDataReplyPacket) in;

		String devId = packet.getDevId();

		switch (packet.getFrameType()) {
		case 0x02: // 开关

			break;
		case 0x10: // 清0
			logger.info("设备" + devId + "清0！");
			
			break;
		}
	}

	/**
	 * 客户端（手机）发服务器
	 * 
	 * @param in
	 */
	public void processCDataSuccess(InPacket in) {
		logger.info("开始处理processCDataSuccess");
		CDataPacket packet = (CDataPacket) in;
		String devId = packet.getDevId();
		String clientId = packet.getClientId();
		byte[] data = packet.getDevData();

		String hostName = packet.getHostName();
		int port = packet.getPort();
		// ip
		packetProcessor.addSocketAddress(clientId, new String[] { hostName, "" + port });

		logger.info(clientId + " 客户 转发 " + "devId " + devId + " data " + data);

		String[] address = packetProcessor.getSocketAddress(devId);

		if (address == null) {
			logger.error(devId + " 设备IP 不存在");
			return;
		}

		DataReplyPacket reply = new DataReplyPacket(packet.getCommand(), packet.getDevId());
		reply.setDevData(data);
		reply.setHostName(address[0]);
		reply.setPort(Integer.valueOf(address[1]));
		logger.info(clientId + " 客户 转发给 " + devId + " 设备 ip " + address[0] + " port " + address[1]);
	}

	

	public static boolean isInDate(Date date, String strDateBegin,  
	        String strDateEnd,String s) {  
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	    String strDate = sdf.format(date);  
	    // 截取当前时间时分秒  
	    int strDateH = Integer.parseInt(strDate.substring(11, 13));  
	    
	    // 截取开始时间时分秒  
	    int strDateBeginH = Integer.parseInt(strDateBegin);  
	    
	    // 截取结束时间时分秒  
	    int strDateEndH = Integer.parseInt(strDateEnd);
	   
	    
	    int ds  = Integer.parseInt(s);
	    if(ds==1){
	    	if (strDateH >= strDateBeginH && strDateH <= strDateEndH ) { 
		        // 当前时间小时数在开始时间和结束时间小时数之间   
		        return true;
		    } else {
		        return false;  
		    }  
	    }else{
	    	 return false; 
	    }
	    
	}  
	
	public static String sendGet(String url, String userCode) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			
			connection.setRequestProperty("userCode",userCode);
			connection.setRequestProperty("timestamp", new Date().getTime()+"");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			System.err.println(result);
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	public static String sendGet2(String url) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			
			
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			System.err.println(result);
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 设备发服务器
	 * 
	 * @param in
	 * @throws Exception 
	 */
	public void processDDataSuccess(InPacket in)  {
		logger.info("开始处理processDDataSuccess");
		DDataPacket packet = (DDataPacket) in;
		String devId = packet.getDevId();
		String clientId = packet.getClientId();
		
		byte[] data = packet.getDevData();

		byte[] dataClone = new byte[data.length];

		System.arraycopy(data, 0, dataClone, 0, data.length);
		
		/* System.err.println("VerifyPhoneNum"+VerifyPhoneNum); */
		System.err.println(new String(dataClone));
		System.out.println("**************************");

		//ZIGBEE_LIGHT-READ-5,661,3,0,0,0,OK
		//ZIGBEE_LIGHT-SEND-20,661,3,0,OK
		
		String lightSend = new String(dataClone);
		String[] lightSendSplit = lightSend.split("-");
		if(lightSendSplit[0].trim().toString().equals("ZIGBEE_LIGHT")&&lightSendSplit[1].trim().toString().equals("SEND")){
			System.err.println("第一步进了");
			String lightSend2 = lightSendSplit[2].trim().toString();
			String[] lightSendSplit2 = lightSend2.split(",");
			if ("OK".equals(lightSendSplit2[4].trim().toString())) {
				String ds = null;
				if (lightSendSplit2[2].equals("1")) {
					ds = lightSendSplit2[1] + 1;
					
					
					try {
						Map<String, Object> maps = new HashMap<String, Object>();
						maps.put("DEVICE_CODE", devId);
						maps.put("DEVICE_ADDRESS",ds);
						maps.put("COMMAND", lightSendSplit2[3]);
						resendVerification = resendverificationService.findMap(maps);
						if(resendVerification!=null){
							System.err.println("resendVerification "+"进了1");
							maps.put("ACCEPT_STATE", "OK");
							maps.put("RESENDVERIFICATION_ID", resendVerification.get("RESENDVERIFICATION_ID"));
							resendverificationService.edits(maps);
						}else{
							System.err.println("*((*(*(1");
						}
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					try {
						PageData pageData = hostdeviceService.findByDeviceAddress(ds);
						if(pageData!=null){
							System.err.println("resendVerification "+"进了2");
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("STATE", lightSendSplit2[3]);
							map.put("DEVICE_ADDRESS", ds);
							hostdeviceService.editDeviceState(map);
						}else{
							System.err.println("*((*(*(2");
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.err.println("进了1");
				} else if (lightSendSplit2[2].equals("2")) {
					ds = lightSendSplit2[1] + 2;
					
					
					try {
						Map<String, Object> maps = new HashMap<String, Object>();
						maps.put("DEVICE_CODE", devId);
						maps.put("DEVICE_ADDRESS",ds);
						maps.put("COMMAND", lightSendSplit2[3]);
						resendVerification = resendverificationService.findMap(maps);
						if(resendVerification!=null){
							System.err.println("resendVerification "+"进了3");
							maps.put("ACCEPT_STATE", "OK");
							maps.put("RESENDVERIFICATION_ID", resendVerification.get("RESENDVERIFICATION_ID"));
							resendverificationService.edits(maps);
						}else{
							System.err.println("*((*(*(1");
						}
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					try {
						PageData pageData = hostdeviceService.findByDeviceAddress(ds);
						if(pageData!=null){
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("STATE", lightSendSplit2[3]);
							map.put("DEVICE_ADDRESS", ds);
							hostdeviceService.editDeviceState(map);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.err.println("进了2");
				} else if (lightSendSplit2[2].equals("3")) {
					ds = lightSendSplit2[1] + 3;
					
					
					try {
						Map<String, Object> maps = new HashMap<String, Object>();
						maps.put("DEVICE_CODE", devId);
						maps.put("DEVICE_ADDRESS",ds);
						maps.put("COMMAND", lightSendSplit2[3]);
						resendVerification = resendverificationService.findMap(maps);
						if(resendVerification!=null){
							maps.put("ACCEPT_STATE", "OK");
							maps.put("RESENDVERIFICATION_ID", resendVerification.get("RESENDVERIFICATION_ID"));
							resendverificationService.edits(maps);
						}else{
							System.err.println("*((*(*(1");
						}
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					try {
						PageData pageData = hostdeviceService.findByDeviceAddress(ds);
						if(pageData!=null){
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("STATE", lightSendSplit2[3]);
							map.put("DEVICE_ADDRESS", ds);
							hostdeviceService.editDeviceState(map);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.err.println("进了3");
				}
				
				
			}else{
				System.err.println("没见OK");
			}
		}
		
		
		String lightRead = new String(dataClone);
		String[] lightReadSplit = lightRead.split("-");
		if(lightReadSplit[0].trim().equals("ZIGBEE_LIGHT")&&lightReadSplit[1].trim().equals("READ")){
			String[] array = StaticUtil.QUERYSTATE.get(devId + "_A");
			String GUEST_ROOM = "";
			if (array != null) {
				GUEST_ROOM = array[0];
			}
			System.err.println("GUEST_ROOM  "+GUEST_ROOM);
			String lightRead2 = lightReadSplit[2].trim().toString();
			String[] lightReadSplit2 = lightRead2.split(",");
			Integer lightCount = Integer.valueOf(lightReadSplit2[2]);
			
			for (int i = 0; i < lightCount; i++) {
				int c = i + 1;
				PageData pageData;
				try {
					pageData = hostdeviceService.findByDeviceAddress(lightReadSplit2[1].trim().toString()+c);
					Map<String, Object> map = new HashMap<String, Object>();
					if(pageData!=null){
						if (c == 1) {
							System.err.println("on_off2split2[3]" + lightReadSplit2[3]);
							map.put("STATE", lightReadSplit2[3]);
							map.put("DEVICE_ADDRESS", lightReadSplit2[1].trim().toString()+c);
						}else if (c == 2) {
							System.err.println("on_off2split2[4]" + lightReadSplit2[4]);
							map.put("STATE", lightReadSplit2[4]);
							map.put("DEVICE_ADDRESS", lightReadSplit2[1].trim().toString()+c);
						}else if (c == 3) {
							System.err.println("on_off2split2[5]" + lightReadSplit2[5]);
							map.put("STATE", lightReadSplit2[5]);
							map.put("DEVICE_ADDRESS", lightReadSplit2[1].trim().toString()+c);
						}
						hostdeviceService.editDeviceState(map);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		
		
		String network = new String(dataClone);
		String[] networkSplit = network.split("-");
		if(networkSplit[0].trim().toString().equals("ZIGBEE_CONFIG2")){
			if(networkSplit[1].trim().toString().equals("READ")){
				String network2 = networkSplit[2].trim().toString();
				String[] networkSplit2 = network2.split(",");
				if(networkSplit2[2].trim().toString().equals("OK")){
					if(networkSplit2[0].trim().toString().equals("OI")){
						StaticUtil.NETWORK.put(devId + "_" + "A", new String[] {
								networkSplit2[1].trim().toString(), new Date().getTime() + "" });
					}else if(networkSplit2[0].trim().toString().equals("CH")){
						StaticUtil.CHANNEL.put(devId + "_" + "A", new String[] {
								networkSplit2[1].trim().toString(), new Date().getTime() + "" });
					}		
				}
				
				
				
				
			}
		}
		

		String[] address = packetProcessor.getSocketAddress(clientId);
		
	}

	/**
	 * 设备上报
	 * 
	 * @param in
	 */
	public void processReportSuccess(InPacket in) {
		logger.info("开始处理processReportSuccess");
		
	}

	/**
	 * 设备报警
	 * 
	 * @param in
	 */
	public void processAlarmSuccess(InPacket in) {
		logger.info("开始处理processAlarmSuccess");
		
	}

	/**
	 * 处理KEEP_ALIVE
	 * 
	 * @param in
	 */
	public void procesKeepAliveSuccess(InPacket in) {
		logger.info("开始处理procesKeepAliveSuccess");
		com.kincony.server.packets.in.KeepAlivePacket packet = (com.kincony.server.packets.in.KeepAlivePacket) in;
		String deviceCode = packet.getDevId();

		String hostName = packet.getHostName();
		int port = packet.getPort();

		int ret = 1;
		DockUser dockUser = userManager.getUser(deviceCode);
		if (dockUser == null) { // 需要
			ret = 0;
		} else {
			ret = dockUser.getStatus();

			if (ret == 1) {
				if (!hostName.equals(dockUser.getIp()) || port != dockUser.getPort()) {
					packetProcessor.addSocketAddress(deviceCode, new String[] { hostName, "" + port });
					userManager.keepAliveUser(deviceCode, new Date(), hostName, port);
					
				}
			}
		}
		

		logger.info(deviceCode + " KeepAlive " + hostName + " " + port + " 状态 " + ret);

		KeepAlivePacket reply = new KeepAlivePacket(packet.getDevId(), packet.getSequence(), ret);
		reply.setHostName(hostName);
		reply.setPort(port);
		packetProcessor.sendStrategy(reply);

		packetProcessor.getKeepAliveTrigger().add(packet);

	}

	/**
	 * 处理 离线
	 * 
	 * @param packet
	 * @throws Exception 
	 */
	public void procesKeepAliveLost(com.kincony.server.packets.in.KeepAlivePacket packet) throws Exception {
		logger.info("开始处理 离线 procesKeepAliveLost");
		String hostName = packet.getHostName();
		int port = packet.getPort();
		userManager.logoutUser(packet.getDevId(), new Date(), hostName);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("DEVICE_CODE", packet.getDevId());
		map.put("STATUS","0");
		hostService.editStatus(map);
		packetProcessor.removeSocketAddress(packet.getDevId());
		String time;
		PageData pageData = hostService.findByDeviceCode(packet.getDevId());
		if(pageData!=null){
			time = DateUtil.getTime();
			map.put("HOST_NUMBER",pageData.get("HOST_NUMBER").toString());
			map.put("OFFLINETIME", time);
			map.put("HOSTOFFLINERECORDS_ID", UuidUtil.get32UUID());	//主键
			hostofflinerecordsService.saves(map);
		}else{
			System.err.println("离线 没有进"+packet.getDevId()  );
		}
		logger.info(packet.getDevId() + " procesKeepAliveLost " + hostName);
	}

	/**
	 * 处理未知命令包，有些和协议无关的包也使用这个命令，比如ErrorPacket
	 * 
	 * @param in
	 */
	public void processUnknown(InPacket in) {
		if (in instanceof ErrorPacket) {
			ErrorPacket error = (ErrorPacket) in;
			switch (error.errorCode) {
			}
		}
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

}
