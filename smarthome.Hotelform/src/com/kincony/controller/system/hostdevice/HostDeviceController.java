package com.kincony.controller.system.hostdevice;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.kincony.controller.base.BaseController;
import com.kincony.entity.Page;
import com.kincony.entity.system.Role;
import com.kincony.util.AppUtil;
import com.kincony.util.FileDownload;
import com.kincony.util.FileUpload;
import com.kincony.util.GetPinyin;
import com.kincony.util.HttpRequestUtil;
import com.kincony.util.MapUtil;
import com.kincony.util.ObjectExcelRead;
import com.kincony.util.ObjectExcelView;
import com.kincony.util.Const;
import com.kincony.util.PageData;
import com.kincony.util.PathUtil;
import com.kincony.util.Tools;
import com.kincony.util.Jurisdiction;
import com.kincony.utils.SendMsgUtil;
import com.kincony.server.helper.PacketProcessHelper;
import com.kincony.server.util.StaticUtil;
import com.kincony.service.system.host.HostService;
import com.kincony.service.system.hostdevice.HostDeviceService;
import com.kincony.service.system.infraredcode.InfraredCodeService;
import com.kincony.service.system.model.ModelService;
import com.kincony.service.system.modelinfo.ModelInfoService;
import com.kincony.service.system.resendverification.ResendVerificationService;
import com.kincony.service.system.user.UserService;

/** 
 * 类名称：HostDeviceController
 * 创建人：FH 
 * 创建时间：2016-09-19
 */
@Controller
@RequestMapping(value="/hostdevice")
public class HostDeviceController extends BaseController {
	
	String menuUrl = "hostdevice/list.do"; //菜单地址(权限用)
	String menuUrlss = "hostdevice/lists.do"; //菜单地址(权限用)
	@Resource(name="hostdeviceService")
	private HostDeviceService hostdeviceService;
	@Resource(name="userService")
	private UserService userService;
	@Resource(name="hostService")
	private HostService hostService;
	@Resource(name="infraredcodeService")
	private InfraredCodeService infraredcodeService;
	@Resource(name="modelService")
	private ModelService modelService;
	@Resource(name="modelinfoService")
	private ModelInfoService modelinfoService;
	@Resource(name="resendverificationService")
	private ResendVerificationService resendverificationService;
	@Resource(name="packetProcessHelper")
	private PacketProcessHelper packetProcessHelper;
	private PageData resendVerification;
	private String readBackStatus = "wait";
	private String id;
	private static Map<String, Integer> user_num = new HashMap<String, Integer>();
	/**
	 * 包序号分装方法
	 * @param userCode
	 */
	public void packNum(String userCode){
		if (user_num.get(userCode) == null) {
			user_num.put(userCode, 0);
		} else {
			user_num.put(userCode, user_num.get(userCode) == 255 ? 0 : user_num.get(userCode) + 1);
		}
	}
	
	
	
