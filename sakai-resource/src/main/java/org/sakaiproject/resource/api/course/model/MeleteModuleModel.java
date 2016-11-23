package org.sakaiproject.resource.api.course.model;

import java.util.Date;

public class MeleteModuleModel implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3362637011547576817L;
	private Long id;//节点id
	private String courseId;//课程id
	private Long parentId;//父节点id
	private Integer version;//版本号
	private String title;//节点标题
	private String learnObj;//学习目标
	private String description;//说明描述
	private String courseGuide;//课程引导
	private String teachGoal;//教学目标
	private String keyAndDifficute;//重点难点
	private String teachMethod;//教学方法
	private String learnNavigation;//学习导航
	private String keywords;//关键字
	private String createdByFname;//创建人
	private String createdByLname;//因为melete表中该字段在数据库中有非空约束,要兼容melete增加此属性,值与createdByFname的值相同
	private String userId;//创建人id
	private String modifiedByFname;//修改人姓名
	private String whatsNext;//下一步
	private Date creationDate;//创建时间
	private Date modificationDate;//修改时间
	private Long required;//必修
	private Long minSecNum;//选修页的最少选修个数
	private Long idx;//节点序号
	private Long studyDay;//学习时长
	private String prerequids;//开启条件
	private String prerequisites;//开启条件中文说明
	private String moduleSelftest;//前测id
	private String requirement;//通过条件中文说明
	private Long avgPassTime;//平均通过时长
	private Long status;//节点状态  删除态-1 隐藏态0  正常态1  停用态2
	private String childType;//下级节点类型
	private String moduleItemID;//课程目录下课件包描述文件imsmanifest.xml中对应module的item元素中的identifier属性值
	private String videoPicPath;//视频图片地址
	private String videoUrl;//视频地址
	private String videoType;//视频格式类型
	private Long videoSize;//视频大小
	private String videoTime;//视频时长
	
	
	
	public MeleteModuleModel() {
	}
	public String getChildType() {
		return childType;
	}
	public void setChildType(String childType) {
		this.childType = childType;
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
	public String getLearnObj() {
		return learnObj;
	}
	public void setLearnObj(String learnObj) {
		this.learnObj = learnObj;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getCreatedByFname() {
		return createdByFname;
	}
	public void setCreatedByFname(String createdByFname) {
		this.createdByFname = createdByFname;
	}
	
	public String getCreatedByLname() {
		return createdByLname;
	}
	public void setCreatedByLname(String createdByLname) {
		this.createdByLname = createdByLname;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getModifiedByFname() {
		return modifiedByFname;
	}
	public void setModifiedByFname(String modifiedByFname) {
		this.modifiedByFname = modifiedByFname;
	}
	public String getWhatsNext() {
		return whatsNext;
	}
	public void setWhatsNext(String whatsNext) {
		this.whatsNext = whatsNext;
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
	public Long getRequired() {
		return required;
	}
	public void setRequired(Long required) {
		this.required = required;
	}
	public Long getMinSecNum() {
		return minSecNum;
	}
	public void setMinSecNum(Long minSecNum) {
		this.minSecNum = minSecNum;
	}
	public Long getStudyDay() {
		return studyDay;
	}
	public void setStudyDay(Long studyDay) {
		this.studyDay = studyDay;
	}
	public String getPrerequids() {
		return prerequids;
	}
	public void setPrerequids(String prerequids) {
		this.prerequids = prerequids;
	}
	public String getPrerequisites() {
		return prerequisites;
	}
	public void setPrerequisites(String prerequisites) {
		this.prerequisites = prerequisites;
	}
	public String getModuleSelftest() {
		return moduleSelftest;
	}
	public void setModuleSelftest(String moduleSelftest) {
		this.moduleSelftest = moduleSelftest;
	}
	public String getRequirement() {
		return requirement;
	}
	public void setRequirement(String requirement) {
		this.requirement = requirement;
	}
	public Long getAvgPassTime() {
		return avgPassTime;
	}
	public void setAvgPassTime(Long avgPassTime) {
		this.avgPassTime = avgPassTime;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public Long getIdx() {
		return idx;
	}
	public void setIdx(Long idx) {
		this.idx = idx;
	}
	public String getModuleItemID() {
		return moduleItemID;
	}
	public void setModuleItemID(String moduleItemID) {
		this.moduleItemID = moduleItemID;
	}
	public String getCourseGuide() {
		return courseGuide;
	}
	public void setCourseGuide(String courseGuide) {
		this.courseGuide = courseGuide;
	}
	public String getTeachGoal() {
		return teachGoal;
	}
	public void setTeachGoal(String teachGoal) {
		this.teachGoal = teachGoal;
	}
	public String getKeyAndDifficute() {
		return keyAndDifficute;
	}
	public void setKeyAndDifficute(String keyAndDifficute) {
		this.keyAndDifficute = keyAndDifficute;
	}
	public String getTeachMethod() {
		return teachMethod;
	}
	public void setTeachMethod(String teachMethod) {
		this.teachMethod = teachMethod;
	}
	public String getLearnNavigation() {
		return learnNavigation;
	}
	public void setLearnNavigation(String learnNavigation) {
		this.learnNavigation = learnNavigation;
	}
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
	
}
