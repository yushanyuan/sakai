package com.bupticet.paperadmin.tool;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;

import com.bupticet.paperadmin.common.DAOSysException;
import com.bupticet.paperadmin.model.AnswerToQuestion;
import com.bupticet.paperadmin.model.FrameTO;
import com.bupticet.paperadmin.model.PraxisTO;
import com.bupticet.praxisadmin.praxistype.model.FillingBlank;
import com.bupticet.praxisadmin.praxistype.model.Reading;
import com.bupticet.praxisadmin.praxistype.model.Selection;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

public class PaperCheckToolUtil {
	/**
	 * @param answerFile
	 *            :试卷标准答案；
	 * @param paperId
	 *            ：试卷id
	 * @param studentAnswerFile：学生答案；
	 * @param result：批改客观题后的返回结果；
	 * @return
	 */
	public static boolean checkPaperAction(File answerFile, File studentAnswerFile) throws Exception {
		HashMap result = new HashMap();
		boolean isok = true;
		Hashtable table = new Hashtable();
		try {
			table = PaperToolUtil.findAllPraxisTemplate();
		} catch (DAOSysException e1) {
			e1.printStackTrace();
			throw e1;
		} catch (SQLException e1) {
			e1.printStackTrace();
			throw e1;
		}
		if (!answerFile.exists() || !studentAnswerFile.exists())
			isok = false;
		else {
			try {
				String answerString = "";
				String studentAnswerString = "";
				answerString = FileUtils.readFileToString(answerFile, "UTF8");
				studentAnswerString = FileUtils.readFileToString(studentAnswerFile, "UTF8");
				HashMap standardAnswerMap = (HashMap) JSONUtil.deserialize(answerString);
				HashMap studentAnswerMap = (HashMap) JSONUtil.deserialize(studentAnswerString);
				Set keySet = studentAnswerMap.keySet();
				Iterator iterator = keySet.iterator();
				while (iterator.hasNext()) {
					HashMap eachQuestionRecordMap = new HashMap();
					String key = iterator.next().toString();
					String questionStringId = key;
					Object studentOneAnswer = studentAnswerMap.get(questionStringId);
					if (studentOneAnswer != null) {
						HashMap standardAnswerRecord = (HashMap) standardAnswerMap.get(questionStringId);
						HashMap studentAnswer = (HashMap) studentOneAnswer;
						int npraxisTypeId = Integer.parseInt(standardAnswerRecord.get(AnswerToQuestion.TYPEID)
								.toString());
						int nPraxisTemplateID = Integer.parseInt(table.get(npraxisTypeId).toString());
						List studentList = new ArrayList();
						List standardList = new ArrayList();
						List scoreList = new ArrayList();
						List isrightList = new ArrayList();
						List studentScoreList = new ArrayList();
						switch (nPraxisTemplateID) {
						case 1:
						case 2:
						case 3:
						case 4:
						case 7:
						case 8: {
							studentList = (List) studentAnswer.get(AnswerToQuestion.STUDENTANSWER);
							standardList = (List) standardAnswerRecord.get(AnswerToQuestion.ANSWER);
		
							float score = new Float(standardAnswerRecord.get(AnswerToQuestion.SCORE).toString());
							float rightNum = 0;// 答对的题目数;
							if (studentList != null) {
								for (int i = 0; i < standardList.size(); i++) {
//									System.out.println(!standardList.get(i).toString().equalsIgnoreCase(
//											studentList.get(i).toString())
//											&& !(standardList.get(i).toString() + ";").equalsIgnoreCase(studentList
//													.get(i).toString()));
//									System.out
//											.println(studentList.size() == 0
//													|| studentList.get(i) == null
//													|| studentList.get(i).equals("")
//													|| (!standardList.get(i).toString().equalsIgnoreCase(
//															studentList.get(i).toString()) && !(standardList.get(i)
//															.toString() + ";").equalsIgnoreCase(studentList.get(i)
//															.toString())));
									String fixStandardAnswer = standardList.get(i).toString().replaceAll("；", ";").replaceAll("　", "").replaceAll(" ", "");
									if (studentList.size() == 0
											|| studentList.get(i) == null
											|| studentList.get(i).equals("")
											|| (!fixStandardAnswer.equalsIgnoreCase(
													studentList.get(i).toString()) && !( fixStandardAnswer+ ";")
													.equalsIgnoreCase(studentList.get(i).toString()))) {
										isrightList.add(0);
										studentScoreList.add(0f);
										scoreList.add(score / standardList.size());
									} else if (fixStandardAnswer.equalsIgnoreCase(
											studentList.get(i).toString())
											|| (fixStandardAnswer+ ";").equalsIgnoreCase(studentList.get(
													i).toString())) {
										isrightList.add(1);
										studentScoreList.add(score / standardList.size());
										scoreList.add(score / standardList.size());
									}
								}
								eachQuestionRecordMap.put(AnswerToQuestion.ID, questionStringId);
								eachQuestionRecordMap.put(AnswerToQuestion.ANSWER, standardList);
								eachQuestionRecordMap.put(AnswerToQuestion.SCORE, scoreList);
								eachQuestionRecordMap.put(AnswerToQuestion.STUDENTSCORE, studentScoreList);
								eachQuestionRecordMap.put(AnswerToQuestion.STUDENTANSWER, studentList);
								eachQuestionRecordMap.put(AnswerToQuestion.TYPEID, npraxisTypeId);
								eachQuestionRecordMap.put(AnswerToQuestion.ISOBJECT, standardAnswerRecord
										.get(AnswerToQuestion.ISOBJECT));
								eachQuestionRecordMap.put(AnswerToQuestion.KNOWID, standardAnswerRecord
										.get(AnswerToQuestion.KNOWID));
								eachQuestionRecordMap.put(AnswerToQuestion.KNOWNAME, standardAnswerRecord
										.get(AnswerToQuestion.KNOWNAME));
								eachQuestionRecordMap.put(AnswerToQuestion.ISRIGHT, isrightList);
								result.put(questionStringId, eachQuestionRecordMap);
								break;
							}
						}
						case 5:
						case 6: {
							studentList = (List) studentAnswer.get(AnswerToQuestion.STUDENTANSWER);
							standardList = (List) standardAnswerRecord.get(AnswerToQuestion.ANSWER);
							float score = new Float(standardAnswerRecord.get(AnswerToQuestion.SCORE).toString());
							scoreList.add(score);
							eachQuestionRecordMap.put(AnswerToQuestion.ID, questionStringId);
							eachQuestionRecordMap.put(AnswerToQuestion.ANSWER, standardList);
							eachQuestionRecordMap.put(AnswerToQuestion.SCORE, scoreList);
							eachQuestionRecordMap.put(AnswerToQuestion.STUDENTANSWER, studentList);
							eachQuestionRecordMap.put(AnswerToQuestion.TYPEID, npraxisTypeId);
							eachQuestionRecordMap.put(AnswerToQuestion.ISOBJECT, standardAnswerRecord
									.get(AnswerToQuestion.ISOBJECT));
							eachQuestionRecordMap.put(AnswerToQuestion.KNOWID, standardAnswerRecord
									.get(AnswerToQuestion.KNOWID));
							eachQuestionRecordMap.put(AnswerToQuestion.KNOWNAME, standardAnswerRecord
									.get(AnswerToQuestion.KNOWNAME));
							result.put(questionStringId, eachQuestionRecordMap);
							break;
						}

						}
					}

				}
				JSONObject serial = JSONObject.fromObject(result);
				FileUtils.writeStringToFile(studentAnswerFile, serial.toString(), "UTF8");
			} catch (IOException e) {
				e.printStackTrace();
				throw e;
			} catch (JSONException e) {
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}

		}
		return isok;
	}