	/**
	 * 新增
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, "新增HostDevice");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("HOSTDEVICE_ID", this.get32UUID());	//主键
		System.err.println(pd);
		hostdeviceService.save(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value="/delete")
	public void delete(PrintWriter out){
		logBefore(logger, "删除HostDevice");
		if(!Jurisdiction.buttonJurisdiction(menuUrlss, "del")){return;} //校验权限
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			hostdeviceService.delete(pd);
			out.write("success");
			out.close();
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		
	}
	
	/**
	 * 修改
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, "修改HostDevice");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		System.err.println(pd);
		hostdeviceService.edit(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**
	 * 当前客房控制
	 * @throws Exception 
	 */
	@ResponseBody  
	@RequestMapping(value="/command")
	public Object command(Page page) {
		logBefore(logger, "当前客房控制接口");
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String, Object> maps = MapUtil.maps();
		try{
			String deviceAddress = pd.getString("deviceAddress");
			System.err.println(deviceAddress);
			String userCode = pd.getString("GUEST_ROOM");
			System.err.println(userCode);
			String deviceCode = pd.getString("deviceCode");
			System.err.println(deviceCode);
			String command = pd.getString("command");
			System.err.println(command);
			String commandType = pd.getString("commandType");
			System.err.println(commandType);
			packNum(userCode);
			PageData host = hostService.findByDeviceCode(deviceCode);
			if(host!=null){
				if(host.get("STATUS").toString().equals("1")){
					if(commandType.equals("1")){
						String Address = deviceAddress;
						final String substring = Address.substring(0, Address.length()-1);
						String substring2 = Address.substring(Address.length()-1, Address.length());
						String strss = "ZIGBEE_LIGHT-SEND-" + user_num.get(userCode) + "," +substring + ","
								+ substring2 + "," + command;
						byte[] bss = strss.getBytes();
						System.err.println(new String(bss));
						packetProcessHelper.processSendDDatas(deviceCode, bss);
					}else if(commandType.equals("2")){
						Integer commands = null;
						if (Integer.valueOf(command) == 0) {
							commands = 19;
						}

						if (Integer.valueOf(command) == 100) {
							commands = 17;
						} else {
							commands = 20;
						}
						final int commandss;
						if (Integer.valueOf(command) >= 95) {
							commandss = 95;
						} else {
							commandss = Integer.valueOf(command);
						}
						String str = "ZIGBEE_CURTAIN-SEND-" + user_num.get(userCode) + ","
								+ deviceAddress + "," + commands + "," + commandss;
						
						byte[] bs = str.getBytes();
						System.err.println(new String(bs));
						packetProcessHelper.processSendDDatas(deviceCode, bs);
					}else if(commandType.equals("1114")){
						String ln = null;
						String deviceType = commandType.toString();
						String substring = deviceType.substring(1, 2);
						Integer valueOf = Integer.valueOf(substring);
						Integer s = null;
						if (valueOf == 1) {
							s = 2262;
						} else {
							s = 1527;
						}

						String substring2 = deviceType.substring(2, 3);
						Integer valueOf2 = Integer.valueOf(substring2);
						Integer b = null;
						if (valueOf2 == 1) {
							b = 315;
						} else {
							b = 433;
						}

						String substring3 = deviceType.substring(3, 4);
						Integer valueOf3 = Integer.valueOf(substring3);
						Integer c = null;
						if (valueOf3 == 1) {
							c = 12;
						}else if (valueOf3 == 2) {
							c = 15;
						}else if (valueOf3 == 3) {
							c = 22;
						}else if (valueOf3 == 4) {
							c = 33;
						}else if (valueOf3 == 5) {
							c = 47;
						}else if(valueOf3 == 6){
							c = 330;
						}else if(valueOf3 == 7){
							c = 390;
						}else if(valueOf3 == 8){
							c = 200;
						}
						
						
						if (Integer.valueOf(command) == 0) {
							System.err.println(deviceAddress);
							String string = deviceAddress.substring(0, 1);
							String string2 = deviceAddress.substring(1, 8);

							if (string.equals("0")) {
								ln = string2;
							} else {
								ln = deviceAddress;
							}
							
							String str = "PT" + s + "_" + b + "M-SEND-" + user_num.get(userCode) + "," + c + ","
									+ ln + 2;
							byte[] bs = str.getBytes();
							System.err.println("<?> " + new String(bs));
							packetProcessHelper.processSendDDatas(deviceCode, bs);
						}

						if (Integer.valueOf(command) == 50) {
							String string = deviceAddress.substring(0, 1);
							String string2 = deviceAddress.substring(1, 8);

							if (string.equals("0")) {
								ln = string2;
							} else {
								ln = deviceAddress;
							}
							String str = "PT" + s + "_" + b + "M-SEND-" + user_num.get(userCode) + "," + c + ","
									+ ln + 3;
							byte[] bs = str.getBytes();
							System.err.println(new String(bs));
							packetProcessHelper.processSendDDatas(deviceCode, bs);
						}

						if (Integer.valueOf(command) == 100) {
							String string = deviceAddress.substring(0, 1);
							String string2 = deviceAddress.substring(1, 8);

							if (string.equals("0")) {
								ln = string2;
							} else {
								ln = deviceAddress;
							}
							String str = "PT" + s + "_" + b + "M-SEND-" + user_num.get(userCode) + "," + c + ","
									+ ln + 1;
							byte[] bs = str.getBytes();
							System.err.println(new String(bs));
							packetProcessHelper.processSendDDatas(deviceCode, bs);
						}
					}else if(commandType.equals("3114")){
						String ln = null;
						String deviceType = commandType.toString();
						String substring = deviceType.substring(1, 2);
						Integer valueOf = Integer.valueOf(substring);
						Integer s = null;
						if (valueOf == 1) {
							s = 2262;
						} else {
							s = 1527;
						}

						String substring2 = deviceType.substring(2, 3);
						Integer valueOf2 = Integer.valueOf(substring2);
						Integer b = null;
						if (valueOf2 == 1) {
							b = 315;
						} else {
							b = 433;
						}

						String substring3 = deviceType.substring(3, 4);
						Integer valueOf3 = Integer.valueOf(substring3);
						Integer c = null;
						if (valueOf3 == 1) {
							c = 12;
						}else if (valueOf3 == 2) {
							c = 15;
						}else if (valueOf3 == 3) {
							c = 22;
						}else if (valueOf3 == 4) {
							c = 33;
						}else if (valueOf3 == 5) {
							c = 47;
						}else if(valueOf3 == 6){
							c = 330;
						}else if(valueOf3 == 7){
							c = 390;
						}else if(valueOf3 == 8){
							c = 200;
						}
						
						
						if (Integer.valueOf(command) == 0) {
							System.err.println(deviceAddress);
							String string = deviceAddress.substring(0, 1);
							String string2 = deviceAddress.substring(1, 8);

							if (string.equals("0")) {
								ln = string2;
							} else {
								ln = deviceAddress;
							}
							
							String str = "PT" + s + "_" + b + "M-SEND-" + user_num.get(userCode) + "," + c + ","
									+ ln + 2;
							byte[] bs = str.getBytes();
							System.err.println("<?> " + new String(bs));
							packetProcessHelper.processSendDDatas(deviceCode, bs);
						}

						if (Integer.valueOf(command) == 50) {
							String string = deviceAddress.substring(0, 1);
							String string2 = deviceAddress.substring(1, 8);

							if (string.equals("0")) {
								ln = string2;
							} else {
								ln = deviceAddress;
							}
							String str = "PT" + s + "_" + b + "M-SEND-" + user_num.get(userCode) + "," + c + ","
									+ ln + 3;
							byte[] bs = str.getBytes();
							System.err.println(new String(bs));
							packetProcessHelper.processSendDDatas(deviceCode, bs);
						}

						if (Integer.valueOf(command) == 100) {
							String string = deviceAddress.substring(0, 1);
							String string2 = deviceAddress.substring(1, 8);

							if (string.equals("0")) {
								ln = string2;
							} else {
								ln = deviceAddress;
							}
							String str = "PT" + s + "_" + b + "M-SEND-" + user_num.get(userCode) + "," + c + ","
									+ ln + 1;
							byte[] bs = str.getBytes();
							System.err.println(new String(bs));
							packetProcessHelper.processSendDDatas(deviceCode, bs);
						}
					}else{
						System.err.println("88888888^^^**&%^&*****");
					}
				}else{
					maps.put("message", "当前房间主机处于离线状态,请联系前台");
					maps.put("success", false);
				}
			}
			Thread.sleep(800);
		}catch (Exception e){
			logger.error(e.toString(), e);
		}
		
		return AppUtil.returnObject(new PageData(), maps);
	}
	
