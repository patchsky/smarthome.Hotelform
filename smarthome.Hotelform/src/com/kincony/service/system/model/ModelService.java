package com.kincony.service.system.model;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kincony.dao.DaoSupport;
import com.kincony.entity.Page;
import com.kincony.util.PageData;


@Service("modelService")
public class ModelService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/*
	* 新增
	*/
	public void save(PageData pd)throws Exception{
		dao.save("ModelMapper.save", pd);
	}
	
	/*
	* 删除
	*/
	public void delete(PageData pd)throws Exception{
		dao.delete("ModelMapper.delete", pd);
	}
	
	/*
	* 修改
	*/
	public void edit(PageData pd)throws Exception{
		dao.update("ModelMapper.edit", pd);
	}
	
	/*
	*列表
	*/
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("ModelMapper.datalistPage", page);
	}
	
	/*
	*列表(全部)
	*/
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("ModelMapper.listAll", pd);
	}
	
	/*
	* 通过房间获取设备数据 
	*/
	public List<PageData> findByRoom(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("ModelMapper.findByRoom", pd);
	}
	
	/*
	* 通过id获取数据
	*/
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("ModelMapper.findById", pd);
	}
	
	/*
	* 批量删除
	*/
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("ModelMapper.deleteAll", ArrayDATA_IDS);
	}
	
}

