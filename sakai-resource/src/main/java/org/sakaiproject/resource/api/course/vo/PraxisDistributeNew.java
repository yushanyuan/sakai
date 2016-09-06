package org.sakaiproject.resource.api.course.vo;

import java.io.Serializable;
import java.util.Hashtable;

public class PraxisDistributeNew implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int nItemID = 0;

	private int nAccording = 0;// 满意度

	private int nImport = 0;// 重要度

	private int nMarkOrAmount = 0;// 按题目数1，还是分值0

	private Hashtable cDemand = null;// 需求

	// 对于知识点，key为Vector类型；其它的key为String类型，

	private Hashtable cFact = null;// 实际

	public PraxisDistributeNew()
	{

	}

	public PraxisDistributeNew(int nMarkOrAmount, int nItemID, int nAccording, int nImport, Hashtable cDemand)
	{
		this.nMarkOrAmount = nMarkOrAmount;

		this.nItemID = nItemID;

		this.nAccording = nAccording;

		this.nImport = nImport;

		this.cDemand = cDemand;
	}

	public int getnItemID() {
		return nItemID;
	}

	public void setnItemID(int nItemID) {
		this.nItemID = nItemID;
	}

	public int getnAccording() {
		return nAccording;
	}

	public void setnAccording(int nAccording) {
		this.nAccording = nAccording;
	}

	public int getnImport() {
		return nImport;
	}

	public void setnImport(int nImport) {
		this.nImport = nImport;
	}

	public int getnMarkOrAmount() {
		return nMarkOrAmount;
	}

	public void setnMarkOrAmount(int nMarkOrAmount) {
		this.nMarkOrAmount = nMarkOrAmount;
	}

	public Hashtable getcDemand() {
		return cDemand;
	}

	public void setcDemand(Hashtable cDemand) {
		this.cDemand = cDemand;
	}

	public Hashtable getcFact() {
		return cFact;
	}

	public void setcFact(Hashtable cFact) {
		this.cFact = cFact;
	}

}