	/**
	 * 情景模式控制
	 * @throws Exception 
	 */
	@ResponseBody  
	@RequestMapping(value="/modelCommand")  
	public Object modelCommand(Page page) {
		logBefore(logger, "控制");
		Subject currentUser = SecurityUtils.getSubject();  
		Session session = currentUser.getSession();
		PageData pd = new PageData();
		pd = this.getPageData();	
		String USERNAME = pd.getString("GUEST_ROOM");
		Map<String, Object> maps = MapUtil.maps();
		try{
			String userCode;
			if(USERNAME==null){
				userCode = (String) session.getAttribute("ssssd");
				pd.put("GUEST_ROOM", userCode);
			}else{
				userCode = USERNAME.trim().toString();
				pd.put("GUEST_ROOM", userCode);
			}
			String modelId = pd.getString("modelId");
			System.err.println(modelId);
			System.err.println(userCode);
			packNum(userCode);
			pd.put("MODEL_ID", modelId);	
			List<PageData> allModelAction = modelinfoService.getAllModelAction(pd);
			System.err.println(allModelAction.size());
			if(allModelAction.size()>0){
				for (int i = 0; i < allModelAction.size(); i++) {
					
					packNum(userCode);
					PageData modelAction = allModelAction.get(i);
					String DELAY_VALUES = modelAction.get("DELAY_VALUES").toString();
					Integer valueOf = Integer.valueOf(DELAY_VALUES);
					Thread.sleep(valueOf);
					String DEVICE_TYPE = modelAction.get("DEVICE_TYPE").toString();
					if(DEVICE_TYPE.equals("1")){
						String deviceAddress2 = modelAction.get("DEVICE_ADDRESS").toString();
						String substring = deviceAddress2.substring(0, deviceAddress2.length()-1);
						String substring2 = deviceAddress2.substring(deviceAddress2.length()-1, deviceAddress2.length());
						String s;
						if (modelAction.get("CONTROL_COMMAND").toString().equals("100")) {
							s = "1";
							
						} else {
							s = modelAction.get("CONTROL_COMMAND").toString();
						}
						try {
							final PageData pds = new PageData();
							pds.put("DEVICE_CODE", modelAction.get("DEVICE_CODE").toString());
							pds.put("DEVICE_ADDRESS", modelAction.get("DEVICE_ADDRESS").toString());
							pds.put("COMMAND", s);
							
							resendVerification = resendverificationService.find(pds);
							if(resendVerification==null){
								pds.put("DEVICE_TYPE", modelAction.get("DEVICE_TYPE").toString());
								pds.put("ACCEPT_STATE", "wait");
								pds.put("RESENDVERIFICATION_ID", this.get32UUID());	//主键
								resendverificationService.save(pds);
							}
							String str = "ZIGBEE_LIGHT-SEND-" + user_num.get(userCode) + "," + substring + ","
									+ substring2 + "," + s;
							byte[] bs = str.getBytes();	
							packetProcessHelper.processSendDData(modelAction.get("DEVICE_CODE").toString(), bs);
							Timer timer = new Timer();
							timer.schedule(new TimerTask() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									try {
										resendVerification = resendverificationService.find(pds);
										readBackStatus = resendVerification.get("ACCEPT_STATE").toString();
										id=resendVerification.get("RESENDVERIFICATION_ID").toString();
										
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}, 300, 300);
							String flag1 = "";
							readBackStatus = "wait";
							int index = 0;
							while (index < 40) {
								if (readBackStatus.equals("OK")) {
									break;
								} else if (readBackStatus.equals("ERR")) {
									break;
								}else if (readBackStatus.equals("1") && flag1 == "") {
									String stsr = "ZIGBEE_LIGHT-SEND-" + user_num.get(userCode) + "," + substring
											+ "," + substring2 + "," + s;
									byte[] stsrs = stsr.getBytes();
									System.err.println(new String(bs));
									
									packetProcessHelper.processSendDData(modelAction.get("DEVICE_CODE").toString(),
											stsrs);
									flag1 = "1";
								} else if (readBackStatus.equals("2") && flag1 == "1") {
									String stsr = "ZIGBEE_LIGHT-SEND-" + user_num.get(userCode) + "," + substring
											+ "," + substring2 + "," + s;
									byte[] stsrs = stsr.getBytes();
									System.err.println(new String(bs));
									packetProcessHelper.processSendDData(modelAction.get("DEVICE_CODE").toString(),
											stsrs);
									flag1 = "2";
								} else if ((readBackStatus.equals("wait") && (index == 3))
										|| (readBackStatus.equals("wait") && (index == 5))) {
									String stsr = "ZIGBEE_LIGHT-SEND-" + user_num.get(userCode) + "," + substring
											+ "," + substring2 + "," + s;
									byte[] stsrs = stsr.getBytes();
									System.err.println(new String(bs));
									packetProcessHelper.processSendDData(modelAction.get("DEVICE_CODE").toString(),
											stsrs);
								}
								Thread.sleep(300);
								index++;
							}
							timer.cancel();
							pds.put("RESENDVERIFICATION_ID",id);
							resendverificationService.delete(pds);
						} catch (InterruptedException e) {
							// TODO: handle exception
						}
						
					}else if(DEVICE_TYPE.equals("1114")){
						String ln = null;
						String deviceType = DEVICE_TYPE.toString();
						String substring = deviceType.substring(1, 2);
						Integer sds = Integer.valueOf(substring);
						Integer s = null;
						if (sds == 1) {
							s = 2262;
						} else {
							s = 1527;
						}

						String substring2 = deviceType.substring(2, 3);
						Integer valueOf2 = Integer.valueOf(substring2);
						Integer b = null;
						if (valueOf2 == 1) {
							b = 315;
						} else {
							b = 433;
						}

						String substring3 = deviceType.substring(3, 4);
						Integer valueOf3 = Integer.valueOf(substring3);
						Integer c = null;
						if (valueOf3 == 1) {
							c = 12;
						}else if (valueOf3 == 2) {
							c = 15;
						}else if (valueOf3 == 3) {
							c = 22;
						}else if (valueOf3 == 4) {
							c = 33;
						}else if (valueOf3 == 5) {
							c = 47;
						}else if(valueOf3 == 6){
							c = 330;
						}else if(valueOf3 == 7){
							c = 390;
						}else if(valueOf3 == 8){
							c = 200;
						}
						
						if (Integer.valueOf(modelAction.get("CONTROL_COMMAND").toString()) == 0) {
							System.err.println( modelAction.get("DEVICE_ADDRESS").toString());
							String string =  modelAction.get("DEVICE_ADDRESS").toString().substring(0, 1);
							String string2 =  modelAction.get("DEVICE_ADDRESS").toString().substring(1, 8);

							if (string.equals("0")) {
								ln = string2;
							} else {
								ln =  modelAction.get("DEVICE_ADDRESS").toString();
							}
							
							String str = "PT" + s + "_" + b + "M-SEND-" + user_num.get(userCode) + "," + c + ","
									+ ln + 2;
							byte[] bs = str.getBytes();
							System.err.println("<?> " + new String(bs));
							packetProcessHelper.processSendDDatas(modelAction.get("DEVICE_CODE").toString(), bs);
						}

						if (Integer.valueOf(modelAction.get("CONTROL_COMMAND").toString()) == 50) {
							String string =  modelAction.get("DEVICE_ADDRESS").toString().substring(0, 1);
							String string2 =  modelAction.get("DEVICE_ADDRESS").toString().substring(1, 8);

							if (string.equals("0")) {
								ln = string2;
							} else {
								ln =  modelAction.get("DEVICE_ADDRESS").toString();
							}
							String str = "PT" + s + "_" + b + "M-SEND-" + user_num.get(userCode) + "," + c + ","
									+ ln + 3;
							byte[] bs = str.getBytes();
							System.err.println(new String(bs));
							packetProcessHelper.processSendDDatas(modelAction.get("DEVICE_CODE").toString(), bs);
						}

						if (Integer.valueOf(modelAction.get("CONTROL_COMMAND").toString()) == 100) {
							String string =  modelAction.get("DEVICE_ADDRESS").toString().substring(0, 1);
							String string2 =  modelAction.get("DEVICE_ADDRESS").toString().substring(1, 8);

							if (string.equals("0")) {
								ln = string2;
							} else {
								ln =  modelAction.get("DEVICE_ADDRESS").toString();
							}
							String str = "PT" + s + "_" + b + "M-SEND-" + user_num.get(userCode) + "," + c + ","
									+ ln + 1;
							byte[] bs = str.getBytes();
							System.err.println(new String(bs));
							packetProcessHelper.processSendDDatas(modelAction.get("DEVICE_CODE").toString(), bs);
						}
					}else if(DEVICE_TYPE.equals("3114")){
						String ln = null;
						String deviceType = DEVICE_TYPE.toString();
						String substring = deviceType.substring(1, 2);
						Integer sds = Integer.valueOf(substring);
						Integer s = null;
						if (sds == 1) {
							s = 2262;
						} else {
							s = 1527;
						}

						String substring2 = deviceType.substring(2, 3);
						Integer valueOf2 = Integer.valueOf(substring2);
						Integer b = null;
						if (valueOf2 == 1) {
							b = 315;
						} else {
							b = 433;
						}

						String substring3 = deviceType.substring(3, 4);
						Integer valueOf3 = Integer.valueOf(substring3);
						Integer c = null;
						if (valueOf3 == 1) {
							c = 12;
						}else if (valueOf3 == 2) {
							c = 15;
						}else if (valueOf3 == 3) {
							c = 22;
						}else if (valueOf3 == 4) {
							c = 33;
						}else if (valueOf3 == 5) {
							c = 47;
						}else if(valueOf3 == 6){
							c = 330;
						}else if(valueOf3 == 7){
							c = 390;
						}else if(valueOf3 == 8){
							c = 200;
						}
						
						if (Integer.valueOf(modelAction.get("CONTROL_COMMAND").toString()) == 0) {
							System.err.println( modelAction.get("DEVICE_ADDRESS").toString());
							String string =  modelAction.get("DEVICE_ADDRESS").toString().substring(0, 1);
							String string2 =  modelAction.get("DEVICE_ADDRESS").toString().substring(1, 8);

							if (string.equals("0")) {
								ln = string2;
							} else {
								ln =  modelAction.get("DEVICE_ADDRESS").toString();
							}
							
							String str = "PT" + s + "_" + b + "M-SEND-" + user_num.get(userCode) + "," + c + ","
									+ ln + 2;
							byte[] bs = str.getBytes();
							System.err.println("<?> " + new String(bs));
							packetProcessHelper.processSendDDatas(modelAction.get("DEVICE_CODE").toString(), bs);
						}

						if (Integer.valueOf(modelAction.get("CONTROL_COMMAND").toString()) == 50) {
							String string =  modelAction.get("DEVICE_ADDRESS").toString().substring(0, 1);
							String string2 =  modelAction.get("DEVICE_ADDRESS").toString().substring(1, 8);

							if (string.equals("0")) {
								ln = string2;
							} else {
								ln =  modelAction.get("DEVICE_ADDRESS").toString();
							}
							String str = "PT" + s + "_" + b + "M-SEND-" + user_num.get(userCode) + "," + c + ","
									+ ln + 3;
							byte[] bs = str.getBytes();
							System.err.println(new String(bs));
							packetProcessHelper.processSendDDatas(modelAction.get("DEVICE_CODE").toString(), bs);
						}

						if (Integer.valueOf(modelAction.get("CONTROL_COMMAND").toString()) == 100) {
							String string =  modelAction.get("DEVICE_ADDRESS").toString().substring(0, 1);
							String string2 =  modelAction.get("DEVICE_ADDRESS").toString().substring(1, 8);

							if (string.equals("0")) {
								ln = string2;
							} else {
								ln =  modelAction.get("DEVICE_ADDRESS").toString();
							}
							String str = "PT" + s + "_" + b + "M-SEND-" + user_num.get(userCode) + "," + c + ","
									+ ln + 1;
							byte[] bs = str.getBytes();
							System.err.println(new String(bs));
							packetProcessHelper.processSendDDatas(modelAction.get("DEVICE_CODE").toString(), bs);
						}
					}
				}
			}else{
				maps.put("message", "该情景模式没有动作");
				maps.put("success", false);
			}
			Thread.sleep(1500);
		}catch (Exception e){
			logger.error(e.toString(), e);
		}
		
		return AppUtil.returnObject(new PageData(), maps);
		
		
		
	}
	
