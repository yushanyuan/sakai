package org.sakaiproject.resource.api.study.model;

import java.util.Date;

/**
 * MeleteModuleRecord entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class MeleteModuleRecordModel implements java.io.Serializable {

	// Fields

	private Long modulerecordId;//模块记录ID
	private Long moduleId;//对应模块ID
	private String courseId;//课程ID
	private Long status;//通过状态
	private Long required;//选必修
	private Long openStatus;//开放状态
	private Date startStudyTime;//学习开始时间
	private Date modulePassTime;//模块通过时间
	private Long passStudyTime;//通过学习用时
	private Long studyTime;//学习时长
	private Long studyrecordId;//学习记录ID

	
	public  MeleteModuleRecordModel(){}
	// Property accessors

	public Long getModulerecordId() {
		return this.modulerecordId;
	}

	public void setModulerecordId(Long modulerecordId) {
		this.modulerecordId = modulerecordId;
	}

	public Long getModuleId() {
		return this.moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public String getCourseId() {
		return this.courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public Long getStatus() {
		return this.status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Long getRequired() {
		return this.required;
	}

	public void setRequired(Long required) {
		this.required = required;
	}

	public Long getOpenStatus() {
		return this.openStatus;
	}

	public void setOpenStatus(Long openStatus) {
		this.openStatus = openStatus;
	}

	public Date getStartStudyTime() {
		return this.startStudyTime;
	}

	public void setStartStudyTime(Date startStudyTime) {
		this.startStudyTime = startStudyTime;
	}

	public Date getModulePassTime() {
		return this.modulePassTime;
	}

	public void setModulePassTime(Date modulePassTime) {
		this.modulePassTime = modulePassTime;
	}

	public Long getStudyrecordId() {
		return this.studyrecordId;
	}

	public void setStudyrecordId(Long studyrecordId) {
		this.studyrecordId = studyrecordId;
	}

	public Long getStudyTime() {
		return studyTime;
	}

	public void setStudyTime(Long studyTime) {
		this.studyTime = studyTime;
	}

	public Long getPassStudyTime() {
		return passStudyTime;
	}

	public void setPassStudyTime(Long passStudyTime) {
		this.passStudyTime = passStudyTime;
	}
}