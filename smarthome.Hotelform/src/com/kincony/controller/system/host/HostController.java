package com.kincony.controller.system.host;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
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
import com.kincony.util.AppUtil;
import com.kincony.util.FileDownload;
import com.kincony.util.FileUpload;
import com.kincony.util.ObjectExcelRead;
import com.kincony.util.ObjectExcelView;
import com.kincony.util.Const;
import com.kincony.util.PageData;
import com.kincony.util.PathUtil;
import com.kincony.util.Tools;
import com.kincony.util.Jurisdiction;
import com.kincony.server.helper.PacketProcessHelper;
import com.kincony.server.util.StaticUtil;
import com.kincony.service.system.host.HostService;
import com.kincony.service.system.hostdevice.HostDeviceService;

/** 
 * 类名称：HostController
 * 创建人：FH 
 * 创建时间：2016-10-08
 */
@Controller
@RequestMapping(value="/host")
public class HostController extends BaseController {
	
	String menuUrl = "host/list.do"; //菜单地址(权限用)
	@Resource(name="hostService")
	private HostService hostService;
	@Resource(name="hostdeviceService")
	private HostDeviceService hostdeviceService;
	@Resource(name="packetProcessHelper")
	private PacketProcessHelper packetProcessHelper;
	/**
	 * 新增
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, "新增Host");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("HOST_ID", this.get32UUID());	//主键
		hostService.save(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value="/delete")
	public void delete(PrintWriter out){
		logBefore(logger, "删除Host");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			PageData findById = hostService.findById(pd);	//根据ID读取
			if(findById!=null){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("DEVICE_CODE", findById.get("DEVICE_CODE"));
				hostdeviceService.deleteByDeviceCode(map);
			}
			hostService.delete(pd);
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
		logBefore(logger, "修改Host");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		hostService.edit(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**
	 * 列表
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page){
		logBefore(logger, "列表Host");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			String DEVICE_CODE = pd.getString("DEVICE_CODE");
			if(null != DEVICE_CODE && !"".equals(DEVICE_CODE)){
				DEVICE_CODE = DEVICE_CODE.trim();
				pd.put("DEVICE_CODE", DEVICE_CODE);
			}
			System.err.println(DEVICE_CODE);
			String HOST_NUMBER = pd.getString("HOST_NUMBER");
			if(null != HOST_NUMBER && !"".equals(HOST_NUMBER)){
				HOST_NUMBER = HOST_NUMBER.trim();
				pd.put("HOST_NUMBER", HOST_NUMBER);
			}
			page.setPd(pd);
			List<PageData>	varList = hostService.list(page);	//列出Host列表
			mv.setViewName("system/host/host_list");
			mv.addObject("varList", varList);
			mv.addObject("pd", pd);
			mv.addObject(Const.SESSION_QX,this.getHC());	//按钮权限
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**
	 * 去新增页面
	 */
	@RequestMapping(value="/goAdd")
	public ModelAndView goAdd(){
		logBefore(logger, "去新增Host页面");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			mv.setViewName("system/host/host_edit");
			mv.addObject("msg", "save");
			mv.addObject("pd", pd);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}						
		return mv;
	}	
	
	/**
	 * 去配置页面
	 */
	@RequestMapping(value="/hostConfig")
	public ModelAndView hostConfig(){
		logBefore(logger, "去配置hostConfig页面");
		ModelAndView mv = this.getModelAndView();
		Subject currentUser = SecurityUtils.getSubject();  
		Session session = currentUser.getSession();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			String deviceCode = pd.getString("deviceCode");
			String networkStr = "ZIGBEE_CONFIG2-READ-OI";
			byte[] networkBs = networkStr.getBytes();
			System.err.println(new String(networkBs));
			packetProcessHelper.processSendDDatas(deviceCode, networkBs);
			String[] array = StaticUtil.NETWORK.get(deviceCode + "_A");
			
			if (array != null) {
				pd.put("network", array[0]);
				StaticUtil.NETWORK.remove(deviceCode);
			}else{
				pd.put("network", "获取失败");
			}
			try {
				
				Thread.sleep(2000);
				String channelStr = "ZIGBEE_CONFIG2-READ-CH";
				byte[] channelBs = channelStr.getBytes();
				System.err.println(new String(channelBs));
				packetProcessHelper.processSendDDatas(deviceCode, channelBs);
				String[] array2 = StaticUtil.CHANNEL.get(deviceCode + "_A");
				if (array2 != null) {
					pd.put("channel", array2[0]);
					StaticUtil.CHANNEL.remove(deviceCode);
				}else{
					pd.put("channel", "获取失败");
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			pd.put("deviceCode", deviceCode);
			
			
			mv.setViewName("system/host/host_config");
			
			mv.addObject("msg", "save");
			mv.addObject("pd", pd);
			
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}						
		return mv;
	}
	/**
	 * 写入
	 */
	@RequestMapping(value="/write")
	public ModelAndView write() throws Exception{
		logBefore(logger, "写入网络号和信道");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			String DEVICE_CODE = pd.getString("DEVICE_CODE");
			String networkStr = "ZIGBEE_CONFIG2-SEND-OI,"+pd.getString("NETWORK");
			byte[] networkBs = networkStr.getBytes();
			System.err.println(new String(networkBs));
			packetProcessHelper.processSendDDatas(DEVICE_CODE, networkBs);
			
			try {
				
				Thread.sleep(3000);
				String channelStr = "ZIGBEE_CONFIG2-SEND-CH,"+pd.getString("CHANNEL");
				byte[] channelBs = channelStr.getBytes();
				System.err.println(new String(channelBs));
				packetProcessHelper.processSendDDatas(DEVICE_CODE, channelBs);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.err.println("传过来的主机序列号 "+DEVICE_CODE);
			mv.addObject("msg","writesuccess");
			mv.setViewName("save_result");
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	
	/**
	 * 写入
	 */
	@RequestMapping(value="/writeTerminal")
	public ModelAndView writeTerminal() throws Exception{
		logBefore(logger, "写入终端网络号和信道");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			String DEVICE_CODE = pd.getString("DEVICE_CODE");
			String networkStr = "ZIGBEE_CONFIG2-SEND-CF,"+pd.getString("NETWORK");
			byte[] networkBs = networkStr.getBytes();
			/*System.err.println(new String(networkBs));*/
			packetProcessHelper.processSendDDatas(DEVICE_CODE, networkBs);
			
			try {
				
				Thread.sleep(1000);
				String channelStr = "ZIGBEE_CONFIG2-SEND-CN,"+pd.getString("CHANNEL");
				byte[] channelBs = channelStr.getBytes();
				System.err.println(new String(channelBs));
				packetProcessHelper.processSendDDatas(DEVICE_CODE, channelBs);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mv.addObject("msg","writeTerminal");
			mv.setViewName("save_result");
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**
	 * 终端配置
	 */
	@RequestMapping(value="/configTerminal")
	public ModelAndView configTerminal() throws Exception{
		logBefore(logger, "终端配置");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			String DEVICE_CODE = pd.getString("DEVICE_CODE");
			String networkStr = "ZIGBEE_CONFIG2-SEND-OI,"+4096;
			byte[] networkBs = networkStr.getBytes();
			System.err.println(new String(networkBs));
			packetProcessHelper.processSendDDatas(DEVICE_CODE, networkBs);
			try {
				
				Thread.sleep(2000);
				String channelStr = "ZIGBEE_CONFIG2-SEND-CH,"+25;
				byte[] channelBs = channelStr.getBytes();
				System.err.println(new String(channelBs));
				packetProcessHelper.processSendDDatas(DEVICE_CODE, channelBs);				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.err.println("传过来的主机序列号 "+DEVICE_CODE);
			mv.addObject("msg","configsuccess");
			mv.setViewName("save_result");
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
		logBefore(logger, "去修改Host页面");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			pd = hostService.findById(pd);	//根据ID读取
			mv.setViewName("system/host/host_edit");
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
		logBefore(logger, "批量删除Host");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "dell")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			pd = this.getPageData();
			List<PageData> pdList = new ArrayList<PageData>();
			String DATA_IDS = pd.getString("DATA_IDS");
			if(null != DATA_IDS && !"".equals(DATA_IDS)){
				String ArrayDATA_IDS[] = DATA_IDS.split(",");
				hostdeviceService.deleteAllByCode(ArrayDATA_IDS);
				hostService.deleteAll(ArrayDATA_IDS);
				
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
		mv.setViewName("system/host/uploadexcel");
		return mv;
	}
	
	/**
	 * 下载模版
	 */
	@RequestMapping(value="/downExcel")
	public void downExcel(HttpServletResponse response)throws Exception{
		
		FileDownload.fileDownload(response, PathUtil.getClasspath() + Const.FILEPATHFILE + "Host.xls", "Host.xls");
		
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
			String fileName =  FileUpload.fileUp(file, filePath, "hostssd");							//执行上传
			
			List<PageData> listPd = (List)ObjectExcelRead.readExcel(filePath, fileName, 2, 0,0);	//执行读EXCEL操作,读出的数据导入List 2:从第3行开始；0:从第A列开始；0:第0个sheet
			
			/*存入数据库操作======================================*/
			/**
			 * var0 :主机序列号
			 * var1 :
			 */
			System.err.println("导入大小: "+listPd.size());
			
			if(listPd.size()>=1){
				for(int i=0;i<listPd.size();i++){		
					pd.put("HOST_ID", this.get32UUID());
					pd.put("STATUS", "0");
					if(listPd.get(i).getString("var0").equals("")){
						continue;
					}
					pd.put("DEVICE_CODE", listPd.get(i).getString("var0"));
					pd.put("HOST_NUMBER", listPd.get(i).getString("var1"));
					pd.put("DEVICE_NAME", listPd.get(i).getString("var2"));
					if(hostService.findByDeviceCode(listPd.get(i).getString("var0"))!=null){
						continue;
					}
					hostService.save(pd);
				}
				mv.addObject("msg","success");
			}else{
				mv.addObject("msg","two");
			}
			/*存入数据库操作======================================*/
			
			
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
		logBefore(logger, "导出Host到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			Map<String,Object> dataMap = new HashMap<String,Object>();
			List<String> titles = new ArrayList<String>();
			titles.add("主机序列号");	//1
			titles.add("主机编号");	//1
			titles.add("主机昵称");	//3
			
			dataMap.put("titles", titles);
			List<PageData> varOList = hostService.listAll(pd);
			List<PageData> varList = new ArrayList<PageData>();
			for(int i=0;i<varOList.size();i++){
				PageData vpd = new PageData();
				vpd.put("var1", varOList.get(i).getString("DEVICE_CODE"));	//1
				vpd.put("var2", varOList.get(i).getString("HOST_NUMBER"));	//1
				vpd.put("var3", varOList.get(i).getString("DEVICE_NAME"));	//3
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
