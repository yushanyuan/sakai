package org.sakaiproject.resource.api.study.model;

import java.util.Date;

/**
 * MeleteSectionRecordDetail entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class MeleteSectionRecordDetailModel implements java.io.Serializable {

	// Fields

	private Long sectionrecorddetailId;
	private Long sectionrecordId;//节点记录ID
	private Long studyTime;//学习时长(分钟)
	private Date startStudyTime;//学习开始时间(精确到分)
	private Date endStudyTime;//学习结束时间(精确到分)


	// Property accessors

	public Long getSectionrecorddetailId() {
		return this.sectionrecorddetailId;
	}

	public void setSectionrecorddetailId(Long sectionrecorddetailId) {
		this.sectionrecorddetailId = sectionrecorddetailId;
	}

	public Long getStudyTime() {
		return this.studyTime;
	}

	public Long getSectionrecordId() {
		return sectionrecordId;
	}

	public void setSectionrecordId(Long sectionrecordId) {
		this.sectionrecordId = sectionrecordId;
	}

	public void setStudyTime(Long studyTime) {
		this.studyTime = studyTime;
	}

	public Date getStartStudyTime() {
		return this.startStudyTime;
	}

	public void setStartStudyTime(Date startStudyTime) {
		this.startStudyTime = startStudyTime;
	}

	public Date getEndStudyTime() {
		return this.endStudyTime;
	}

	public void setEndStudyTime(Date endStudyTime) {
		this.endStudyTime = endStudyTime;
	}

}