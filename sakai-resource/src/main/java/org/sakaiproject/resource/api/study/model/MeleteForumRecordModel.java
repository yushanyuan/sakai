package org.sakaiproject.resource.api.study.model;

import java.util.Date;

/**
 * MeleteForumRecord entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class MeleteForumRecordModel implements java.io.Serializable {

	// Fields

	private Long forumrecordId;//讨论记录ID
	private Long forumId;//讨论ID
	private String courseId;//课程ID
	private String forumName;//讨论名称
	private Long attemptNumber;//尝试次数
	private Long status;//状态
	private Long required;//选必修
	private Long sectionId;//节点ID
	private Long moduleId;//模块ID
	private Date startStudyTime;//开始学习时间
	private Long studyrecordId;//学习记录ID

	public Long getForumrecordId() {
		return this.forumrecordId;
	}

	public void setForumrecordId(Long forumrecordId) {
		this.forumrecordId = forumrecordId;
	}

	public Long getForumId() {
		return this.forumId;
	}

	public void setForumId(Long forumId) {
		this.forumId = forumId;
	}

	public String getCourseId() {
		return this.courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getForumName() {
		return this.forumName;
	}

	public void setForumName(String forumName) {
		this.forumName = forumName;
	}

	public Long getAttemptNumber() {
		return this.attemptNumber;
	}

	public void setAttemptNumber(Long attemptNumber) {
		this.attemptNumber = attemptNumber;
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