/**
 * 版权所有 北京思开科技有限公司 
 * All Rights Reserved
 */
package com.bysakai.mooc.api.impl;

import java.util.List;

import com.bysakai.mooc.api.CategoryService;
import com.bysakai.mooc.dao.MoocDao;
import com.bysakai.mooc.model.Categorys;

/**
 * @author nesdu
 *
 */
public class CategoryServiceImpl implements CategoryService {

	private MoocDao moocDao;
	
	 
	public MoocDao getMoocDao() {
		return moocDao;
	}

	public void setMoocDao(MoocDao moocDao) {
		this.moocDao = moocDao;
	}

	/* (non-Javadoc)
	 * @see com.bysakai.mooc.api.CategoryService#list()
	 */
	@Override
	public List<Categorys> list() {
		// TODO Auto-generated method stub
		return moocDao.findAll(Categorys.class);
	}

	@Override
	public void save(Categorys category) {
		if(category!=null && category.getId()!=null && !"".equals(category.getId())){
			moocDao.update(category);
		}else{
			moocDao.save(category);	
		}
	}

	@Override
	public void remove(Categorys category) {
		moocDao.delete(category);
	}

	@Override
	public Categorys findOne(String id) {
		return moocDao.findById(Categorys.class, id);
	}
}
