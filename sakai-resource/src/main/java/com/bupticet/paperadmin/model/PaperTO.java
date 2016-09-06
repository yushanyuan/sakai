package com.bupticet.paperadmin.model;

import java.util.Hashtable;

import java.io.Serializable;

/**
 * 
 * 
 * <p>
 * Title:
 * </p>
 * 
 * 
 * <p>
 * Description:
 * </p>
 * 
 * 
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * 
 * 
 * <p>
 * Company:
 * </p>
 * 
 * 
 * @author unascribed
 * 
 * 
 * @version 1.0
 * 
 * 
 */

public class PaperTO implements Serializable {

	public Hashtable cFrames = null;// 大题

	public int nPaperID = 0;// 试卷ID

	public String strPaperName = "";// 试卷名称

	public String strCreator = "";// 出卷人姓名

	public java.sql.Date cCreaDate = null;// 出卷日期

	public java.sql.Date cExpoTime = null;// 最后暴光时间

	public float fFullMark = 0;// 试卷总分

	public int nTotalTime = 0;// 完成时间

	public int nCourseID = 0;// 所属课程ID

	public int nUsedTimes = 0;// 试卷被使用的次数

	public String strComment = "";// 试卷备注

	public int nCreatorID = 0;// 出卷人ID

	public int nPaperType = 0;// 试卷创建类型，1自动，2手动，4整张录入

	public int nZipTimes = 0;// 试卷被打包的次数

	public PaperTO() {
	}

	public PaperTO(Hashtable cFrames, int nPaperID, String strPaperName,
			String strCreator,

			java.sql.Date cCreaDate, java.sql.Date cExpoTime, float fFullMark,
			int nTotalTime,

			int nCourseID, int nUsedTimes, String strComment, int nCreatorID,
			int nPaperType, int nZipTimes)

	{

		this.cFrames = cFrames;

		this.nPaperID = nPaperID;

		this.strPaperName = strPaperName;

		this.strCreator = strCreator;

		this.cCreaDate = cCreaDate;

		this.cExpoTime = cExpoTime;

		this.fFullMark = fFullMark;

		this.nTotalTime = nTotalTime;

		this.nCourseID = nCourseID;

		this.nUsedTimes = nUsedTimes;

		this.strComment = strComment;

		this.nCreatorID = nCreatorID;

		this.nPaperType = nPaperType;

		this.nZipTimes = nZipTimes;

	}

	public java.sql.Date getCCreaDate() {
		return cCreaDate;
	}

	public void setCCreaDate(java.sql.Date creaDate) {
		cCreaDate = creaDate;
	}

	public java.sql.Date getCExpoTime() {
		return cExpoTime;
	}

	public void setCExpoTime(java.sql.Date expoTime) {
		cExpoTime = expoTime;
	}

	public Hashtable getCFrames() {
		return cFrames;
	}

	public void setCFrames(Hashtable frames) {
		cFrames = frames;
	}

	public float getFFullMark() {
		return fFullMark;
	}

	public void setFFullMark(float fullMark) {
		fFullMark = fullMark;
	}

	public int getNCourseID() {
		return nCourseID;
	}

	public void setNCourseID(int courseID) {
		nCourseID = courseID;
	}

	public int getNCreatorID() {
		return nCreatorID;
	}

	public void setNCreatorID(int creatorID) {
		nCreatorID = creatorID;
	}

	public int getNPaperID() {
		return nPaperID;
	}

	public void setNPaperID(int paperID) {
		nPaperID = paperID;
	}

	public int getNPaperType() {
		return nPaperType;
	}

	public void setNPaperType(int paperType) {
		nPaperType = paperType;
	}

	public int getNTotalTime() {
		return nTotalTime;
	}

	public void setNTotalTime(int totalTime) {
		nTotalTime = totalTime;
	}

	public int getNUsedTimes() {
		return nUsedTimes;
	}

	public void setNUsedTimes(int usedTimes) {
		nUsedTimes = usedTimes;
	}

	public int getNZipTimes() {
		return nZipTimes;
	}

	public void setNZipTimes(int zipTimes) {
		nZipTimes = zipTimes;
	}

	public String getStrComment() {
		return strComment;
	}

	public void setStrComment(String strComment) {
		this.strComment = strComment;
	}

	public String getStrCreator() {
		return strCreator;
	}

	public void setStrCreator(String strCreator) {
		this.strCreator = strCreator;
	}

	public String getStrPaperName() {
		return strPaperName;
	}

	public void setStrPaperName(String strPaperName) {
		this.strPaperName = strPaperName;
	}
	

}