	/**
	 * 电视机控制
	 * @throws Exception 
	 */
	@ResponseBody  
	@RequestMapping(value="/tv_command")  
	public Object  tv_command() {
		logBefore(logger, "电视机控制");
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String, Object> maps = MapUtil.maps();
		try{	
			String DEVICE_ADDRESS = pd.get("DEVICE_ADDRESS").toString();
			String GUEST_ROOM = pd.get("GUEST_ROOM").toString();
			String DEVICE_CODE = pd.get("DEVICE_CODE").toString();
			String VALUES = pd.get("VALUES").toString();
			packNum(GUEST_ROOM);
			PageData host = hostService.findByDeviceCode(DEVICE_CODE);
			if(host!=null){
				if(host.get("STATUS").toString().equals("1")){
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("INFRARED_ADDRESS_CODE", DEVICE_ADDRESS);
					PageData infraredAddressCode = infraredcodeService.findByInfraredAddressCode(map);
					String s;
					if(infraredAddressCode==null){
						s = "0000000000";
					}else{
						s = infraredAddressCode.getString("INFRARED_VERIFY_CODE");
					}
					String str = "ZIGBEE_INFRARED-SEND-" + user_num.get(GUEST_ROOM) + "," + DEVICE_ADDRESS + ","
							+ VALUES + "," + s;
					byte[] bs = str.getBytes();
					System.err.println(new String(bs));
					packetProcessHelper.processSendDDatas(DEVICE_CODE, bs);
				}else{
					maps.put("message", "当前房间主机处于离线状态,请联系前台");
					maps.put("success", false);
				}
			}
			Thread.sleep(800);
			
		}catch (Exception e){
			logger.error(e.toString(), e);
		}
		return AppUtil.returnObject(new PageData(), maps);
	}
	
	/**
	 * 电视机学习
	 * @throws Exception 
	 */
	@ResponseBody  
	@RequestMapping(value="/tv_study",produces = "text/html; charset=UTF-8")  
	public ModelAndView tv_study() {
		logBefore(logger, "电视机学习");
		ModelAndView mv = this.getModelAndView();
		String success = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		
		try{
			/*pd.put("GUEST_ROOM", userCode);*/
			
			String DEVICE_ADDRESS = pd.get("DEVICE_ADDRESS").toString();
			String GUEST_ROOM = pd.get("GUEST_ROOM").toString();
			String DEVICE_CODE = pd.get("DEVICE_CODE").toString();
			String VALUES = pd.get("VALUES").toString();
			packNum(GUEST_ROOM);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("INFRARED_ADDRESS_CODE", DEVICE_ADDRESS);
			PageData infraredAddressCode = infraredcodeService.findByInfraredAddressCode(map);
			String s;
			if(infraredAddressCode==null){
				s = "0000000000";
			}else{
				s = infraredAddressCode.getString("INFRARED_VERIFY_CODE");
			}
			String str = "ZIGBEE_INFRARED-STUDY-" + user_num.get(GUEST_ROOM) + "," + DEVICE_ADDRESS + ","
					+ VALUES + "," + s;
			byte[] bs = str.getBytes();
			System.err.println(new String(bs));
			packetProcessHelper.processSendDDatas(DEVICE_CODE, bs);
			mv.setViewName("system/hostdevice/tv_study");
			
			mv.addObject("pd", pd);
		}catch (Exception e){
			logger.error(e.toString(), e);
		}
		
		return mv;
		
		
		
	}
	
