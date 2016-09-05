package org.sakaiproject.resource.api.courseware.service;

import org.sakaiproject.resource.api.courseware.model.CoursewareModel;

public interface CoursewareService {
	
	/**
	 * 保存课件资源对象
	 * @param courseModel
	 * @throws Exception
	 */
	public void save(CoursewareModel courseModel) throws Exception;
	
	/**
	 * 删除课件资源，这个删除是把对象放在回收站里
	 * @param ids 课程资源ID集合
	 * @throws Exception
	 */
	public void delete(String[] ids) throws Exception;
	
	/**
	 * 删除课件资源，这个删除是把数据从回收站删除
	 * @param ids 课件资源ID集合
	 * @throws Exception
	 */
	public void remove(String[] ids) throws Exception;
	
	/**
	 * 把所有数据从回收站删除
	 * @throws Exception
	 */
	public void removeAll() throws Exception;
	
	/**
	 * 查询课件资源，根据传入的状态不同查询不同的数据
	 * @param status 课件资源状态
	 * @param startInt 分页下标
	 * @return
	 * @throws Exception
	 */
	public Object[] query(String status, int startInt) throws Exception;
	
	/**
	 * 根据用户输入的课件名称查询数据
	 * @param courseName 课件资源名称或摘要
	 * @param status 课件资源状态
	 * @param startInt 分页下标
	 * @return
	 * @throws Exception
	 */
	public Object[] findByName(String courseName, String status, int startInt, int pageSize) throws Exception;
	
	/**
	 * 把数据从回收站还原回来
	 * @param ids 课件资源ID集合
	 * @throws Exception
	 */
	public void courseBack(String[] ids) throws Exception;
	
	/**
	 * 把数据从回收站全部还原回来
	 * @throws Exception
	 */
	public void courseBackAll() throws Exception;
	
	/**
	 * 根据课件资源ID获得课件资源
	 * @param id
	 * @return
	 */
	public CoursewareModel getCoursewareModelById(String id);
}
