package org.sakaiproject.resource.impl.courseware.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.sakaiproject.resource.api.courseware.model.CoursewareModel;
import org.sakaiproject.resource.api.courseware.service.CoursewareService;
import org.sakaiproject.resource.util.HibernateDaoSupport;
import org.sakaiproject.resource.util.QueryString;
import org.sakaiproject.user.api.UserDirectoryService;
import org.sakaiproject.component.cover.ComponentManager;

public class CoursewareServiceImpl extends HibernateDaoSupport implements CoursewareService {
	
	/**
	 * 删除课件资源，这个删除是把对象放在回收站里
	 * @param ids 课程资源ID集合
	 * @throws Exception
	 */
	public void delete(String[] ids) throws Exception{
		Map<String, Object> parameters = new HashMap<String, Object>();
		StringBuffer queryString = new StringBuffer("update CoursewareModel cm set cm.status=2 where cm.id in(:ids)");
		parameters.put("ids", ids);
		this.updateEntity(queryString.toString(), parameters);
	}
	
	/**
	 * 删除课件资源，这个删除是把数据从回收站删除
	 * @param ids 课件资源ID集合
	 * @throws Exception
	 */
	public void remove(String[] ids) throws Exception {
		Map<String, Object> parameters = new HashMap<String, Object>();
		StringBuffer queryString = new StringBuffer("update CoursewareModel cm set cm.status=-1 where cm.id in(:ids)");
		parameters.put("ids", ids);
		this.updateEntity(queryString.toString(), parameters);
	}
	
	/**
	 * 查询课件资源，根据传入的状态不同查询不同的数据
	 * @param status 课件资源状态
	 * @param startInt 分页下标
	 * @return
	 * @throws Exception
	 */
	public Object[] query(String status, int startInt) throws Exception {
		Map<String, Object> parameters = new HashMap<String, Object>();
		QueryString queryString = new QueryString();
		queryString.setFrom(" from CoursewareModel cm ");
		if (status != null) {
			parameters.put("status", Long.parseLong(status));
			queryString
					.setWhere(" where cm.status=:status");
			queryString.setOrder(" order by cm.courseName");
		
		}
		Object[] obj = this.findEntity(queryString, parameters,
				HibernateDaoSupport.LIMIT, startInt);
		return obj;
	}
	
	/**
	 * 把所有数据从回收站删除
	 * @throws Exception
	 */
	public void removeAll() throws Exception {
		StringBuffer queryString = new StringBuffer("update CoursewareModel cm set cm.status=-1 where cm.status=2");
		this.updateEntity(queryString.toString());
	}
	
	/**
	 * 根据用户输入的课件名称查询数据
	 * @param courseName 课件资源名称或摘要
	 * @param status 课件资源状态
	 * @param startInt 分页下标
	 * @return
	 * @throws Exception
	 */
	public Object[] findByName(String courseName, String status, int startInt, int pageSize){
		Map<String, Object> parameters = new HashMap<String, Object>();
		QueryString queryString = new QueryString();
		queryString.setFrom(" from CoursewareModel cm where 1=1");
		if (StringUtils.isNotBlank(status)) {
			queryString.setWhere(" and cm.status=:status");
			parameters.put("status", Long.parseLong(status));
		
		}
		if (StringUtils.isNotBlank(courseName)) {
			queryString.appendWhere(" and (cm.courseName like:courseName)");
			parameters.put("courseName", "%"+courseName+"%");
			
		}
		queryString.setOrder(" order by cm.courseName");
		Object[] obj =  null;
		if(pageSize == 0){
			obj =  this.findEntity(queryString, parameters,
					HibernateDaoSupport.LIMIT, startInt);
		}else{
			obj =  this.findEntity(queryString, parameters,
					pageSize, startInt);
		}
		return obj;
	}
	
	/**
	 * 把数据从回收站还原回来
	 * @param ids 课件资源ID集合
	 * @throws Exception
	 */
	public void courseBack(String[] ids) throws Exception {
		Map<String, Object> parameters = new HashMap<String, Object>();
		StringBuffer queryString = new StringBuffer("update CoursewareModel cm set cm.status=1 where cm.id in(:ids)");
		parameters.put("ids", ids);
		this.updateEntity(queryString.toString(), parameters);
	}
	
	/**
	 * 把数据从回收站全部还原回来
	 * @throws Exception
	 */
	public void courseBackAll() throws Exception {
		StringBuffer queryString = new StringBuffer("update CoursewareModel cm set cm.status=1 where cm.status=2");
		this.updateEntity(queryString.toString());
	}
	
	/**
	 * 保存课件资源对象
	 * @param courseModel
	 * @throws Exception
	 */
	public void save(CoursewareModel courseModel) {
		this.getHibernateTemplate().saveOrUpdate(courseModel);
	}
	
	/**
	 * 根据课件资源ID获得课件资源
	 * @param id
	 * @return
	 */
	public CoursewareModel getCoursewareModelById(String id){
		return (CoursewareModel)this.findEntityById(CoursewareModel.class, id);
	}
}
