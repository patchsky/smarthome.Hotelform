package com.kincony.service.system.host;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kincony.dao.DaoSupport;
import com.kincony.entity.Page;
import com.kincony.util.PageData;


@Service("hostService")
public class HostService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/*
	* 新增
	*/
	public void save(PageData pd)throws Exception{
		dao.save("HostMapper.save", pd);
	}
	
	/*
	* 删除
	*/
	public void delete(PageData pd)throws Exception{
		dao.delete("HostMapper.delete", pd);
	}
	
	/*
	* 服务停止修改所有主机状态为离线0
	*/
	public void editAllStatus()throws Exception{
		dao.update("HostMapper.editAllStatus");
	}
	
	/*
	* 修改
	*/
	public void edit(PageData pd)throws Exception{
		dao.update("HostMapper.edit", pd);
	}
	
	/*
	* 修改状态
	*/
	public void editStatus(Map<String, Object> map)throws Exception{
		dao.update("HostMapper.editStatus", map);
	}
	
	/*
	*列表
	*/
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("HostMapper.datalistPage", page);
	}
	
	/*
	*列表(全部)
	*/
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("HostMapper.listAll", pd);
	}
	
	/*
	* 通过id获取数据
	*/
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("HostMapper.findById", pd);
	}
	
	/*
	* 通过id获取数据
	*/
	public PageData findByDeviceName(PageData pd)throws Exception{
		return (PageData)dao.findForObject("HostMapper.findByDeviceName", pd);
	}
	
	/*
	* 通过DeviceCode获取数据
	*/
	public PageData findByDeviceCode(String deviceCode)throws Exception{
		return (PageData)dao.findForObject("HostMapper.findByDeviceCode", deviceCode);
	}
	
	/*
	* 批量删除
	*/
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("HostMapper.deleteAll", ArrayDATA_IDS);
	}
}

