package org.sakaiproject.resource.api.study.model;

import java.util.Date;

/**
 * MeleteSectionRecordDetail entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class MeleteSectionRecordActivityModel implements java.io.Serializable {

	// Fields

	private Long sectionRecordActivityId;
	private Long sectionRecordId;//节点记录ID
	private String activityId;//活动ID
	private Float activityValue;//活动值
	private Date activityTime;//活动时间
	
	public  MeleteSectionRecordActivityModel(){}
	public Long getSectionRecordActivityId() {
		return sectionRecordActivityId;
	}
	public void setSectionRecordActivityId(Long sectionRecordActivityId) {
		this.sectionRecordActivityId = sectionRecordActivityId;
	}
	public Long getSectionRecordId() {
		return sectionRecordId;
	}
	public void setSectionRecordId(Long sectionRecordId) {
		this.sectionRecordId = sectionRecordId;
	}
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	public Float getActivityValue() {
		return activityValue;
	}
	public void setActivityValue(Float activityValue) {
		this.activityValue = activityValue;
	}
	public Date getActivityTime() {
		return activityTime;
	}
	public void setActivityTime(Date activityTime) {
		this.activityTime = activityTime;
	}


	
}