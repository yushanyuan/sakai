package com.bupticet.paperadmin.model;

import java.io.Serializable;
import java.util.Date;

public class PaperListTO implements Serializable {

	public int nPaperID;

	public String strPaperName;

	public String strCreator;

	public Date cCreateDate = null;

	public int nPaperType = 0;// 试卷创建类型，1自动，2手动，4整张录入

	public int nZipTimes = 0;// 试卷被打包的次数

	public PaperListTO() {

		nPaperID = 0;

		strPaperName = "";

		strCreator = "";

		cCreateDate = null;

		nPaperType = 0;

	}

	public PaperListTO(Date cCreateDate, int nPaperID, String strPaperName,
			String strCreator, int nPaperType, int nZipTimes) {

		this.cCreateDate = cCreateDate;

		this.nPaperID = nPaperID;

		this.strPaperName = strPaperName;

		this.strCreator = strCreator;

		this.nPaperType = nPaperType;

		this.nZipTimes = nZipTimes;

	}

}