	/**
	 * @param paperPath
	 * @param paperName
	 * @param studnetAnswerPath
	 * @param studentAnswerName
	 * @return
	 * @throws DAOSysException
	 * @throws SQLException
	 *             将主观题输入，以便教师批改；
	 */
	public static String exportObjectPaperAction(String paperPath, String paperName, String studnetAnswerPath,
			String studentAnswerName) throws DAOSysException, SQLException

	{
		List praxisTo =null;
		try {
			praxisTo = PaperToolUtil.converterJsonToPaper(paperPath, paperName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		HashMap answermap = AnswerToolUtil.converterJsonToAnswer(studnetAnswerPath, studentAnswerName);
		Hashtable frametable = PaperToolUtil.converterToPaperFromPraxisTo(praxisTo);
		Hashtable keytable = PaperToolUtil.formatChange(frametable);
		StringBuffer content = new StringBuffer();
		content
				.append("<table border='0' cellpadding='2' cellspacing='3' style='border-collapse: collapse' bordercolor='#111111' width='100%' id='PaperCon'>");
		for (int i = 1; i <= frametable.size(); i++) {
			FrameTO frameTO = (FrameTO) frametable.get(keytable.get(i));
			int nPraxisTemplateID = (Integer) (PaperToolUtil.findAllPraxisTemplate().get(frameTO.nPraxisTypeID));
			if (nPraxisTemplateID == 5 || nPraxisTemplateID == 6) {
				content.append("<tr>");
				content.append("<td width='100%' align='left' valign='top'><div class='Frame'>");
				content.append(Helper.getAllCondition().get(i) + "、" + frameTO.strPraxisTypeName);
				content.append("（共" + frameTO.cQuestions.size() + "道小题，共" + frameTO.fScoreSum + "分） </div>");
				content.append("<ol>");
				for (int j = 1; j <= frameTO.cQuestions.size(); j++) {
					PraxisTO questionTO = (PraxisTO) frameTO.cQuestions.get(new Integer(j));
					String strStudentAnswer = "";
					String strStandardAnswer = "";
					HashMap studentRecord = (HashMap) answermap.get(questionTO.nPraxisID + "");
					content.append(" <li>");
					switch (nPraxisTemplateID) {
					case 5:
						// 填空题
						content.append("<div class='QuestionMain'>" + questionTO.cPraxisCont.getPraxisMain()
								+ "</div>\r\n");
						strStandardAnswer += "<ol type='1'>" + "\r\n";
						for (int k = 1; k <= ((FillingBlank) questionTO.cPraxisCont).getBlankSum(); k++) {
							strStandardAnswer += "<li class='QuestionOption'>"
									+ ((FillingBlank) questionTO.cPraxisCont).findAnswer(k) + "</li>" + "\r\n";
						}
						strStandardAnswer += "</ol>" + "\r\n";
						strStudentAnswer += "<ol type='1'>" + "\r\n";
						List answer = (List) studentRecord.get(AnswerToQuestion.ANSWER);
						for (int k = 0; k < answer.size(); k++) {
							strStudentAnswer += "<li class='QuestionOption'>" + answer.get(k) + "</li>" + "\r\n";
						}
						strStudentAnswer += "</ol>" + "\r\n";
						content.append("<br><table border='1'>");
						content.append("<tr><td width='15%'>学生答案:</td><td width='85%'>" + strStudentAnswer
								+ "</td></tr>");
						content.append("<tr><td>标准答案:</td><td>" + strStandardAnswer + "</td></tr>");
						content.append("<tr><td>试题分值:</td><td>" + questionTO.fSuggScore + "</td></tr>");
						content
								.append("<tr><td>评分标准:</td><td>"
										+ (questionTO.strGradApproach == null ? "" : questionTO.strGradApproach)
										+ "</td></tr>");
						content.append("<tr><td>得分:</td><td><input name='"
								+ questionTO.nPraxisID
								+ "_0' Qid='"
								+ questionTO.nPraxisID
								+ "' num='0' value='"
								+ (studentRecord.get(AnswerToQuestion.SCORE) == null ? 0 : studentRecord
										.get(AnswerToQuestion.SCORE)) + "' class='inputText' size='10'></td></tr>");
						content.append("</table>");
						break;
					case 6:
						// 问答题
						content.append("<div class='QuestionMain'><table><tr><td>"
								+ questionTO.cPraxisCont.getPraxisMain() + "</td></tr></table></div>\r\n");
						for (int k = 1; k <= ((FillingBlank) questionTO.cPraxisCont).getBlankSum(); k++) {
							strStandardAnswer += "<li class='QuestionOption'>"
									+ ((FillingBlank) questionTO.cPraxisCont).findAnswer(k) + "</li>" + "\r\n";
						}
						strStandardAnswer += "</ol>" + "\r\n";
						strStudentAnswer += "<ol type='1'>" + "\r\n";
						List answertwo = (List) studentRecord.get(AnswerToQuestion.ANSWER);
						for (int k = 0; k < answertwo.size(); k++) {
							strStudentAnswer += "<li class='QuestionOption'>" + answertwo.get(k) + "</li>" + "\r\n";
						}
						strStudentAnswer += "</ol>" + "\r\n";
						content.append("<br><table border='1'>");
						content.append("<tr><td width='15%'>学生答案:</td><td width='85%'>" + strStudentAnswer
								+ "</td></tr>");
						content.append("<tr><td>标准答案:</td><td><font color='#FF0000'>" + strStandardAnswer
								+ "</font></td></tr>");
						content.append("<tr><td>试题分值:</td><td>" + questionTO.fSuggScore + "</td></tr>");
						content
								.append("<tr><td>评分标准:</td><td>"
										+ (questionTO.strGradApproach == null ? "" : questionTO.strGradApproach)
										+ "</td></tr>");
						content.append("<tr><td>得分:</td><td><input  type='text' name='"
								+ questionTO.nPraxisID
								+ "_0' Qid='"
								+ questionTO.nPraxisID
								+ "' num='0' value='"
								+ (studentRecord.get(AnswerToQuestion.SCORE) == null ? 0 : studentRecord
										.get(AnswerToQuestion.SCORE)) + "' class='inputText' size='10'></td></tr>");
						content.append("</table>");
						break;
					}
					content.append("</li>");
				}
				content.append("</ol></td></tr>");
			}
		}
		return content.toString();
	}

	public static String exportExamPaperInfoInJiKao(String materialPath, String paperPath, String paperName,
			File answerFile, String studentAnswerString) {
		HashMap result = new HashMap();
		boolean isok = true;
		Hashtable table = new Hashtable();
		try {
			table = PaperToolUtil.findAllPraxisTemplate();
		} catch (DAOSysException e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1);
		} catch (SQLException e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1);
		}
		// System.out.println("answerFile========="+answerFile.exists());
		if (!answerFile.exists())
			isok = false;
		else {
			// System.out.println("biging............");
			try {
				String answerString = "";
				// String studentAnswerString="";
				answerString = FileUtils.readFileToString(answerFile, "UTF8");
				HashMap standardAnswerMap = (HashMap) JSONUtil.deserialize(answerString);
				HashMap studentAnswerMap = (HashMap) JSONUtil.deserialize(studentAnswerString);
				// System.out.println("studentAnswerMap======"+studentAnswerMap);
				Set keySet = studentAnswerMap.keySet();
				Iterator iterator = keySet.iterator();
				while (iterator.hasNext()) {
					HashMap eachQuestionRecordMap = new HashMap();
					String key = iterator.next().toString();
					String questionStringId = key;
					Object studentOneAnswer = studentAnswerMap.get(questionStringId);
					// System.out.println("studentOneAnswer====="+studentOneAnswer);
					if (studentOneAnswer != null) {
						HashMap standardAnswerRecord = (HashMap) standardAnswerMap.get(questionStringId);
						// System.out.println("standardAnswerRecord==="+standardAnswerRecord);
						HashMap studentAnswer = (HashMap) studentOneAnswer;
						int npraxisTypeId = Integer.parseInt(standardAnswerRecord.get(AnswerToQuestion.TYPEID)
								.toString());
						// System.out.println("npraxisTypeId==="+npraxisTypeId);
						int nPraxisTemplateID = Integer.parseInt(table.get(npraxisTypeId).toString());
						// System.out.println("nPraxisTemplateID==="+nPraxisTemplateID);
						List studentList = new ArrayList();
						List standardList = new ArrayList();
						List scoreList = new ArrayList();
						List isrightList = new ArrayList();
						List studentScoreList = new ArrayList();
						// System.out.println("nPraxisTemplateID======"+nPraxisTemplateID);
						switch (nPraxisTemplateID) {
						case 1:
						case 2:
						case 3:
						case 4:
						case 7:
						case 8: {
							studentList = (List) studentAnswer.get(AnswerToQuestion.STUDENTANSWER);
							standardList = (List) standardAnswerRecord.get(AnswerToQuestion.ANSWER);
							float score = new Float(standardAnswerRecord.get(AnswerToQuestion.SCORE).toString());
							float rightNum = 0;// 答对的题目数;
							// System.out.println("studentList=1---------8===="+studentList);
							if (studentList != null) {
								for (int i = 0; i < standardList.size(); i++) {
									if (studentList.size() == 0
											|| studentList.get(i) == null
											|| studentList.get(i).equals("")
											|| !standardList.get(i).toString().equalsIgnoreCase(
													studentList.get(i).toString())) {
										isrightList.add(0);
										studentScoreList.add(0f);
										scoreList.add(score / standardList.size());
									} else if (standardList.get(i).toString().equalsIgnoreCase(
											studentList.get(i).toString())) {
										isrightList.add(1);
										studentScoreList.add(score / standardList.size());
										scoreList.add(score / standardList.size());
									}
								}
								eachQuestionRecordMap.put(AnswerToQuestion.ID, questionStringId);
								eachQuestionRecordMap.put(AnswerToQuestion.ANSWER, standardList);
								eachQuestionRecordMap.put(AnswerToQuestion.SCORE, scoreList);
								eachQuestionRecordMap.put(AnswerToQuestion.STUDENTSCORE, studentScoreList);
								// System.out.println("studentList=============="+studentList);
								eachQuestionRecordMap.put(AnswerToQuestion.STUDENTANSWER, studentList);
								eachQuestionRecordMap.put(AnswerToQuestion.TYPEID, npraxisTypeId);
								eachQuestionRecordMap.put(AnswerToQuestion.ISOBJECT, standardAnswerRecord
										.get(AnswerToQuestion.ISOBJECT));
								eachQuestionRecordMap.put(AnswerToQuestion.KNOWID, standardAnswerRecord
										.get(AnswerToQuestion.KNOWID));
								eachQuestionRecordMap.put(AnswerToQuestion.KNOWNAME, standardAnswerRecord
										.get(AnswerToQuestion.KNOWNAME));
								eachQuestionRecordMap.put(AnswerToQuestion.ISRIGHT, isrightList);
								result.put(questionStringId, eachQuestionRecordMap);
								break;
							}
						}
						case 5:
						case 6: {// System.out.println("studentAnswer
							// 5566==========="+studentAnswer);
							studentList = (List) studentAnswer.get(AnswerToQuestion.STUDENTANSWER);

							// System.out.println("studentList
							// 56656===="+studentList);
							if (studentList != null) {
								standardList = (List) standardAnswerRecord.get(AnswerToQuestion.ANSWER);
								float score = new Float(standardAnswerRecord.get(AnswerToQuestion.SCORE).toString());
								scoreList.add(score);
								eachQuestionRecordMap.put(AnswerToQuestion.ID, questionStringId);
								eachQuestionRecordMap.put(AnswerToQuestion.ANSWER, standardList);
								eachQuestionRecordMap.put(AnswerToQuestion.SCORE, scoreList);
								eachQuestionRecordMap.put(AnswerToQuestion.STUDENTANSWER, studentList);
								eachQuestionRecordMap.put(AnswerToQuestion.TYPEID, npraxisTypeId);
								eachQuestionRecordMap.put(AnswerToQuestion.ISOBJECT, standardAnswerRecord
										.get(AnswerToQuestion.ISOBJECT));
								eachQuestionRecordMap.put(AnswerToQuestion.KNOWID, standardAnswerRecord
										.get(AnswerToQuestion.KNOWID));
								eachQuestionRecordMap.put(AnswerToQuestion.KNOWNAME, standardAnswerRecord
										.get(AnswerToQuestion.KNOWNAME));
								result.put(questionStringId, eachQuestionRecordMap);
								break;
							}
						}
						}
					}

				}

			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} catch (JSONException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		// System.out.println("end check;;;;;;;;;;;");
		List praxisTo=null;
		try {
			praxisTo = PaperToolUtil.converterJsonToPaper(paperPath, paperName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		HashMap answermap = result;
		Hashtable frametable = PaperToolUtil.converterToPaperFromPraxisTo(praxisTo);
		Hashtable keytable = PaperToolUtil.formatChange(frametable);
		StringBuffer content = new StringBuffer();
		content
				.append("<table border='0' cellpadding='2' cellspacing='3' style='border-collapse: collapse' bordercolor='#111111' width='100%' id='PaperCon'>");
		for (int i = 1; i <= frametable.size(); i++) {
			FrameTO frameTO = (FrameTO) frametable.get(keytable.get(i));
			int nPraxisTemplateID = (Integer) (table.get(frameTO.nPraxisTypeID));
			// System.out.println("nPraxisTemplateID====="+nPraxisTemplateID);
			content.append("<tr>");
			content.append("<td width='100%' align='left' valign='top'><div class='Frame'>");
			content.append(Helper.getAllCondition().get(i) + "、" + frameTO.strPraxisTypeName);
			content.append("（共" + frameTO.cQuestions.size() + "道小题，共" + frameTO.fScoreSum + "分） </div>");
			content.append("<ol>");
			for (int j = 1; j <= frameTO.cQuestions.size(); j++) {
				PraxisTO questionTO = (PraxisTO) frameTO.cQuestions.get(new Integer(j));
				String strStudentAnswer = "";
				String strStandardAnswer = "";
				HashMap studentRecord = (HashMap) answermap.get(questionTO.nPraxisID + "");
				content.append(" <li>");
				switch (nPraxisTemplateID) {
				case 1:
					// 单项选择题
					if (studentRecord == null || studentRecord.get(AnswerToQuestion.ISRIGHT) == null
							|| ((List) studentRecord.get(AnswerToQuestion.ISRIGHT)).get(0).toString().equals("0")) {
						content.append("<div class='QuestionMain'><font color='#FF0000'><strong>(错误)</strong></font>"
								+ PaperToolUtil.processMaterial2(materialPath, questionTO.cPraxisCont.getPraxisMain(),
										questionTO) + "</div>\r\n");
					} else {
						content.append("<div class='QuestionMain'>"
								+ PaperToolUtil.processMaterial2(materialPath, questionTO.cPraxisCont.getPraxisMain(),
										questionTO) + "</div>\r\n");
					}
					content.append("<ol type='A'>\r\n");
					for (int k = 1; k <= ((Selection) (questionTO.cPraxisCont)).findOptionSum(); k++) {
						content.append("<li class='QuestionOption'>"
								+ PaperToolUtil.processMaterial2(materialPath, Helper
										.parseString(((Selection) (questionTO.cPraxisCont)).findOption(k)), questionTO)
								+ "</li>\r\n");
					}
					content.append("<br><table border='0'>");
					content.append("<tr><td width='15%'>知识点:</td><td width='85%'>"
							+ studentRecord.get(AnswerToQuestion.KNOWNAME) + "</td></tr>");
					content.append("<tr><td width='15%'>学生答案:</td><td width='85%'>"
							+ (studentRecord == null ? " " : studentRecord.get(AnswerToQuestion.STUDENTANSWER))
							+ "</td></tr>");
					content.append("<tr><td>标准答案:</td><td>" + ((Selection) (questionTO.cPraxisCont)).findAnswer()
							+ "</td></tr>");
					content.append("<tr><td>试题分值:</td><td>" + questionTO.fSuggScore + "</td></tr>");
					content.append("<tr><td>得分:</td><td>"
							+ (studentRecord == null ? 0 : studentRecord.get(AnswerToQuestion.STUDENTSCORE))
							+ "</td></tr>");
					content.append("</table>");
					content.append("</ol>\r\n");
					break;
				case 2:
					// 多项选择题
					if (studentRecord == null || studentRecord.get(AnswerToQuestion.ISRIGHT) == null
							|| ((List) studentRecord.get(AnswerToQuestion.ISRIGHT)).get(0).toString().equals("0")) {
						content.append("<div class='QuestionMain'><font color='#FF0000'><strong>(错误)</strong></font>"
								+ PaperToolUtil.processMaterial2(materialPath, questionTO.cPraxisCont.getPraxisMain(),
										questionTO) + "</div>\r\n");
					} else {
						content.append("<div class='QuestionMain'>"
								+ PaperToolUtil.processMaterial2(materialPath, questionTO.cPraxisCont.getPraxisMain(),
										questionTO) + "</div>\r\n");
					}
					content.append("<ol type='A'>\r\n");
					for (int k = 1; k <= ((Selection) (questionTO.cPraxisCont)).findOptionSum(); k++) {
						content.append("<li class='QuestionOption'>"
								+ PaperToolUtil.processMaterial2(materialPath, Helper
										.parseString(((Selection) (questionTO.cPraxisCont)).findOption(k)), questionTO)
								+ "</li>\r\n");
					}
					content.append("<br><table border='0'>");
					content.append("<tr><td width='15%'>知识点:</td><td width='85%'>"
							+ studentRecord.get(AnswerToQuestion.KNOWNAME) + "</td></tr>");
					content.append("<tr><td width='15%'>学生答案:</td><td width='85%'>"
							+ (studentRecord == null ? " " : studentRecord.get(AnswerToQuestion.STUDENTANSWER))
							+ "</td></tr>");
					content.append("<tr><td>标准答案:</td><td>" + ((Selection) (questionTO.cPraxisCont)).findAnswer()
							+ "</td></tr>");
					content.append("<tr><td>试题分值:</td><td>" + questionTO.fSuggScore + "</td></tr>");
					content.append("<tr><td>得分:</td><td>"
							+ (studentRecord == null ? 0 : studentRecord.get(AnswerToQuestion.STUDENTSCORE))
							+ "</td></tr>");
					content.append("</table>");
					content.append("</ol>\r\n");
					break;
				case 3:
					// 判断题
					if (studentRecord == null || studentRecord.get(AnswerToQuestion.ISRIGHT) == null
							|| ((List) studentRecord.get(AnswerToQuestion.ISRIGHT)).get(0).toString().equals("0")) {
						content.append("<div class='QuestionMain'><font color='#FF0000'><strong>(错误)</strong></font>"
								+ PaperToolUtil.processMaterial2(materialPath, questionTO.cPraxisCont.getPraxisMain(),
										questionTO) + "</div>\r\n");
					} else {
						content.append("<div class='QuestionMain'>"
								+ PaperToolUtil.processMaterial2(materialPath, questionTO.cPraxisCont.getPraxisMain(),
										questionTO) + "</div>\r\n");
					}
					content.append("<ol type='A'>\r\n");
					for (int k = 1; k <= ((Selection) (questionTO.cPraxisCont)).findOptionSum(); k++) {
						content.append("<li class='QuestionOption'>"
								+ PaperToolUtil.processMaterial2(materialPath, Helper
										.parseString(((Selection) (questionTO.cPraxisCont)).findOption(k)), questionTO)
								+ "</li>\r\n");
					}
					content.append("<br><table border='0'>");
					content.append("<tr><td width='15%'>知识点:</td><td width='85%'>"
							+ studentRecord.get(AnswerToQuestion.KNOWNAME) + "</td></tr>");
					content.append("<tr><td width='15%'>学生答案:</td><td width='85%'>"
							+ (studentRecord == null ? " " : studentRecord.get(AnswerToQuestion.STUDENTANSWER))
							+ "</td></tr>");
					content.append("<tr><td>标准答案:</td><td>" + ((Selection) (questionTO.cPraxisCont)).findAnswer()
							+ "</td></tr>");
					content.append("<tr><td>试题分值:</td><td>" + questionTO.fSuggScore + "</td></tr>");
					content.append("<tr><td>得分:</td><td>"
							+ (studentRecord == null ? 0 : studentRecord.get(AnswerToQuestion.STUDENTSCORE))
							+ "</td></tr>");
					content.append("</table>");
					content.append("</ol>\r\n");
					break;
				case 4:
					// 阅读理解题
					if (studentRecord == null
							|| studentRecord.get(AnswerToQuestion.STUDENTSCORE) == null
							|| questionTO.fSuggScore != findTotalScore((List) studentRecord
									.get(AnswerToQuestion.STUDENTSCORE))) {
						content.append("<div class='QuestionMain'><font color='#FF0000'><strong>(错误)</strong></font>"
								+ PaperToolUtil.processMaterial2(materialPath, questionTO.cPraxisCont.getPraxisMain(),
										questionTO) + "</div>\r\n");
					} else {
						content.append("<div class='QuestionMain'>"
								+ PaperToolUtil.processMaterial2(materialPath, questionTO.cPraxisCont.getPraxisMain(),
										questionTO) + "</div>\r\n");
					}
					content.append("<ol type='I'>");
					int subPraxisSum = ((Reading) (questionTO.cPraxisCont)).getSubPraxisSum();
					String[] answer = new String[subPraxisSum];
					for (int m = 0; m < subPraxisSum; m++)
						answer[m] = "";
					if (studentRecord != null) {
						List answerList = (List) studentRecord.get(AnswerToQuestion.STUDENTANSWER);
						for (int m = 0; m < subPraxisSum; m++) {
							if (m + 1 <= answerList.size()) {
								answer[m] = answerList.get(m).toString();
							}
						}
					}
					for (int l = 1; l <= subPraxisSum; l++) {
						Selection cSelection = ((Reading) (questionTO.cPraxisCont)).getSubPraxis(l);
						Selection cInitSelection = new Selection();
						if ((((Reading) questionTO.cPraxisCont).getSubPraxis(l) != null)) {
							cInitSelection = ((Reading) questionTO.cPraxisCont).getSubPraxis(l);
						}
						content.append("<li><div class='QuestionMain'>"
								+ PaperToolUtil.processMaterial2(materialPath, cSelection.getPraxisMain(), questionTO)
								+ "</div>");
						content.append("<ol type='i'>");
						for (int k = 1; k <= cSelection.findOptionSum(); k++) {
							content.append("<li class='QuestionOption'>"
									+ PaperToolUtil.processMaterial2(materialPath, Helper.parseString(cSelection
											.findOption(k)), questionTO) + "</li>");
						}

						content.append("</ol>");
						content.append("</li>");
						content.append("<br><table border='0'>");
						content.append("<tr><td width='15%'>学生答案:</td><td width='85%'>" + answer[l - 1] + "</td></tr>");
						content.append("<tr><td>标准答案:</td><td>" + ((Selection) cSelection).findAnswer() + "</td></tr>");
						// content.append("<tr><td>试题分值:</td><td>"+questionTO.fScore/subPraxisSum+"</td></tr>");
						// content.append("<tr><td>得分:</td><td>"+((ExerciseRecordTO)hm.get(new
						// Integer(questionTO.nQuestionID))).fScore/subPraxisSum+"</td></tr>");
						content.append("</table>");
					}
					content.append("</ol>");
					content.append("<br>");
					content.append("<div>试题分值：" + questionTO.fSuggScore + "</div>");
					content
							.append("<div>得分："
									+ (studentRecord == null ? 0 : studentRecord.get(AnswerToQuestion.STUDENTSCORE))
									+ "</div>");
					content.append("<br>");
					break;
				case 5:
					// 填空题
					content.append("<div class='QuestionMain'>"
							+ PaperToolUtil.processMaterial2(materialPath, questionTO.cPraxisCont.getPraxisMain(),
									questionTO) + "</div>\r\n");
					strStandardAnswer += "<ol type='1'>" + "\r\n";
					for (int k = 1; k <= ((FillingBlank) questionTO.cPraxisCont).getBlankSum(); k++) {
						strStandardAnswer += "<li class='QuestionOption'>"
								+ ((FillingBlank) questionTO.cPraxisCont).findAnswer(k) + "</li>" + "\r\n";
					}
					strStandardAnswer += "</ol>" + "\r\n";
					strStudentAnswer += "<ol type='1'>" + "\r\n";
					List answer11 = (List) studentRecord.get(AnswerToQuestion.STUDENTANSWER);
					for (int k = 0; k < answer11.size(); k++) {
						strStudentAnswer += "<li class='QuestionOption'>" + answer11.get(k) + "</li>" + "\r\n";
					}
					strStudentAnswer += "</ol>" + "\r\n";
					content.append("<br><table border='1'>");
					content.append("<tr><td width='15%'>知识点:</td><td width='85%'>"
							+ studentRecord.get(AnswerToQuestion.KNOWNAME) + "</td></tr>");
					content.append("<tr><td width='15%'>学生答案:</td><td width='85%'>" + strStudentAnswer + "</td></tr>");
					content.append("<tr><td>标准答案:</td><td>" + strStandardAnswer + "</td></tr>");
					content.append("<tr><td>试题分值:</td><td>" + questionTO.fSuggScore + "</td></tr>");
					content.append("<tr><td>评分标准:</td><td>"
							+ (questionTO.strGradApproach == null ? "" : questionTO.strGradApproach) + "</td></tr>");
					content.append("<tr><td>得分:</td><td>"
							+ (studentRecord.get(AnswerToQuestion.STUDENTSCORE) == null ? 0 : studentRecord
									.get(AnswerToQuestion.STUDENTSCORE)) + "</td></tr>");
					content.append("</table>");
					break;
				case 6:
					// 问答题
					content.append("<div class='QuestionMain'><table><tr><td>"
							+ PaperToolUtil.processMaterial2(materialPath, questionTO.cPraxisCont.getPraxisMain(),
									questionTO) + "</td></tr></table></div>\r\n");
					for (int k = 1; k <= ((FillingBlank) questionTO.cPraxisCont).getBlankSum(); k++) {
						strStandardAnswer += "<li class='QuestionOption'>"
								+ ((FillingBlank) questionTO.cPraxisCont).findAnswer(k) + "</li>" + "\r\n";
					}
					strStandardAnswer += "</ol>" + "\r\n";
					strStudentAnswer += "<ol type='1'>" + "\r\n";
					List answertwo = (List) studentRecord.get(AnswerToQuestion.STUDENTANSWER);
					for (int k = 0; k < answertwo.size(); k++) {
						strStudentAnswer += "<li class='QuestionOption'>" + answertwo.get(k) + "</li>" + "\r\n";
					}
					strStudentAnswer += "</ol>" + "\r\n";
					content.append("<br><table border='1'>");
					content.append("<tr><td width='15%'>知识点:</td><td width='85%'>"
							+ studentRecord.get(AnswerToQuestion.KNOWNAME) + "</td></tr>");
					content.append("<tr><td width='15%'>学生答案:</td><td width='85%'>" + strStudentAnswer + "</td></tr>");
					content.append("<tr><td>标准答案:</td><td><font color='#FF0000'>" + strStandardAnswer
							+ "</font></td></tr>");
					content.append("<tr><td>试题分值:</td><td>" + questionTO.fSuggScore + "</td></tr>");
					content.append("<tr><td>评分标准:</td><td>"
							+ (questionTO.strGradApproach == null ? "" : questionTO.strGradApproach) + "</td></tr>");
					content.append("<tr><td>得分:</td><td>"
							+ (studentRecord.get(AnswerToQuestion.STUDENTSCORE) == null ? 0 : studentRecord
									.get(AnswerToQuestion.STUDENTSCORE)) + "</td></tr>");
					content.append("</table>");
					break;
				case 7:
					// 句子判错题
					if (studentRecord == null
							|| questionTO.fSuggScore != findTotalScore((List) studentRecord
									.get(AnswerToQuestion.STUDENTSCORE))) {
						content.append("<div class='QuestionMain'><font color='#FF0000'><strong>(错误)</strong></font>"
								+ PaperToolUtil.processMaterial2(materialPath, questionTO.cPraxisCont.getPraxisMain(),
										questionTO) + "</div>\r\n");
					} else {
						content.append("<div class='QuestionMain'>"
								+ PaperToolUtil.processMaterial2(materialPath, questionTO.cPraxisCont.getPraxisMain(),
										questionTO) + "</div>\r\n");
					}
					content.append("<ol type='A'>\r\n");
					for (int k = 1; k <= ((Selection) (questionTO.cPraxisCont)).findOptionSum(); k++) {
						content.append("<li class='QuestionOption'>"
								+ PaperToolUtil.processMaterial2(materialPath, Helper
										.parseString(((Selection) (questionTO.cPraxisCont)).findOption(k)), questionTO)
								+ "</li>\r\n");
					}
					content.append("</ol>\r\n");
					content.append("<br><table border='0'>");
					content.append("<tr><td width='15%'>知识点:</td><td width='85%'>"
							+ studentRecord.get(AnswerToQuestion.KNOWNAME) + "</td></tr>");
					content.append("<tr><td width='15%'>学生答案:</td><td width='85%'>"
							+ (studentRecord == null ? " " : studentRecord.get(AnswerToQuestion.STUDENTANSWER))
							+ "</td></tr>");
					content.append("<tr><td>标准答案:</td><td>" + ((Selection) (questionTO.cPraxisCont)).findAnswer()
							+ "</td></tr>");
					content.append("<tr><td>试题分值:</td><td>" + questionTO.fSuggScore + "</td></tr>");
					content.append("<tr><td>得分:</td><td>"
							+ (studentRecord.get(AnswerToQuestion.STUDENTSCORE) == null ? 0 : studentRecord
									.get(AnswerToQuestion.STUDENTSCORE)) + "</td></tr>");
					content.append("</table>");
					content.append("</ol>\r\n");
					break;
				case 8:
					// 完形填空
					if (studentRecord == null
							|| questionTO.fSuggScore != findTotalScore((List) studentRecord
									.get(AnswerToQuestion.STUDENTSCORE))) {
						content.append("<div class='QuestionMain'>><font color='#FF0000'><strong>(错误)</strong></font>"
								+ PaperToolUtil.processMaterial2(materialPath, questionTO.cPraxisCont.getPraxisMain(),
										questionTO) + "</div>\r\n");
					} else {
						content.append("<div class='QuestionMain'"
								+ PaperToolUtil.processMaterial2(materialPath, questionTO.cPraxisCont.getPraxisMain(),
										questionTO) + "</div>\r\n");
					}
					content.append("<ol type='I'>");
					int nSum = ((Reading) (questionTO.cPraxisCont)).getSubPraxisSum();
					String[] answera = new String[nSum];
					for (int m = 0; m < nSum; m++)
						answera[m] = "";
					if (studentRecord != null) {
						List answerList = (List) studentRecord.get(AnswerToQuestion.STUDENTANSWER);
						for (int m = 0; m < nSum; m++) {
							if (m + 1 <= answerList.size()) {
								answera[m] = answerList.get(m).toString();
							}
						}
					}
					for (int l = 1; l <= nSum; l++) {
						Selection cSelection = ((Reading) (questionTO.cPraxisCont)).getSubPraxis(l);

						content.append("<li><div class='QuestionMain'></div>");
						content.append("<ol type='i'>");
						for (int k = 1; k <= cSelection.findOptionSum(); k++) {
							content.append("<li class='QuestionOption'>" + Helper.parseString(cSelection.findOption(k))
									+ "</li>");
						}
						content.append("</ol>");
						content.append("</li>");
						content.append("<br><table border='0'>");
						content
								.append("<tr><td width='15%'>学生答案:</td><td width='85%'>" + answera[l - 1]
										+ "</td></tr>");
						content.append("<tr><td>标准答案:</td><td>" + (cSelection).findAnswer() + "</td></tr>");
						content.append("</table>");
					}
					content.append("</ol>");
					content.append("<br>");
					content.append("<div>试题分值：" + questionTO.fSuggScore + "</div>");
					content.append("<div>得分："
							+ (studentRecord.get(AnswerToQuestion.STUDENTSCORE) == null ? 0 : studentRecord
									.get(AnswerToQuestion.STUDENTSCORE)) + "</div>");
					content.append("<br>");
					break;

				}
				content.append("</li>");
			}
			content.append("</ol></td></tr>");
		}
		return content.toString();
	}

	public static String exportExamPaperInfo(String paperPath, String paperName, String studnetAnswerPath,
			String studentAnswerName) throws DAOSysException, SQLException {
		List praxisTo =null;
		try {
			praxisTo = PaperToolUtil.converterJsonToPaper(paperPath, paperName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		HashMap answermap = AnswerToolUtil.converterJsonToAnswer(studnetAnswerPath, studentAnswerName);
		Hashtable frametable = PaperToolUtil.converterToPaperFromPraxisTo(praxisTo);
		Hashtable keytable = PaperToolUtil.formatChange(frametable);
		// System.out.println("frametable.size()============="+frametable.size());
		StringBuffer content = new StringBuffer();
		content
				.append("<table border='0' cellpadding='2' cellspacing='3' style='border-collapse: collapse' bordercolor='#111111' width='100%' id='PaperCon'>");
		for (int i = 1; i <= frametable.size(); i++) {
			FrameTO frameTO = (FrameTO) frametable.get(keytable.get(i));
			int nPraxisTemplateID = (Integer) (PaperToolUtil.findAllPraxisTemplate().get(frameTO.nPraxisTypeID));
			if (nPraxisTemplateID == 5 || nPraxisTemplateID == 6) {
				content.append("<tr>");
				content.append("<td width='100%' align='left' valign='top'><div class='Frame'>");
				content.append(Helper.getAllCondition().get(i) + "、" + frameTO.strPraxisTypeName);
				content.append("（共" + frameTO.cQuestions.size() + "道小题，共" + frameTO.fScoreSum + "分） </div>");
				content.append("<ol>");
				for (int j = 1; j <= frameTO.cQuestions.size(); j++) {
					PraxisTO questionTO = (PraxisTO) frameTO.cQuestions.get(new Integer(j));
					String strStudentAnswer = "";
					String strStandardAnswer = "";
					HashMap studentRecord = (HashMap) answermap.get(questionTO.nPraxisID + "");
					content.append(" <li>");
					switch (nPraxisTemplateID) {
					case 5:
						// 填空题
						content.append("<div class='QuestionMain'>" + questionTO.cPraxisCont.getPraxisMain()
								+ "</div>\r\n");
						strStandardAnswer += "<ol type='1'>" + "\r\n";
						for (int k = 1; k <= ((FillingBlank) questionTO.cPraxisCont).getBlankSum(); k++) {
							strStandardAnswer += "<li class='QuestionOption'>"
									+ ((FillingBlank) questionTO.cPraxisCont).findAnswer(k) + "</li>" + "\r\n";
						}
						strStandardAnswer += "</ol>" + "\r\n";
						strStudentAnswer += "<ol type='1'>" + "\r\n";
						List answer = (List) studentRecord.get(AnswerToQuestion.ANSWER);
						for (int k = 0; k < answer.size(); k++) {
							strStudentAnswer += "<li class='QuestionOption'>" + answer.get(k) + "</li>" + "\r\n";
						}
						strStudentAnswer += "</ol>" + "\r\n";
						content.append("<br><table border='1'>");
						content.append("<tr><td width='15%'>学生答案:</td><td width='85%'>" + strStudentAnswer
								+ "</td></tr>");
						content.append("<tr><td>标准答案:</td><td>" + strStandardAnswer + "</td></tr>");
						content.append("<tr><td>试题分值:</td><td>" + questionTO.fSuggScore + "</td></tr>");
						content
								.append("<tr><td>评分标准:</td><td>"
										+ (questionTO.strGradApproach == null ? "" : questionTO.strGradApproach)
										+ "</td></tr>");
						content.append("<tr><td>得分:</td><td><input name='"
								+ questionTO.nPraxisID
								+ "_0' Qid='"
								+ questionTO.nPraxisID
								+ "' num='0' value='"
								+ (studentRecord.get(AnswerToQuestion.SCORE) == null ? 0 : studentRecord
										.get(AnswerToQuestion.SCORE)) + "' class='inputText' size='10'></td></tr>");
						content.append("</table>");
						break;
					case 6:
						// 问答题
						content.append("<div class='QuestionMain'><table><tr><td>"
								+ questionTO.cPraxisCont.getPraxisMain() + "</td></tr></table></div>\r\n");
						for (int k = 1; k <= ((FillingBlank) questionTO.cPraxisCont).getBlankSum(); k++) {
							strStandardAnswer += "<li class='QuestionOption'>"
									+ ((FillingBlank) questionTO.cPraxisCont).findAnswer(k) + "</li>" + "\r\n";
						}
						strStandardAnswer += "</ol>" + "\r\n";
						strStudentAnswer += "<ol type='1'>" + "\r\n";
						List answertwo = (List) studentRecord.get(AnswerToQuestion.ANSWER);
						for (int k = 0; k < answertwo.size(); k++) {
							strStudentAnswer += "<li class='QuestionOption'>" + answertwo.get(k) + "</li>" + "\r\n";
						}
						strStudentAnswer += "</ol>" + "\r\n";
						content.append("<br><table border='1'>");
						content.append("<tr><td width='15%'>学生答案:</td><td width='85%'>" + strStudentAnswer
								+ "</td></tr>");
						content.append("<tr><td>标准答案:</td><td><font color='#FF0000'>" + strStandardAnswer
								+ "</font></td></tr>");
						content.append("<tr><td>试题分值:</td><td>" + questionTO.fSuggScore + "</td></tr>");
						content
								.append("<tr><td>评分标准:</td><td>"
										+ (questionTO.strGradApproach == null ? "" : questionTO.strGradApproach)
										+ "</td></tr>");
						content.append("<tr><td>得分:</td><td><input  type='text' name='"
								+ questionTO.nPraxisID
								+ "_0' Qid='"
								+ questionTO.nPraxisID
								+ "' num='0' value='"
								+ (studentRecord.get(AnswerToQuestion.SCORE) == null ? 0 : studentRecord
										.get(AnswerToQuestion.SCORE)) + "' class='inputText' size='10'></td></tr>");
						content.append("</table>");
						break;
					}
					content.append("</li>");
				}
				content.append("</ol></td></tr>");
			}
		}
		return content.toString();
	}

	/**
	 * @param scoreMap
	 * @param studentAnswerFile
	 * @return 批改主观题;
	 */
	public static boolean checkObjPaperAction(String scoreString, File studentAnswerFile) {
		boolean isok = true;
		if (!studentAnswerFile.exists())
			isok = false;
		else {
			try {
				HashMap scoreMap = (HashMap) JSONUtil.deserialize(scoreString);
				String studentAnswerString = "";
				studentAnswerString = FileUtils.readFileToString(studentAnswerFile, "UTF8");
				HashMap studentAnswerMap = (HashMap) JSONUtil.deserialize(studentAnswerString);
				Iterator keyIterator = scoreMap.keySet().iterator();
				while (keyIterator.hasNext()) {
					String questionStringId = keyIterator.next().toString();
					HashMap oneQuestion = (HashMap) scoreMap.get(questionStringId);
					Object studentOneAnswer = studentAnswerMap.get(questionStringId);
					if (studentOneAnswer != null) {
						HashMap studentRecord = (HashMap) studentOneAnswer;
						studentRecord
								.put(AnswerToQuestion.STUDENTSCORE, oneQuestion.get(AnswerToQuestion.STUDENTSCORE));
					}
				}
				FileUtils.writeStringToFile(studentAnswerFile, JSONUtil.serialize(studentAnswerMap), "UTF8");
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return isok;
	}

	/**
	 * @param studentAnswerFile,学生提交答案,必须是在批改完成后方可.
	 * @return 找到学生成绩.
	 */
	public static float findStudentScore(File studentAnswerFile) {
		float score = 0;
		try {
			String record = FileUtils.readFileToString(studentAnswerFile, "UTF8");
			HashMap recordMap = (HashMap) JSONUtil.deserialize(record);
			Iterator keyIterator = recordMap.keySet().iterator();
			while (keyIterator.hasNext()) {
				String key = keyIterator.next().toString();
				HashMap oneRecord = (HashMap) recordMap.get(key);
				Object scoreObject = oneRecord.get(AnswerToQuestion.SCORE);
				if (scoreObject != null) {
					List list = (List) scoreObject;
					for (int i = 0; i < list.size(); i++) {
						if (list.get(i) != null && !list.get(i).equals(""))
							score += new Float(list.get(i).toString());
					}

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (JSONException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return score;
	}

	// 找到学生客观题分数；
	public static float findObjStudentScore(File studentAnswerFile) throws Exception {
		Hashtable table = new Hashtable();
		try {
			table = PaperToolUtil.findAllPraxisTemplate();
		} catch (DAOSysException e1) {
			e1.printStackTrace();
			throw e1;
		} catch (SQLException e1) {
			e1.printStackTrace();
			throw e1;
		}
		float score = 0;
		try {
			String record = FileUtils.readFileToString(studentAnswerFile, "UTF8");
			HashMap recordMap = (HashMap) JSONUtil.deserialize(record);
			Iterator keyIterator = recordMap.keySet().iterator();
			while (keyIterator.hasNext()) {
				String key = keyIterator.next().toString();
				HashMap oneRecord = (HashMap) recordMap.get(key);
				int npraxisTypeId = new Integer(oneRecord.get(AnswerToQuestion.TYPEID).toString());
				int nPraxisTemplateID = new Integer(table.get(npraxisTypeId).toString());
				Object scoreObject = oneRecord.get(AnswerToQuestion.STUDENTSCORE);
				switch (nPraxisTemplateID) {
				case 1:
				case 2:
				case 3:
				case 4:
				case 7:
				case 8: {
					if (scoreObject != null) {
						List list = (List) scoreObject;
						for (int i = 0; i < list.size(); i++) {
							if (list.get(i) != null && !list.get(i).equals(""))
								score += new Float(list.get(i).toString());
						}

					}
					break;
				}
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} catch (JSONException e) {
			e.printStackTrace();
			throw e;
		}
		return score;
	}

	// 找到学生主观题分数；
	public static float findSubStudentScore(File studentAnswerFile) {
		Hashtable table = new Hashtable();
		try {
			table = PaperToolUtil.findAllPraxisTemplate();
		} catch (DAOSysException e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1);
		} catch (SQLException e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1);
		}

		float score = 0;
		try {
			String record = FileUtils.readFileToString(studentAnswerFile, "UTF8");
			HashMap recordMap = (HashMap) JSONUtil.deserialize(record);
			Iterator keyIterator = recordMap.keySet().iterator();
			while (keyIterator.hasNext()) {
				String key = keyIterator.next().toString();
				HashMap oneRecord = (HashMap) recordMap.get(key);
				int npraxisTypeId = new Integer(oneRecord.get(AnswerToQuestion.TYPEID).toString());
				int nPraxisTemplateID = new Integer((table.get(npraxisTypeId)).toString());
				Object scoreObject = oneRecord.get(AnswerToQuestion.STUDENTSCORE);
				switch (nPraxisTemplateID) {
				case 5:
				case 6: {
					if (scoreObject != null) {
						List list = (List) scoreObject;
						for (int i = 0; i < list.size(); i++) {
							if (list.get(i) != null && !list.get(i).equals(""))
								score += new Float(list.get(i).toString());
						}

					}
					break;
				}
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (JSONException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return score;
	}

	public static float findTotalScore(List scoreList) {
		float totalScore = 0;
		for (int i = 0; i < scoreList.size(); i++) {
			totalScore += new Float(scoreList.get(i).toString());
		}
		return totalScore;
	}

}
