package org.sakaiproject.resource.api.course.model;

public class MeleteCourseModel implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1553232460700983764L;
	private String id;//课程id
	private Integer version;//版本号 
	private String creator;//创建人
	private String title;//课程名称
	private String requirement;//通过条件的中文描述
	private Long minModNum;//选修模块的最少选修个数
	private String resCourseid;//资源系统课程id
	private String exCourseid;//作业系统课程id
	private String siteId;//站点id
	private Long avgPassSum;//平均通过模块数
	private String playerTemplate;//播放模板
	private Long status;//课程状态"-1";删除态  "0"; 隐藏态  "1"; 正常态  "2"; 停用态
	private Float avgPassTime;//平均通过时间
	private Float impressionScore;//最高印象分
	private Long impressionType;//加分方式
	private String showStuStatus;//是否显示课程通过状态 0否  1是
	
	public MeleteCourseModel() {
	}
	public String getShowStuStatus() {
		return showStuStatus;
	}
	public void setShowStuStatus(String showStuStatus) {
		this.showStuStatus = showStuStatus;
	}
	public Long getImpressionType() {
		return impressionType;
	}
	public void setImpressionType(Long impressionType) {
		this.impressionType = impressionType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getRequirement() {
		return requirement;
	}
	public void setRequirement(String requirement) {
		this.requirement = requirement;
	}
	public String getResCourseid() {
		return resCourseid;
	}
	public void setResCourseid(String resCourseid) {
		this.resCourseid = resCourseid;
	}
	/*public String getJwCourseid() {
		return jwCourseid;
	}
	public void setJwCourseid(String jwCourseid) {
		this.jwCourseid = jwCourseid;
	}*/
	public String getExCourseid() {
		return exCourseid;
	}
	public void setExCourseid(String exCourseid) {
		this.exCourseid = exCourseid;
	}
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public Long getMinModNum() {
		return minModNum;
	}
	public void setMinModNum(Long minModNum) {
		this.minModNum = minModNum;
	}
	public Long getAvgPassSum() {
		return avgPassSum;
	}
	public void setAvgPassSum(Long avgPassSum) {
		this.avgPassSum = avgPassSum;
	}
	public String getPlayerTemplate() {
		return playerTemplate;
	}
	public void setPlayerTemplate(String playerTemplate) {
		this.playerTemplate = playerTemplate;
	}
	public Long getStatus() {
		return status;
	}
	public void setStatus(Long status) {
		this.status = status;
	}
	public Float getAvgPassTime() {
		return avgPassTime;
	}
	public void setAvgPassTime(Float avgPassTime) {
		this.avgPassTime = avgPassTime;
	}
	public Float getImpressionScore() {
		return impressionScore;
	}
	public void setImpressionScore(Float impressionScore) {
		this.impressionScore = impressionScore;
	}
}
