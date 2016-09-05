package com.bupticet.paperadmin.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import com.bupticet.paperadmin.common.MaterialTO;
import com.bupticet.praxisadmin.praxistype.model.PraxisContent;
import com.bupticet.praxisadmin.praxistype.modelConvertor.PraxisTOConvertor;

public class PraxisTO implements Serializable {

	public PraxisContent cPraxisCont; // 题目内容

	public int nPraxisID; // 题目ID

	public int nCourseID; // 所属课程

	public int nIsObjective; // 题类：0客观题，1非客观题

	public int nPraxisTypeID; // 题目类型ID

	public String strPraxisTypeName; // 题目类型名称

	public int nPraxisUse; // 题目用途

	public float fDifficulty; // 难度

	public float fDivision; // 区分度

	public String strCognizetype; // 认知分类

	public int nSuggTime; // 建议的考试时间

	public float fSuggScore; // 建议的考试得分

	public int nUsedTimes; // 本道题目的正式使用次数

	public java.sql.Date cExpoTime; // 最后曝光时间

	public int nRelaModulus; // 相关系数

	public int nAudiStatus; // 试题审核标识

	public java.sql.Date cAudiDate; // 审核日期

	public java.sql.Date cCreaDate; // 出题日期

	public String strExamDemand; // 考试要求

	public int nJudgType; // 批改类型

	public String strAssessor; // 审核人姓名

	public String strCreator; // 出题人姓名

	public int nCreatorID; // 出题人ID

	public String strHint; // 题目提示

	public String strTeacDemand; // 题目要求

	public String strGradApproach; // 评分标准

	public Collection cMaterialIDs;

	public int nKnowID; // 知识点ID

	public String strKnowName; // 知识点名称

	public String strCheckAdvice; // 审核意见

	public int PraxisNo; // 题目编号

	public List list;// 图片

	public PraxisTO()

	{

	}

	public PraxisTO(PraxisContent cPraxisCont, int nPraxisID, int nCourseID,
			int nIsObjective, int nPraxisTypeID, String strPraxisTypeName,
			int nPraxisUse,

			float fDifficulty, float fDivision, String strCognizetype,
			int nSuggTime, float fSuggScore,

			int nUsedTimes, Date cExpoTime, int nRelaModulus, int nAudiStatus,
			Date cAudiDate, Date cCreaDate,

			String strExamDemand, int nJudgType, String strAssessor,
			String strCreator, String strHint,

			String strTeacDemand, String strGradApproach,
			Collection cMaterialIDs, int nKnowID, String strKnowName,
			int nCreatorID, List list)

	{

		this.cPraxisCont = cPraxisCont;

		this.nPraxisID = nPraxisID;

		this.nCourseID = nCourseID;

		this.nIsObjective = nIsObjective;

		this.nPraxisTypeID = nPraxisTypeID;

		this.strPraxisTypeName = strPraxisTypeName;

		this.nPraxisUse = nPraxisUse;

		this.fDifficulty = fDifficulty;

		this.fDivision = fDivision;

		this.strCognizetype = strCognizetype;

		this.nSuggTime = nSuggTime;

		this.fSuggScore = fSuggScore;

		this.nUsedTimes = nUsedTimes;

		this.cExpoTime = cExpoTime;

		this.nRelaModulus = nRelaModulus;

		this.nAudiStatus = nAudiStatus;

		this.cAudiDate = cAudiDate;

		this.cCreaDate = cCreaDate;

		this.strExamDemand = strExamDemand;

		this.nJudgType = nJudgType;

		this.strAssessor = strAssessor;

		this.strCreator = strCreator;

		this.strHint = strHint;

		this.strTeacDemand = strTeacDemand;

		this.strGradApproach = strGradApproach;

		this.cMaterialIDs = cMaterialIDs;

		this.nKnowID = nKnowID;

		this.strKnowName = strKnowName;

		this.nCreatorID = nCreatorID;
		this.list = list;

	}

