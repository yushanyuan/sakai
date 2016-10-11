/**
 * 版权所有 北京思开科技有限公司 
 * All Rights Reserved
 */
package com.bysakai.mooc.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @author nesdu
 *
 */
@Data
@NoArgsConstructor
public class RecItem implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String id;
	private Course course;
	private int recValue;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public int getRecValue() {
		return recValue;
	}
	public void setRecValue(int recValue) {
		this.recValue = recValue;
	}
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}
	
}
