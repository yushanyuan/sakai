package org.sakaiproject.resource.api.study.model;

import java.util.Date;

/**
 * MeleteStudyRecord entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class MeleteStudyRecordModel implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -9095818900044782148L;
	private Long studyrecordId;//学习记录ID
	private String courseId;//课程ID
	private Float score;//总分数
	private Long attemptNumber;//尝试次数
	private String studentId;//对应学生ID
	private String lessonLocation;//课的位置
	private Long lessonStatus;//课的状态
	private Long totalTime;//总时长
	private String suspendData;//暂停数据
	private String launchData;//启动信息
	private String comments;//评语
	private Date startStudyTime;//学习开始时间
	private Date coursePassTime;//学习通过时刻
	private Date scoreUpdateTime;//分数更新时间
	private String coursechoiceplanId;//选课计划ID

	// Property accessors

	public Long getStudyrecordId() {
		return this.studyrecordId;
	}

	public void setStudyrecordId(Long studyrecordId) {
		this.studyrecordId = studyrecordId;
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

	public String getStudentId() {
		return this.studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getLessonLocation() {
		return this.lessonLocation;
	}

	public void setLessonLocation(String lessonLocation) {
		this.lessonLocation = lessonLocation;
	}

	public Long getLessonStatus() {
		return this.lessonStatus;
	}

	public void setLessonStatus(Long lessonStatus) {
		this.lessonStatus = lessonStatus;
	}

	public Long getTotalTime() {
		return this.totalTime;
	}

	public void setTotalTime(Long totalTime) {
		this.totalTime = totalTime;
	}

	public String getSuspendData() {
		return this.suspendData;
	}

	public void setSuspendData(String suspendData) {
		this.suspendData = suspendData;
	}

	public String getLaunchData() {
		return this.launchData;
	}

	public void setLaunchData(String launchData) {
		this.launchData = launchData;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Date getStartStudyTime() {
		return this.startStudyTime;
	}

	public void setStartStudyTime(Date startStudyTime) {
		this.startStudyTime = startStudyTime;
	}

	public Date getCoursePassTime() {
		return this.coursePassTime;
	}

	public void setCoursePassTime(Date coursePassTime) {
		this.coursePassTime = coursePassTime;
	}

	public Date getScoreUpdateTime() {
		return this.scoreUpdateTime;
	}

	public void setScoreUpdateTime(Date scoreUpdateTime) {
		this.scoreUpdateTime = scoreUpdateTime;
	}

	public String getCoursechoiceplanId() {
		return this.coursechoiceplanId;
	}

	public void setCoursechoiceplanId(String coursechoiceplanId) {
		this.coursechoiceplanId = coursechoiceplanId;
	}
}