	/**
	 * 空调学习
	 * @throws Exception 
	 */
	@ResponseBody  
	@RequestMapping(value="/air_study",produces = "text/html; charset=UTF-8")  
	public ModelAndView air_study() {
		logBefore(logger, "空调学习");
		ModelAndView mv = this.getModelAndView();
		String success = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		
		try{
			/*pd.put("GUEST_ROOM", userCode);*/
			
			String DEVICE_ADDRESS = pd.get("DEVICE_ADDRESS").toString();
			String GUEST_ROOM = pd.get("GUEST_ROOM").toString();
			String DEVICE_CODE = pd.get("DEVICE_CODE").toString();
			String VALUES = pd.get("VALUES").toString();
			packNum(GUEST_ROOM);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("INFRARED_ADDRESS_CODE", DEVICE_ADDRESS);
			PageData infraredAddressCode = infraredcodeService.findByInfraredAddressCode(map);
			String s;
			if(infraredAddressCode==null){
				s = "0000000000";
			}else{
				s = infraredAddressCode.getString("INFRARED_VERIFY_CODE");
			}
			String str = "ZIGBEE_INFRARED-STUDY-" + user_num.get(GUEST_ROOM) + "," + DEVICE_ADDRESS + ","
					+ VALUES + "," + s;
			byte[] bs = str.getBytes();
			System.err.println(new String(bs));
			packetProcessHelper.processSendDDatas(DEVICE_CODE, bs);
			mv.setViewName("system/hostdevice/air_study");
			
			mv.addObject("pd", pd);
		}catch (Exception e){
			logger.error(e.toString(), e);
		}
		
		return mv;
		
		
		
	}
	/**
	 * 空调控制
	 * @throws Exception 
	 */
	@ResponseBody  
	@RequestMapping(value="/air_command",produces = "text/html; charset=UTF-8")  
	public ModelAndView air_command() {
		logBefore(logger, "空调控制");
		ModelAndView mv = this.getModelAndView();
		String success = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		
		try{
			/*pd.put("GUEST_ROOM", userCode);*/
			
			String DEVICE_ADDRESS = pd.get("DEVICE_ADDRESS").toString();
			String GUEST_ROOM = pd.get("GUEST_ROOM").toString();
			String DEVICE_CODE = pd.get("DEVICE_CODE").toString();
			String VALUES = pd.get("VALUES").toString();
			packNum(GUEST_ROOM);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("INFRARED_ADDRESS_CODE", DEVICE_ADDRESS);
			PageData infraredAddressCode = infraredcodeService.findByInfraredAddressCode(map);
			String s;
			if(infraredAddressCode==null){
				s = "0000000000";
			}else{
				s = infraredAddressCode.getString("INFRARED_VERIFY_CODE");
			}
			String str = "ZIGBEE_INFRARED-SEND-" + user_num.get(GUEST_ROOM) + "," + DEVICE_ADDRESS + ","
					+ VALUES + "," + s;
			byte[] bs = str.getBytes();
			System.err.println(new String(bs));
			packetProcessHelper.processSendDDatas(DEVICE_CODE, bs);
			Thread.sleep(1500);
			mv.setViewName("system/hostdevice/air_command");
			
			mv.addObject("pd", pd);
		}catch (Exception e){
			logger.error(e.toString(), e);
		}
		
		return mv;
		
		
		
	}
	
