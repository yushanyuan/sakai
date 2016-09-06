package com.bupticet.paperadmin.common;

public class ItemInfo {

	public int nItemID = 0;

	public int nKnowID = 0; // 知识点ID

	public String strDiff = ""; // 难度

	public int nPraxisTypeID; // 题目类型ID

	public int nSuggTime = 0; // 建议的考试时间

	public float fSuggScore = 0; // 建议的考试得分

	public String strCognizetype = ""; // 认知分类

	public int nRelaModulus = -1; // 相关系数

	public ItemInfo() {

	}

	public ItemInfo(int nItemID, int nKnowID, String strDiff,
			int nPraxisTypeID,

			int nSuggTime, float fSuggScore, String strCognizetype,
			int nRelaModulus) {

		this.nItemID = nItemID;

		this.nKnowID = nKnowID;

		this.strDiff = strDiff;

		this.strCognizetype = strCognizetype;

		this.nSuggTime = nSuggTime;

		this.nPraxisTypeID = nPraxisTypeID;

		this.nRelaModulus = nRelaModulus;

		this.fSuggScore = fSuggScore;

	}

}