package org.sakaiproject.resource.api.course.model;

import java.util.Date;

public class MeleteTestModel implements java.io.Serializable{
	private Long id;//作业id
	private String courseId;//课程id
	private Long moduleId;//所属节点id
	private Long sectionId;//所属页id
	private String belongType;//所属类型：1节点，2页
	private String name;//作业名称
	private Long minTimeInterval;//最小时间间隔
	private Long schemaId;//策略id
	private Long totalScore;//总分
	private Long masteryScore;//通过分数百分比
	private String samepaper;//使用同一策略
	private String requirement;//通过条件中文说明
	private Long idx;//作业序号
	private Float ratio;//百分比
	private Date startOpenDate;//作业开放开始时间
	private Date endOpenDate;//作业开放结束时间
	private Date creationDate;//创建时间
	private Date modificationDate;//修改时间
	private Long status;//作业状态
	private String buildType;//组卷方式，即时和已有试卷
	private Long buildNum;//组卷份数
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
	public String getBelongType() {
		return belongType;
	}
	public void setBelongType(String belongType) {
		this.belongType = belongType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getSchemaId() {
		return schemaId;
	}
	public void setSchemaId(Long schemaId) {
		this.schemaId = schemaId;
	}
	public Long getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(Long totalScore) {
		this.totalScore = totalScore;
	}
	public Long getMasteryScore() {
		return masteryScore;
	}
	public void setMasteryScore(Long masteryScore) {
		this.masteryScore = masteryScore;
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
	public Long getMinTimeInterval() {
		return minTimeInterval;
	}
	public void setMinTimeInterval(Long minTimeInterval) {
		this.minTimeInterval = minTimeInterval;
	}
	public Date getStartOpenDate() {
		return startOpenDate;
	}
	public void setStartOpenDate(Date startOpenDate) {
		this.startOpenDate = startOpenDate;
	}
	public Date getEndOpenDate() {
		return endOpenDate;
	}
	public void setEndOpenDate(Date endOpenDate) {
		this.endOpenDate = endOpenDate;
	}
	public String getBuildType() {
		return buildType;
	}
	public void setBuildType(String buildType) {
		this.buildType = buildType;
	}
	public Long getBuildNum() {
		return buildNum;
	}
	public void setBuildNum(Long buildNum) {
		this.buildNum = buildNum;
	}
}
