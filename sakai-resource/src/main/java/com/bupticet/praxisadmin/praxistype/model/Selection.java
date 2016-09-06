package com.bupticet.praxisadmin.praxistype.model;

import java.io.*;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Reader;
import java.io.StringReader;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
//import com.ibm.xml.parsers.*;
import javax.xml.parsers.*;
import org.apache.xerces.parsers.DOMParser;


public class Selection  extends PraxisContent{
   private String strAnswer="";
    public Selection() {
    }
    //将题目内容和答案转换成XML格式
    public void writeToXML(){
      StringBuffer temp=new StringBuffer(300);
      temp.append("<小题 序号=\"\">");
           temp.append("<题干>");
               temp.append("<![CDATA["+super.getPraxisMain()+"]]>");
           temp.append("</题干>");
           temp.append("<诸选项>");
                for(int i=1;i<=findOptionSum();i++){
                  temp.append("<选项 序号=\""+i+"\">");
                  temp.append("<![CDATA["+findOption(i)+"]]>");
                  temp.append("</选项>");
                }
           temp.append("</诸选项>");
      temp.append("</小题>");
      super.setXMLBody(temp.toString());
      if(strAnswer==null)
         strAnswer="";
      super.setXMLAnswer("<答案>"+
                               "<题号></题号>"+ "<内容>"+strAnswer+"</内容>"+
                         "</答案>");
    }
    public void readFromXML(){
        String strXMLBody="<ROOT>"+super.getXMLBody()+"</ROOT>";
        String strXMLAnswer="<ROOT>"+super.getXMLAnswer()+"</ROOT>";
        String praxisMain=null;
        String praxisOption=null;
        StringReader sr=null;
        int nseq;
        InputSource iSrc=null;
        Document doc = null;

        if(strXMLAnswer!=null)
        {
        try{
         //System.out.println("strXMLAnswer::::::::::::::::::::::::::"+strXMLAnswer);
         //System.out.println(":::::::::::::::::::::::::::::::");
        sr = new StringReader(strXMLAnswer);
        iSrc = new InputSource(sr);
        DOMParser parser = new DOMParser();
        //System.out.println(":::::::::::::::::::::::::::::::"+parser);
        parser.parse(iSrc);
        doc = parser.getDocument();
        }
        catch (Exception e)
        {
           System.err.println("Sorry, an error1 occurred: " + e.getMessage());
           throw new RuntimeException(e);
         }
        //System.out.println("doc======================="+doc);
        if(doc!=null)
        {
        NodeList nl=doc.getElementsByTagName("内容");
        // System.out.println("nl======================="+nl);
         if(nl.getLength()>0){
         NodeList textlist=nl.item(0).getChildNodes();
         if(textlist.getLength()>0){
             strAnswer=textlist.item(0).getNodeValue();
          }
         }
        }
        }

        try{
        sr = new StringReader(strXMLBody);
        iSrc = new InputSource(sr);
        //System.out.println("isrc======================"+iSrc);
        DOMParser parser = new DOMParser();
        //System.out.println("parser======================"+parser);
        parser.parse(iSrc);
        doc = parser.getDocument();
        }
        catch (Exception e)
        {
           System.err.println("Sorry, an error2 occurred: " + e.getMessage());
           throw new RuntimeException(e);
        }
        NodeList mainlist=doc.getElementsByTagName("题干");
        if(mainlist.getLength()>0){
        NodeList maintextlist=mainlist.item(0).getChildNodes();
        if(maintextlist.getLength()>0){
        praxisMain=maintextlist.item(0).getNodeValue();
        super.setPraxisMain(praxisMain);
         }
        }
        NodeList optionlist=doc.getElementsByTagName("选项");
        for(int i=0;i<optionlist.getLength();i++){
        nseq=(Integer.parseInt(optionlist.item(i).getAttributes().getNamedItem("序号").getNodeValue()));
        NodeList optiontextlist=optionlist.item(i).getChildNodes();
        if(optiontextlist.getLength()>0){
        praxisOption=optiontextlist.item(0).getNodeValue();
        addOption(nseq,praxisOption);
          }
         }
    }
    public void readAnswerXML(){}
    public void addAnswer(String strAnswer){
      this.strAnswer=strAnswer;
    }
    public String findAnswer(){
      return strAnswer;
    }
    public int findOptionSum()
    {
     return super.findItemSum();
    }

    public void addOption(int index, String strAnswer)
    {
      super.addItem(index,strAnswer);
    }

    public String findOption(int index)
    {
     return (String)super.findItem(index);
    }
}