package com.bupticet.paperadmin.tool;

import java.io.File;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import com.bupticet.paperadmin.common.DAOSysException;
import com.bupticet.paperadmin.common.PaperAdminException;
import com.bupticet.paperadmin.model.AnswerToQuestion;
import com.bupticet.paperadmin.model.FrameTO;
import com.bupticet.paperadmin.model.PraxisTO;
import com.bupticet.paperadmin.service.imp.PaperAdminIntImp;
import com.bupticet.praxisadmin.praxistype.model.FillingBlank;
import com.bupticet.praxisadmin.praxistype.model.Reading;
import com.bupticet.praxisadmin.praxistype.model.Selection;

public class ServerActionTool {

	/**
	 * 生成作业文件
	 * 
	 * @param materialPath
	 *            作业资源文件路径
	 * @param jsonPath
	 *            保存作业的路径；
	 * @param id
	 *            作业ID
	 */
	public static void genExerciseAndFileById(String materialPath, String jsonPath, int id) {
		List praxisTOList = ExerciseToolUtil.findPraxisTOListById(id, materialPath);
		try {
			PaperToolUtil.converterPaperToJson(jsonPath, Helper.HomeWorkPrefix + id + Helper.EndTag, praxisTOList);
			AnswerToolUtil.converterAnswerToJSON(jsonPath, Helper.HomeWorkPrefix + id + Helper.Answer + Helper.EndTag,
					praxisTOList);
		} catch (DAOSysException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param id
	 *            策略id； 根据作业id，声称作业，并且根据给定的文件名保存到指定的文件中；
	 */
	public static void genJiKaoExerciseAndFileById(String paperJsonPath, String paperHtmlPath, String materialPath,
			String materialPathRe, String answerJsonPath, int id, int number) {
		Date date = new Date();
		long dateNum = date.getTime();
		PaperAdminIntImp newOne = new PaperAdminIntImp();

		for (int i = 0; i < number; i++) {
			Hashtable questionList = new Hashtable();
			try {
				questionList = newOne.autoPaperGen(id);
			} catch (PaperAdminException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			List praxisTOList = PraxisToolUtil.getPraxis(questionList, materialPath);
			// System.out.println("pracistoList==="+i+"===="+praxisTOList.size())
			// ;
			// List praxisTOList=ExerciseToolUtil.findPraxisTOListById(id,path);
			try {
				PaperToolUtil.converterPaperToJson(paperJsonPath, dateNum + "_" + i + ".jsonData", praxisTOList);
				PaperToolUtil.converterPaperToHTML(paperHtmlPath, materialPathRe, dateNum + "_" + i + ".html",
						praxisTOList);
				AnswerToolUtil.converterAnswerToJSON(answerJsonPath, dateNum + "_" + i + Helper.EndTag, praxisTOList);
			} catch (DAOSysException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * @param paperPath
	 * @param paperName
	 * @param studnetAnswerPath
	 * @param studentAnswerName
	 * @throws DAOSysException
	 * @throws SQLException
	 *             将主观题的试卷导出，以便老师批改；
	 */
	public static String exportObjectPaper(String materialPath, String paperPath, String paperName,
			String studnetAnswerPath, String studentAnswerName) throws DAOSysException, SQLException, Exception {
		List praxisTo = PaperToolUtil.converterJsonToPaper(paperPath, paperName);
		HashMap answermap = AnswerToolUtil.converterJsonToAnswer(studnetAnswerPath, studentAnswerName);
		Hashtable frametable = PaperToolUtil.converterToPaperFromPraxisTo(praxisTo);
		Hashtable keytable = PaperToolUtil.formatChange(frametable);
		StringBuffer content = new StringBuffer();
		content.append("<table border='0' cellpadding='2' cellspacing='3' style='border-collapse: collapse' bordercolor='#111111' width='100%' id='PaperCon'>");
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
						content.append("<div class='QuestionMain'>");
						content.append(PaperToolUtil.processMaterial2(materialPath,
								questionTO.cPraxisCont.getPraxisMain(), questionTO));
						content.append("</div>\r\n");
						strStandardAnswer += "<ol type='1'>" + "\r\n";
						for (int k = 1; k <= ((FillingBlank) questionTO.cPraxisCont).getBlankSum(); k++) {
							strStandardAnswer += "<li class='QuestionOption'>"
									+ ((FillingBlank) questionTO.cPraxisCont).findAnswer(k) + "</li>" + "\r\n";
						}
						strStandardAnswer += "</ol>" + "\r\n";
						strStudentAnswer += "<ol type='1'>" + "\r\n";
						List answer = (List) studentRecord.get(AnswerToQuestion.STUDENTANSWER);
						for (int k = 0; k < answer.size(); k++) {
							strStudentAnswer += "<li class='QuestionOption'>" + answer.get(k) + "</li>" + "\r\n";
						}
						strStudentAnswer += "</ol>" + "\r\n";
						content.append("<br><table border='1'>");
						content.append("<tr><td width='25%'>学生答案:</td><td width='75%'>" + strStudentAnswer
								+ "</td></tr>");
						content.append("<tr><td>标准答案:</td><td><font color='#FF0000'>" + strStandardAnswer
								+ "</font></td></tr>");
						content.append("<tr><td>试题分值:</td><td>" + questionTO.fSuggScore + "</td></tr>");
						content.append("<tr><td>评分标准:</td><td>"
								+ (questionTO.strGradApproach == null ? "" : questionTO.strGradApproach) + "</td></tr>");
						content.append("<tr><td>得分:</td><td><input name='"
								+ questionTO.nPraxisID
								+ "_0' Qid='"
								+ questionTO.nPraxisID
								+ "' num='0' value='"
								+ (studentRecord.get(AnswerToQuestion.STUDENTSCORE) == null ? 0 : studentRecord
										.get(AnswerToQuestion.STUDENTSCORE))
								+ "' class='inputText' size='10'></td></tr>");
						content.append("</table>");
						break;
					case 6:// 问答题
						content.append("<div class='QuestionMain'>");
						content.append(PaperToolUtil.processMaterial2(materialPath,
								questionTO.cPraxisCont.getPraxisMain(), questionTO));
						content.append("</td></tr></table></div>\r\n");

						for (int k = 1; k <= ((FillingBlank) questionTO.cPraxisCont).getBlankSum(); k++) {
							strStandardAnswer += "<div class='QuestionOption'>"
									+ ((FillingBlank) questionTO.cPraxisCont).findAnswer(k) + "</li>" + "\r\n";
						}
						strStandardAnswer += "</div>" + "\r\n";

						strStudentAnswer += "<div>" + "\r\n";
						List answertwo = (List) studentRecord.get(AnswerToQuestion.STUDENTANSWER);
						for (int k = 0; k < answertwo.size(); k++) {
							strStudentAnswer += "<li class='QuestionOption'>" + answertwo.get(k) + "</li>" + "\r\n";
						}
						strStudentAnswer += "</div>" + "\r\n";

						content.append("<br><table border='1'>");
						content.append("<tr><td width='25%'>学生答案:</td><td width='75%'>" + strStudentAnswer
								+ "</td></tr>");
						content.append("<tr><td>标准答案:</td><td><font color='#FF0000'>" + strStandardAnswer
								+ "</font></td></tr>");
						content.append("<tr><td>试题分值:</td><td>" + questionTO.fSuggScore + "</td></tr>");
						content.append("<tr><td>评分标准:</td><td>"
								+ (questionTO.strGradApproach == null ? "" : questionTO.strGradApproach) + "</td></tr>");
						content.append("<tr><td>得分:</td><td><input  type='text' name='"
								+ questionTO.nPraxisID
								+ "_0' Qid='"
								+ questionTO.nPraxisID
								+ "' num='0' value='"
								+ (studentRecord.get(AnswerToQuestion.STUDENTSCORE) == null ? 0 : studentRecord
										.get(AnswerToQuestion.STUDENTSCORE))
								+ "' class='inputText' size='10'></td></tr>");
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
	 * 将学生答案格式[1;12;123] 转化为[A;AB;ABC]
	 * 
	 * @param studentRecord
	 *            学生答案
	 * @return 转化后的学生答案
	 */
	private static List<String> convert123ToABC(List<String> studentRecord) {
		List<String> converedStr = new ArrayList<String>();
		for (String sr : studentRecord) {
			converedStr.add(convert123ToABC(sr));
		}
		return converedStr;
	}

	/**
	 * 将答案格式1;12;123 转化为A;AB;ABC
	 * 
	 * @param ansString
	 *            答案字符串
	 * @return 转化后答案
	 */
	private static String convert123ToABC(String ansString) {
		for (int i = 0; i < ansString.length(); i++) {
			if ("123456789".indexOf(ansString.substring(i, i + 1)) > -1) {
				ansString = ansString.replace(ansString.toCharArray()[i], (char) (16 + ansString.charAt(i)));
			}
		}
		return ansString;
	}

	/**
	 * 根据给定的试卷位置和资源位置显示试卷；
	 * 
	 * @param materialPath
	 *            资源位置(图片)
	 * @param path
	 *            试卷文件位置
	 * @param name
	 *            试卷文件名称
	 * @return 卷面的String
	 * @throws DAOSysException
	 * @throws SQLException
	 */
	public static String exportExercisePaper(String materialPath, String path, String name) throws DAOSysException,
			SQLException, Exception {
		String result = "";
		List praxistToList = PaperToolUtil.converterJsonToPaper(path, name);
		result = PaperToolUtil.converterToStringBuffer(materialPath, praxistToList).toString();
		return result;
	}

	/**
	 * 返回客观题的试题答案分析表格(适用1单选，2多选，3判断，7句子判错题)
	 * 
	 * @param studentRecord
	 *            学生答案
	 * @param showRightAnwer
	 *            是否显示正确答案
	 * @param questionTO
	 *            试卷
	 */
	public static String objQuestionAnalyze(HashMap studentRecord, boolean showRightAnwer, PraxisTO questionTO) {
		StringBuffer content = new StringBuffer();
		content.append("<table class='QuestionAnalyze' width='400'>");
		content.append("<tr><td width='60'>知识点:</td><td colspan='3'>");
		content.append(studentRecord.get(AnswerToQuestion.KNOWNAME) + "</td></tr>");

		content.append("<tr><td width='60'>学生答案:</td><td>");
		content.append(studentRecord == null ? " " : convert123ToABC((List) studentRecord
				.get(AnswerToQuestion.STUDENTANSWER)));
		content.append("</td>");

		// 需要判断是否要显示标准答案
		content.append("<td width='60'>" + (showRightAnwer ? "标准答案:" : "&nbsp;") + "</td><td>");
		content.append(showRightAnwer ? convert123ToABC(((Selection) (questionTO.cPraxisCont)).findAnswer()) : "&nbsp;");
		content.append("</td></tr>");

		content.append("<tr><td width='60'>得分:</td><td>");
		content.append(studentRecord == null ? 0 : studentRecord.get(AnswerToQuestion.STUDENTSCORE));
		content.append("</td>");
		content.append("<td width='60'>试题分值:</td><td>" + questionTO.fSuggScore + "</td></tr>");

		content.append("<tr><td width='60'>提示:</td><td colspan='3'>");
		content.append(questionTO.strHint == null || questionTO.strHint.trim().length() == 0 ? "&nbsp;"
				: entitle(questionTO.strHint));
		content.append("</td></tr>");

		content.append("</table>");
		return content.toString();
	}

	public static String questionType5Analyze(HashMap studentRecord, PraxisTO questionTO, String studentAnswer,
			String standardAnswer, boolean showRightAnwer) {
		StringBuffer content = new StringBuffer();
		content.append("<table class='QuestionAnalyze' width='400'>");

		content.append("<tr><td width='60'>知识点:</td><td colspan='3'>");
		content.append((studentRecord == null || studentRecord.get(AnswerToQuestion.KNOWNAME) == null ? "" : studentRecord.get(AnswerToQuestion.KNOWNAME)) + "</td></tr>");
		content.append("<tr><td width='60'>得分:</td><td width='140'>"
				+ (studentRecord == null || studentRecord.get(AnswerToQuestion.STUDENTSCORE) == null ? 0 : studentRecord
						.get(AnswerToQuestion.STUDENTSCORE)) + "</td>");
		content.append("<td width='60'>试题分值:</td><td width='140'>" + questionTO.fSuggScore + "</td></tr>");
		content.append("<tr><td>学生答案:</td><td colspan='3'>" + studentAnswer + "</td></tr>");

		if (showRightAnwer)
			content.append("<tr><td>标准答案:</td><td colspan='3'>" + standardAnswer + "</td></tr>");

		content.append("<tr><td width='60'>提示:</td><td colspan='3'>");
		content.append(questionTO.strHint == null || questionTO.strHint.trim().length() == 0 ? "&nbsp;"
				: entitle(questionTO.strHint));
		content.append("</td></tr>");

		content.append("</table>");

		return content.toString();
	}

	/**
	 * 生成批改结束后的试卷信息
	 * 
	 * @param materialPath
	 *            试卷内图片等素材资源的路径(web 绝对路径)
	 * @param paperPath
	 *            试卷位置路径(磁盘绝对路径)
	 * @param paperName
	 *            试卷名称
	 * @param studnetAnswerPath
	 *            学生答案保存路径(磁盘绝对路径)
	 * @param studentAnswerName
	 *            学生答案文件名
	 * @param showRightAnwer
	 *            是否显示学生答案
	 * @return 带批改信息的试卷信息
	 * @throws DAOSysException
	 * @throws SQLException
	 */
	public static String exportExamInfPaper(String materialPath, String paperPath, String paperName,
			String studnetAnswerPath, String studentAnswerName, boolean showRightAnwer) throws DAOSysException,
			SQLException, Exception {
		// 组织试卷 if==null 所给出的文件不存在
		List praxisTo = PaperToolUtil.converterJsonToPaper(paperPath, paperName);
		HashMap answermap = AnswerToolUtil.converterJsonToAnswer(studnetAnswerPath, studentAnswerName);
		Hashtable frametable = PaperToolUtil.converterToPaperFromPraxisTo(praxisTo);
		Hashtable keytable = PaperToolUtil.formatChange(frametable);
		StringBuffer content = new StringBuffer();
		content.append("<table id='PaperContext'>");
		for (int i = 1; i <= frametable.size(); i++) {
			FrameTO frameTO = (FrameTO) frametable.get(keytable.get(i));
			int nPraxisTemplateID = (Integer) (PaperToolUtil.findAllPraxisTemplate().get(frameTO.nPraxisTypeID));
			content.append("<tr><td width='100%' align='left' valign='top'><div class='Frame'>");
			content.append(Helper.getAllCondition().get(i) + "、" + frameTO.strPraxisTypeName);
			content.append("（共" + frameTO.cQuestions.size() + "道小题，共" + frameTO.fScoreSum + "分） </div>");
			content.append("<ol>");

			for (int j = 1; j <= frameTO.cQuestions.size(); j++) {
				PraxisTO questionTO = (PraxisTO) frameTO.cQuestions.get(new Integer(j));
				String strStudentAnswer = ""; // 学生做答答案
				String strStandardAnswer = ""; // 试题标准答案
				HashMap studentRecord = (HashMap) answermap.get(questionTO.nPraxisID + "");
				content.append(" <br><li>");
				switch (nPraxisTemplateID) {
				case 1: // 单项选择题
					content.append("<div class='QuestionMain'>");

					if (studentRecord == null || studentRecord.get(AnswerToQuestion.ISRIGHT) == null
							|| ((List) studentRecord.get(AnswerToQuestion.ISRIGHT)).get(0).toString().equals("0")) {
						content.append("<font color='#FF0000'><strong>(错误)</strong></font>");
					}
					content.append(PaperToolUtil.processMaterial2(materialPath, questionTO.cPraxisCont.getPraxisMain(),
							questionTO));
					content.append("</div>\r\n");

					content.append("<ol type='A'>\r\n");
					for (int k = 1; k <= ((Selection) (questionTO.cPraxisCont)).findOptionSum(); k++) {
						content.append("<li class='QuestionOption'>");
						content.append(PaperToolUtil.processMaterial2(materialPath,
								Helper.parseString(((Selection) (questionTO.cPraxisCont)).findOption(k)), questionTO));
						content.append("</li>\r\n");
					}
					content.append("</ol>\r\n");

					// 试题答案分析表格
					content.append(objQuestionAnalyze(studentRecord, showRightAnwer, questionTO));

					break;
				case 2: // 多项选择题
					content.append("<div class='QuestionMain'>");
					if (studentRecord == null || studentRecord.get(AnswerToQuestion.ISRIGHT) == null
							|| ((List) studentRecord.get(AnswerToQuestion.ISRIGHT)).get(0).toString().equals("0")) {
						content.append("<font color='#FF0000'><strong>(错误)</strong></font>");
					}
					content.append(PaperToolUtil.processMaterial2(materialPath, questionTO.cPraxisCont.getPraxisMain(),
							questionTO));
					content.append("</div>\r\n");

					content.append("<ol type='A'>\r\n");
					for (int k = 1; k <= ((Selection) (questionTO.cPraxisCont)).findOptionSum(); k++) {
						content.append("<li class='QuestionOption'>"
								+ PaperToolUtil.processMaterial2(materialPath,
										Helper.parseString(((Selection) (questionTO.cPraxisCont)).findOption(k)),
										questionTO) + "</li>\r\n");
					}
					content.append("</ol>\r\n");

					// 试题答案分析表格
					content.append(objQuestionAnalyze(studentRecord, showRightAnwer, questionTO));
					break;
				case 3: // 判断题
					content.append("<div class='QuestionMain'>");
					if (studentRecord == null || studentRecord.get(AnswerToQuestion.ISRIGHT) == null
							|| ((List) studentRecord.get(AnswerToQuestion.ISRIGHT)).get(0).toString().equals("0")) {
						content.append("<font color='#FF0000'><strong>(错误)</strong></font>");
					}
					content.append(PaperToolUtil.processMaterial2(materialPath, questionTO.cPraxisCont.getPraxisMain(),
							questionTO));
					content.append("</div>\r\n");

					content.append("<ol type='A'>\r\n");
					for (int k = 1; k <= ((Selection) (questionTO.cPraxisCont)).findOptionSum(); k++) {
						content.append("<li class='QuestionOption'>"
								+ PaperToolUtil.processMaterial2(materialPath,
										Helper.parseString(((Selection) (questionTO.cPraxisCont)).findOption(k)),
										questionTO) + "</li>\r\n");
					}
					content.append("</ol>\r\n");
					content.append(objQuestionAnalyze(studentRecord, showRightAnwer, questionTO));
					break;
				case 4: // 阅读理解题
					content.append("<div class='QuestionMain'>");
					if (studentRecord == null
							|| studentRecord.get(AnswerToQuestion.STUDENTSCORE) == null
							|| questionTO.fSuggScore != findTotalScore((List) studentRecord
									.get(AnswerToQuestion.STUDENTSCORE))) {
						content.append("<font color='#FF0000'><strong>(错误)</strong></font>");
					}

					content.append(PaperToolUtil.processMaterial2(materialPath, questionTO.cPraxisCont.getPraxisMain(),
							questionTO));
					content.append("</div>\r\n");

					content.append("<ol type='1'>");
					int subPraxisSum = ((Reading) (questionTO.cPraxisCont)).getSubPraxisSum();
					String[] answer = new String[subPraxisSum];
					for (int m = 0; m < subPraxisSum; m++)
						answer[m] = "";
					if (studentRecord != null) {
						List answerList = convert123ToABC((List) studentRecord.get(AnswerToQuestion.STUDENTANSWER));
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
						content.append("<ol type='A'>");
						for (int k = 1; k <= cSelection.findOptionSum(); k++) {
							content.append("<li class='QuestionOption'>"
									+ PaperToolUtil.processMaterial2(materialPath,
											Helper.parseString(cSelection.findOption(k)), questionTO) + "</li>");
						}
						content.append("</ol>");
						content.append("</li>");
						questionType8Analyze(answer[l - 1], cSelection.findAnswer(), showRightAnwer);
					}
					content.append("</ol>");
					content.append("<br>");
					content.append("<div>试题分值：" + questionTO.fSuggScore + "</div>");
					content.append("<div>得分："
							+ (studentRecord == null ? 0 : studentRecord.get(AnswerToQuestion.STUDENTSCORE)) + "</div>");

					content.append("<div>提示："
							+ (questionTO.strHint == null || questionTO.strHint.trim().length() == 0 ? "&nbsp;"
									: entitle(questionTO.strHint)) + "</div>");

					content.append("<br>");
					break;
				case 5: // 填空题
					content.append("<div class='QuestionMain'>");
					if (studentRecord == null
							|| questionTO.fSuggScore != findTotalScore((List) studentRecord
									.get(AnswerToQuestion.STUDENTSCORE))) {
						content.append("<font color='#FF0000'><strong>(错误)</strong></font>");
					}
					content.append(PaperToolUtil.processMaterial2(materialPath, questionTO.cPraxisCont.getPraxisMain(),
							questionTO));
					content.append("</div>\r\n");
					// 试题标准答题
					strStandardAnswer = "<ol type='1'>";
					for (int k = 1; k <= ((FillingBlank) questionTO.cPraxisCont).getBlankSum(); k++) {
						strStandardAnswer += "<li class='QuestionOption'>";
						strStandardAnswer += ((FillingBlank) questionTO.cPraxisCont).findAnswer(k) + "</li>\r\n";
					}
					strStandardAnswer += "</ol>";

					// 学生答题
					strStudentAnswer += "<ol type='1'>";
					if (studentRecord != null) {
						List answer11 = (List) studentRecord.get(AnswerToQuestion.STUDENTANSWER);
						for (int k = 0; k < answer11.size(); k++) {
							strStudentAnswer += "<li class='QuestionOption'>" + answer11.get(k) + "</li>" + "\r\n";
						}
					}
					strStudentAnswer += "</ol>";

					content.append(questionType5Analyze(studentRecord, questionTO, strStudentAnswer, strStandardAnswer,
							showRightAnwer));

					break;
				case 6:// 问答题
					content.append("<div class='QuestionMain'>");
					content.append(PaperToolUtil.processMaterial2(materialPath, questionTO.cPraxisCont.getPraxisMain(),
							questionTO));
					content.append("</div>\r\n");

					strStandardAnswer = "<div class='QuestionOption'>";
					for (int k = 1; k <= ((FillingBlank) questionTO.cPraxisCont).getBlankSum(); k++) {
						strStandardAnswer += ((FillingBlank) questionTO.cPraxisCont).findAnswer(k);
					}
					strStandardAnswer += "</div>";

					strStudentAnswer += "<div class='QuestionOption'>";
					// List answertwo = convert123ToABC((List)
					// studentRecord.get(AnswerToQuestion.STUDENTANSWER));
					List answertwo = (List) studentRecord.get(AnswerToQuestion.STUDENTANSWER);
					for (int k = 0; k < answertwo.size(); k++) {
						strStudentAnswer += answertwo.get(k);
					}
					strStudentAnswer += "</div>";

					content.append(questionType5Analyze(studentRecord, questionTO, strStudentAnswer, strStandardAnswer,
							showRightAnwer));
					break;
				case 7:// 句子判错题
					content.append("<div class='QuestionMain'>");
					if (studentRecord == null
							|| questionTO.fSuggScore != findTotalScore((List) studentRecord
									.get(AnswerToQuestion.STUDENTSCORE))) {
						content.append("<font color='#FF0000'><strong>(错误)</strong></font>");
					}

					content.append(PaperToolUtil.processMaterial2(materialPath, questionTO.cPraxisCont.getPraxisMain(),
							questionTO));
					content.append("</div>\r\n");

					content.append("<ol type='A'>");
					for (int k = 1; k <= ((Selection) (questionTO.cPraxisCont)).findOptionSum(); k++) {
						content.append("<li class='QuestionOption'>"
								+ PaperToolUtil.processMaterial2(materialPath,
										Helper.parseString(((Selection) (questionTO.cPraxisCont)).findOption(k)),
										questionTO) + "</li>");
					}
					content.append("</ol>");
					content.append(objQuestionAnalyze(studentRecord, showRightAnwer, questionTO));
					break;
				case 8:// 完形填空、语音题
					content.append("<div class='QuestionMain'");

					if (studentRecord == null
							|| questionTO.fSuggScore != findTotalScore((List) studentRecord
									.get(AnswerToQuestion.STUDENTSCORE))) {
						content.append("<font color='#FF0000'><strong>(错误)</strong></font>");

					}
					content.append(PaperToolUtil.processMaterial2(materialPath, questionTO.cPraxisCont.getPraxisMain(),
							questionTO));
					content.append("</div>\r\n");

					content.append("<ol type='a'>");
					int nSum = ((Reading) (questionTO.cPraxisCont)).getSubPraxisSum();
					String[] answera = new String[nSum];
					for (int m = 0; m < nSum; m++)
						answera[m] = "";
					if (studentRecord != null) {
						List answerList = convert123ToABC((List) studentRecord.get(AnswerToQuestion.STUDENTANSWER));
						for (int m = 0; m < nSum; m++) {
							if (m + 1 <= answerList.size()) {
								answera[m] = answerList.get(m).toString();
							}
						}
					}
					for (int l = 1; l <= nSum; l++) {
						Selection cSelection = ((Reading) (questionTO.cPraxisCont)).getSubPraxis(l);

						content.append("<li>");
						content.append("<ol type='A'>");
						for (int k = 1; k <= cSelection.findOptionSum(); k++) {
							content.append("<li class='QuestionOption'>"
									+ PaperToolUtil.processMaterial2(materialPath,
											Helper.parseString(cSelection.findOption(k)), questionTO) + "</li>");

						}
						content.append("</ol>");
						content.append("</li>");
						content.append(questionType8Analyze(answera[l - 1], cSelection.findAnswer(), showRightAnwer));
					}
					content.append("</ol>");
					content.append("<br>");
					content.append("<div>试题分值：" + questionTO.fSuggScore + "</div>");
					content.append("<div>得分："
							+ (studentRecord.get(AnswerToQuestion.STUDENTSCORE) == null ? 0 : studentRecord
									.get(AnswerToQuestion.STUDENTSCORE)) + "</div>");
					content.append("<div>提示："
							+ (questionTO.strHint == null || questionTO.strHint.trim().length() == 0 ? "&nbsp;"
									: entitle(questionTO.strHint)) + "</div>");
					content.append("<br>");
					break;

				}
				content.append("</li>");
			}
			content.append("</ol></td></tr>");
		}
		return content.toString();
	}

	/**
	 * 返回试题类型8的答案分析表格(适用 8语音题,4阅读理解题)
	 * 
	 * @param studentAnswer
	 *            学生答案
	 * @param showRightAnwer
	 *            是否显示正确答案
	 * @param standardAnswer
	 *            标准答案
	 */
	public static String questionType8Analyze(String studentAnswer, String standardAnswer, boolean showRightAnwer) {
		StringBuffer content = new StringBuffer();

		content.append("<table class='QuestionAnalyze' width='400'>");
		content.append("<tr><td width='60'>学生答案:</td><td>" + studentAnswer + "</td>");
		content.append("<td width='60'>"
				+ (showRightAnwer ? "标准答案:</td><td>" + convert123ToABC(standardAnswer) : "&nbsp;") + "</td></tr>");
		content.append("</table>");
		return content.toString();
	}

	/**
	 * 同一策略用同一试卷
	 * 
	 * @param materialPath
	 *            图片资源保存路径
	 * @param cSchemaContId
	 *            出卷策略id
	 * @param path
	 *            试卷与正确答案 的json文件的保存位置
	 * @param testId
	 *            测试ID，json 文件名组成的一部分
	 * @return 返回试卷名称
	 */
	public static String genTestNameBycSchemaContIdByOneTime(String materialPath, int cSchemaContId, String path,
			String testId) throws Exception {
		String paperName = Helper.TestPrefix + testId + Helper.EndTag;
		File file = new File(path, paperName);
		if (file.exists()) {
		} else {
			File pathFile = new File(path);
			if (!pathFile.exists())
				pathFile.mkdirs();
			PaperAdminIntImp newOne = new PaperAdminIntImp();
			Hashtable questionList = new Hashtable();

			questionList = newOne.autoPaperGen(cSchemaContId);

			List questions = PraxisToolUtil.getPraxis(questionList, materialPath);
			PaperToolUtil.converterPaperToJson(path, Helper.TestPrefix + testId + Helper.EndTag, questions);
			AnswerToolUtil.converterAnswerToJSON(path, Helper.TestPrefix + testId + Helper.Answer + Helper.EndTag,
					questions);
		}
		paperName = Helper.TestPrefix + testId;
		return paperName;
	}

	/**
	 * @param materialPath
	 *            试题图片的保存位置
	 * @param cSchemaContId
	 *            策略id
	 * @param path
	 *            试卷的位置。因为生成试题时会将图片导出； 根据策略生成试卷，每次生成新的；
	 * @param testId
	 *            测试id；
	 */
	public static String genTestNameBycSchemaContIdByAllNew(String materialPath, int cSchemaContId, String path,
			String testId) throws Exception {
		PaperAdminIntImp newOne = new PaperAdminIntImp();
		Hashtable questionList = new Hashtable();
		String paperName = "";
		questionList = newOne.autoPaperGen(cSchemaContId);
		List questions = PraxisToolUtil.getPraxis(questionList, materialPath);
		long timeLong = Helper.findNow();
		PaperToolUtil
				.converterPaperToJson(path, Helper.TestPrefix + testId + "_" + timeLong + Helper.EndTag, questions);
		AnswerToolUtil.converterAnswerToJSON(path, Helper.TestPrefix + testId + "_" + timeLong + Helper.Answer
				+ Helper.EndTag, questions);
		paperName = Helper.TestPrefix + testId + "_" + timeLong;
		return paperName;
	}

	public static void generateTest(String paperPath, String answerPath, String materialPath, int cSchemaContId,
			int operateCount) throws Exception {
		PaperAdminIntImp newOne = new PaperAdminIntImp();
		String paperNamePrefix = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String paperName = null;
		List questions = null;
		for (int i = 0; i < operateCount; i++) {
			paperName = paperNamePrefix + new DecimalFormat("0000").format(i);
			questions = PraxisToolUtil.getPraxis(newOne.autoPaperGen(cSchemaContId), materialPath);
			PaperToolUtil.converterPaperToJson(paperPath, Helper.TestPrefix + paperName + Helper.EndTag, questions);
			AnswerToolUtil.converterAnswerToJSON(answerPath, Helper.TestPrefix + paperName + Helper.Answer
					+ Helper.EndTag, questions);
		}
	}

	public static float findTotalScore(List scoreList) {
		float totalScore = 0;
		for (int i = 0; i < scoreList.size(); i++) {
			totalScore += new Float(scoreList.get(i).toString());
		}
		return totalScore;
	}

	private static String entitle(String fString) {
		if ((fString == null) || "".equals(fString)) {
			return "";
		}
		fString = fString.replaceAll("&", "&amp;");
		fString = fString.replaceAll("'", "&acute;");
		fString = fString.replaceAll("\"", "&quot;");
		fString = fString.replaceAll("<", "&lt;");
		fString = fString.replaceAll(">", "&gt;");
		fString = fString.replaceAll("  ", "&nbsp;&nbsp;");
		fString = fString.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
		fString = fString.replaceAll("\r\n", "<br />");
		fString = fString.replaceAll("\r", "<br />");
		return fString.replaceAll("\n", "<br />");
	}
}
