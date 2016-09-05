package com.bupticet.paperadmin.tool;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.ezmorph.MorpherRegistry;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;

import org.apache.commons.io.FileUtils;
import org.sakaiproject.resource.util.Constants;

import com.bupticet.morpther.BeanMorpher;
import com.bupticet.paperadmin.common.DAOSysException;
import com.bupticet.paperadmin.common.MaterialTO;
import com.bupticet.paperadmin.common.PaperAdminBDException;
import com.bupticet.paperadmin.common.PraxisAdminBDException;
import com.bupticet.paperadmin.model.AnswerToQuestion;
import com.bupticet.paperadmin.model.FrameTO;
import com.bupticet.paperadmin.model.PraxisTO;
import com.bupticet.praxisadmin.praxistype.model.Reading;
import com.bupticet.praxisadmin.praxistype.model.Selection;
import com.bupticet.praxisadmin.praxistype.modelConvertor.FillingBlankConvertor;
import com.bupticet.praxisadmin.praxistype.modelConvertor.MaterialTOConvertor;
import com.bupticet.praxisadmin.praxistype.modelConvertor.PraxisTOConvertor;
import com.bupticet.praxisadmin.praxistype.modelConvertor.ReadingConvertor;
import com.bupticet.praxisadmin.praxistype.modelConvertor.SelectionConvertor;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

