package org.sakaiproject.resource.api.course.model;

import java.util.Date;

public class MeleteForumModel  implements java.io.Serializable{

	private Long id;//论坛id
	private String courseId;//课程id
	private String belongType;//所属类型：1节点，2页
	private String name;//论坛名称
	private String requirement;//通过条件中文说明
	private String areaId;//讨论区id(论坛的上一级)
	private String topicId;//主题id
	private String forumId;//论坛id
	private Date creationDate;//创建时间
	private Date modificationDate;//修改时间
	private Long moduleId;//所属节点id
	private Long sectionId;//所属页id
	private Long idx;//讨论序号
	private Float ratio;//百分比
	private Long status;//论坛状态
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
	public Float getRatio() {
		return ratio;
	}
	public void setRatio(Float ratio) {
		this.ratio = ratio;
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
	public String getRequirement() {
		return requirement;
	}
	public void setRequirement(String requirement) {
		this.requirement = requirement;
	}
	public String getTopicId() {
		return topicId;
	}
	public void setTopicId(String topicId) {
		this.topicId = topicId;
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
	public Long getIdx() {
		return idx;
	}
	public void setIdx(Long idx) {
		this.idx = idx;
	}
	public String getForumId() {
		return forumId;
	}
	public void setForumId(String forumId) {
		this.forumId = forumId;
	}
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
 
}
