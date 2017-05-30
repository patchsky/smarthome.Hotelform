package com.kincony.service.system.resendverification;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kincony.dao.DaoSupport;
import com.kincony.entity.Page;
import com.kincony.util.PageData;


@Service("resendverificationService")
public class ResendVerificationService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/*
	* 新增
	*/
	public void save(PageData pd)throws Exception{
		dao.save("ResendVerificationMapper.save", pd);
	}
	
	/*
	* 删除
	*/
	public void delete(PageData pd)throws Exception{
		dao.delete("ResendVerificationMapper.delete", pd);
	}
	
	/*
	* 修改
	*/
	public void edit(PageData pd)throws Exception{
		dao.update("ResendVerificationMapper.edit", pd);
	}
	
	/*
	* 修改
	*/
	public void edits(Map<String, Object> map)throws Exception{
		dao.update("ResendVerificationMapper.edits", map);
	}
	
	/*
	*列表
	*/
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("ResendVerificationMapper.datalistPage", page);
	}
	
	/*
	*列表(全部)
	*/
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("ResendVerificationMapper.listAll", pd);
	}
	
	/*
	* 通过id获取数据
	*/
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("ResendVerificationMapper.findById", pd);
	}
	
	/*
	* 批量删除
	*/
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("ResendVerificationMapper.deleteAll", ArrayDATA_IDS);
	}
	
	/*
	* 通过参数获取数据
	*/
	public PageData find(PageData pd)throws Exception{
		return (PageData)dao.findForObject("ResendVerificationMapper.find", pd);
	}
	
	/*
	* 通过参数获取数据
	*/
	public PageData findMap(Map<String, Object>map)throws Exception{
		return (PageData)dao.findForObject("ResendVerificationMapper.findMap", map);
	}
}

