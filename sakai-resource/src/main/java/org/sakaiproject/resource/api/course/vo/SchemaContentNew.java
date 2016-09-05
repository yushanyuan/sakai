package org.sakaiproject.resource.api.course.vo;

import java.io.Serializable;

/**
 * 题库：策略内容
 * @author cedarwu
 *
 */
public class SchemaContentNew implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private PaperAttributeNew cTotalTime = null;// 总的时间

	private PaperAttributeNew cFullMark = null;// 总分

	private PraxisDistributeNew cTypeDist = null;// 类型分布

	private PraxisDistributeNew cDiffDist = null;// 难度分布

	private PraxisDistributeNew cKnowDist = null;// 知识点分布

	private PraxisDistributeNew cCognDist = null;// 认知程度分布

	private int nMaxUsedTimes = 0;// 最大使用次数

	private int nMinTimeSlot = 0;// 最小使用日期间隔，天数

	private int nRelativite = 0;// 相关系数

	private int nPraxisUse = 0;// 题目用途

	private int nAuditStatus = 0;// 题目审核情况

	private int nCourseID = 0;// 所属课程ID

	private int nSchemaID = 0;// 策略ID

	private int nTotalAccording = 0;// 总满意度

	public PaperAttributeNew getcTotalTime() {
		return cTotalTime;
	}

	public void setcTotalTime(PaperAttributeNew cTotalTime) {
		this.cTotalTime = cTotalTime;
	}

	public PaperAttributeNew getcFullMark() {
		return cFullMark;
	}

	public void setcFullMark(PaperAttributeNew cFullMark) {
		this.cFullMark = cFullMark;
	}

	public PraxisDistributeNew getcTypeDist() {
		return cTypeDist;
	}

	public void setcTypeDist(PraxisDistributeNew cTypeDist) {
		this.cTypeDist = cTypeDist;
	}

	public PraxisDistributeNew getcDiffDist() {
		return cDiffDist;
	}

	public void setcDiffDist(PraxisDistributeNew cDiffDist) {
		this.cDiffDist = cDiffDist;
	}

	public PraxisDistributeNew getcKnowDist() {
		return cKnowDist;
	}

	public void setcKnowDist(PraxisDistributeNew cKnowDist) {
		this.cKnowDist = cKnowDist;
	}

	public PraxisDistributeNew getcCognDist() {
		return cCognDist;
	}

	public void setcCognDist(PraxisDistributeNew cCognDist) {
		this.cCognDist = cCognDist;
	}

	public int getnMaxUsedTimes() {
		return nMaxUsedTimes;
	}

	public void setnMaxUsedTimes(int nMaxUsedTimes) {
		this.nMaxUsedTimes = nMaxUsedTimes;
	}

	public int getnMinTimeSlot() {
		return nMinTimeSlot;
	}

	public void setnMinTimeSlot(int nMinTimeSlot) {
		this.nMinTimeSlot = nMinTimeSlot;
	}

	public int getnRelativite() {
		return nRelativite;
	}

	public void setnRelativite(int nRelativite) {
		this.nRelativite = nRelativite;
	}

	public int getnPraxisUse() {
		return nPraxisUse;
	}

	public void setnPraxisUse(int nPraxisUse) {
		this.nPraxisUse = nPraxisUse;
	}

	public int getnAuditStatus() {
		return nAuditStatus;
	}

	public void setnAuditStatus(int nAuditStatus) {
		this.nAuditStatus = nAuditStatus;
	}

	public int getnCourseID() {
		return nCourseID;
	}

	public void setnCourseID(int nCourseID) {
		this.nCourseID = nCourseID;
	}

	public int getnSchemaID() {
		return nSchemaID;
	}

	public void setnSchemaID(int nSchemaID) {
		this.nSchemaID = nSchemaID;
	}

	public int getnTotalAccording() {
		return nTotalAccording;
	}

	public void setnTotalAccording(int nTotalAccording) {
		this.nTotalAccording = nTotalAccording;
	}

}