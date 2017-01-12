/**
 * 
 */
package org.sakaiproject.resource.api.course.vo;

import java.io.Serializable;

/**
 * @author nesdu
 *
 */
public class TestContentVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String courseId; //课程ID
	private String testId; //作业ID
	private boolean samePaper;//每次取同一份试卷还是随机取卷
	private String startTime;//开始做作业时间
	private String passScore;//通过分数线
	private String testRecordId;//作业记录ID
	private String studyRecordId;//学习记录ID
	private String paperId; //试卷ID
	private String paperContent; //试卷内容(JSON文本)
	private String errorFlag;//错误标志
	private String errorTip;//错误提示
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getTestId() {
		return testId;
	}
	public void setTestId(String testId) {
		this.testId = testId;
	}
	public boolean isSamePaper() {
		return samePaper;
	}
	public void setSamePaper(boolean samePaper) {
		this.samePaper = samePaper;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	 
	public String getPassScore() {
		return passScore;
	}
	public void setPassScore(String passScore) {
		this.passScore = passScore;
	}
	public String getTestRecordId() {
		return testRecordId;
	}
	public void setTestRecordId(String testRecordId) {
		this.testRecordId = testRecordId;
	}
	public String getStudyRecordId() {
		return studyRecordId;
	}
	public void setStudyRecordId(String studyRecordId) {
		this.studyRecordId = studyRecordId;
	}
	public String getPaperId() {
		return paperId;
	}
	public void setPaperId(String paperId) {
		this.paperId = paperId;
	}
	public String getPaperContent() {
		return paperContent;
	}
	public void setPaperContent(String paperContent) {
		this.paperContent = paperContent;
	}
	public String getErrorFlag() {
		return errorFlag;
	}
	public void setErrorFlag(String errorFlag) {
		this.errorFlag = errorFlag;
	}
	public String getErrorTip() {
		return errorTip;
	}
	public void setErrorTip(String errorTip) {
		this.errorTip = errorTip;
	}

}
