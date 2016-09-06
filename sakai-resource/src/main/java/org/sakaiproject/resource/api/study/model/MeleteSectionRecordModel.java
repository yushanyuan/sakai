package org.sakaiproject.resource.api.study.model;

import java.util.Date;

/**
 * MeleteSectionRecord entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class MeleteSectionRecordModel implements java.io.Serializable {

	// Fields

	private Long sectionrecordId;//节点记录ID
	private Long sectionId;//对应节点ID
	private String courseId;//课程ID
	private Long studyTime;//学习时长
	private Date recordTime;//记录时间
	private Long status;//通过状态
	private Long required;//选必修
	private Long openStatus;//开启状态
	private Date startStudyTime;//学习开始时间
	private Long passStudyTime;//通过学习用时
	private Date sectionPassTime;//节点通过时间
	private Long studyrecordId;//学习记录ID
	private Long meleteModuleRecordId;//所属模块记录ID

	// Property accessors

	public Long getSectionrecordId() {
		return this.sectionrecordId;
	}

	public void setSectionrecordId(Long sectionrecordId) {
		this.sectionrecordId = sectionrecordId;
	}


	public Long getMeleteModuleRecordId() {
		return meleteModuleRecordId;
	}

	public void setMeleteModuleRecordId(Long meleteModuleRecordId) {
		this.meleteModuleRecordId = meleteModuleRecordId;
	}

	public Long getSectionId() {
		return this.sectionId;
	}

	public void setSectionId(Long sectionId) {
		this.sectionId = sectionId;
	}

	public String getCourseId() {
		return this.courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public Long getStudyTime() {
		return this.studyTime;
	}

	public void setStudyTime(Long studyTime) {
		this.studyTime = studyTime;
	}

	public Date getRecordTime() {
		return this.recordTime;
	}

	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
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

	public Long getPassStudyTime() {
		return this.passStudyTime;
	}

	public void setPassStudyTime(Long passStudyTime) {
		this.passStudyTime = passStudyTime;
	}

	public Date getSectionPassTime() {
		return this.sectionPassTime;
	}

	public void setSectionPassTime(Date sectionPassTime) {
		this.sectionPassTime = sectionPassTime;
	}

	public Long getStudyrecordId() {
		return this.studyrecordId;
	}

	public void setStudyrecordId(Long studyrecordId) {
		this.studyrecordId = studyrecordId;
	}
}