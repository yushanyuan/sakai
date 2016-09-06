package com.bupticet.paperadmin.common;

import java.io.Serializable;

public class KnowledgeTreeTO implements Serializable {

	public int nKnowledgeID;

	public String strKnowledgeName;

	public int nCourseID;

	public int nParentKnowID;

	public int nSequenceNO;

	public int nlevelNO;

	public String strKnowledgeClick;

	public int nSum;

	public KnowledgeTreeTO()

	{

		nKnowledgeID = 0;

		strKnowledgeName = "";

		nCourseID = 0;

		nParentKnowID = 0;

		nSequenceNO = 0;

		nlevelNO = 0;

		strKnowledgeClick = "";

		nSum = 0;

	}

	public KnowledgeTreeTO(int nKnowledgeID, String strKnowledgeName,
			int nCourseID,

			int nParentKnowID, int nSequenceNO, int nlevelNO,
			String strKnowledgeClick, int nSum)

	{

		this.nKnowledgeID = nKnowledgeID;

		this.strKnowledgeName = strKnowledgeName;

		this.nCourseID = nCourseID;

		this.nParentKnowID = nParentKnowID;

		this.nSequenceNO = nSequenceNO;

		this.nlevelNO = nlevelNO;

		this.strKnowledgeClick = strKnowledgeClick;

		this.nSum = nSum;

	}

}
