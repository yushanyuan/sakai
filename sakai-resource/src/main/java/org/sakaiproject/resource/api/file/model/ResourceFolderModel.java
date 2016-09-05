package org.sakaiproject.resource.api.file.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * ResourceFolderModel entity. @author MyEclipse Persistence Tools
 */

public class ResourceFolderModel implements java.io.Serializable {

	// Fields

	/** 资源文件夹ID */
	private String id;
	/** 所属站点ID */
	private String siteId;
	/** 父资源文件夹ID */
	private String parentId;
	/** 资源文件夹名称 */
	private String folderName;
	/** 状态 */
	private Integer status;
	/** 资源文件夹创建者ID */
	private String creater;
	/** 资源文件夹创建时间 */
	private Date createTime;
	/** 资源文件夹修改者ID */
	private String modifier;
	/** 资源文件夹修改时间 */
	private Date updateTime;

	// Constructors

	/** default constructor */
	public ResourceFolderModel() {
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSiteId() {
		return this.siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getParentId() {
		return this.parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getFolderName() {
		return this.folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCreater() {
		return this.creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
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

	public String getModifier() {
		return this.modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

}