	public PraxisTO(PraxisContent cPraxisCont, int nPraxisID, int nCourseID,
			int nIsObjective, int nPraxisTypeID, String strPraxisTypeName,
			int nPraxisUse,

			float fDifficulty, float fDivision, String strCognizetype,
			int nSuggTime, float fSuggScore,

			int nUsedTimes, Date cExpoTime, int nRelaModulus, int nAudiStatus,
			Date cAudiDate, Date cCreaDate,

			String strExamDemand, int nJudgType, String strAssessor,
			String strCreator, String strHint,

			String strTeacDemand, String strGradApproach,
			Collection cMaterialIDs, int nKnowID, String strKnowName,
			int nCreatorID, String strCheckAdvice, int PraxisNo)

	{

		this.cPraxisCont = cPraxisCont;

		this.nPraxisID = nPraxisID;

		this.nCourseID = nCourseID;

		this.nIsObjective = nIsObjective;

		this.nPraxisTypeID = nPraxisTypeID;

		this.strPraxisTypeName = strPraxisTypeName;

		this.nPraxisUse = nPraxisUse;

		this.fDifficulty = fDifficulty;

		this.fDivision = fDivision;

		this.strCognizetype = strCognizetype;

		this.nSuggTime = nSuggTime;

		this.fSuggScore = fSuggScore;

		this.nUsedTimes = nUsedTimes;

		this.cExpoTime = cExpoTime;

		this.nRelaModulus = nRelaModulus;

		this.nAudiStatus = nAudiStatus;

		this.cAudiDate = cAudiDate;

		this.cCreaDate = cCreaDate;

		this.strExamDemand = strExamDemand;

		this.nJudgType = nJudgType;

		this.strAssessor = strAssessor;

		this.strCreator = strCreator;

		this.strHint = strHint;

		this.strTeacDemand = strTeacDemand;

		this.strGradApproach = strGradApproach;

		this.cMaterialIDs = cMaterialIDs;

		this.nKnowID = nKnowID;

		this.strKnowName = strKnowName;

		this.nCreatorID = nCreatorID;

		this.strCheckAdvice = strCheckAdvice;

		this.PraxisNo = PraxisNo;

	}

	public java.sql.Date getCAudiDate() {
		return cAudiDate;
	}

