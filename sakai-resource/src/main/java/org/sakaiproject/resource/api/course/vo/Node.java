package org.sakaiproject.resource.api.course.vo;

import java.util.List;

public class Node {

	private String type;//类型：节点、页、活动等
	
	private String id;//modelId
	
	private String title;//标题
	
	private String studyTime;//建议学习时长
	
	private String avgTime;//平均通过时长
	
	private String myTime;//我的时长
	
	private String required;//必修
	
	private String openStatus;//开启状态
	
	private String passStatus;//通过状态
	
	private String description;//描述
	
	private String courseGuide;//课程引导
	
	private String teachGoal;//教学目标
	
	private String keyAndDifficute;//重点难点
	
	private String teachMethod;//教学方法
	
	private String learnNavigation;//学习导航
	
	private String requirement;//通过条件中文说明
	
	private String openFlag;//开启标志，是否可以开启
	
	private Long attemptNumber;//前测尝试次数
	
	private String videoPicPath;//视频图片地址
	private String videoUrl;//视频地址
	private String videoType;//视频格式类型
	private Long videoSize;//视频大小
	private String videoTime;//视频时长
	
	private List<Node> children;//子节点

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getAttemptNumber() {
		return attemptNumber;
	}

	public void setAttemptNumber(Long attemptNumber) {
		this.attemptNumber = attemptNumber;
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

	public String getStudyTime() {
		return studyTime;
	}

	public void setStudyTime(String studyTime) {
		this.studyTime = studyTime;
	}

	public String getAvgTime() {
		return avgTime;
	}

	public void setAvgTime(String avgTime) {
		this.avgTime = avgTime;
	}

	public String getMyTime() {
		return myTime;
	}

	public void setMyTime(String myTime) {
		this.myTime = myTime;
	}

	public String getRequired() {
		return required;
	}

	public void setRequired(String required) {
		this.required = required;
	}

	public String getOpenStatus() {
		return openStatus;
	}

	public void setOpenStatus(String openStatus) {
		this.openStatus = openStatus;
	}

	public String getPassStatus() {
		return passStatus;
	}

	public void setPassStatus(String passStatus) {
		this.passStatus = passStatus;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRequirement() {
		return requirement;
	}

	public void setRequirement(String requirement) {
		this.requirement = requirement;
	}

	public String getOpenFlag() {
		return openFlag;
	}

	public void setOpenFlag(String openFlag) {
		this.openFlag = openFlag;
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

	public List<Node> getChildren() {
		return children;
	}

	public void setChildren(List<Node> children) {
		this.children = children;
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