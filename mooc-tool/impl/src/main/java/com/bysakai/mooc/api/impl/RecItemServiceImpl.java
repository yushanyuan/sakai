/**
 * 版权所有 北京思开科技有限公司 
 * All Rights Reserved
 */
package com.bysakai.mooc.api.impl;

import java.util.List;

import com.bysakai.mooc.api.RecItemService;
import com.bysakai.mooc.dao.MoocDao;
import com.bysakai.mooc.model.RecItem;

/**
 * @author nesdu
 *
 */
public class RecItemServiceImpl implements RecItemService {

	private MoocDao moocDao;
	
	 
	public MoocDao getMoocDao() {
		return moocDao;
	}

	public void setMoocDao(MoocDao moocDao) {
		this.moocDao = moocDao;
	}

	/* (non-Javadoc)
	 * @see com.bysakai.mooc.api.RecListService#list()
	 */
	@Override
	public List<RecItem> list() {
		// TODO Auto-generated method stub
		return moocDao.findAll(RecItem.class);
	}

	@Override
	public void save(RecItem recItem) {
		if(recItem!=null && recItem.getId()!=null && !"".equals(recItem.getId())){
			moocDao.update(recItem);
		}else{
			moocDao.save(recItem);	
		}
	}

	@Override
	public void remove(RecItem recItem) {
		moocDao.delete(recItem);
	}

	@Override
	public RecItem findOne(String id) {
		return moocDao.findById(RecItem.class, id);
	}

}
