/**
 * 版权所有 北京思开科技有限公司 
 * All Rights Reserved
 */
package com.bysakai.mooc.rest.vo;

import java.io.Serializable;

import com.bysakai.mooc.model.Teacher;

/**
 * @author nesdu
 *
 */
public class RecItemVO implements Serializable{

	private static final long serialVersionUID = 1L;

	private String id;
	
	private String name;
	
	private String image;
	
	private Teacher teacher;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
