/**
 * 版权所有 北京思开科技有限公司 
 * All Rights Reserved
 */
package com.bysakai.mooc.api.impl;

import java.util.List;

import com.bysakai.mooc.api.TeacherService;
import com.bysakai.mooc.dao.MoocDao;
import com.bysakai.mooc.model.Teacher;

/**
 * @author nesdu
 *
 */
public class TeacherServiceImpl implements TeacherService {

	private MoocDao moocDao;
	
	 
	public MoocDao getMoocDao() {
		return moocDao;
	}

	public void setMoocDao(MoocDao moocDao) {
		this.moocDao = moocDao;
	}

	/* (non-Javadoc)
	 * @see com.bysakai.mooc.api.TeacherService#list()
	 */
	@Override
	public List<Teacher> list() {
		 
		return moocDao.findAll(Teacher.class);
	}

	@Override
	public void save(Teacher teacher) {
		if(teacher!=null && teacher.getId()!=null && !"".equals(teacher.getId())){
			moocDao.update(teacher);
		}else{
			moocDao.save(teacher);	
		}
	}

	@Override
	public void remove(Teacher teacher) {
		moocDao.delete(teacher);
	}

	@Override
	public Teacher findOne(String id) {
		return moocDao.findById(Teacher.class, id);
	}

}
