/**
 * 
 */
package org.sakaiproject.resource.api.course.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yushanyuan
 * 移动端接口，作业vo
 *
 */
public class TestVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String testId;
	private String moduleId;
	private String sectionId;
	private String name;
	private String totalNum;
	private String score;
	private String isPass;
	private Date openStart;
	private Date openEnd;
	private Long minInterval;

	public String getTestId() {
		return testId;
	}
	public void setTestId(String testId) {
		this.testId = testId;
	}
	public String getModuleId() {
		return moduleId;
	}
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	public String getSectionId() {
		return sectionId;
	}
	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(String totalNum) {
		this.totalNum = totalNum;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getIsPass() {
		return isPass;
	}
	public void setIsPass(String isPass) {
		this.isPass = isPass;
	}
	public Date getOpenStart() {
		return openStart;
	}
	public void setOpenStart(Date openStart) {
		this.openStart = openStart;
	}
	public Date getOpenEnd() {
		return openEnd;
	}
	public void setOpenEnd(Date openEnd) {
		this.openEnd = openEnd;
	}
	public Long getMinInterval() {
		return minInterval;
	}
	public void setMinInterval(Long minInterval) {
		this.minInterval = minInterval;
	}
}