	/**
	 * 控制
	 * @throws Exception 
	 */
	@ResponseBody  
	@RequestMapping(value="/sadasdas")  
	public Object commands(Page page) {
		logBefore(logger, "控制");
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String, Object> maps = MapUtil.maps();
		try{
			String deviceAddress = pd.getString("deviceAddress");
			System.err.println(deviceAddress);
			String userCode = pd.getString("GUEST_ROOM");
			System.err.println(userCode);
			String deviceCode = pd.getString("deviceCode");
			System.err.println(deviceCode);
			String command = pd.getString("command");
			System.err.println(command);
			String commandType = pd.getString("commandType");
			System.err.println(commandType);
			packNum(userCode);
			PageData host = hostService.findByDeviceCode(deviceCode);
			if(host!=null){
				if(host.get("STATUS").toString().equals("1")){
					if(commandType.equals("1")){
						String Address = deviceAddress;
						final String substring = Address.substring(0, Address.length()-1);
						String substring2 = Address.substring(Address.length()-1, Address.length());
						String strss = "ZIGBEE_LIGHT-SEND-" + user_num.get(userCode) + "," +substring + ","
								+ substring2 + "," + command;
						byte[] bss = strss.getBytes();
						System.err.println(new String(bss));
						packetProcessHelper.processSendDDatas(deviceCode, bss);
					}else if(commandType.equals("2")){
						Integer commands = null;
						if (Integer.valueOf(command) == 0) {
							commands = 19;
						}

						if (Integer.valueOf(command) == 100) {
							commands = 17;
						} else {
							commands = 20;
						}
						final int commandss;
						if (Integer.valueOf(command) >= 95) {
							commandss = 95;
						} else {
							commandss = Integer.valueOf(command);
						}
						String str = "ZIGBEE_CURTAIN-SEND-" + user_num.get(userCode) + ","
								+ deviceAddress + "," + commands + "," + commandss;
						
						byte[] bs = str.getBytes();
						System.err.println(new String(bs));
						packetProcessHelper.processSendDDatas(deviceCode, bs);
					}else if(commandType.equals("1114")){
						String ln = null;
						String deviceType = commandType.toString();
						String substring = deviceType.substring(1, 2);
						Integer valueOf = Integer.valueOf(substring);
						Integer s = null;
						if (valueOf == 1) {
							s = 2262;
						} else {
							s = 1527;
						}

						String substring2 = deviceType.substring(2, 3);
						Integer valueOf2 = Integer.valueOf(substring2);
						Integer b = null;
						if (valueOf2 == 1) {
							b = 315;
						} else {
							b = 433;
						}

						String substring3 = deviceType.substring(3, 4);
						Integer valueOf3 = Integer.valueOf(substring3);
						Integer c = null;
						if (valueOf3 == 1) {
							c = 12;
						}else if (valueOf3 == 2) {
							c = 15;
						}else if (valueOf3 == 3) {
							c = 22;
						}else if (valueOf3 == 4) {
							c = 33;
						}else if (valueOf3 == 5) {
							c = 47;
						}else if(valueOf3 == 6){
							c = 330;
						}else if(valueOf3 == 7){
							c = 390;
						}else if(valueOf3 == 8){
							c = 200;
						}
						
						
						if (Integer.valueOf(command) == 0) {
							System.err.println(deviceAddress);
							String string = deviceAddress.substring(0, 1);
							String string2 = deviceAddress.substring(1, 8);

							if (string.equals("0")) {
								ln = string2;
							} else {
								ln = deviceAddress;
							}
							
							String str = "PT" + s + "_" + b + "M-SEND-" + user_num.get(userCode) + "," + c + ","
									+ ln + 2;
							byte[] bs = str.getBytes();
							System.err.println("<?> " + new String(bs));
							packetProcessHelper.processSendDDatas(deviceCode, bs);
						}

						if (Integer.valueOf(command) == 50) {
							String string = deviceAddress.substring(0, 1);
							String string2 = deviceAddress.substring(1, 8);

							if (string.equals("0")) {
								ln = string2;
							} else {
								ln = deviceAddress;
							}
							String str = "PT" + s + "_" + b + "M-SEND-" + user_num.get(userCode) + "," + c + ","
									+ ln + 3;
							byte[] bs = str.getBytes();
							System.err.println(new String(bs));
							packetProcessHelper.processSendDDatas(deviceCode, bs);
						}

						if (Integer.valueOf(command) == 100) {
							String string = deviceAddress.substring(0, 1);
							String string2 = deviceAddress.substring(1, 8);

							if (string.equals("0")) {
								ln = string2;
							} else {
								ln = deviceAddress;
							}
							String str = "PT" + s + "_" + b + "M-SEND-" + user_num.get(userCode) + "," + c + ","
									+ ln + 1;
							byte[] bs = str.getBytes();
							System.err.println(new String(bs));
							packetProcessHelper.processSendDDatas(deviceCode, bs);
						}
					}else if(commandType.equals("3114")){
						String ln = null;
						String deviceType = commandType.toString();
						String substring = deviceType.substring(1, 2);
						Integer valueOf = Integer.valueOf(substring);
						Integer s = null;
						if (valueOf == 1) {
							s = 2262;
						} else {
							s = 1527;
						}

						String substring2 = deviceType.substring(2, 3);
						Integer valueOf2 = Integer.valueOf(substring2);
						Integer b = null;
						if (valueOf2 == 1) {
							b = 315;
						} else {
							b = 433;
						}

						String substring3 = deviceType.substring(3, 4);
						Integer valueOf3 = Integer.valueOf(substring3);
						Integer c = null;
						if (valueOf3 == 1) {
							c = 12;
						}else if (valueOf3 == 2) {
							c = 15;
						}else if (valueOf3 == 3) {
							c = 22;
						}else if (valueOf3 == 4) {
							c = 33;
						}else if (valueOf3 == 5) {
							c = 47;
						}else if(valueOf3 == 6){
							c = 330;
						}else if(valueOf3 == 7){
							c = 390;
						}else if(valueOf3 == 8){
							c = 200;
						}
						
						
						if (Integer.valueOf(command) == 0) {
							System.err.println(deviceAddress);
							String string = deviceAddress.substring(0, 1);
							String string2 = deviceAddress.substring(1, 8);

							if (string.equals("0")) {
								ln = string2;
							} else {
								ln = deviceAddress;
							}
							
							String str = "PT" + s + "_" + b + "M-SEND-" + user_num.get(userCode) + "," + c + ","
									+ ln + 2;
							byte[] bs = str.getBytes();
							System.err.println("<?> " + new String(bs));
							packetProcessHelper.processSendDDatas(deviceCode, bs);
						}

						if (Integer.valueOf(command) == 50) {
							String string = deviceAddress.substring(0, 1);
							String string2 = deviceAddress.substring(1, 8);

							if (string.equals("0")) {
								ln = string2;
							} else {
								ln = deviceAddress;
							}
							String str = "PT" + s + "_" + b + "M-SEND-" + user_num.get(userCode) + "," + c + ","
									+ ln + 3;
							byte[] bs = str.getBytes();
							System.err.println(new String(bs));
							packetProcessHelper.processSendDDatas(deviceCode, bs);
						}

						if (Integer.valueOf(command) == 100) {
							String string = deviceAddress.substring(0, 1);
							String string2 = deviceAddress.substring(1, 8);

							if (string.equals("0")) {
								ln = string2;
							} else {
								ln = deviceAddress;
							}
							String str = "PT" + s + "_" + b + "M-SEND-" + user_num.get(userCode) + "," + c + ","
									+ ln + 1;
							byte[] bs = str.getBytes();
							System.err.println(new String(bs));
							packetProcessHelper.processSendDDatas(deviceCode, bs);
						}
					}else{
						System.err.println("88888888^^^**&%^&*****");
					}
				}else{
					maps.put("message", "当前房间主机处于离线状态,请联系前台");
					maps.put("success", false);
				}
			}
			Thread.sleep(800);
		}catch (Exception e){
			logger.error(e.toString(), e);
		}
		
		return AppUtil.returnObject(new PageData(), maps);
	
	}
	
