/**
 * 版权所有 北京思开科技有限公司 
 * All Rights Reserved
 */
package com.bysakai.mooc.api;

import java.util.List;

import com.bysakai.mooc.model.Teacher;

/**
 * @author nesdu
 *
 */
public interface TeacherService {

	public List<Teacher> list();
	
	public void save(Teacher teacher);
	public void remove(Teacher teacher);
	public Teacher findOne(String id);
}
