package org.sakaiproject.resource.api.course.model;

import java.util.Date;

public class MeleteSectionModel implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6665465620779714813L;
	private Long id;//页id
	private String courseId;//课程id
	private Long moduleId;//节点id
	private Integer version;//版本号
	private String title;//页标题
	private String createdByFname;//创建人
	private String createdByLname;///要兼容melete,此属性是以后增加的,因为melete表中该字段在数据库中有非空约束,值与createdByFname的值相同
	private String modifiedByFname;//修改人
	private Long audioContent;//是否包含音频
	private Long videoContent;//是否包含视频
	private Long textualContent;//是否包含文本
	private Long idx;//页序号
	private Date creationDate;//创建时间
	private Date modificationDate;//修改时间
	private String description;//说明描述
	private String path;//存储路径
	private String prerequisites;//开启条件中文说明
	private Long required;//必修
	private Long studyTime;//学习时长
	private String prerequids;//开启条件
	private String requirement;//通过条件中文说明
	private String sectionSelftest;//前测id
	private Long status;//页状态 删除态-1 隐藏态0  正常态1  停用态2
	private Long avgPassTime;//平均通过时长
	private String contentType;//要兼容melete,此属性是以后增加的,因为melete表中该字段在数据库中有非空约束,无实际作用
	private String sectionItemId;//课程目录下课件包描述文件imsmanifest.xml中对应section的item元素中的identifier属性值
	
	private String videoPicPath;//视频图片地址
	private String videoUrl;//视频地址
	private String videoType;//视频格式类型
	private Long videoSize;//视频大小
	private String videoTime;//视频时长
	
	public MeleteSectionModel() {
	}
	public String getSectionItemId() {
		return sectionItemId;
	}
	public void setSectionItemId(String sectionItemId) {
		this.sectionItemId = sectionItemId;
	}
	public Long getAvgPassTime() {
		return avgPassTime;
	}
	public void setAvgPassTime(Long avgPassTime) {
		this.avgPassTime = avgPassTime;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getModuleId() {
		return moduleId;
	}
	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCreatedByFname() {
		return createdByFname;
	}
	public void setCreatedByFname(String createdByFname) {
		this.createdByFname = createdByFname;
	}
	public String getModifiedByFname() {
		return modifiedByFname;
	}
	
	public String getCreatedByLname() {
		return createdByLname;
	}
	public void setCreatedByLname(String createdByLname) {
		this.createdByLname = createdByLname;
	}
	public void setModifiedByFname(String modifiedByFname) {
		this.modifiedByFname = modifiedByFname;
	}
	public Long getAudioContent() {
		return audioContent;
	}
	public void setAudioContent(Long audioContent) {
		this.audioContent = audioContent;
	}
	public Long getVideoContent() {
		return videoContent;
	}
	public void setVideoContent(Long videoContent) {
		this.videoContent = videoContent;
	}
	public Long getTextualContent() {
		return textualContent;
	}
	public void setTextualContent(Long textualContent) {
		this.textualContent = textualContent;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPrerequisites() {
		return prerequisites;
	}
	public void setPrerequisites(String prerequisites) {
		this.prerequisites = prerequisites;
	}
	public Long getRequired() {
		return required;
	}
	public void setRequired(Long required) {
		this.required = required;
	}
	public Long getStudyTime() {
		return studyTime;
	}
	public void setStudyTime(Long studyTime) {
		this.studyTime = studyTime;
	}
	public String getPrerequids() {
		return prerequids;
	}
	public void setPrerequids(String prerequids) {
		this.prerequids = prerequids;
	}
	public String getRequirement() {
		return requirement;
	}
	public void setRequirement(String requirement) {
		this.requirement = requirement;
	}
	public String getSectionSelftest() {
		return sectionSelftest;
	}
	public void setSectionSelftest(String sectionSelftest) {
		this.sectionSelftest = sectionSelftest;
	}
	public Long getStatus() {
		return status;
	}
	public void setStatus(Long status) {
		this.status = status;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Long getIdx() {
		return idx;
	}
	public void setIdx(Long idx) {
		this.idx = idx;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	public String getVideoType() {
		return videoType;
	}
	public void setVideoType(String videoType) {
		this.videoType = videoType;
	}
	public Long getVideoSize() {
		return videoSize;
	}
	public void setVideoSize(Long videoSize) {
		this.videoSize = videoSize;
	}
	public String getVideoTime() {
		return videoTime;
	}
	public void setVideoTime(String videoTime) {
		this.videoTime = videoTime;
	}
	public String getVideoPicPath() {
		return videoPicPath;
	}
	public void setVideoPicPath(String videoPicPath) {
		this.videoPicPath = videoPicPath;
	}
	
}
