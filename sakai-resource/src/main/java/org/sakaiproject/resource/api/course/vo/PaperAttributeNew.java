package org.sakaiproject.resource.api.course.vo;

import java.io.Serializable;

public class PaperAttributeNew implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int nItemID = 0;

	private int nAccording = 0;// 满意度

	private int nImport = 0;// 重要度

	private int nDemand = 0; // 需求

	private int nFact = 0; // 实际

	public PaperAttributeNew() {

	}

	public PaperAttributeNew(int nItemID, int nAccording, int nImport, int nDemand) {
		this.nItemID = nItemID;

		this.nAccording = nAccording;

		this.nImport = nImport;

		this.nDemand = nDemand;
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

	public int getnDemand() {
		return nDemand;
	}

	public void setnDemand(int nDemand) {
		this.nDemand = nDemand;
	}

	public int getnFact() {
		return nFact;
	}

	public void setnFact(int nFact) {
		this.nFact = nFact;
	}

}