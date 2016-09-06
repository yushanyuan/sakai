package org.sakaiproject.resource.api.template.model;

import java.sql.Timestamp;

/**
 * 课件模板
 * @author cedarwu
 *
 */
public class TemplateModel implements java.io.Serializable {
	/**
	 * 模板实体
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 模板名称：自定义
	 */
	public static final String TEMPLATE_NAME_CUSTOM = "custom";
	/**
	 * 模板名称：默认
	 */
	public static final String TEMPLATE_NAME_DEFAULT = "default";
	
	public static final String TEMPLATE_PAGE_CHAPTER = "chapter.html";
	public static final String TEMPLATE_PAGE_MODULE = "module.html";
	
	/**
	 * 状态：可用
	 */
	public static final String ENUM_STATUS_ENABLED = "1";
	
	public static final String ENUM_STATUS_DISABLED = "9";
	
	private String id;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 模板路径名， 必须唯一
	 */
	private String name;
	
	/**
	 * 摘要
	 */
	private String summary;	
	
	/**
	 * 1 可用，9停用
	 */
	private String status;
	
	/**
	 * 创建者
	 */
	private String createUser;
	/**
	 * 创建时间
	 */
	private Timestamp createTime;
	/**
	 * 修改者
	 */
	private String updateUser;
	/**
	 * 修改时间
	 */
	private Timestamp updateTime;
	
	private Integer isDefault = 0;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}
}
