package org.sakaiproject.resource.api.study.model;

import java.util.Date;

/**
 * MeleteSelftestRecord entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class MeleteSelftestRecordModel implements java.io.Serializable {

	// Fields

	private Long selftestrecordId;//前测记录ID
	private String courseId;//课程ID
	private Float score;//总分数
	private Long attemptNumber;//尝试次数
	private String selftestName;//前测名称
	private Date lastCommitTime;//最后提交时间
	private Long status;//通过状态
	private Long sectionId;//节点ID
	private Long moduleId;//模块ID
	private Date startStudyTime;//开始时间
	private Long selftestId;//前测id
	private Long studyrecordId;//学习记录ID
	// Property accessors

	public MeleteSelftestRecordModel(){}
	public Long getStudyrecordId() {
		return studyrecordId;
	}

	public void setStudyrecordId(Long studyrecordId) {
		this.studyrecordId = studyrecordId;
	}

	public Long getSelftestId() {
		return selftestId;
	}

	public void setSelftestId(Long selftestId) {
		this.selftestId = selftestId;
	}

	public Long getSelftestrecordId() {
		return this.selftestrecordId;
	}

	public void setSelftestrecordId(Long selftestrecordId) {
		this.selftestrecordId = selftestrecordId;
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

	public String getSelftestName() {
		return this.selftestName;
	}

	public void setSelftestName(String selftestName) {
		this.selftestName = selftestName;
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
}