package com.bupticet.praxisadmin.praxistype.model;

import java.io.Serializable;

import java.lang.Object;

import java.util.*;

import com.bupticet.paperadmin.model.PraxisTO;
import com.bupticet.praxisadmin.praxistype.modelConvertor.FillingBlankConvertor;
import com.bupticet.praxisadmin.praxistype.modelConvertor.MaterialTOConvertor;
import com.bupticet.praxisadmin.praxistype.modelConvertor.PraxisContentConvertor;
import com.bupticet.praxisadmin.praxistype.modelConvertor.PraxisTOConvertor;
import com.bupticet.praxisadmin.praxistype.modelConvertor.ReadingConvertor;
import com.bupticet.praxisadmin.praxistype.modelConvertor.SelectionConvertor;

/**
 * 
 * 
 * <p>
 * Title:
 * </p>
 * 
 * 
 * <p>
 * Description:
 * </p>
 * 
 * 
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * 
 * 
 * <p>
 * Company:
 * </p>
 * 
 * 
 * @author unascribed
 * 
 * 
 * @version 1.0
 * 
 * 
 */

public abstract class PraxisContent implements Serializable {

	protected String strXMLBody = "";

	protected String strXMLAnswer = "";

	protected String strPraxisMain = ""; // 题干

	protected Hashtable cItems = new Hashtable();

	private int nSnapshotNum = 25;

	// 将题目内容和答案转换成XML格式

	public abstract void writeToXML();

	public abstract void readFromXML();

	public abstract void readAnswerXML();

	public String getSnapshot() {

		// 把题干的前nSnapshotNum*2个字符（nSnapshotNum个汉字）作为快照

		if (this.strPraxisMain == null)

			return "";

		else {

			StringBuffer sb = new StringBuffer(strPraxisMain);

			// 去html标签

			int nIndexLeft = sb.toString().indexOf('<');

			while (nIndexLeft > -1) {

				sb = sb.delete(nIndexLeft, sb.toString().indexOf('>',
						nIndexLeft) + 1);

				nIndexLeft = sb.toString().indexOf('<', nIndexLeft);

			}

			// 去空格

			int nIndexSpace = sb.toString().indexOf("&nbsp;");

			while ((nIndexSpace > -1) && (nIndexSpace < (nSnapshotNum * 4))) {

				sb = sb.delete(nIndexSpace, nIndexSpace + 6);

				nIndexSpace = sb.toString().indexOf("&nbsp;", nIndexSpace);

			}

			// 去换行符

			int nIndexNewLine = sb.toString().indexOf('\n');

			while ((nIndexNewLine > -1) && (nIndexNewLine < (nSnapshotNum * 3))) {

				sb = sb.delete(nIndexNewLine, nIndexNewLine + 1);

				nIndexNewLine = sb.toString().indexOf('\n', nIndexNewLine);

			}

			// 去回车符

			int nIndexReturn = sb.toString().indexOf('\r');

			while ((nIndexReturn > -1)
					&& (nIndexReturn < (nSnapshotNum * 2 + 1))) {

				sb = sb.delete(nIndexReturn, nIndexReturn + 1);

				nIndexReturn = sb.toString().indexOf('\r', nIndexReturn);

			}

			// 如果是字母则长度加0.5

			int nLen = ((nSnapshotNum * 2 + 1) < sb.length() ? (nSnapshotNum * 2 + 1)
					: sb.length());

			float nEndIndex = 0;
			char c = 'a';

			if (nLen > nSnapshotNum) {

				for (int i = 0; ((i <= nEndIndex + nSnapshotNum) && (nEndIndex
						+ nSnapshotNum < nLen)); i++) {

					c = sb.charAt(i);

					if (((c >= 'a') && (c <= 'z')) ||

					((c >= 'A') && (c <= 'Z'))) {

						nEndIndex = nEndIndex + (float) 0.5;

					}

				}

			}

			nEndIndex = nEndIndex + nSnapshotNum < nLen ? nEndIndex
					+ nSnapshotNum : nLen;

			return sb.substring(0, (int) nEndIndex);

		}

	}

	public int findItemSum() {

		return cItems.size();

	}

	public void addItem(int index, Object cItem) {

		cItems.put(new Integer(index), cItem);

	}

	public Object findItem(int index) {

		return cItems.get(new Integer(index));

	}

	public void setPraxisMain(String strPraxisMain) {

		this.strPraxisMain = strPraxisMain;

	}

	public String getPraxisMain() {

		return strPraxisMain;

	}

	public void setXMLBody(String strXMLBody) {

		this.strXMLBody = strXMLBody;

	}

	public String getXMLBody() {

		return strXMLBody;

	}

	public void setXMLAnswer(String strXMLAnswer) {

		this.strXMLAnswer = strXMLAnswer;

	}

	public String getXMLAnswer() {

		return strXMLAnswer;

	}

	public Hashtable getCItems() {
		return cItems;
	}

	public void setCItems(Hashtable items) {
		cItems = items;
	}

	public int getNSnapshotNum() {
		return nSnapshotNum;
	}

	public void setNSnapshotNum(int snapshotNum) {
		nSnapshotNum = snapshotNum;
	}
	public PraxisContentConvertor converToPraxisContent(PraxisTO one,Hashtable oneparam,Hashtable twoparam)
	{
		PraxisContentConvertor result=null;
		try{
		Integer type=(Integer)oneparam.get(one.getNPraxisTypeID());		
		String typeClass=(String)twoparam.get(type);	
		if(typeClass.toString().equals("com.bupticet.praxisadmin.praxistype.model.Selection"))
		{
			 result=new SelectionConvertor();
			 result.setNSnapshotNum(this.nSnapshotNum);
			 result.setStrPraxisMain(this.strPraxisMain);
			 result.setStrXMLAnswer(this.strXMLAnswer);
			 result.setStrXMLBody(this.strXMLBody);
		}
		else if(typeClass.toString().equals("com.bupticet.praxisadmin.praxistype.model.FillingBlank"))
		{
			result=new FillingBlankConvertor();
			result.setNSnapshotNum(this.nSnapshotNum);
			 result.setStrPraxisMain(this.strPraxisMain);
			 result.setStrXMLAnswer(this.strXMLAnswer);
			 result.setStrXMLBody(this.strXMLBody);
		}
		else
		{
			result=new ReadingConvertor();
			result.setNSnapshotNum(this.nSnapshotNum);
			 result.setStrPraxisMain(this.strPraxisMain);
			 result.setStrXMLAnswer(this.strXMLAnswer);
			 result.setStrXMLBody(this.strXMLBody);
		}
		}catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		return result;
		
	}
}