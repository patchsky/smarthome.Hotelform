package com.kincony.service.system.hostdevice;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kincony.dao.DaoSupport;
import com.kincony.entity.Page;
import com.kincony.util.PageData;


@Service("hostdeviceService")
public class HostDeviceService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/*
	* 新增
	*/
	public void save(PageData pd)throws Exception{
		dao.save("HostDeviceMapper.save", pd);
	}
	
	/*
	* 删除
	*/
	public void delete(PageData pd)throws Exception{
		dao.delete("HostDeviceMapper.delete", pd);
	}
	
	/*
	* 删除
	*/
	public void deleteByDeviceCode(Map<String, Object> map)throws Exception{
		dao.delete("HostDeviceMapper.deleteByDeviceCode", map);
	}
	
	/*
	* 修改
	*/
	public void edit(PageData pd)throws Exception{
		dao.update("HostDeviceMapper.edit", pd);
	}
	
	/*
	* 修改设备状态
	*/
	public void editDeviceState(Map<String, Object> map)throws Exception{
		dao.update("HostDeviceMapper.editDeviceState", map);
	}
	
	/*
	*列表
	*/
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("HostDeviceMapper.datalistPage", page);
	}
	
	/*
	*列表
	*/
	public List<PageData> lists(Page page)throws Exception{
		return (List<PageData>)dao.findForList("HostDeviceMapper.datalistPages", page);
	}
	
	/*
	*列表(全部)
	*/
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("HostDeviceMapper.listAll", pd);
	}
	
	/*
	* 通过id获取数据
	*/
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("HostDeviceMapper.findById", pd);
	}
	
	/*
	* 通过deviceAddress获取数据
	*/
	public PageData findByDeviceAddress(String deviceAddress)throws Exception{
		return (PageData)dao.findForObject("HostDeviceMapper.findByDeviceAddress", deviceAddress);
	}
	
	/*
	* 通过deviceCode获取数据
	*/
	public PageData findByDeviceCode(String deviceCode)throws Exception{
		return (PageData)dao.findForObject("HostDeviceMapper.findByDeviceCode", deviceCode);
	}
	
	/*
	* 通过房间获取设备数据 
	*/
	public List<PageData> findByRoom(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("HostDeviceMapper.findByRoom", pd);
	}
	
	/*
	* 通过房间获取设备数据 
	*/
	public List<PageData> getAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("HostDeviceMapper.getAll", pd);
	}
	
	/*
	* 批量删除
	*/
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("HostDeviceMapper.deleteAll", ArrayDATA_IDS);
	}
	
	/*
	* 批量删除
	*/
	public void deleteAllByCode(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("HostDeviceMapper.deleteAllByCode", ArrayDATA_IDS);
	}
	
}

