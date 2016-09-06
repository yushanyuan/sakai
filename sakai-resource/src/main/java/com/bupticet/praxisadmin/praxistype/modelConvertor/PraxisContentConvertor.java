package com.bupticet.praxisadmin.praxistype.modelConvertor;

import java.util.Hashtable;

import com.bupticet.paperadmin.tool.PaperToolUtil;
import com.bupticet.paperadmin.tool.PraxisToolUtil;
import com.bupticet.praxisadmin.praxistype.model.FillingBlank;
import com.bupticet.praxisadmin.praxistype.model.PraxisContent;
import com.bupticet.praxisadmin.praxistype.model.Reading;
import com.bupticet.praxisadmin.praxistype.model.Selection;

public class PraxisContentConvertor {
	protected String strXMLBody = "";

	protected String strXMLAnswer = "";

	protected String strPraxisMain = ""; // 题干
	
	private int nSnapshotNum = 25;

	public int getNSnapshotNum() {
		return nSnapshotNum;
	}

	public void setNSnapshotNum(int snapshotNum) {
		nSnapshotNum = snapshotNum;
	}

	public String getStrPraxisMain() {
		return strPraxisMain;
	}

	public void setStrPraxisMain(String strPraxisMain) {
		this.strPraxisMain = strPraxisMain;
	}

	public String getStrXMLAnswer() {
		return strXMLAnswer;
	}

	public void setStrXMLAnswer(String strXMLAnswer) {
		this.strXMLAnswer = strXMLAnswer;
	}

	public String getStrXMLBody() {
		return strXMLBody;
	}

	public void setStrXMLBody(String strXMLBody) {
		this.strXMLBody = strXMLBody;
	}
	public PraxisContent converToPraxisContent(PraxisTOConvertor one,Hashtable oneparam,Hashtable twoparam)
	{
		PraxisContent result=null;
		try{
		Integer type=(Integer)oneparam.get(one.getNPraxisTypeID());		
		String typeClass=(String)twoparam.get(type);	
		if(typeClass.toString().equals("com.bupticet.praxisadmin.praxistype.model.Selection"))
		{
			 result=new Selection();
			 result.setNSnapshotNum(this.nSnapshotNum);
			 result.setPraxisMain(this.strPraxisMain);
			 result.setXMLAnswer(this.strXMLAnswer);
			 result.setXMLBody(this.strXMLBody);
			 result.readFromXML();
		}
		else if(typeClass.toString().equals("com.bupticet.praxisadmin.praxistype.model.FillingBlank"))
		{
			result=new FillingBlank();
			result.setNSnapshotNum(this.nSnapshotNum);
			 result.setPraxisMain(this.strPraxisMain);
			 result.setXMLAnswer(this.strXMLAnswer);
			 result.setXMLBody(this.strXMLBody);
			 result.readFromXML();
		}
		else
		{
			result=new Reading();
			result.setNSnapshotNum(this.nSnapshotNum);
			 result.setPraxisMain(this.strPraxisMain);
			 result.setXMLAnswer(this.strXMLAnswer);
			 result.setXMLBody(this.strXMLBody);
			 result.readFromXML();
		}
		}catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		return result;
		
	}
}
