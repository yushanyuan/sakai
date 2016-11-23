package org.sakaiproject.resource.api.study.model;

import java.util.Date;

/**
 * MeleteSelftestAttempt entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class MeleteSelftestAttemptModel implements java.io.Serializable {

	// Fields

	private Long selftestattemptId;//前测尝试记录ID
	private Long meleteSelftestRecordId;//前测记录ID
	private String courseId;//课程ID
	private Float score;//总分数
	private Long orderIndex;//
	private Date startTime;//开始时间
	private Date endTime;//结束时间
	private Float objScore;//客观题得分
	private Float subScore;//主观题得分
	private String selftestPaperid;//前测试卷ID
	private String pagerstatus;//试卷通过状态
	private String checkstatus;//批改状态

	
	public MeleteSelftestAttemptModel(){}
	// Property accessors

	public String getCheckstatus() {
		return checkstatus;
	}

	public void setCheckstatus(String checkstatus) {
		this.checkstatus = checkstatus;
	}

	public Long getSelftestattemptId() {
		return this.selftestattemptId;
	}

	public void setSelftestattemptId(Long selftestattemptId) {
		this.selftestattemptId = selftestattemptId;
	}


	public Long getMeleteSelftestRecordId() {
		return meleteSelftestRecordId;
	}

	public void setMeleteSelftestRecordId(Long meleteSelftestRecordId) {
		this.meleteSelftestRecordId = meleteSelftestRecordId;
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

	public String getSelftestPaperid() {
		return this.selftestPaperid;
	}

	public void setSelftestPaperid(String selftestPaperid) {
		this.selftestPaperid = selftestPaperid;
	}

	public String getPagerstatus() {
		return this.pagerstatus;
	}

	public void setPagerstatus(String pagerstatus) {
		this.pagerstatus = pagerstatus;
	}
}