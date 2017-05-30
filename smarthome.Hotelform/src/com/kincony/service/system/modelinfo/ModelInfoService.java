package com.kincony.service.system.modelinfo;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kincony.dao.DaoSupport;
import com.kincony.entity.Page;
import com.kincony.util.PageData;


@Service("modelinfoService")
public class ModelInfoService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/*
	* 新增
	*/
	public void save(PageData pd)throws Exception{
		dao.save("ModelInfoMapper.save", pd);
	}
	
	/*
	* 删除
	*/
	public void delete(PageData pd)throws Exception{
		dao.delete("ModelInfoMapper.delete", pd);
	}
	
	/*
	* 修改
	*/
	public void edit(PageData pd)throws Exception{
		dao.update("ModelInfoMapper.edit", pd);
	}
	
	/*
	*列表
	*/
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("ModelInfoMapper.datalistPage", page);
	}
	
	/*
	* 通过情景模式ID获取数据 
	*/
	public List<PageData> getAllModelAction(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("ModelInfoMapper.getAllModelAction", pd);
	}
	
	/*
	*列表(全部)
	*/
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("ModelInfoMapper.listAll", pd);
	}
	
	/*
	* 通过id获取数据
	*/
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("ModelInfoMapper.findById", pd);
	}
	
	/*
	* 批量删除
	*/
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("ModelInfoMapper.deleteAll", ArrayDATA_IDS);
	}
	
}

