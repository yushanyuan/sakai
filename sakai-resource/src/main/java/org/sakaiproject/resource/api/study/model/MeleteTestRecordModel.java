package org.sakaiproject.resource.api.study.model;

import java.util.Date;

/**
 * MeleteTestRecord entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class MeleteTestRecordModel implements java.io.Serializable {

	// Fields

	private Long testrecordId;//作业记录ID
	private String courseId;//课程ID
	private Float score;//总分数
	private Long attemptNumber;//尝试次数
	private String testName;//作业名称
	private Date lastCommitTime;//最后提交时间
	private Long status;//通过状态
	private Boolean required;//选必修
	private Long sectionId;//节点ID
	private Long moduleId;//模块ID
	private Date startStudyTime;//开始时间
	private Long studyrecordId;//学习记录ID
	private Long testId;//作业id

	public MeleteTestRecordModel(){}
	// Property accessors

	public Long getTestId() {
		return testId;
	}

	public void setTestId(Long testId) {
		this.testId = testId;
	}

	public Long getTestrecordId() {
		return this.testrecordId;
	}

	public void setTestrecordId(Long testrecordId) {
		this.testrecordId = testrecordId;
	}

	public String getCourseId() {
		return this.courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public Float getScore() {
		return this.score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	public Long getAttemptNumber() {
		return this.attemptNumber;
	}

	public void setAttemptNumber(Long attemptNumber) {
		this.attemptNumber = attemptNumber;
	}

	public String getTestName() {
		return this.testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public Date getLastCommitTime() {
		return this.lastCommitTime;
	}

	public void setLastCommitTime(Date lastCommitTime) {
		this.lastCommitTime = lastCommitTime;
	}

	public Long getStatus() {
		return this.status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public Long getSectionId() {
		return this.sectionId;
	}

	public void setSectionId(Long sectionId) {
		this.sectionId = sectionId;
	}

	public Long getModuleId() {
		return this.moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public Date getStartStudyTime() {
		return this.startStudyTime;
	}

	public void setStartStudyTime(Date startStudyTime) {
		this.startStudyTime = startStudyTime;
	}

	public Long getStudyrecordId() {
		return this.studyrecordId;
	}

	public void setStudyrecordId(Long studyrecordId) {
		this.studyrecordId = studyrecordId;
	}
}