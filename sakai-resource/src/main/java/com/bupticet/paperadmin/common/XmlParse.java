package com.bupticet.paperadmin.common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

public class XmlParse {

	public void XmlParse() {

	}

	public void writehead(String filepath) {

		Setting setting = new Setting();

		String dtddir = setting.getdir("dtddir");

		String str = "<?xml version=\"1.0\" encoding=\"ASCII\"?>" +

		"<!DOCTYPE Catalog SYSTEM \"" + dtddir + "\">" +

		"<ROOT BGCOLOR=\"#218BB5\" BGSRC=\"\" CSS=\"../xml/body.css\"></ROOT>";

		// String str="<ddddddddddddddddddd>";

		try {

			File file = new File(filepath);

			PrintWriter writexml = new PrintWriter(new FileWriter(filepath,
					false));

			writexml.println(str);

			writexml.close();

		} catch (IOException ie) {

			System.out.println("Error:" + ie.getMessage());
			throw new RuntimeException(ie);

		}

	}

	public Document parseXml(String filepath) throws Exception {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		// create db

		DocumentBuilder db = null;

		Document d1 = null;

		try {

			db = dbf.newDocumentBuilder();

		} catch (ParserConfigurationException pce) {

			System.err.println(pce);
			throw new RuntimeException(pce);

		}

		try {

			d1 = db.parse(new File(filepath));

		} catch (SAXException se) {

			System.err.println(se.getMessage());
			throw new RuntimeException(se);
		}

		return d1;

	}

	public void writeXml(String filepath, Document d1) {

		try {

			// File f = new File(filepath);

			// DataOutputStream DataOutputStream1 = new DataOutputStream(new
			// FileOutputStream(f));

			// DataOutputStream1.write((d1.toString()).getBytes("GBK"));

			// DataOutputStream1.close();

			OutputFormat format = new OutputFormat();

			format.setEncoding("ASCII");

			FileWriter writer = new FileWriter(filepath, false);

			XMLSerializer serializer = new XMLSerializer(writer, format);

			serializer.serialize(d1);

			writer.flush();

			writer.close();

		} catch (IOException ie) {

			System.out.println("Error:" + ie.getMessage());
			throw new RuntimeException(ie);
		}

	}

	public boolean appendnode(Document document, KnowledgeTreeTO knowledgenode,
			int nLevel) {

		boolean flag = false;

		// get the root Element of the xml file

		Element root = document.getDocumentElement();

		Element temp = document.createElement("CH" + knowledgenode.nlevelNO);

		// creat temp1's five attributes

		Attr attr1 = document.createAttribute("NODENAME");

		Attr attr2 = document.createAttribute("IDVALUE");

		Attr attr3 = document.createAttribute("CLICK");

		Attr attr4 = document.createAttribute("SEQ");

		Attr attr5 = document.createAttribute("SRC");

		Attr attr6 = document.createAttribute("SUM");

		Attr attr7 = document.createAttribute("AMOUNT");

		if (nLevel > 1) {

			Attr attr = document.createAttribute("S" + (nLevel - 1));

			Text text = document.createTextNode("1");

			attr.appendChild(text);

			temp.setAttributeNode(attr);

		}
		// append five attribute's value the value is got from resultset!

		// System.out.println("李建伟kkkkkkk：：："+knowledgenode.strKnowledgeName);

		Text text1 = document.createTextNode(knowledgenode.strKnowledgeName);

		Text text2 = document.createTextNode("" + knowledgenode.nKnowledgeID);

		Text text3 = document.createTextNode(knowledgenode.strKnowledgeClick);

		Text text4 = document.createTextNode("" + knowledgenode.nSequenceNO);

		Text text5 = document.createTextNode("leaf2.gif");

		Text text6 = document.createTextNode("" + knowledgenode.nSum);

		attr1.appendChild(text1);

		attr2.appendChild(text2);

		attr3.appendChild(text3);

		attr4.appendChild(text4);

		attr5.appendChild(text5);

		attr6.appendChild(text6);

		// append the five attributes to the temp

		temp.setAttributeNode(attr1);

		temp.setAttributeNode(attr2);

		temp.setAttributeNode(attr3);

		temp.setAttributeNode(attr4);

		temp.setAttributeNode(attr5);

		temp.setAttributeNode(attr6);

		temp.setAttributeNode(attr7);

		if (knowledgenode.nlevelNO == 0) {

			text5.setNodeValue("booktop1.gif");

			if (root.appendChild(temp).hasAttributes()) {

				flag = true;

			}

		}

		else {

			int tagNO = knowledgenode.nlevelNO - 1;

			NodeList nl = root.getElementsByTagName("CH" + tagNO);

			int length = nl.getLength();

			for (int i = 0; i < length; i++) {

				Element tempelement = (Element) nl.item(i);

				if (tempelement.getAttributes().getNamedItem("IDVALUE")
						.getNodeValue().equals(

						"" + knowledgenode.nParentKnowID)) {

					if (tempelement.appendChild(temp).hasAttributes()) {

						flag = true;

						if (!tempelement.getNodeName().equals("CH0")) {

							tempelement.getAttributes().getNamedItem("SRC")
									.setNodeValue("book0.gif");

							Attr attr8 = document.createAttribute("OSRC");

							Text text8 = document.createTextNode("book1.gif");

							attr8.appendChild(text8);

							tempelement.setAttributeNode(attr8);

						}

					}

				}

			}

		}

		return flag;

	}

	public Element goElement(String strID, Document document) {

		// Element ele=document.createElement("dsfa");

		int i = 0;

		Element tempelement = null;

		while (document.getElementsByTagName("CH" + i).getLength() > 0) {

			NodeList nlist = document.getElementsByTagName("CH" + i);

			for (int ni = 0; ni < nlist.getLength(); ni++) {

				tempelement = (Element) nlist.item(ni);

				if (tempelement.getAttributes().getNamedItem("IDVALUE")
						.getNodeValue().equals(

						strID)) {

					return tempelement;

				}

			}

			i++;

		}

		return tempelement;

	}

}
