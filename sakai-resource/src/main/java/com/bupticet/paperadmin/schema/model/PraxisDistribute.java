package com.bupticet.paperadmin.schema.model;

import java.io.Serializable;

import java.util.Hashtable;

public class PraxisDistribute implements Serializable {

	public int nItemID = 0;

	public int nAccording = 0;// 满意度

	public int nImport = 0;// 重要度

	public int nMarkOrAmount = 0;// 按题目数1，还是分值0

	public Hashtable cDemand = null;// 需求

	// 对于知识点，key为Vector类型；其它的key为String类型，

	public Hashtable cFact = null;// 实际

	/**
	 * 
	 * 
	 * @roseuid 3DFFDD970138
	 * 
	 * 
	 */

	public PraxisDistribute()

	{

	}

	public PraxisDistribute(int nMarkOrAmount, int nItemID, int nAccording,
			int nImport, Hashtable cDemand)

	{

		this.nMarkOrAmount = nMarkOrAmount;

		this.nItemID = nItemID;

		this.nAccording = nAccording;

		this.nImport = nImport;

		this.cDemand = cDemand;

	}

}