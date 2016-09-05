package org.sakaiproject.resource.api.course.model;

import java.util.Date;

public class MeleteSelfTestModel implements java.io.Serializable{
	private Long id;//前测id
	private String courseId;//课程id
	private String name;//前测名称
	private Long totalScore;//总分
	private Long schemaId;//策略id
	private String belongType;//所属类型：1节点，2页
	private String samepaper;//使用同一策略
	private String requirement;//通过条件中文说明
	private Long moduleId;//所属节点id
	private Long sectionId;//所属页id
	private Date creationDate;//创建时间
	private Date modificationDate;//修改时间
	private Long masteryScore;//通过分数
	private Long idx;//前测序号
	private Float ratio;//百分比
	private Long status;//前测状态
	private Long isCaculateScore;//是否计算平时成绩
	public Long getIsCaculateScore() {
		return isCaculateScore;
	}
	public void setIsCaculateScore(Long isCaculateScore) {
		this.isCaculateScore = isCaculateScore;
	}
	public Long getStatus() {
		return status;
	}
	public void setStatus(Long status) {
		this.status = status;
	}
	public Long getMasteryScore() {
		return masteryScore;
	}
	public void setMasteryScore(Long masteryScore) {
		this.masteryScore = masteryScore;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(Long totalScore) {
		this.totalScore = totalScore;
	}
	public Long getSchemaId() {
		return schemaId;
	}
	public void setSchemaId(Long schemaId) {
		this.schemaId = schemaId;
	}
	public String getBelongType() {
		return belongType;
	}
	public void setBelongType(String belongType) {
		this.belongType = belongType;
	}
	public String getSamepaper() {
		return samepaper;
	}
	public void setSamepaper(String samepaper) {
		this.samepaper = samepaper;
	}
	public String getRequirement() {
		return requirement;
	}
	public void setRequirement(String requirement) {
		this.requirement = requirement;
	}
	public Long getModuleId() {
		return moduleId;
	}
	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}
	public Long getSectionId() {
		return sectionId;
	}
	public void setSectionId(Long sectionId) {
		this.sectionId = sectionId;
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
	public Long getIdx() {
		return idx;
	}
	public void setIdx(Long idx) {
		this.idx = idx;
	}
	public Float getRatio() {
		return ratio;
	}
	public void setRatio(Float ratio) {
		this.ratio = ratio;
	}
}