	public void setCAudiDate(java.sql.Date audiDate) {
		cAudiDate = audiDate;
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

	public Collection getCMaterialIDs() {
		return cMaterialIDs;
	}

	public void setCMaterialIDs(Collection materialIDs) {
		cMaterialIDs = materialIDs;
	}

	public PraxisContent getCPraxisCont() {
		return cPraxisCont;
	}

	public void setCPraxisCont(PraxisContent praxisCont) {
		cPraxisCont = praxisCont;
	}

	public float getFDifficulty() {
		return fDifficulty;
	}

	public void setFDifficulty(float difficulty) {
		fDifficulty = difficulty;
	}

	public float getFDivision() {
		return fDivision;
	}

	public void setFDivision(float division) {
		fDivision = division;
	}

	public float getFSuggScore() {
		return fSuggScore;
	}

	public void setFSuggScore(float suggScore) {
		fSuggScore = suggScore;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public int getNAudiStatus() {
		return nAudiStatus;
	}

	public void setNAudiStatus(int audiStatus) {
		nAudiStatus = audiStatus;
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

	public int getNIsObjective() {
		return nIsObjective;
	}

	public void setNIsObjective(int isObjective) {
		nIsObjective = isObjective;
	}

	public int getNJudgType() {
		return nJudgType;
	}

	public void setNJudgType(int judgType) {
		nJudgType = judgType;
	}

	public int getNKnowID() {
		return nKnowID;
	}

	public void setNKnowID(int knowID) {
		nKnowID = knowID;
	}

	public int getNPraxisID() {
		return nPraxisID;
	}

	public void setNPraxisID(int praxisID) {
		nPraxisID = praxisID;
	}

	public int getNPraxisTypeID() {
		return nPraxisTypeID;
	}

	public void setNPraxisTypeID(int praxisTypeID) {
		nPraxisTypeID = praxisTypeID;
	}

	public int getNPraxisUse() {
		return nPraxisUse;
	}

	public void setNPraxisUse(int praxisUse) {
		nPraxisUse = praxisUse;
	}

	public int getNRelaModulus() {
		return nRelaModulus;
	}

	public void setNRelaModulus(int relaModulus) {
		nRelaModulus = relaModulus;
	}

	public int getNSuggTime() {
		return nSuggTime;
	}

	public void setNSuggTime(int suggTime) {
		nSuggTime = suggTime;
	}

	public int getNUsedTimes() {
		return nUsedTimes;
	}

	public void setNUsedTimes(int usedTimes) {
		nUsedTimes = usedTimes;
	}

	public int getPraxisNo() {
		return PraxisNo;
	}

	public void setPraxisNo(int praxisNo) {
		PraxisNo = praxisNo;
	}

	public String getStrAssessor() {
		return strAssessor;
	}

	public void setStrAssessor(String strAssessor) {
		this.strAssessor = strAssessor;
	}

	public String getStrCheckAdvice() {
		return strCheckAdvice;
	}

	public void setStrCheckAdvice(String strCheckAdvice) {
		this.strCheckAdvice = strCheckAdvice;
	}

	public String getStrCognizetype() {
		return strCognizetype;
	}

	public void setStrCognizetype(String strCognizetype) {
		this.strCognizetype = strCognizetype;
	}

	public String getStrCreator() {
		return strCreator;
	}

	public void setStrCreator(String strCreator) {
		this.strCreator = strCreator;
	}

	public String getStrExamDemand() {
		return strExamDemand;
	}

	public void setStrExamDemand(String strExamDemand) {
		this.strExamDemand = strExamDemand;
	}

	public String getStrGradApproach() {
		return strGradApproach;
	}

	public void setStrGradApproach(String strGradApproach) {
		this.strGradApproach = strGradApproach;
	}

	public String getStrHint() {
		return strHint;
	}

	public void setStrHint(String strHint) {
		this.strHint = strHint;
	}

	public String getStrKnowName() {
		return strKnowName;
	}

	public void setStrKnowName(String strKnowName) {
		this.strKnowName = strKnowName;
	}

	public String getStrPraxisTypeName() {
		return strPraxisTypeName;
	}

	public void setStrPraxisTypeName(String strPraxisTypeName) {
		this.strPraxisTypeName = strPraxisTypeName;
	}

	public String getStrTeacDemand() {
		return strTeacDemand;
	}

	public void setStrTeacDemand(String strTeacDemand) {
		this.strTeacDemand = strTeacDemand;
	}
	public PraxisTOConvertor convertToPraxisTOConvertor(Hashtable one,Hashtable two)
	{
		PraxisTOConvertor result=new PraxisTOConvertor();
		result.setCPraxisCont(this.cPraxisCont.converToPraxisContent(this, one, two));
		result.setFSuggScore(this.fSuggScore);
		List oneList = new ArrayList();
		for(int i=0;i<this.list.size();i++)
		{
			oneList.add(((MaterialTO)this.list.get(i)).convertToMaterialTo());
		}
		result.setList(oneList);
		result.setNIsObjective(this.nIsObjective);
		result.setNKnowID(this.nKnowID);
		result.setNPraxisID(this.nPraxisID);
		result.setNPraxisTypeID(this.nPraxisTypeID);
		result.setNSuggTime(this.nSuggTime);
		result.setStrGradApproach(this.strGradApproach);
		result.setStrHint(this.strHint);
		result.setStrKnowName(this.strKnowName);
		result.setStrPraxisTypeName(this.strPraxisTypeName);
		return result;
	}
}