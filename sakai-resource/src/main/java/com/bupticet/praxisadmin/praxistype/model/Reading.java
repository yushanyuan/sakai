package com.bupticet.praxisadmin.praxistype.model;

import java.io.StringReader;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


/**


 * <p>Title: </p>


 * <p>Description: </p>


 * <p>Copyright: Copyright (c) 2002</p>


 * <p>Company: </p>


 * @author unascribed


 * @version 1.0


 */

/**
 * 
 * 
 * 阅读理解，包括完形填空
 * 
 * 
 */

public class Reading extends PraxisContent

{

	/**
	 * 
	 * 
	 * @roseuid 3DF7E4E80232
	 * 
	 * 
	 */

	public Reading()

	{

	}

	// 将题目内容和答案转换成XML格式

	public void writeToXML() {

		StringBuffer temp = new StringBuffer(300);

		Selection sel = null;

		String str = null;

		temp.append("<小题 序号=\"\">");

		temp.append("<题干>");

		temp.append("<![CDATA[" + super.getPraxisMain() + "]]>");

		temp.append("</题干>");

		for (int i = 1; i <= getSubPraxisSum(); i++) {

			temp.append("<子题 序号=\"" + i + "\">");

			sel = getSubPraxis(i);
			sel.writeToXML();
			str = sel.getXMLBody();

			if (str != null)

				temp.append(str.substring(str.indexOf('>') + 1, str
						.lastIndexOf('<')));

			temp.append("</子题>");

		}

		temp.append("</小题>");

		super.setXMLBody(temp.toString());

		temp.delete(0, temp.length());

		for (int i = 1; i <= getSubPraxisSum(); i++) {

			temp.append("<答案>");

			temp.append("<题号>" + i + "</题号>");

			temp.append("<内容>" + "<![CDATA[" + getSubPraxis(i).findAnswer()
					+ "]]>" + "</内容>");

			temp.append("</答案>");

		}

		super.setXMLAnswer(temp.toString());

	}

	public void readFromXML() {

		String strXMLBody = "<ROOT>" + super.getXMLBody() + "</ROOT>";

		String strXMLAnswer = "<ROOT>" + super.getXMLAnswer() + "</ROOT>";

		String strAnswer = null;

		String praxisMain = null;

		String str = null;

		StringReader sr = null;

		InputSource iSrc = null;

		Document doc = null;

		try {

			sr = new StringReader(strXMLBody);

			iSrc = new InputSource(sr);

			DOMParser parser = new DOMParser();

			parser.parse(iSrc);

			doc = parser.getDocument();

		}

		catch (Exception e)

		{

			System.err.println("Sorry, an error occurred: " + e);
			throw new RuntimeException(e);

		}

		NodeList praxislist = doc.getElementsByTagName("小题");

		if (praxislist.getLength() > 0) {

			Element praxiselement = (Element) praxislist.item(0);

			NodeList mainlist = praxiselement.getElementsByTagName("题干");

			if (mainlist.getLength() > 0) {

				NodeList maintextlist = mainlist.item(0).getChildNodes();

				if (maintextlist.getLength() > 0) {

					praxisMain = maintextlist.item(0).getNodeValue();

					super.setPraxisMain(praxisMain);

				}

			}

		}

		NodeList subPraxislist = doc.getElementsByTagName("子题");

		for (int i = 0; i < subPraxislist.getLength(); i++) {

			int nseq = Integer.parseInt(subPraxislist.item(i).getAttributes()
					.getNamedItem("序号").getNodeValue());

			Element tempele = (Element) subPraxislist.item(i);

			NodeList subPraxistextlist = tempele.getChildNodes();

			if (subPraxistextlist.getLength() > 0) {

				Selection subsel = new Selection();

				NodeList mainlist = tempele.getElementsByTagName("题干");

				if (mainlist.getLength() > 0) {

					NodeList maintextlist = mainlist.item(0).getChildNodes();

					if (maintextlist.getLength() > 0) {

						praxisMain = maintextlist.item(0).getNodeValue();

						subsel.setPraxisMain(praxisMain);

						// System.out.println("Praxismain"+subsel.getPraxisMain());

					}

				}

				NodeList optionlist = tempele.getElementsByTagName("选项");

				for (int oi = 0; oi < optionlist.getLength(); oi++) {

					int noseq = (Integer.parseInt(optionlist.item(oi)
							.getAttributes().getNamedItem("序号").getNodeValue()));

					NodeList optiontextlist = optionlist.item(oi)
							.getChildNodes();

					if (optiontextlist.getLength() > 0) {

						String praxisOption = optiontextlist.item(0)
								.getNodeValue();

						subsel.addOption(noseq, praxisOption);

					}

				}

				setSubPraxis(nseq, subsel);

				// System.out.println("Praxismainwwwwww"+getSubPraxis(nseq).getOption(1));

			}

		}

		try {

			sr = new StringReader(strXMLAnswer);

			iSrc = new InputSource(sr);

			DOMParser parser = new DOMParser();

			parser.parse(iSrc);

			doc = parser.getDocument();

		}

		catch (Exception e)

		{

			System.err.println("Sorry, an error occurred: " + e);
			throw new RuntimeException(e);

		}

		NodeList nl = doc.getElementsByTagName("内容");

		for (int i = 0; i < nl.getLength(); i++) {

			Element tempelement = (Element) nl.item(i).getParentNode();

			int nanswer = Integer.parseInt(tempelement.getElementsByTagName(
					"题号").item(0).getChildNodes().item(0).getNodeValue());

			NodeList textlist = nl.item(i).getChildNodes();

			if (textlist.getLength() > 0) {

				strAnswer = textlist.item(0).getNodeValue();

				// Selection selection=new Selection();

				// selection.setAnswer(strAnswer);

				// setSubPraxis(nanswer,selection);

				getSubPraxis(nanswer).addAnswer(strAnswer);

				getSubPraxis(nanswer).writeToXML();

			}

		}

	}

