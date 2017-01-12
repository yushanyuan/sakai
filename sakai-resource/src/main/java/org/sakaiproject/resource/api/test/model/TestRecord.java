package org.sakaiproject.resource.api.test.model;

import java.util.Date;

public class TestRecord implements java.io.Serializable{
	private int testrecordId ;
	private String courseId ;
	private String siteId ;
	private String studentId ;
	private String status ;
	private String testPaperid ;
	private String checkStatus ;//0无主观题;1未批改;2已批改
	private int testId ;
	private int idx ;
	private float score ;
	private float objScore ;
	private float subScore ;
	private Date startTime ;
	private Date endTime ;
	
	
	public TestRecord() {
	}
	public int getTestrecordId() {
		return testrecordId;
	}
	public void setTestrecordId(int testrecordId) {
		this.testrecordId = testrecordId;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTestPaperid() {
		return testPaperid;
	}
	public void setTestPaperid(String testPaperid) {
		this.testPaperid = testPaperid;
	}
	public String getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}
	public int getTestId() {
		return testId;
	}
	public void setTestId(int testId) {
		this.testId = testId;
	}
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	public float getObjScore() {
		return objScore;
	}
	public void setObjScore(float objScore) {
		this.objScore = objScore;
	}
	public float getSubScore() {
		return subScore;
	}
	public void setSubScore(float subScore) {
		this.subScore = subScore;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}
