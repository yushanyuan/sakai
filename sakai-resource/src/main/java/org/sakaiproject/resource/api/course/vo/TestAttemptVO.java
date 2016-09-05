/**
 * 
 */
package org.sakaiproject.resource.api.course.vo;

import java.io.Serializable;

/**
 * @author yushanyuan
 *
 */
public class TestAttemptVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String testRecordId;
	private String testName;
	private String score;
	private String startStudyTime;
	private String objScore;//客观题得分
	private String subScore;//主观题得分
	
	public String getTestRecordId() {
		return testRecordId;
	}
	public void setTestRecordId(String testRecordId) {
		this.testRecordId = testRecordId;
	}
	public String getTestName() {
		return testName;
	}
	public void setTestName(String testName) {
		this.testName = testName;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getStartStudyTime() {
		return startStudyTime;
	}
	public void setStartStudyTime(String startStudyTime) {
		this.startStudyTime = startStudyTime;
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
}
