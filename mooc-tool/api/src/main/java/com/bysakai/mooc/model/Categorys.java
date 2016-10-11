/**
 * 版权所有 北京思开科技有限公司 
 * All Rights Reserved
 */
package com.bysakai.mooc.model;

import java.io.Serializable;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 课程分类
 * @author nesdu
 *
 */
@Data
@NoArgsConstructor
public class Categorys implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String name;
	private String icon;
	private Set<Course> courseList;
	
	 
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Set<Course> getCourseList() {
		return courseList;
	}
	public void setCourseList(Set<Course> courseList) {
		this.courseList = courseList;
	}
	 
}
