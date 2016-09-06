package org.sakaiproject.resource.api.file.model;

import java.sql.Timestamp;
import java.util.Date;

/**
 * ResourceFileModel entity. @author MyEclipse Persistence Tools
 */

public class ResourceFileModel implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 1723821983048266299L;

	/** 文件记录ID */
	private String id;
	/** 所属文件夹ID */
	private String folderId;
	/** 文件名称 */
	private String fileName;
	/** 文件摘要 */
	private String summary;
	/** 文件类型 */
	private String fileType;
	/** 文件URL */
	private String fileUrl;
	/** 文件大小 */
	private Double fileSize;
	/** 文件是否计算平时成绩 1为计算 0为不计算 */
	private Integer extendOption;
	/** 状态 */
	private Integer status;
	/** 创建用户ID */
	private String creater;
	/** 创建时间 */
	private Date createTime;
	/** 修改用户 */
	private String modifier;
	/** 修改时间 */
	private Date updateTime;

	/** 资源下载次数 */
	private Integer downloadCount = 0;

	public Integer getDownloadCount() {
		return downloadCount;
	}

	public void setDownloadCount(Integer downloadCount) {
		this.downloadCount = downloadCount;
	}

	/** 资源占百分比 */
	private Float ratio;

	/** 文件内容 在修改文件内容时用于存放 txt html 文件内容 不为数据库表字段 */
	private String fileContent;

	// Constructors

	public String getFileContent() {
		return fileContent;
	}

	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}

	public Integer getExtendOption() {
		return extendOption;
	}

	public void setExtendOption(Integer extendOption) {
		this.extendOption = extendOption;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/** default constructor */
	public ResourceFileModel() {
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFolderId() {
		return this.folderId;
	}

	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getSummary() {
		return this.summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getFileType() {
		return this.fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileUrl() {
		return this.fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public Double getFileSize() {
		return this.fileSize;
	}

	public void setFileSize(Double fileSize) {
		this.fileSize = fileSize;
	}

	public String getCreater() {
		return this.creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getModifier() {
		return this.modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public Float getRatio() {
		return ratio;
	}

	public void setRatio(Float ratio) {
		this.ratio = ratio;
	}

}