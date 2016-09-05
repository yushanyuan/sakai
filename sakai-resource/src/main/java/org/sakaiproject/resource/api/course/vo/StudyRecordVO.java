/**
 * 
 */
package org.sakaiproject.resource.api.course.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yushanyuan
 *
 */
public class StudyRecordVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String studyrecordId;//学习记录ID
	private String courseId;//课程ID
	private String score;//总分数
	private String attemptNumber;//尝试次数
	private String studentId;//对应学生ID
	private String lessonLocation;//课的位置
	private String lessonStatus;//课的状态
	private String totalTime;//总时长
	private String suspendData;//暂停数据
	private String launchData;//启动信息
	private String comments;//评语
	private String startStudyTime;//学习开始时间
	private String coursePassTime;//学习通过时刻
	private String scoreUpdateTime;//分数更新时间
	private String coursechoiceplanId;//选课计划ID
	public String getStudyrecordId() {
		return studyrecordId;
	}
	public void setStudyrecordId(String studyrecordId) {
		this.studyrecordId = studyrecordId;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getAttemptNumber() {
		return attemptNumber;
	}
	public void setAttemptNumber(String attemptNumber) {
		this.attemptNumber = attemptNumber;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getLessonLocation() {
		return lessonLocation;
	}
	public void setLessonLocation(String lessonLocation) {
		this.lessonLocation = lessonLocation;
	}
	public String getLessonStatus() {
		return lessonStatus;
	}
	public void setLessonStatus(String lessonStatus) {
		this.lessonStatus = lessonStatus;
	}
	public String getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}
	public String getSuspendData() {
		return suspendData;
	}
	public void setSuspendData(String suspendData) {
		this.suspendData = suspendData;
	}
	public String getLaunchData() {
		return launchData;
	}
	public void setLaunchData(String launchData) {
		this.launchData = launchData;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getStartStudyTime() {
		return startStudyTime;
	}
	public void setStartStudyTime(String startStudyTime) {
		this.startStudyTime = startStudyTime;
	}
	public String getCoursePassTime() {
		return coursePassTime;
	}
	public void setCoursePassTime(String coursePassTime) {
		this.coursePassTime = coursePassTime;
	}
	public String getScoreUpdateTime() {
		return scoreUpdateTime;
	}
	public void setScoreUpdateTime(String scoreUpdateTime) {
		this.scoreUpdateTime = scoreUpdateTime;
	}
	public String getCoursechoiceplanId() {
		return coursechoiceplanId;
	}
	public void setCoursechoiceplanId(String coursechoiceplanId) {
		this.coursechoiceplanId = coursechoiceplanId;
	}

	
}
