package org.sakaiproject.resource.api.course.model;
// default package

import java.util.Date;

/**
 * MeleteSakaiUserModel entity. @author MyEclipse Persistence Tools
 */

public class MeleteSakaiUserModel implements java.io.Serializable {

	// Fields

	/**用户ID*/
	private String userId;
	private String email; //EMAIL
	private String emailLc; 
	private String firstName; //名
	private String lastName; //姓
	private String type; //类型
	private String pw; 	//
	private String createdby; //创建于
	private String modifiedby; //修改于
	private Date createdon; //创建在
	private Date modifiedon; //修改在
	private String stunum; //学号
	private String publicstunum; //公共学号
	private String studenttype;  //学生类型
	private String studystatus;  //学习状态
	private String cellphone;  //手机
	private String telephone; //电话
	private String sex; //性别
	private String organizationId; //组织ID
	private String organizationName; //组织名称
	private String specialtyId; //特征ID
	private String specialtyName; //特征名
	private String studentclassId; //学生班级ID
	private String studentclassName; //学生班级名称
	private String eduplanId; 
	private String eduplanName;
	private String studenttagId; //学生标签ID
	private String studenttagName; //学生标签名
	private String photofile; //图像文件
	private Integer year; //年
	private Integer season; //季节

	// Constructors

	/** default constructor */
	public MeleteSakaiUserModel() {
	}

	/** minimal constructor */
	public MeleteSakaiUserModel(String createdby, String modifiedby, Date createdon, Date modifiedon) {
		this.createdby = createdby;
		this.modifiedby = modifiedby;
		this.createdon = createdon;
		this.modifiedon = modifiedon;
	}

	/** full constructor */
	public MeleteSakaiUserModel(String email, String emailLc, String firstName, String lastName, String type,
			String pw, String createdby, String modifiedby, Date createdon, Date modifiedon, String stunum,
			String publicstunum, String studenttype, String studystatus, String cellphone, String telephone,
			String sex, String organizationId, String organizationName, String specialtyId, String specialtyName,
			String studentclassId, String studentclassName, String eduplanId, String eduplanName, String studenttagId,
			String studenttagName, String photofile, Integer year, Integer season) {
		this.email = email;
		this.emailLc = emailLc;
		this.firstName = firstName;
		this.lastName = lastName;
		this.type = type;
		this.pw = pw;
		this.createdby = createdby;
		this.modifiedby = modifiedby;
		this.createdon = createdon;
		this.modifiedon = modifiedon;
		this.stunum = stunum;
		this.publicstunum = publicstunum;
		this.studenttype = studenttype;
		this.studystatus = studystatus;
		this.cellphone = cellphone;
		this.telephone = telephone;
		this.sex = sex;
		this.organizationId = organizationId;
		this.organizationName = organizationName;
		this.specialtyId = specialtyId;
		this.specialtyName = specialtyName;
		this.studentclassId = studentclassId;
		this.studentclassName = studentclassName;
		this.eduplanId = eduplanId;
		this.eduplanName = eduplanName;
		this.studenttagId = studenttagId;
		this.studenttagName = studenttagName;
		this.photofile = photofile;
		this.year = year;
		this.season = season;
	}

	// Property accessors

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmailLc() {
		return this.emailLc;
	}

	public void setEmailLc(String emailLc) {
		this.emailLc = emailLc;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPw() {
		return this.pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public String getCreatedby() {
		return this.createdby;
	}

	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}

	public String getModifiedby() {
		return this.modifiedby;
	}

	public void setModifiedby(String modifiedby) {
		this.modifiedby = modifiedby;
	}

	public Date getCreatedon() {
		return this.createdon;
	}

	public void setCreatedon(Date createdon) {
		this.createdon = createdon;
	}

	public Date getModifiedon() {
		return this.modifiedon;
	}

	public void setModifiedon(Date modifiedon) {
		this.modifiedon = modifiedon;
	}

	public String getStunum() {
		return this.stunum;
	}

	public void setStunum(String stunum) {
		this.stunum = stunum;
	}

	public String getPublicstunum() {
		return this.publicstunum;
	}

	public void setPublicstunum(String publicstunum) {
		this.publicstunum = publicstunum;
	}

	public String getStudenttype() {
		return this.studenttype;
	}

	public void setStudenttype(String studenttype) {
		this.studenttype = studenttype;
	}

	public String getStudystatus() {
		return this.studystatus;
	}

	public void setStudystatus(String studystatus) {
		this.studystatus = studystatus;
	}

	public String getCellphone() {
		return this.cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getOrganizationId() {
		return this.organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getOrganizationName() {
		return this.organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getSpecialtyId() {
		return this.specialtyId;
	}

	public void setSpecialtyId(String specialtyId) {
		this.specialtyId = specialtyId;
	}

	public String getSpecialtyName() {
		return this.specialtyName;
	}

	public void setSpecialtyName(String specialtyName) {
		this.specialtyName = specialtyName;
	}

	public String getStudentclassId() {
		return this.studentclassId;
	}

	public void setStudentclassId(String studentclassId) {
		this.studentclassId = studentclassId;
	}

	public String getStudentclassName() {
		return this.studentclassName;
	}

	public void setStudentclassName(String studentclassName) {
		this.studentclassName = studentclassName;
	}

	public String getEduplanId() {
		return this.eduplanId;
	}

	public void setEduplanId(String eduplanId) {
		this.eduplanId = eduplanId;
	}

	public String getEduplanName() {
		return this.eduplanName;
	}

	public void setEduplanName(String eduplanName) {
		this.eduplanName = eduplanName;
	}

	public String getStudenttagId() {
		return this.studenttagId;
	}

	public void setStudenttagId(String studenttagId) {
		this.studenttagId = studenttagId;
	}

	public String getStudenttagName() {
		return this.studenttagName;
	}

	public void setStudenttagName(String studenttagName) {
		this.studenttagName = studenttagName;
	}

	public String getPhotofile() {
		return this.photofile;
	}

	public void setPhotofile(String photofile) {
		this.photofile = photofile;
	}

	public Integer getYear() {
		return this.year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getSeason() {
		return this.season;
	}

	public void setSeason(Integer season) {
		this.season = season;
	}

}