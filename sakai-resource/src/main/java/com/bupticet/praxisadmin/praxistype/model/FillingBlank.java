package com.bupticet.praxisadmin.praxistype.model;

import java.io.StringReader;
import java.util.Iterator;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * 填空，暂时不考虑自动判卷的问题 各空答案是否按序等判卷注意问题 在评分标准里注明
 */
public class FillingBlank extends PraxisContent {

	public FillingBlank() {
	}

	// 将题目内容和答案转换成XML格式
	public void writeToXML() {
		StringBuffer temp = new StringBuffer(200);
		String st = null;
		temp.append("<小题 序号=\"\">");
		temp.append("<题干>");
		temp.append("<![CDATA[" + super.getPraxisMain() + "]]>");
		temp.append("</题干>");
		temp.append(" <空数 长度=\"" + getAnswerMaxLen() + "\">");
		temp.append(getBlankSum());
		temp.append("</空数>");
		temp.append("</小题>");
		super.setXMLBody(temp.toString());

		temp.delete(0, temp.length());
		for (int i = 1; i <= getBlankSum(); i++) {
			temp.append("<答案>");
			temp.append("<题号>" + i + "</题号>");
			temp.append("<内容>" + "<![CDATA[" + findAnswer(i) + "]]>" + "</内容>");
			temp.append("</答案>");
		}
		super.setXMLAnswer(temp.toString());
	}

	public void readFromXML() {
		String strEncodXMLAnswer = "";
		try {
			int len = super.getXMLAnswer().length();
			char[] ioBuf = new char[len];
			ioBuf = super.getXMLAnswer().toCharArray();//

			for (int i = 0; i < len; i++) {
				if (ioBuf[i] == 0x1e || ioBuf[i] == 0xb || ioBuf[i] == 0xc || ioBuf[i] == 0xf) {
					// 这里过虑 0x1e 替换为空格
					ioBuf[i] = ' ';
				}
			}
			strEncodXMLAnswer = new String(ioBuf);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		String strXMLBody = "<ROOT>" + super.getXMLBody() + "</ROOT>";
		String strXMLAnswer = "<ROOT>" + strEncodXMLAnswer + "</ROOT>";
		String strAnswer = null;
		String praxisMain = null;
		StringReader sr = null;
		InputSource iSrc = null;
		Document doc = null;

		try {
			sr = new StringReader(strXMLAnswer);
			iSrc = new InputSource(sr);
			DOMParser parser = new DOMParser();
			parser.parse(iSrc);
			doc = parser.getDocument();
		} catch (Exception e) {
			System.err.println("Sorry, an error occurred: " + e);
			throw new RuntimeException(e);
		}
		NodeList nl = doc.getElementsByTagName("内容");
		for (int i = 0; i < nl.getLength(); i++) {
			Element tempelement = (Element) nl.item(i).getParentNode();
			int npraxis = Integer.parseInt(tempelement.getElementsByTagName("题号").item(0).getChildNodes().item(0)
					.getNodeValue());
			NodeList textlist = nl.item(i).getChildNodes();
			if (textlist.getLength() > 0) {
				strAnswer = textlist.item(0).getNodeValue();
				addAnswer(npraxis, strAnswer);
			}
		}
		try {
			sr = new StringReader(strXMLBody);
			iSrc = new InputSource(sr);
			DOMParser parser = new DOMParser();
			parser.parse(iSrc);
			doc = parser.getDocument();
		} catch (Exception e) {
			System.err.println("Sorry, an error occurred: " + e);
			throw new RuntimeException(e);
		}
		NodeList mainlist = doc.getElementsByTagName("题干");
		if (mainlist.getLength() > 0) {
			NodeList textlist = mainlist.item(0).getChildNodes();
			if (textlist.getLength() > 0) {
				praxisMain = textlist.item(0).getNodeValue();
				super.setPraxisMain(praxisMain);
			}
		}
	}

	public void readAnswerXML() {
	}

	/**
	 * @return int 返回空的总数
	 */
	public int getBlankSum() {
		return super.findItemSum();
	}

	public void addAnswer(int index, String strAnswer) {
		super.addItem(index, strAnswer);
	}

	public String findAnswer(int index) {
		return (String) super.findItem(index);
	}

	/**
	 * @return int计算最长答案的的长度
	 * @roseuid 3DF5A45D007D
	 */
	public int getAnswerMaxLen() {
		int nLen = 0, nTemp = 0;
		String str = "";
		Iterator it = super.cItems.values().iterator();
		while (it.hasNext()) {
			str = (String) it.next();
			if (str != null) {
				nTemp = str.length();
				if (nTemp > nLen)
					nLen = nTemp;
			}
		}
		return nLen;
	}
}
