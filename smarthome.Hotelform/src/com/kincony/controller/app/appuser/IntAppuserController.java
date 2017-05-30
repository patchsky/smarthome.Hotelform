package com.kincony.controller.app.appuser;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kincony.controller.base.BaseController;
import com.kincony.server.helper.PacketProcessHelper;
/*import com.kincony.server.helper.PacketProcessHelper;*/
import com.kincony.service.system.appuser.AppuserService;
import com.kincony.util.AppUtil;
import com.kincony.util.JsonUtil;
import com.kincony.util.MapUtil;
import com.kincony.util.PageData;
import com.kincony.util.Tools;

/**
 * 会员-接口类
 * 
 * 相关参数协议： 00 请求失败 01 请求成功 02 返回空值 03 请求协议参数不完整 04 用户名或密码错误 05 FKEY验证失败
 */
@Controller
@RequestMapping(value = "/appuser")
public class IntAppuserController extends BaseController {

	@Resource(name = "appuserService")
	private AppuserService appuserService;

	@Resource(name = "packetProcessHelper")
	private PacketProcessHelper packetProcessHelper;
	private static Map<String, Integer> user_num = new HashMap<String, Integer>();

	/**
	 * 包序号分装方法
	 * 
	 * @param userCode
	 */
	public void packNum(String userCode) {
		if (user_num.get(userCode) == null) {
			user_num.put(userCode, 0);
		} else {
			user_num.put(userCode,
					user_num.get(userCode) == 255 ? 0
							: user_num.get(userCode) + 1);
		}
	}

	/**
	 * 根据用户名获取会员信息
	 */
	@RequestMapping(value = "/getAppuserByUm")
	@ResponseBody
	public Object getAppuserByUsernmae() {
		logBefore(logger, "根据用户名获取会员信息");
		Map<String, Object> map = new HashMap<String, Object>();
		PageData pd = new PageData();
		pd = this.getPageData();
		String result = "00";

		try {
			if (Tools.checkKey("USERNAME", pd.getString("FKEY"))) { // 检验请求key值是否合法
				if (AppUtil.checkParam("getAppuserByUsernmae", pd)) { // 检查参数
					pd = appuserService.findByUId(pd);

					map.put("pd", pd);
					result = (null == pd) ? "02" : "01";

				} else {
					result = "03";
				}
			} else {
				result = "05";
			}
		} catch (Exception e) {
			logger.error(e.toString(), e);
		} finally {
			map.put("result", result);
			logAfter(logger);
		}
		
		return JSONObject.fromObject(result);
	}

	/**
	 * 根据用户名获取会员信息
	 */
	@RequestMapping(value = "/test")
	@ResponseBody
	public Object test() {
		logBefore(logger, "根据用户名获取会员信息");
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String, Object> maps = MapUtil.maps();
		if(pd.get("userCode")==null){
			pd.put("userCode", "");
		}
		if(pd.get("userCode").equals("")){
			maps.put("message", "userCode不能为空");
			maps.put("success", false);
		}else{
			List<String> sad = new ArrayList<String>();
			sad.add("SD");
			maps.put("date", sad);
		}
		return AppUtil.returnObject(new PageData(), maps);
	}
}
