package com.bupticet.paperadmin.model;

import java.io.Serializable;
import java.util.Hashtable;

public class FrameTO implements Serializable {

	public Hashtable cQuestions = null;// 小题

	public int nFrameID = 0;// 大题编号

	public int nPaperID = 0;// 所属试卷编号

	public int nOrder = 0;// 大题顺序号

	public int nPraxisTypeID = 0;// 题目类型ID

	public String strPraxisTypeName = "";// 题目类型名称

	public String strComment = "";// 大题备注

	public int nQuesSum = 0;// 小题总数

	public float fScoreSum = 0;// 该大题总分

	public FrameTO() {
	};

	public FrameTO(Hashtable cQuestions, int nFrameID, int nPaperID, int nOrder, int nPraxisTypeID,
			String strPraxisTypeName, String strComment, int nQuesSum, float fScoreSum) {

		this.cQuestions = cQuestions;

		this.nFrameID = nFrameID;

		this.nPaperID = nPaperID;

		this.nOrder = nOrder;

		this.nPraxisTypeID = nPraxisTypeID;

		this.strPraxisTypeName = strPraxisTypeName;

		this.strComment = strComment;

		this.nQuesSum = nQuesSum;

		this.fScoreSum = fScoreSum;

	}

}