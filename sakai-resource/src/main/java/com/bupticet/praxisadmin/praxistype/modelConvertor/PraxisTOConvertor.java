package com.bupticet.praxisadmin.praxistype.modelConvertor;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.bupticet.paperadmin.model.PraxisTO;


public class PraxisTOConvertor {
	public PraxisContentConvertor cPraxisCont; // 题目内容
	public int nPraxisID; // 题目ID
	public int nIsObjective; // 题类：0客观题，1非客观题
	public int nPraxisTypeID; // 题目类型ID
	public String strPraxisTypeName; // 题目类型名称
	public int nSuggTime; // 建议的考试时间
	public float fSuggScore; // 建议的考试得分
	public String strHint; // 题目提示
	public String strGradApproach; // 评分标准
	public int nKnowID; // 知识点ID
	public String strKnowName; // 知识点名称
	public List list;// 图片
	public PraxisContentConvertor getCPraxisCont() {
		return cPraxisCont;
	}
	public void setCPraxisCont(PraxisContentConvertor praxisCont) {
		cPraxisCont = praxisCont;
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
	public int getNIsObjective() {
		return nIsObjective;
	}
	public void setNIsObjective(int isObjective) {
		nIsObjective = isObjective;
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
	public int getNSuggTime() {
		return nSuggTime;
	}
	public void setNSuggTime(int suggTime) {
		nSuggTime = suggTime;
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
	public PraxisTO convertToPraxisTO(Hashtable one,Hashtable two)
	{
		PraxisTO result=new PraxisTO();
		result.cPraxisCont=this.cPraxisCont.converToPraxisContent(this, one, two);
		result.fSuggScore=this.fSuggScore;
		List oneList=new ArrayList();
		for(int i=0;i<this.list.size();i++)
		{
			oneList.add(((MaterialTOConvertor)list.get(i)).convertToMaterialTo());
		}
		result.setList(oneList);
		result.nIsObjective=this.nIsObjective;
		result.nKnowID=this.nKnowID;
		result.nPraxisID=this.nPraxisID;
		result.nPraxisTypeID=this.nPraxisTypeID;
		result.nSuggTime=this.nSuggTime;
		result.strGradApproach=this.strGradApproach;
		result.strHint=this.strHint;
		result.strKnowName=this.strKnowName;
		result.strPraxisTypeName=this.strPraxisTypeName;
		return result;
	}
}
