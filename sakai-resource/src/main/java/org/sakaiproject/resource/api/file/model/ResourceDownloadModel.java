package org.sakaiproject.resource.api.file.model;

import java.sql.Timestamp;
import java.util.Date;

/**
 * ResourceDownloadModel entity. @author MyEclipse Persistence Tools
 */

public class ResourceDownloadModel implements java.io.Serializable {

	// Fields

	/** 下载记录ID */
	private String id;
	/** 对应的文件ID */
	private String fileId;
	/** 下载用户ID */
	private String downloader;
	/** 下载时间 */
	private Date lastTime;

	// Constructors

	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	/** default constructor */
	public ResourceDownloadModel() {
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFileId() {
		return this.fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getDownloader() {
		return this.downloader;
	}

	public void setDownloader(String downloader) {
		this.downloader = downloader;
	}

}