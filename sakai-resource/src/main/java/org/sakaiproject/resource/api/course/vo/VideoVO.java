/**
 * 
 */
package org.sakaiproject.resource.api.course.vo;

import java.io.Serializable;

/**
 * @author yushanyuan
 * 移动端接口，视频vo
 *
 */
public class VideoVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String videoPicPath;
	private String videoUrl;
	private String title;
	private String nodeType;//section或者module
	private String moduleId;
	private String sectionId;
	public String getVideoPicPath() {
		return videoPicPath;
	}
	public void setVideoPicPath(String videoPicPath) {
		this.videoPicPath = videoPicPath;
	}
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getNodeType() {
		return nodeType;
	}
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	public String getModuleId() {
		return moduleId;
	}
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	public String getSectionId() {
		return sectionId;
	}
	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}
	
}