	/**
	 * 列表
	 * @throws Exception 
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page) throws Exception{
		logBefore(logger, "列表HostDevice");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限
		Subject currentUser = SecurityUtils.getSubject();  
		Session session = currentUser.getSession();
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		
		String USERNAME = pd.getString("GUEST_ROOM");
		String s;
		if(USERNAME==null){
			s = (String) session.getAttribute("ssssd");
			pd.put("GUEST_ROOM", s);
			
		}else{
			s = USERNAME.trim().toString();
			pd.put("GUEST_ROOM", s);
			
		}
		System.err.println("----000000)))) "+s);
		List<PageData> model = modelService.findByRoom(pd);
		List<PageData> findByRoom = hostdeviceService.findByRoom(pd);
		for (int i = 0; i < findByRoom.size(); i++) {
			Thread.sleep(1000);
			packNum(USERNAME);
			PageData pageData = findByRoom.get(i);
			
			String DEVICE_TYPE = pageData.get("DEVICE_TYPE").toString();
			if(DEVICE_TYPE.equals("1")){
				String substring = pageData.get("DEVICE_ADDRESS").toString().substring(0, pageData.get("DEVICE_ADDRESS").toString().length()-1);
				String str = "ZIGBEE_LIGHT-READ-" + user_num.get(USERNAME) + "," + substring;
				byte[] bs = str.getBytes();
				System.err.println(new String(bs));
				packetProcessHelper.processSendDData(pageData.get("DEVICE_CODE").toString(), bs);
				StaticUtil.QUERYSTATE.put(pageData.get("DEVICE_CODE").toString() + "_" + "A", new String[] {
					USERNAME, new Date().getTime() + "" });
			}else if(DEVICE_TYPE.equals("2")){
				String str = "ZIGBEE_CURTAIN-READ-" + user_num.get(USERNAME) + ","
						+ pageData.get("DEVICE_ADDRESS").toString() + "," + "21";
				byte[] bs = str.getBytes();
				System.err.println(new String(bs));
				packetProcessHelper.processSendDData(pageData.get("DEVICE_CODE").toString(), bs);
				StaticUtil.CURTAIN.put(pageData.get("DEVICE_CODE").toString() + "_" + "A", new String[] {
					USERNAME, new Date().getTime() + "" });
			}
		}
		page.setPd(pd);
		
		List<PageData>	varList = hostdeviceService.list(page);	//列出HostDevice列表
		
		mv.setViewName("system/hostdevice/hostdevice_list");
		mv.addObject("varList", varList);
		mv.addObject("model", model);
		mv.addObject("pd", pd);
		mv.addObject(Const.SESSION_QX,this.getHC());	//按钮权限
		return mv;
	}
	
	/**
	 * 列表
	 * @throws Exception 
	 */
	@RequestMapping(value="/lists")
	public ModelAndView lists(Page page) throws Exception{
		
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		
		String USERNAME = pd.getString("GUEST_ROOM");
		/*System.err.println(USERNAME);*/
		if(null != USERNAME && !"".equals(USERNAME)){
			USERNAME = USERNAME.trim();
			pd.put("GUEST_ROOM", USERNAME);
			List<PageData> findByRoom = hostdeviceService.findByRoom(pd);
			for (int i = 0; i < findByRoom.size(); i++) {
				Thread.sleep(1000);
				packNum(USERNAME);
				PageData pageData = findByRoom.get(i);
				
				String DEVICE_TYPE = pageData.get("DEVICE_TYPE").toString();
				if(DEVICE_TYPE.equals("1")){
					String substring = pageData.get("DEVICE_ADDRESS").toString().substring(0, pageData.get("DEVICE_ADDRESS").toString().length()-1);
					String str = "ZIGBEE_LIGHT-READ-" + user_num.get(USERNAME) + "," + substring;
					byte[] bs = str.getBytes();
					System.err.println(new String(bs));
					packetProcessHelper.processSendDData(pageData.get("DEVICE_CODE").toString(), bs);
					StaticUtil.QUERYSTATE.put(pageData.get("DEVICE_CODE").toString() + "_" + "A", new String[] {
						USERNAME, new Date().getTime() + "" });
				}else if(DEVICE_TYPE.equals("2")){
					String str = "ZIGBEE_CURTAIN-READ-" + user_num.get(USERNAME) + ","
							+ pageData.get("DEVICE_ADDRESS").toString() + "," + "21";
					byte[] bs = str.getBytes();
					System.err.println(new String(bs));
					packetProcessHelper.processSendDData(pageData.get("DEVICE_CODE").toString(), bs);
					StaticUtil.CURTAIN.put(pageData.get("DEVICE_CODE").toString() + "_" + "A", new String[] {
						USERNAME, new Date().getTime() + "" });
				}
			}
		}else{
			pd.put("GUEST_ROOM", "");
		}
		
		page.setPd(pd);
		List<PageData> all = userService.getAll(pd);
		List<PageData>	varList = hostdeviceService.lists(page);
		mv.setViewName("system/hostdevice/hostdevice_lists");
		mv.addObject("varList", varList);
		mv.addObject("allUser", all);
		mv.addObject("pd", pd);
		mv.addObject(Const.SESSION_QX,this.getHC());	//按钮权限
		return mv;
	}
	
