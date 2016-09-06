package com.bupticet.paperadmin.common;

import java.io.Serializable;
import java.util.Collection;

import com.bupticet.praxisadmin.praxistype.model.PraxisContent;

public class QuestionTO implements Serializable {

	public int nQuestionID = 0;// 试题编号

	public int nOriginalPraxisID = 0;// 原题(在题库)编号

	public int nPraxisTypeID = 0;// 题型ID

	public int nFrameID = 0;// 所属大题ID

	public int nOrder = 0;// 小题序号

	public float fScore = (float) 5;// 试题分值

	public PraxisContent cQuestionCont = null;// 试题内容答案

	public String strGradApproach = "";// 试题评分标准

	public String strSnapShot = "";// 试题快照

	public float fDifficulty = (float) 0.5;// 难度

	public String strCognizetype = "理解";// 认知分类

	public int nSuggTime = 30;// 建议考试时间

	public Collection cMaterialIDs = null;

	public int nRelaModulus = 0;

	public QuestionTO() {
	}

	public QuestionTO(int nQuestionID, int nOriginalPraxisID,
			int nPraxisTypeID, int nFrameID, int nOrder, float fScore,
			PraxisContent cQuestionCont, String strGradApproach,
			String strSnapShot, float fDifficulty, String strCognizetype,
			int nSuggTime, Collection cMaterialIDs)

	{

		this.nQuestionID = nQuestionID;

		this.nOriginalPraxisID = nOriginalPraxisID;

		this.nPraxisTypeID = nPraxisTypeID;

		this.nFrameID = nFrameID;

		this.nOrder = nOrder;

		this.fScore = fScore;

		this.cQuestionCont = cQuestionCont;

		this.strGradApproach = strGradApproach;

		this.strSnapShot = strSnapShot;

		this.fDifficulty = fDifficulty;

		this.strCognizetype = strCognizetype;

		this.nSuggTime = nSuggTime;

		this.cMaterialIDs = cMaterialIDs;

	}

	// 李建伟加方法，添加了题目相似标志
	public QuestionTO(int nQuestionID, int nOriginalPraxisID,
			int nPraxisTypeID, int nFrameID, int nOrder, float fScore,
			PraxisContent cQuestionCont, String strGradApproach,
			String strSnapShot, float fDifficulty, String strCognizetype,
			int nSuggTime, Collection cMaterialIDs, int nRelaModulus)

	{

		this.nQuestionID = nQuestionID;

		this.nOriginalPraxisID = nOriginalPraxisID;

		this.nPraxisTypeID = nPraxisTypeID;

		this.nFrameID = nFrameID;

		this.nOrder = nOrder;

		this.fScore = fScore;

		this.cQuestionCont = cQuestionCont;

		this.strGradApproach = strGradApproach;

		this.strSnapShot = strSnapShot;

		this.fDifficulty = fDifficulty;

		this.strCognizetype = strCognizetype;

		this.nSuggTime = nSuggTime;

		this.cMaterialIDs = cMaterialIDs;
		this.nRelaModulus = nRelaModulus;

	}

}