public class PaperToolUtil {
	/**
	 * @param paperAnswerFile
	 * @return 是为T，否为F 判断是否有主观题；
	 */
	public static boolean isExistObject(File paperAnswerFile) throws Exception {
		boolean isexist = false;
		try {
			String answerString = FileUtils.readFileToString(paperAnswerFile, "utf8");
			HashMap answerMap = (HashMap) JSONUtil.deserialize(answerString);
			Iterator key = answerMap.keySet().iterator();
			while (key.hasNext()) {
				HashMap contentMap = (HashMap) answerMap.get(key.next());
				float isobject = new Float(contentMap.get(AnswerToQuestion.ISOBJECT).toString());
				if (isobject == 1) {
					isexist = true;
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} catch (JSONException e) {
			e.printStackTrace();
			throw e;
		}

		return isexist;
	}

	/**
	 * @param praxisList
	 * @return funciton:从小题组装出大体；
	 */
	public static Hashtable converterToPaperFromPraxisTo(List praxisList) {
		net.sf.ezmorph.Morpher one;
		Hashtable htFrames = new Hashtable();
		// System.out.println("praxisList.size=========="+praxisList.size());
		for (int i = 0; i < praxisList.size(); i++) {
			PraxisTO praxisTo = (PraxisTO) praxisList.get(i);
			if (htFrames.containsKey(praxisTo.nPraxisTypeID)) {
				// System.out.println("praxisTo.nPraxisTypeID======="+praxisTo.
				// nPraxisTypeID);
				FrameTO frameTO = (FrameTO) (htFrames.get(praxisTo.nPraxisTypeID));
				frameTO.fScoreSum = frameTO.fScoreSum + praxisTo.fSuggScore;
				frameTO.cQuestions.put(frameTO.cQuestions.size() + 1, praxisTo);
			} else {
				Hashtable htQuesOrder = new Hashtable();
				FrameTO frameTo = new FrameTO();
				frameTo.cQuestions = htQuesOrder;
				frameTo.fScoreSum = praxisTo.fSuggScore;
				frameTo.cQuestions.put(1, praxisTo);
				frameTo.strPraxisTypeName = praxisTo.strPraxisTypeName;
				frameTo.nPraxisTypeID = praxisTo.nPraxisTypeID;
				// System.out.println("frameTo======="+frameTo);
				htFrames.put(frameTo.nPraxisTypeID, frameTo);
			}

		}

		return htFrames;
	}

	/**
	 * @param nPraxisTypeID
	 * @return function :找到所有试题模版；
	 * @throws SQLException
	 * @throws DAOSysException
	 */
	public static Hashtable findAllPraxisTemplate() throws DAOSysException, SQLException {
		Hashtable result = new Hashtable();
		/*Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String GET_SCHEMATO_SQL = "SELECT PT_PT_ID, PT_BELO_TEMPLATE FROM PRAXISTYPE ";
		conn = PaperAdminDAOImp.getDataSource().getConnection();
		ps = conn.prepareStatement(GET_SCHEMATO_SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		rs = ps.executeQuery();
		while (rs.next()) {
			result.put(rs.getInt(1), rs.getInt(2));
		}
		conn.close();*/
		String values = "[{\"PT_PT_ID\":1,\"PT_BELO_TEMPLATE\":1},{\"PT_PT_ID\":2,\"PT_BELO_TEMPLATE\":1},{\"PT_PT_ID\":3,\"PT_BELO_TEMPLATE\":2},{\"PT_PT_ID\":4,\"PT_BELO_TEMPLATE\":3},{\"PT_PT_ID\":5,\"PT_BELO_TEMPLATE\":4},{\"PT_PT_ID\":6,\"PT_BELO_TEMPLATE\":8},{\"PT_PT_ID\":7,\"PT_BELO_TEMPLATE\":4},{\"PT_PT_ID\":8,\"PT_BELO_TEMPLATE\":5},{\"PT_PT_ID\":9,\"PT_BELO_TEMPLATE\":5},{\"PT_PT_ID\":10,\"PT_BELO_TEMPLATE\":5},{\"PT_PT_ID\":11,\"PT_BELO_TEMPLATE\":5},{\"PT_PT_ID\":12,\"PT_BELO_TEMPLATE\":6},{\"PT_PT_ID\":13,\"PT_BELO_TEMPLATE\":6},{\"PT_PT_ID\":14,\"PT_BELO_TEMPLATE\":6},{\"PT_PT_ID\":15,\"PT_BELO_TEMPLATE\":6},{\"PT_PT_ID\":16,\"PT_BELO_TEMPLATE\":6},{\"PT_PT_ID\":17,\"PT_BELO_TEMPLATE\":6},{\"PT_PT_ID\":18,\"PT_BELO_TEMPLATE\":6},{\"PT_PT_ID\":19,\"PT_BELO_TEMPLATE\":6},{\"PT_PT_ID\":20,\"PT_BELO_TEMPLATE\":6},{\"PT_PT_ID\":21,\"PT_BELO_TEMPLATE\":7},{\"PT_PT_ID\":22,\"PT_BELO_TEMPLATE\":6},{\"PT_PT_ID\":23,\"PT_BELO_TEMPLATE\":8}]";
		JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON(values);
		if (jsonArray != null) {
			List list = (List) JSONSerializer.toJava(jsonArray);
			for (Object o : list) {
				JSONObject jsonObject = JSONObject.fromObject(o);
				result.put(jsonObject.get("PT_PT_ID"),jsonObject.get("PT_BELO_TEMPLATE"));
			}
		}
		
		// System.out.println("findAllPraxisTemplate========"+result);
		return result;
	}

	/**
	 * @param cframes
	 * @return 格式转化；
	 */
	public static Hashtable formatChange(Hashtable cframes) {
		Hashtable result = new Hashtable();
		int i = 0;
		Enumeration key = cframes.keys();
		while (key.hasMoreElements()) {
			i++;
			Object one = key.nextElement();
			// System.out.println("one============"+one);
			result.put(i, one);
		}
		return result;
	}

	/**
	 * @param path
	 * @param name
	 * @param praxisToList
	 * @throws SQLException
	 * @throws DAOSysException
	 * @return将所有的试题转化为json格式；
	 * 
	 */
	public static boolean converterPaperToJson(String path, String name, List<PraxisTO> praxisToList)
			throws DAOSysException, SQLException {
		List<PraxisTOConvertor> convertor = new ArrayList<PraxisTOConvertor>();
		Hashtable oneparamter = findAllPraxisTemplate();
		Hashtable twoparamter = new Hashtable();
		try {
			twoparamter = PraxisToolUtil.findAllPRAXISTEMPLATE();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		convertor = PraxisToolUtil.convertToPraxisToConvertor(praxisToList, oneparamter, twoparamter);
		boolean success = true;
		File filePath = new File(path);
		if (!filePath.exists())
			filePath.mkdirs();
		File file = new File(path, name);
		// System.out.println("file==================="+file.getAbsolutePath());
		JsonConfig jsonConfig = new JsonConfig();
		/*
		 * String[] prop=new String[16]; prop[0]="cExpoTime";
		 * prop[1]="cAudiDate"; prop[2]="cCreaDate"; prop[3]="cMaterialData";
		 * prop[4]="cItems"; prop[5]="cMaterialIDs"; prop[6]="content";
		 * prop[7]="strExamDemand"; prop[8]="strAssessor"; prop[9]="strCreator";
		 * prop[10]="nCreatorID"; prop[11]="strTeacDemand";
		 * prop[12]="strCheckAdvice"; prop[13]="PraxisNo";
		 * prop[14]="nUsedTimes"; prop[15]="cPraxisCont";
		 * jsonConfig.setExcludes(prop);
		 */
		JSONArray serial = JSONArray.fromObject(convertor, jsonConfig);
		try {
			FileUtils.writeStringToFile(file, serial.toString(), "UTF8");
		} catch (Exception e) {
			System.out.println("在导出json格式试卷时发生错误" + e);
			return false;
		}
		return success;
	}

	/**
	 * 将JSON 格式的文件反序列化为PraxioTO List；
	 * 
	 * @param path
	 *            文件路径
	 * @param name
	 *            文件名
	 * @return PraxioTO List；
	 */
	public static List converterJsonToPaper(String path, String name) throws Exception {
		List result = new ArrayList();
		List tempList = new ArrayList();
		File file = new File(path, name);
		if (!file.exists()) {
			return null;
		}
		try {
			String data = FileUtils.readFileToString(file, "UTF8");
			JSONArray json = JSONArray.fromObject(data);
			List temp = JSONArray.toList(json, PraxisTOConvertor.class);
			BeanMorpher praxisTo = new BeanMorpher(
					com.bupticet.praxisadmin.praxistype.modelConvertor.PraxisTOConvertor.class, JSONUtils
							.getMorpherRegistry());
			BeanMorpher materaiTo = new BeanMorpher(
					com.bupticet.praxisadmin.praxistype.modelConvertor.MaterialTOConvertor.class, JSONUtils
							.getMorpherRegistry());
			BeanMorpher praxisContent = new BeanMorpher(
					com.bupticet.praxisadmin.praxistype.modelConvertor.PraxisContentConvertor.class, JSONUtils
							.getMorpherRegistry());
			BeanMorpher fillBlank = new BeanMorpher(
					com.bupticet.praxisadmin.praxistype.modelConvertor.FillingBlankConvertor.class, JSONUtils
							.getMorpherRegistry());
			BeanMorpher reading = new BeanMorpher(
					com.bupticet.praxisadmin.praxistype.modelConvertor.ReadingConvertor.class, JSONUtils
							.getMorpherRegistry());
			BeanMorpher selection = new BeanMorpher(
					com.bupticet.praxisadmin.praxistype.modelConvertor.SelectionConvertor.class, JSONUtils
							.getMorpherRegistry());

			MorpherRegistry morpherRegistry = new MorpherRegistry();
			morpherRegistry.registerMorpher(praxisTo);
			morpherRegistry.registerMorpher(praxisContent);
			morpherRegistry.registerMorpher(fillBlank);
			morpherRegistry.registerMorpher(reading);
			morpherRegistry.registerMorpher(selection);
			morpherRegistry.registerMorpher(materaiTo);

			Hashtable onetable = findAllPraxisTemplate();
			Hashtable twotable = PraxisToolUtil.findAllPRAXISTEMPLATE();
			for (int i = 0; i < temp.size(); i++) {
				PraxisTOConvertor one = (PraxisTOConvertor) temp.get(i);
				Integer type = (Integer) onetable.get(one.getNPraxisTypeID());
				Class typeclass = null;
				String classString = (String) twotable.get(type);
				if (classString.equals("com.bupticet.praxisadmin.praxistype.model.Selection")) {
					SelectionConvertor content = (SelectionConvertor) morpherRegistry.morph(
							com.bupticet.praxisadmin.praxistype.modelConvertor.SelectionConvertor.class, one
									.getCPraxisCont());
					one.setCPraxisCont(content);
				} else if (classString.endsWith("com.bupticet.praxisadmin.praxistype.model.Reading")) {
					ReadingConvertor content = (ReadingConvertor) morpherRegistry.morph(
							com.bupticet.praxisadmin.praxistype.modelConvertor.ReadingConvertor.class, one
									.getCPraxisCont());
					one.setCPraxisCont(content);
				} else {
					FillingBlankConvertor content = (FillingBlankConvertor) morpherRegistry.morph(
							com.bupticet.praxisadmin.praxistype.modelConvertor.FillingBlankConvertor.class, one
									.getCPraxisCont());
					one.setCPraxisCont(content);
				}
				List materialList = new ArrayList();
				for (int m = 0; m < one.getList().size(); m++) {
					MaterialTOConvertor ttt = (MaterialTOConvertor) morpherRegistry.morph(MaterialTOConvertor.class,
							one.getList().get(m));
					materialList.add(ttt);
				}
				one.setList(materialList);
				tempList.add(one);
			}

			/*Hashtable oneparamter = findAllPraxisTemplate();
			Hashtable twoparamter = PraxisToolUtil.findAllPRAXISTEMPLATE();*/
			result = PraxisToolUtil.convertorConvertorToPraxisTo(tempList, onetable, twotable);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("error in convertro form json" + e);
			throw e;
		}
		return result;
	}

	/**
	 * @param path
	 *            存放html试卷的路径
	 * @param name
	 *            html试卷的名字;
	 * @param praxisToList
	 *            代表试卷中所有的试题 function: 将试卷对象本身转化为path/name的html对象;
	 * @throws SQLException
	 * @throws DAOSysException
	 */
	public static boolean converterPaperToHTML(String path, String materialPath, String name, List praxisToList)
			throws PraxisAdminBDException, IOException, PaperAdminBDException, DAOSysException, SQLException {
		File filePath = new File(path);
		if (!filePath.exists())
			filePath.mkdirs();
		File file = new File(path, name);
		// System.out.println("file========="+file);
		StringBuffer content = converterToStringBuffer(materialPath, praxisToList);
		FileUtils.writeStringToFile(file, content.toString(), "UTF8");
		return true;
	}

	public static StringBuffer converterToStringBuffer(String materialPath, List praxisToList) throws DAOSysException,
			SQLException {
		Hashtable cFrames = converterToPaperFromPraxisTo(praxisToList);
		StringBuffer content = new StringBuffer();
		//content.append("<table class='PaperHead' border='0' cellpadding='2' cellspacing='3' style='border-collapse: collapse' bordercolor='#111111' width='95%' id='PaperHead'>");
		//content.append("<tr><td width='100%' align='center'>试卷题目</td></tr>");
		//content.append("<tr><td width='100%' align='center'>试卷描述</td></tr>");
		//content.append("<tr><td width='100%' align='center'></td></tr></table>");
		content
				.append("<table border='0' cellpadding='2' cellspacing='3' style='border-collapse: collapse' bordercolor='#111111' width='100%' id='PaperCon'>");
		Hashtable keyIndex = formatChange(cFrames);
		// System.out.println("keyindex========="+keyIndex);
		// System.out.println("cFrames========="+cFrames.size());
		int textareIndex = 1;
		for (int i = 1; i <= cFrames.size(); i++) {
			FrameTO frameTO = (FrameTO) cFrames.get(keyIndex.get(i));
			// System.out.println("frameTO==========="+frameTO.cQuestions.size())
			// ;
			content.append("<tr><td width='100%' align='left' valign='top'><div class='Frame'>");
			content.append(Helper.getAllCondition().get(i) + "、" + frameTO.strPraxisTypeName);
			content.append("（共" + frameTO.cQuestions.size() + "道小题，共" + frameTO.fScoreSum + "分）" + "</div><ol>");
			// System.out.println("frameTO.nPraxisTypeID==========="+frameTO.
			// nPraxisTypeID);
			int nPraxisTemplateID = (Integer) (findAllPraxisTemplate().get(frameTO.nPraxisTypeID));
			// System.out.println("nPraxisTemplateID==========="+nPraxisTemplateID
			// );
			for (int j = 1; j <= frameTO.cQuestions.size(); j++) {
				PraxisTO questionTO = (PraxisTO) frameTO.cQuestions.get(j);
				String strCheck = "";
				String strAnswer = "";
				content.append("<li>");
				// System.out.println("nPraxisTemplateID============="+
				// nPraxisTemplateID);
				switch (nPraxisTemplateID) {
				case 1: {
					// System.out.println("小题id============"+questionTO.nPraxisID
					// );
					// 单项选择题
					content.append("<div class='QuestionMain'>"
							+ processMaterial2(materialPath, questionTO.cPraxisCont.getPraxisMain(), questionTO)
							+ "</div>\r\n");
					content.append("<ol type='A'>\r\n");
					for (int k = 1; k <= ((Selection) (questionTO.cPraxisCont)).findOptionSum(); k++) {
						// System.out.println("小题id============"+questionTO.
						// nPraxisID+"====选项序列======"+k);
						content.append("<li class='QuestionOption'><input type='radio' name='"
								+ questionTO.nPraxisID
								+ "_0' value='"
								+ k
								+ "' num='0' Qid='"
								+ questionTO.nPraxisID
								+ "' class='objText' "
								+ strCheck
								+ ">"
								+ processMaterial2(materialPath, Helper
										.parseString(((Selection) (questionTO.cPraxisCont)).findOption(k)), questionTO)
								+ "</li>\r\n");
						strCheck = "";
					}
					content.append("</ol>\r\n");
					break;
				}
				case 2: {
					// 多项选择题
					// System.out.println("2222222222222");
					content.append("<div class='QuestionMain'>"
							+ processMaterial2(materialPath, questionTO.cPraxisCont.getPraxisMain(), questionTO)
							+ "</div>\r\n");
					content.append("<ol type='A'>\r\n");
					for (int k = 1; k <= ((Selection) (questionTO.cPraxisCont)).findOptionSum(); k++) {
						content.append("<li class='QuestionOption'><input type='checkbox' name='"
								+ questionTO.nPraxisID
								+ "_0,'num='0' Qid='"
								+ questionTO.nPraxisID
								+ "' value='"
								+ k
								+ "' class='objText' "
								+ strCheck
								+ ">"
								+ processMaterial2(materialPath, Helper
										.parseString(((Selection) (questionTO.cPraxisCont)).findOption(k)), questionTO)
								+ "</li>\r\n");
						strCheck = "";
					}
					content.append("</ol>\r\n");
					break;
				}
				case 3: {
					// 判断题
					// System.out.println("33333333333333");
					content.append("<div class='QuestionMain'>"
							+ processMaterial2(materialPath, questionTO.cPraxisCont.getPraxisMain(), questionTO)
							+ "</div>\r\n");
					content.append("<ol type='A'>\r\n");
					for (int k = 1; k <= ((Selection) (questionTO.cPraxisCont)).findOptionSum(); k++) {
						content.append("<li class='QuestionOption'><input type='radio' name='"
								+ questionTO.nPraxisID
								+ "_0' num='0' Qid='"
								+ questionTO.nPraxisID
								+ "' value='"
								+ k
								+ "' class='objText' "
								+ strCheck
								+ ">"
								+ processMaterial2(materialPath, Helper
										.parseString(((Selection) (questionTO.cPraxisCont)).findOption(k)), questionTO)
								+ "</li>\r\n");
						strCheck = "";
					}
					content.append("</ol>\r\n");
					break;
				}
				case 4: {
					// 阅读理解题
					// System.out.println("4444444444444444");
					content.append("<div class='QuestionMain'>"
							+ processMaterial2(materialPath, questionTO.cPraxisCont.getPraxisMain(), questionTO)
							+ "</div>");
					content.append("<ol type='1'>");
					for (int l = 1; l <= ((Reading) (questionTO.cPraxisCont)).getSubPraxisSum(); l++) {
						Selection cSelection = ((Reading) (questionTO.cPraxisCont)).getSubPraxis(l);
						Selection cInitSelection = new Selection();
						content.append("<li><div class='QuestionMain'>"
								+ processMaterial2(materialPath, cSelection.getPraxisMain(), questionTO) + "</div>");
						content.append("<ol type='A'>");
						for (int k = 1; k <= cSelection.findOptionSum(); k++) {
							content.append("<li class='QuestionOption'><input type='radio' name='"
									+ questionTO.nPraxisID
									+ "_"
									+ (l - 1)
									+ "' value='"
									+ k
									+ "'num='"
									+ (l - 1)
									+ "'Qid='"
									+ questionTO.nPraxisID
									+ "' class='objText' "
									+ strCheck
									+ ">"
									+ processMaterial2(materialPath, Helper.parseString(cSelection.findOption(k)),
											questionTO) + "</li>");
							strCheck = "";
						}
						content.append("</ol>");
						content.append("</li>");
					}
					content.append("</ol>");
					break;
				}
				case 5: {
					// 填空题
					// System.out.println("5555555555555555555555");
					content.append("<div class='QuestionMain'>"
							+ processMaterial2(materialPath, questionTO.cPraxisCont.getPraxisMain(), questionTO)
							+ "</div>\r\n");
					content.append("<br><table>");
					for (int k = 0; k < questionTO.cPraxisCont.findItemSum(); k++) {
						content
								.append("<tr><td>空"
										+ (k + 1)
										+ ".</td><td><DIV id='5_"
										+ (k + 1)
										+ "_"
										+ questionTO.nPraxisID
										+ "' class='editArea' ALIGN='left' STYLE='height:100;width:500;'>"
										+ "<input class='subText' name='" + questionTO.nPraxisID + "_" + (k)
										+ "' Qid='" + questionTO.nPraxisID + "' num='" + k
										+ "' STYLE='height:100;width:500;'/></DIV></td></tr>");
						content.append("<input type='hidden' name='inFillpraxis_" + (k + 1) + "_"
								+ questionTO.nPraxisID + "' value=''>");
					}
					content.append("</table>");
					break;
				}
				case 6: {
					// 问答题
					// System.out.println("６６６６６６６６６６６６６６");
					content.append("<div class='QuestionMain'><table><tr><td>"
							+ processMaterial2(materialPath, questionTO.cPraxisCont.getPraxisMain(), questionTO)
							+ "</td></tr></table></div>\r\n");
					content
							.append("<br><DIV id='6_"
									+ questionTO.nPraxisID
									+ "' class='editArea' ALIGN='left'><a href=\"/library/additional-help/curriculum-design/\" target=\"_blank\">如何添加附件、如何贴图以及如何添加Flash动画？请点击此处查看帮助。</a><br />"
									+ "<TEXTAREA  class='subText' name='" + questionTO.nPraxisID + "_0' Qid='"
									+ questionTO.nPraxisID
									+ "' num='0' id=\"textareIndex"+ textareIndex++ +"\"></TEXTAREA></DIV><br>");
					content.append("<input type='hidden' name='essaypraxis_" + questionTO.nPraxisID + "' value=''>");
					break;
				}
				case 7: {
					// 句子判错题
					// System.out.println("777777777777777777");
					content.append("<div class='QuestionMain'>"
							+ processMaterial2(materialPath, questionTO.cPraxisCont.getPraxisMain(), questionTO)
							+ "</div>\r\n");
					content.append("<ol type='A'>\r\n");
					for (int k = 1; k <= ((Selection) (questionTO.cPraxisCont)).findOptionSum(); k++) {
						content.append("<li class='QuestionOption'><input type='radio' name='"
								+ questionTO.nPraxisID
								+ "_0' Qid='"
								+ questionTO.nPraxisID
								+ "' num='0' value='"
								+ k
								+ "' class='objText' "
								+ strCheck
								+ ">"
								+ processMaterial2(materialPath, Helper
										.parseString(((Selection) (questionTO.cPraxisCont)).findOption(k)), questionTO)
								+ "</li>\r\n");
						strCheck = "";
					}
					content.append("</ol>\r\n");
					break;
				}
				case 8: {
					// 完形填空
					// System.out.println("8888888888888");
					content.append("<div class='QuestionMain'>"
							+ processMaterial2(materialPath, questionTO.cPraxisCont.getPraxisMain(), questionTO)
							+ "</div>");
					content.append("<ol type='1'>");
					for (int l = 1; l <= ((Reading) (questionTO.cPraxisCont)).getSubPraxisSum(); l++) {
						Selection cSelection = ((Reading) (questionTO.cPraxisCont)).getSubPraxis(l);
						Selection cInitSelection = new Selection();
						content.append("<li><div class='QuestionMain'></div>");
						content.append("<ol type='A'>");
						for (int k = 1; k <= cSelection.findOptionSum(); k++) {
							/*
							 * if((!strSubmitID.equals("0"))&&(cInitSelection!=null )
							 * &&(((Selection)cInitSelection).getAnswer().indexOf
							 * (k+";")>-1)){ strCheck="checked"; }
							 */
							content.append("<li class='QuestionOption'><input type='radio' name='"
									+ questionTO.nPraxisID
									+ "_"
									+ (l - 1)
									+ "'  Qid='"
									+ questionTO.nPraxisID
									+ "' num='"
									+ (l - 1)
									+ "'value='"
									+ k
									+ "' class='objText' "
									+ strCheck
									+ ">"
									+ processMaterial2(materialPath, Helper.parseString(cSelection.findOption(k)),
											questionTO) + "</li>");
							strCheck = "";
						}
						content.append("</ol>");
						content.append("</li>");
					}
					content.append("</ol>");
					break;
				}
				}
				content.append("</li>");
			}
		}
		content.append("</table>");
		return content;
	}

	public static String processMaterial2(String path, String strProcess, PraxisTO questionTO) {
		String strFileName = "";
		String strTemp = strProcess;
		if (strProcess == null)
			strTemp = "";
		path += "material/";
		// 通过src为图片路径来处理图片
		if(strTemp!=null){
			Pattern pattern=Pattern.compile("(src=\")(\\d+/)(\\w+.\\w+\")"); 
			Matcher m=pattern.matcher(strTemp);
			Map<String,String> replaceStrMap = new HashMap<String,String>();
			while(m.find()){
				if(m.groupCount()==3){
					replaceStrMap.put(m.group(), m.group(1)+path+m.group(3));
				}
			}
			if(!replaceStrMap.isEmpty()){
				for(String key : replaceStrMap.keySet()){
					strTemp = strTemp.replace(key, replaceStrMap.get(key));
				}
			}
		}
		
		// 通过nMaterialID来处理图片路径
		// System.out.println("strFilePath=========="+strFilePath);
		int indexBegin = 0, indexEnd = 0;
		// System.out.println("material strProcess======="+strTemp);
		// System.out.println("strProcess======="+strProcess);		
		StringBuffer strTempBuf = new StringBuffer(strTemp);
		indexBegin = strTemp.indexOf("nMaterialID");
		while (indexBegin > 0) {
			indexEnd = strTemp.indexOf('&', indexBegin);
			// System.out.println("indexEnd======="+(indexEnd<indexBegin+24));
			if (indexEnd < indexBegin + 24) {
				// System.out.println("strFileName==========="+strFileName);
				Integer materialID = Integer.parseInt(strTemp.substring(indexBegin + 12, indexEnd));
				indexBegin = indexBegin - "../servlet/GetMaterialServlet?".length();
				indexEnd = strTemp.indexOf('>', indexEnd);
				char temp = strTemp.charAt(indexBegin - 1);
				String newPath = "";
				if (temp != '\"') {
					newPath = "\"" + path;
				}else{
					newPath = path;
				}
				strFileName = getMaterialFileName(newPath,materialID, questionTO);
				newPath += strFileName+ '"';
				//System.out.println(newPath);
				strTempBuf.replace(indexBegin, indexEnd,newPath);
				strTemp = strTempBuf.toString();
				indexBegin = strTemp.indexOf("nMaterialID", indexEnd);
				// System.out.println("strTemp======="+strTemp);
			}
		}
		// System.out.println("material strTemp======="+strTemp);
		return strTemp;
	}

	public static String getMaterialFileName(String path,int nMaterialID, PraxisTO questionTO) throws DAOSysException {
		/*
		 * System.out.println("praxisTo.id=========="+questionTO.nPraxisID);
		 * System.out.println("nMaterialID.id=========="+nMaterialID);
		 */
		MaterialTO materialTo = getMaterial(nMaterialID, questionTO);
		String strFileName = nMaterialID + "";
		if (materialTo != null){
			strFileName = strFileName + "." + materialTo.strFileSuffix;
		}else{
			// 如果没有后缀则根据名称在文件中查找文件
			String dirPath = path.replace(Constants.getTestMaterialURL(), Constants.getTestMaterialPath());
			File f = new File(dirPath);
			String[] filelist = f.list();
			if(filelist!=null){
				for(String file : filelist){
					if(file.indexOf(strFileName)!=-1){
						strFileName = file;
						//System.out.println(strFileName);
					}
				}
			}
		}
		return strFileName;
	}

	public static MaterialTO getMaterial(int nMaterialID, PraxisTO questionTO) {
		MaterialTO materialTo = null;

		for (int i = 0; i < questionTO.list.size(); i++) {
			MaterialTO one = (MaterialTO) questionTO.list.get(i);
			if (one.nMaterialID == nMaterialID) {
				materialTo = one;
				break;
			}
		}
		return materialTo;
	}
	
	public static void main(String[] args) throws DAOSysException, SQLException {
		Hashtable h = PraxisToolUtil.findAllPRAXISTEMPLATE();	
		System.out.println(h.size());
	}
}
