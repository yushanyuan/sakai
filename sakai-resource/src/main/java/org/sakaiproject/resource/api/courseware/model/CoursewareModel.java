package org.sakaiproject.resource.api.courseware.model;

import java.sql.Timestamp;
import java.util.Date;

/**
 * CourseModel entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CoursewareModel implements java.io.Serializable {

	// Fields

	private String id;
	/**
	 * 课件名称
	 */
	private String courseName;
	/**
	 * 课件摘要
	 */
	private String summary;
	/**
	 * 课件路径
	 */
	private String fileUrl;
	/**
	 * 课件大小
	 */
	private Float fileSize;
	/**
	 * 课件状态
	 */
	private Long status;
	/**
	 * 课件创建者
	 */
	private String creater;
	/**
	 * 课件创建时间
	 */
	private Timestamp createTime;
	/**
	 * 课件修改者
	 */
	private String modifier;
	/**
	 * 课件修改时间
	 */
	private Timestamp updateTime;
	
	/**
	 * 播放地址
	 */
	private String play_url;

	// Constructors

	/** default constructor */
	public CoursewareModel() {
	}

	/** full constructor */
	public CoursewareModel(String courseName, String summary, String fileUrl,
			Float fileSize, Long status, String creater, Timestamp createTime,
			String modifier, Timestamp updateTime, String paly_url) {
		this.courseName = courseName;
		this.summary = summary;
		this.fileUrl = fileUrl;
		this.fileSize = fileSize;
		this.status = status;
		this.creater = creater;
		this.createTime = createTime;
		this.modifier = modifier;
		this.updateTime = updateTime;
		this.play_url = paly_url;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCourseName() {
		return this.courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getSummary() {
		return this.summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getFileUrl() {
		return this.fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public Float getFileSize() {
		return this.fileSize;
	}

	public void setFileSize(Float fileSize) {
		this.fileSize = fileSize;
	}

	public Long getStatus() {
		return this.status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public String getCreater() {
		return this.creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getModifier() {
		return this.modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public String getPlay_url() {
		return play_url;
	}

	public void setPlay_url(String play_url) {
		this.play_url = play_url;
	}

}