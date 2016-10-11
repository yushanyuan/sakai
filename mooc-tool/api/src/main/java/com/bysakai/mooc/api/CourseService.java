/**
 * 版权所有 北京思开科技有限公司 
 * All Rights Reserved
 */
package com.bysakai.mooc.api;

import java.util.List;
import org.sakaiproject.site.api.Site;
import com.bysakai.mooc.model.Course;

/**
 * @author nesdu
 *
 */
public interface CourseService {

	public List<Site> siteList();
	public List<Course> hotList(int first, int maxResults);
	public List<Course> newList(int first, int maxResults);
	public List<Course> recList(int first, int maxResults);
	public List<Course> list();
	public List<Course> findByCategoryId(String categoryId);
	
	public void save(Course course);
	public void remove(Course course);
	public Course findOne(String id);
	public List findCourse(final String speId,final String q,final String stutas,final Integer pages);
}
