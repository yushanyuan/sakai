package org.sakaiproject.resource.api.test.model;

import java.util.Date;

public class Test implements java.io.Serializable{
	private int testId;//作业id
	private String courseId;//课程id
	private String siteId;//站点id
	private String testName;//作业名称
	private int schemaId;//策略id
	private int totalScore;//满分
	private int masteryScore;//通过分数
	private String samepaper;//是否使用同一策略:1是、0否
	private int idx;//作业序号
	private String status;//作业状态:1未发布、0无效、2已发布、3发布中
	private Date creationDate;//创建时间
	private Date modificationDate;//修改时间
	
	
	public Test() {
	}
	public int getTestId() {
		return testId;
	}
	public void setTestId(int testId) {
		this.testId = testId;
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
	public String getTestName() {
		return testName;
	}
	public void setTestName(String testName) {
		this.testName = testName;
	}
	public int getSchemaId() {
		return schemaId;
	}
	public void setSchemaId(int schemaId) {
		this.schemaId = schemaId;
	}
	public int getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}
	public int getMasteryScore() {
		return masteryScore;
	}
	public void setMasteryScore(int masteryScore) {
		this.masteryScore = masteryScore;
	}
	public String getSamepaper() {
		return samepaper;
	}
	public void setSamepaper(String samepaper) {
		this.samepaper = samepaper;
	}
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Date getModificationDate() {
		return modificationDate;
	}
	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}
	
}
