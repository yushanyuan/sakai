package com.bupticet.paperadmin.model;

import java.util.Hashtable;

import com.bupticet.paperadmin.common.ItemInfo;

public class TempData {

	StringBuffer pMain[] = null; // 种群

	float pMainFit[] = null; // 适应值

	int nItemTotleNum = 0; // 抽取的题目数目

	ItemInfo pItem[] = null; // 题库

	static StringBuffer sNull = new StringBuffer(""); // 空的染色体

	float fBsfTurn = 0.0f; // 本轮最佳个体适应值

	StringBuffer BestSeed; // 当前最佳个体

	float fBsfBest; // 当前最佳个体适应值

	int nTypeNum; // 类型数目

	int pnTypeIndex[] = null; // 所选题型索引

	int pnTypeDemand[] = null; // 约束,题目数

	float pfTypeDemand[] = null; // 约束，分值

	int pnTypeCount[] = null; // 处理过的约束，数量

	float pfTypeCount[] = null; // 处理过的约束,分值

	int pnTypeMark[] = null; // 记录每类题在数据库中的数目

	float pfTypeAve[] = null; // 题目类型平均分

	float fScoreAve = 0.0f; // 总的平均分

	int nValueSum = 0; // 重要度总和

	boolean bCognHasFree = false; // 认知类型中有没有指定项

	boolean bDiffHasFree = false; // 难度中有没有指定项

	boolean bTypeHasFree = false;

	boolean bKnowHasFree = false;

	Hashtable result = null; // 组卷结果集合

	public TempData() {

	}

}