/**
 * 版权所有 北京思开科技有限公司 
 * All Rights Reserved
 */
package com.bysakai.mooc.api.impl;
import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.genericdao.api.search.Order;
import org.sakaiproject.genericdao.api.search.Restriction;
import org.sakaiproject.genericdao.api.search.Search;
import org.sakaiproject.javax.PagingPosition;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;

import com.bysakai.mooc.api.CourseService;
import com.bysakai.mooc.dao.MoocDao;
import com.bysakai.mooc.model.Course;
import com.bysakai.mooc.model.RecItem;
/**
 * @author nesdu
 *
 */
public class CourseServiceImpl implements CourseService {

	 
	private SiteService siteService;
	
	
	private MoocDao moocDao;
	
	 
	public MoocDao getMoocDao() {
		return moocDao;
	}

	public void setMoocDao(MoocDao moocDao) {
		this.moocDao = moocDao;
	}
	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}
	
	@Override
	public List<Course> hotList(int first, int maxResults) {
		return moocDao.findBySearch(Course.class, new Search(new Restriction[]{}, new Order("studyNum", true), first,maxResults));
	}
	@Override
	public List<Course> newList(int first, int maxResults) {
		return moocDao.findBySearch(Course.class, new Search(new Restriction[]{}, new Order("createDate", false), first,maxResults));
	}
	@Override
	public List<Course> recList(int first, int maxResults) {
		List<Course> courseList = new ArrayList<Course>();
		List<RecItem> recList = moocDao.findBySearch(RecItem.class, new Search(new Restriction[]{}, new Order("recValue", false), first,maxResults));
		if(recList!=null && recList.size()>0){
			for(RecItem r : recList){
				courseList.add(r.getCourse());
			}
		}
		return courseList;
	}
	
	@Override
	public List<Course> list() {
		return moocDao.findAll(Course.class);
	}

	@Override
	public void save(Course course) {
		if(course!=null && course.getId()!=null && !"".equals(course.getId())){
			moocDao.update(course);
		}else{
			moocDao.save(course);	
		}
	}

	@Override
	public void remove(Course course) {
		moocDao.delete(course);
		
	}

	@Override
	public Course findOne(String id) {
		return moocDao.findById(Course.class, id);
	}

	@Override
	public List<Site> siteList() {
		return siteService.getSites(org.sakaiproject.site.api.SiteService.SelectionType.ACCESS, null, null, null, null, new PagingPosition(1, 1000));
	}

	@Override
	public List<Course> findByCategoryId(String categoryId) {
		
		return moocDao.findByNamedQueryAndNamedParam("findByCategoryId", new String[] {"categoryId"}, new Object[]{categoryId});
	}

	public List findCourse(final String speId,final String q,final String stutas,final Integer pages){
		return moocDao.findCourse(speId, q, stutas, pages);
	}
}
