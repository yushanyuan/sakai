package com.bupticet.paperadmin.model;

import java.io.Serializable;

public class FrameXmlTO implements Serializable {

	public int nFrameID = 0;// 大题编号

	// public int nPaperID=0;//所属试卷编号

	public int nOrder = 0;// 大题顺序号

	// public String strPraxisTypeName="";//题目类型名称

	public String strComment = "";// 大题备注

	public String strDealflag = "";// 模板处理标识

	public FrameXmlTO() {

	}

	public FrameXmlTO(int nFrameID, int nOrder, String strComment,
			String strDealflag) {

		this.nFrameID = nFrameID;

		// this.nPaperID=nPaperID;

		this.nOrder = nOrder;

		this.strComment = strComment;

		this.strDealflag = strDealflag;

	}

}
