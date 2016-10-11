/**
 * 版权所有 北京思开科技有限公司 
 * All Rights Reserved
 */
package com.bysakai.mooc.rest.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.bysakai.mooc.model.Categorys;
import com.bysakai.mooc.model.Course;
import com.bysakai.mooc.model.Teacher;

/**
 * @author nesdu
 *
 */
public class CourseVO implements Serializable{

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	
	private String name;
	
	private String desc;
	
	private String syllabus;//大纲
	
	private String image;
	
	private String video;
	
	private Teacher teacher;
	
	private String siteId;//对应的sakai站点id
	
	private String sakaiSiteUrl;
	
	private Categorys category;
	
	private String qas;//常见问题
	
	private Date createDate;
	
	private Date startDate = new Date();//开课时间
	
	private Date endDate = new Date();//结束时间
	
	private String assistant;//助教
	
	private String certification;//认证
	
	private String studyTime; //学时	
	
	private Integer studyNum;//学习人数
	
	private List<Course> relatedCourse;// 关联课程

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getSyllabus() {
		return syllabus;
	}

	public void setSyllabus(String syllabus) {
		this.syllabus = syllabus;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getSakaiSiteUrl() {
		return sakaiSiteUrl;
	}

	public void setSakaiSiteUrl(String sakaiSiteUrl) {
		this.sakaiSiteUrl = sakaiSiteUrl;
	}

	public Categorys getCategory() {
		return category;
	}

	public void setCategory(Categorys category) {
		this.category = category;
	}

	public String getQas() {
		return qas;
	}

	public void setQas(String qas) {
		this.qas = qas;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getAssistant() {
		return assistant;
	}

	public void setAssistant(String assistant) {
		this.assistant = assistant;
	}

	public String getCertification() {
		return certification;
	}

	public void setCertification(String certification) {
		this.certification = certification;
	}

	public String getStudyTime() {
		return studyTime;
	}

	public void setStudyTime(String studyTime) {
		this.studyTime = studyTime;
	}

	public Integer getStudyNum() {
		return studyNum;
	}

	public void setStudyNum(Integer studyNum) {
		this.studyNum = studyNum;
	}

	public List<Course> getRelatedCourse() {
		return relatedCourse;
	}

	public void setRelatedCourse(List<Course> relatedCourse) {
		this.relatedCourse = relatedCourse;
	}

}
