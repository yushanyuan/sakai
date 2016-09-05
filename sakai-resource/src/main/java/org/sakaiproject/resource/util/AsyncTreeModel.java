package org.sakaiproject.resource.util;

import java.util.Map;

public class AsyncTreeModel {
	private String id;
	private String parentId;
	private String text;
	private String icon;//设置节点上图标的路径
	private String iconCls;
	private String qtip;
	private String href;
	private boolean leaf;
	private String uiProvider = "col";
	private boolean allowDrag = true;//设置为fals将使得该节点不能拖拽
	private boolean allowDrop = true;//为false时该节点不能将拖拽的对象放在该节点下。
	private boolean expanded = true;//如果为"true",该节点被展开

	/** 是否计算平时成绩 */
	private String isCaculateScore;

	/** 树节点类型 folder file */
	private String treeNodeType;

	/** 文件操作类型 create modify */
	private String fileOpType;

	/** 文件大小 */
	private String folderSize;

	/** 更改时间 */
	private String updateTime;

	/** 文件摘要 */
	private String fileSummary;

	/** 文件类型文件后缀名.html .txt 等等 */
	private String fileType;
	private Map<String, Object> attributes;

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileOpType() {
		return fileOpType;
	}

	public void setFileOpType(String fileOpType) {
		this.fileOpType = fileOpType;
	}

	public String getIsCaculateScore() {
		return isCaculateScore;
	}

	public void setIsCaculateScore(String isCaculateScore) {
		this.isCaculateScore = isCaculateScore;
	}

	public String getTreeNodeType() {
		return treeNodeType;
	}

	public void setTreeNodeType(String treeNodeType) {
		this.treeNodeType = treeNodeType;
	}

	public String getFileSummary() {
		return fileSummary;
	}

	public void setFileSummary(String fileSummary) {
		this.fileSummary = fileSummary;
	}

	public String getUiProvider() {
		return uiProvider;
	}

	public void setUiProvider(String uiProvider) {
		this.uiProvider = uiProvider;
	}

	public String getFolderSize() {
		return folderSize;
	}

	public void setFolderSize(String folderSize) {
		this.folderSize = folderSize;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public String getQtip() {
		return qtip;
	}

	public void setQtip(String qtip) {
		this.qtip = qtip;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public boolean getLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public boolean isAllowDrag() {
		return allowDrag;
	}

	public void setAllowDrag(boolean allowDrag) {
		this.allowDrag = allowDrag;
	}

	public boolean isAllowDrop() {
		return allowDrop;
	}

	public void setAllowDrop(boolean allowDrop) {
		this.allowDrop = allowDrop;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

}