	/**
	 * 去新增页面
	 */
	@RequestMapping(value="/goAdd")
	public ModelAndView goAdd(){
		logBefore(logger, "去新增HostDevice页面");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			mv.setViewName("system/hostdevice/hostdevice_edit");
			mv.addObject("msg", "save");
			mv.addObject("pd", pd);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}						
		return mv;
	}	
	
	/**
	 * 去电视机控制页面
	 */
	@RequestMapping(value="/goTvCommand")
	public ModelAndView goTvCommand(){
		logBefore(logger, "去电视机控制页面");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			String string = pd.get("DEVICE_ADDRESS").toString();
			String string2 = pd.get("GUEST_ROOM").toString();
			String string3 = pd.get("DEVICE_CODE").toString();
			mv.setViewName("system/hostdevice/tv_command");
			mv.addObject("msg", "save");
			mv.addObject("pd", pd);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}						
		return mv;
	}
	
	
	/**
	 * 去电视机控制页面
	 */
	@RequestMapping(value="/goTvStudy")
	public ModelAndView goTvStudy(){
		logBefore(logger, "去电视机学习页面");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			String string = pd.get("DEVICE_ADDRESS").toString();
			String string2 = pd.get("GUEST_ROOM").toString();
			String string3 = pd.get("DEVICE_CODE").toString();
			mv.setViewName("system/hostdevice/tv_study");
			mv.addObject("msg", "save");
			mv.addObject("pd", pd);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}						
		return mv;
	}
	
	/**
	 * 去空调控制页面
	 */
	@RequestMapping(value="/goAirCommand")
	public ModelAndView goAirCommand(){
		logBefore(logger, "去空调控制页面");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			String string = pd.get("DEVICE_ADDRESS").toString();
			String string2 = pd.get("GUEST_ROOM").toString();
			String string3 = pd.get("DEVICE_CODE").toString();
			mv.setViewName("system/hostdevice/air_command");
			mv.addObject("msg", "save");
			mv.addObject("pd", pd);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}						
		return mv;
	}
	
	/**
	 * 去空调学习页面
	 */
	@RequestMapping(value="/goAirStudy")
	public ModelAndView goAirStudy(){
		logBefore(logger, "去空调学习页面");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			String string = pd.get("DEVICE_ADDRESS").toString();
			String string2 = pd.get("GUEST_ROOM").toString();
			String string3 = pd.get("DEVICE_CODE").toString();
			mv.setViewName("system/hostdevice/air_study");
			mv.addObject("msg", "save");
			mv.addObject("pd", pd);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}						
		return mv;
	}
	
	/**
	 * 去修改页面
	 */
	@RequestMapping(value="/goEdit")
	public ModelAndView goEdit(){
		logBefore(logger, "去修改HostDevice页面");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			pd = hostdeviceService.findById(pd);	//根据ID读取
			mv.setViewName("system/hostdevice/hostdevice_edit");
			mv.addObject("msg", "edit");
			mv.addObject("pd", pd);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}						
		return mv;
	}	
	
	/**
	 * 批量删除
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() {
		logBefore(logger, "批量删除HostDevice");
		if(!Jurisdiction.buttonJurisdiction(menuUrlss, "dell")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			pd = this.getPageData();
			List<PageData> pdList = new ArrayList<PageData>();
			String DATA_IDS = pd.getString("DATA_IDS");
			if(null != DATA_IDS && !"".equals(DATA_IDS)){
				String ArrayDATA_IDS[] = DATA_IDS.split(",");
				hostdeviceService.deleteAll(ArrayDATA_IDS);
				pd.put("msg", "ok");
			}else{
				pd.put("msg", "no");
			}
			pdList.add(pd);
			map.put("list", pdList);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		} finally {
			logAfter(logger);
		}
		return AppUtil.returnObject(pd, map);
	}
	
	/**
	 * 打开上传EXCEL页面
	 */
	@RequestMapping(value="/goUploadExcel")
	public ModelAndView goUploadExcel()throws Exception{
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("system/hostdevice/uploadexcel");
		return mv;
	}
	/**
	 * 下载模版
	 */
	@RequestMapping(value="/downExcel")
	public void downExcel(HttpServletResponse response)throws Exception{
		
		FileDownload.fileDownload(response, PathUtil.getClasspath() + Const.FILEPATHFILE + "HostDevice.xls", "HostDevice.xls");
		
	}
	/**
	 * 从EXCEL导入到数据库
	 */
	@RequestMapping(value="/readExcel")
	public ModelAndView readExcel(
			@RequestParam(value="excel",required=false) MultipartFile file
			) throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;}
		if (null != file && !file.isEmpty()) {
			String filePath = PathUtil.getClasspath() + Const.FILEPATHFILE;								//文件上传路径
			String fileName =  FileUpload.fileUp(file, filePath, "hostdeviceexcel");							//执行上传
			
			List<PageData> listPd = (List)ObjectExcelRead.readExcel(filePath, fileName, 2, 0, 0);	//执行读EXCEL操作,读出的数据导入List 2:从第3行开始；0:从第A列开始；0:第0个sheet
			
			/*存入数据库操作======================================*/
			/**
			 * var0 :主机序列号
			 * var1 :客房号
			 * var2 :设备类型
			 * var3 :设备名称
			 * var4 :设备地址码
			 */
			System.err.println("导入大小: "+listPd.size());
			
			if(listPd.size()>2){
				for(int i=0;i<listPd.size();i++){
					pd.put("DEVICE_NAME", listPd.get(i).getString("var0"));
					PageData data = hostService.findByDeviceName(pd);
					if(data==null){
						continue;
					}else{
						pd.put("HOSTDEVICE_ID", this.get32UUID());										
						pd.put("DEVICE_CODE", data.get("DEVICE_CODE"));							
						String GUEST_ROOM = listPd.get(i).getString("var1");	
						pd.put("GUEST_ROOM", GUEST_ROOM);
						if(listPd.get(i).getString("var2").equals("双向灯")){
							pd.put("DEVICE_TYPE", "1");
						}else if(listPd.get(i).getString("var2").equals("窗帘")){
							pd.put("DEVICE_TYPE", "2");
						}else if(listPd.get(i).getString("var2").equals("射频灯")){
							pd.put("DEVICE_TYPE", "1114");
						}else if(listPd.get(i).getString("var2").equals("射频窗帘")){
							pd.put("DEVICE_TYPE", "3114");
						}else if(listPd.get(i).getString("var2").equals("电视机")){
							pd.put("DEVICE_TYPE", "98");
						}else if(listPd.get(i).getString("var2").equals("空调")){
							pd.put("DEVICE_TYPE", "99");
						}
						
						pd.put("NICK_NAME", listPd.get(i).getString("var3"));	//备注
						SendMsgUtil s = new SendMsgUtil();
						
						if(listPd.get(i).getString("var2").equals("射频灯")){
							pd.put("DEVICE_ADDRESS", s.createRandom());
						}else if(listPd.get(i).getString("var2").equals("射频窗帘")){
							pd.put("DEVICE_ADDRESS", s.createRandom());
						}else{
							pd.put("DEVICE_ADDRESS", listPd.get(i).getString("var4"));
						}
						
						hostdeviceService.save(pd);
					}
					
				}
				/*存入数据库操作======================================*/
				
				mv.addObject("msg","success");
			}else{
				mv.addObject("msg","two");
			}
			
		}
		
		mv.setViewName("save_result");
		return mv;
	}
	
	/*
	 * 导出到excel
	 * @return
	 */
	@RequestMapping(value="/excel")
	public ModelAndView exportExcel(){
		logBefore(logger, "导出HostDevice到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			Map<String,Object> dataMap = new HashMap<String,Object>();
			List<String> titles = new ArrayList<String>();
			titles.add("主机序列号");	//1
			titles.add("客房号");	//2
			titles.add("设备类型");	//3
			titles.add("设备名称");	//5
			titles.add("设备地址码");	//4
			dataMap.put("titles", titles);
			List<PageData> varOList = hostdeviceService.listAll(pd);
			List<PageData> varList = new ArrayList<PageData>();
			for(int i=0;i<varOList.size();i++){
				PageData vpd = new PageData();
				vpd.put("var1", varOList.get(i).getString("DEVICE_CODE"));	//1
				vpd.put("var2", varOList.get(i).getString("GUEST_ROOM"));	//2
				if(varOList.get(i).getString("DEVICE_TYPE").equals("1")){
					vpd.put("var3", "双向灯");	//3
				}else if(varOList.get(i).getString("DEVICE_TYPE").equals("2")){
					vpd.put("var3", "窗帘");	//3
				}else if(varOList.get(i).getString("DEVICE_TYPE").equals("1114")){
					vpd.put("var3", "射频");	//3
				}
				
				vpd.put("var4", varOList.get(i).getString("NICK_NAME"));	//4
				vpd.put("var5", varOList.get(i).getString("DEVICE_ADDRESS"));	//4
				varList.add(vpd);
			}
			dataMap.put("varList", varList);
			ObjectExcelView erv = new ObjectExcelView();
			mv = new ModelAndView(erv,dataMap);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	
	
	
	/* ===============================权限================================== */
	public Map<String, String> getHC(){
		Subject currentUser = SecurityUtils.getSubject();  //shiro管理的session
		Session session = currentUser.getSession();
		return (Map<String, String>)session.getAttribute(Const.SESSION_QX);
	}
	/* ===============================权限================================== */
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
