package com.kincony.controller.system.modelinfo;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kincony.controller.base.BaseController;
import com.kincony.entity.Page;
import com.kincony.util.AppUtil;
import com.kincony.util.ObjectExcelView;
import com.kincony.util.Const;
import com.kincony.util.PageData;
import com.kincony.util.Tools;
import com.kincony.util.Jurisdiction;
import com.kincony.service.system.hostdevice.HostDeviceService;
import com.kincony.service.system.model.ModelService;
import com.kincony.service.system.modelinfo.ModelInfoService;
import com.kincony.service.system.user.UserService;

/** 
 * 类名称：ModelInfoController
 * 创建人：FH 
 * 创建时间：2016-11-29
 */
@Controller
@RequestMapping(value="/modelinfo")
public class ModelInfoController extends BaseController {
	
	String menuUrl = "modelinfo/list.do"; //菜单地址(权限用)
	@Resource(name="modelinfoService")
	private ModelInfoService modelinfoService;
	@Resource(name="modelService")
	private ModelService modelService;
	@Resource(name="hostdeviceService")
	private HostDeviceService hostdeviceService;
	@Resource(name="userService")
	private UserService userService;
	/**
	 * 新增
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, "新增ModelInfo");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData pageData = hostdeviceService.findByDeviceAddress(pd.get("DEVICE_ADDRESS").toString());
		
		pd.put("MODELINFO_ID", this.get32UUID());	//主键
		pd.put("DEVICE_CODE", pageData.get("DEVICE_CODE"));
		pd.put("DEVICE_TYPE", pageData.get("DEVICE_TYPE"));
		
		modelinfoService.save(pd);
		
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value="/delete")
	public void delete(PrintWriter out){
		logBefore(logger, "删除ModelInfo");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			modelinfoService.delete(pd);
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
		logBefore(logger, "修改ModelInfo");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		modelinfoService.edit(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**
	 * 列表
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page){
		logBefore(logger, "列表ModelInfo");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			page.setPd(pd);
			List<PageData> all = userService.getAll(pd);
			List<PageData>	varList = modelinfoService.list(page);	//列出ModelInfo列表
			mv.setViewName("system/modelinfo/modelinfo_list");
			mv.addObject("varList", varList);
			mv.addObject("allUser", all);
			mv.addObject("pd", pd);
			mv.addObject(Const.SESSION_QX,this.getHC());	//按钮权限
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**
	 * 去选择客房页面
	 */
	@RequestMapping(value="/goChooseRoom")
	public ModelAndView goChooseRoom(){
		logBefore(logger, "去选择客房页面");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			
			List<PageData> all = userService.getAll(pd);
			/*List<PageData> model = modelService.findByRoom(pd);
			List<PageData> getAll = hostdeviceService.getAll(pd);*/
			mv.setViewName("system/modelinfo/choose_room");
			mv.addObject("allUser", all);
			mv.addObject("pd", pd);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}						
		return mv;
	}	
	
	
	/**
	 * 去新增页面
	 */
	@RequestMapping(value="/goAdd")
	public ModelAndView goAdd(){
		logBefore(logger, "去新增ModelInfo页面");
		Subject currentUser = SecurityUtils.getSubject();  
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String GUEST_ROOM = pd.getString("GUEST_ROOM");
		try {
			
			pd.put(GUEST_ROOM, GUEST_ROOM);
			List<PageData> model = modelService.findByRoom(pd);
			List<PageData> getAll = hostdeviceService.getAll(pd);
			mv.setViewName("system/modelinfo/modelinfo_add");
			mv.addObject("model", model);
			mv.addObject("getAll", getAll);
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
		logBefore(logger, "去修改ModelInfo页面");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			pd = modelinfoService.findById(pd);	//根据ID读取
			mv.setViewName("system/modelinfo/modelinfo_edit");
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
		logBefore(logger, "批量删除ModelInfo");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "dell")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			pd = this.getPageData();
			List<PageData> pdList = new ArrayList<PageData>();
			String DATA_IDS = pd.getString("DATA_IDS");
			if(null != DATA_IDS && !"".equals(DATA_IDS)){
				String ArrayDATA_IDS[] = DATA_IDS.split(",");
				modelinfoService.deleteAll(ArrayDATA_IDS);
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
	
	/*
	 * 导出到excel
	 * @return
	 */
	@RequestMapping(value="/excel")
	public ModelAndView exportExcel(){
		logBefore(logger, "导出ModelInfo到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			Map<String,Object> dataMap = new HashMap<String,Object>();
			List<String> titles = new ArrayList<String>();
			titles.add("MODEL_ID");	//1
			titles.add("DEVICE_ADDRESS");	//2
			titles.add("DEVICE_TYPE");	//3
			titles.add("CONTROL_COMMAND");	//4
			titles.add("DELAY_VALUES");	//5
			dataMap.put("titles", titles);
			List<PageData> varOList = modelinfoService.listAll(pd);
			List<PageData> varList = new ArrayList<PageData>();
			for(int i=0;i<varOList.size();i++){
				PageData vpd = new PageData();
				vpd.put("var1", varOList.get(i).getString("MODEL_ID"));	//1
				vpd.put("var2", varOList.get(i).getString("DEVICE_ADDRESS"));	//2
				vpd.put("var3", varOList.get(i).getString("DEVICE_TYPE"));	//3
				vpd.put("var4", varOList.get(i).getString("CONTROL_COMMAND"));	//4
				vpd.put("var5", varOList.get(i).getString("DELAY_VALUES"));	//5
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
