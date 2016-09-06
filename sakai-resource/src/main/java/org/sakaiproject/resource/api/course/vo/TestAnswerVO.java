/**
 * 
 */
package org.sakaiproject.resource.api.course.vo;

import java.io.Serializable;

/**
 * @author nesdu
 *
 */
public class TestAnswerVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String courseId; //课程ID
	private String paperId; //作业试卷ID
	private String testRecordId;//作业记录ID
	private String testAttemptId;//作业尝试记录ID
	private int orderIndex;//第几次
	private String startTime;//开始做作业时间
	private String endTime;//结束时间
	private String objScore; //客观题得分
	private String subScore; //主观题得分
	private String score; //总分数
	private boolean showAnswer;
	private String paperStatus;//试卷状态"0":主观题未批改,"1":主观题已批改,"2":无主观题
	private String paperContent; //试卷内容(JSON文本)
	
	private String answerContent;//包括正确答案、作答结果和批改结果的内容(JSON文本)

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getPaperId() {
		return paperId;
	}

	public void setPaperId(String paperId) {
		this.paperId = paperId;
	}

	public String getTestRecordId() {
		return testRecordId;
	}

	public void setTestRecordId(String testRecordId) {
		this.testRecordId = testRecordId;
	}

	public String getTestAttemptId() {
		return testAttemptId;
	}

	public void setTestAttemptId(String testAttemptId) {
		this.testAttemptId = testAttemptId;
	}

	public int getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(int orderIndex) {
		this.orderIndex = orderIndex;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getObjScore() {
		return objScore;
	}

	public void setObjScore(String objScore) {
		this.objScore = objScore;
	}

	public String getSubScore() {
		return subScore;
	}

	public void setSubScore(String subScore) {
		this.subScore = subScore;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public boolean isShowAnswer() {
		return showAnswer;
	}

	public void setShowAnswer(boolean showAnswer) {
		this.showAnswer = showAnswer;
	}

	public String getPaperStatus() {
		return paperStatus;
	}

	public void setPaperStatus(String paperStatus) {
		this.paperStatus = paperStatus;
	}

	public Object getPaperContent() {
		return paperContent;
	}

	public void setPaperContent(String paperContent) {
		this.paperContent = paperContent;
	}

	public String getAnswerContent() {
		return answerContent;
	}

	public void setAnswerContent(String answerContent) {
		this.answerContent = answerContent;
	}
}
