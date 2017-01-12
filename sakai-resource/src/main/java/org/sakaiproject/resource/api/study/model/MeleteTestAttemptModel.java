package org.sakaiproject.resource.api.study.model;

import java.util.Date;

/**
 * MeleteTestAttempt entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class MeleteTestAttemptModel implements java.io.Serializable {

	// Fields

	private Long testattemptId;//作业尝试记录ID
	private Long meleteTestRecordId;//作业记录ID
	private String courseId;//课程ID
	private Float score;//总分数
	private Long orderIndex;//
	private Date startTime;//开始时间
	private Date endTime;//结束时间
	private Float objScore;//客观题得分
	private Float subScore;//主观题得分
	private String testPaperid;//作业试卷ID
	private String pagerstatus;//试卷状态"0"主观题未批改"1"主观题已经批改"2"无主观题
	private String checkstatus;//批改状态"1"已批改"2"未批改

	// Property accessors

	public MeleteTestAttemptModel() {
	}

	public String getCheckstatus() {
		return checkstatus;
	}

	public void setCheckstatus(String checkstatus) {
		this.checkstatus = checkstatus;
	}

	public Long getTestattemptId() {
		return this.testattemptId;
	}

	public void setTestattemptId(Long testattemptId) {
		this.testattemptId = testattemptId;
	}

	public Long getMeleteTestRecordId() {
		return meleteTestRecordId;
	}

	public void setMeleteTestRecordId(Long meleteTestRecordId) {
		this.meleteTestRecordId = meleteTestRecordId;
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

	public Long getOrderIndex() {
		return this.orderIndex;
	}

	public void setOrderIndex(Long orderIndex) {
		this.orderIndex = orderIndex;
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Float getObjScore() {
		return this.objScore;
	}

	public void setObjScore(Float objScore) {
		this.objScore = objScore;
	}

	public Float getSubScore() {
		return this.subScore;
	}

	public void setSubScore(Float subScore) {
		this.subScore = subScore;
	}

	public String getTestPaperid() {
		return this.testPaperid;
	}

	public void setTestPaperid(String testPaperid) {
		this.testPaperid = testPaperid;
	}

	public String getPagerstatus() {
		return this.pagerstatus;
	}

	public void setPagerstatus(String pagerstatus) {
		this.pagerstatus = pagerstatus;
	}

}