	public void readAnswerXML() {

		String strXMLAnswer = "<ROOT>" + super.getXMLAnswer() + "</ROOT>";

		String strAnswer = null;

		String praxisMain = null;

		String str = null;

		StringReader sr = null;

		InputSource iSrc = null;

		Document doc = null;

		try {

			sr = new StringReader(strXMLAnswer);

			iSrc = new InputSource(sr);

			DOMParser parser = new DOMParser();

			parser.parse(iSrc);

			doc = parser.getDocument();

		}

		catch (Exception e)

		{

			System.err.println("Sorry, an error occurred: " + e);
			throw new RuntimeException(e);

		}

		NodeList nl = doc.getElementsByTagName("内容");

		for (int i = 0; i < nl.getLength(); i++) {

			Element tempelement = (Element) nl.item(i).getParentNode();

			int nanswer = Integer.parseInt(tempelement.getElementsByTagName(
					"题号").item(0).getChildNodes().item(0).getNodeValue());

			NodeList textlist = nl.item(i).getChildNodes();

			if (textlist.getLength() > 0) {

				strAnswer = textlist.item(0).getNodeValue();

				Selection selection = new Selection();

				selection.addAnswer(strAnswer);

				setSubPraxis(nanswer, selection);

				getSubPraxis(nanswer).addAnswer(strAnswer);

				getSubPraxis(nanswer).writeToXML();

			}

		}

	}

	/**
	 * 
	 * 
	 * @param index
	 * 
	 * 
	 * @param cSubPraxise
	 * 
	 * 
	 * @roseuid 3DF5A53F008C
	 * 
	 * 
	 */

	public void setSubPraxis(int index, Selection cSubPraxise)

	{

		super.addItem(index, cSubPraxise);

	}

	/**
	 * 
	 * 
	 * @param index
	 * 
	 * 
	 * @return Selection
	 * 
	 * 
	 * @roseuid 3DF5A6980213
	 * 
	 * 
	 */

	public Selection getSubPraxis(int index)

	{

		return (Selection) super.findItem(index);

	}

	/**
	 * 
	 * 
	 * @return int 返回子题的总数
	 * 
	 * 
	 * @roseuid 3DF5A6B0000F
	 * 
	 * 
	 */

	public int getSubPraxisSum()

	{

		return super.findItemSum();

	}

}