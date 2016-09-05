package com.bupticet.paperadmin.tool;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;

import com.bupticet.paperadmin.common.DAOSysException;
import com.bupticet.paperadmin.model.AnswerToPaper;
import com.bupticet.paperadmin.model.AnswerToQuestion;
import com.bupticet.paperadmin.model.PraxisTO;
import com.bupticet.praxisadmin.praxistype.model.FillingBlank;
import com.bupticet.praxisadmin.praxistype.model.Reading;
import com.bupticet.praxisadmin.praxistype.model.Selection;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

public class AnswerToolUtil {
	/**
	 * @param paperTO
	 *            找到数据的答案；
	 * @return
	 */
	public static AnswerToPaper fromPaperToAnswer(List<PraxisTO> praxisTOList) {
		AnswerToPaper result = new AnswerToPaper();

		ArrayList questionAnswer = new ArrayList();
		Hashtable table = new Hashtable();
		try {
			table = PaperToolUtil.findAllPraxisTemplate();
		} catch (DAOSysException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		for (int i = 0; i < praxisTOList.size(); i++) {
			PraxisTO praxisTO = (PraxisTO) praxisTOList.get(i);
			String[] answer = null;
			AnswerToQuestion oneAnswer = new AnswerToQuestion();
			oneAnswer.setId(praxisTO.nPraxisID);
			oneAnswer.setScore(praxisTO.fSuggScore);
			int nPraxisTemplateID = (Integer) (table.get(praxisTO.nPraxisTypeID));
			switch (nPraxisTemplateID) {
			case 1: {
				answer = new String[1];
				answer[0] = ((Selection) (praxisTO.cPraxisCont)).findAnswer();
				break;
			}
			case 2: {
				answer = new String[1];
				answer[0] = ((Selection) (praxisTO.cPraxisCont)).findAnswer();
				break;
			}
			case 3: {
				answer = new String[1];
				answer[0] = ((Selection) (praxisTO.cPraxisCont)).findAnswer();
				break;
			}
			case 4: {
				int number = ((Reading) (praxisTO.cPraxisCont)).getSubPraxisSum();
				answer = new String[number];
				for (int l = 1; l <= number; l++) {
					Selection cSelection = ((Reading) (praxisTO.cPraxisCont)).getSubPraxis(l);
					answer[l - 1] = ((Selection) cSelection).findAnswer();
				}
				break;
			}
			case 5: {// 填空题
				int number = ((FillingBlank) (praxisTO.cPraxisCont)).getBlankSum();
				answer = new String[number];
				for (int k = 1; k <= ((FillingBlank) praxisTO.cPraxisCont).getBlankSum(); k++) {
					answer[k - 1] = ((FillingBlank) praxisTO.cPraxisCont).findAnswer(k) + "    ";
				}
				break;
			}
			case 6: {// 问答题
				int number = ((FillingBlank) (praxisTO.cPraxisCont)).getBlankSum();
				answer = new String[number];
				for (int k = 1; k <= ((FillingBlank) praxisTO.cPraxisCont).getBlankSum(); k++) {
					answer[k - 1] = ((FillingBlank) praxisTO.cPraxisCont).findAnswer(k);
				}
				break;
			}
			case 7: {
				answer = new String[1];
				answer[0] = ((Selection) (praxisTO.cPraxisCont)).findAnswer();
				break;
			}
			case 8: {// 完型填空
				int number = ((Reading) (praxisTO.cPraxisCont)).getSubPraxisSum();
				answer = new String[number];
				for (int l = 1; l <= ((Reading) (praxisTO.cPraxisCont)).getSubPraxisSum(); l++) {
					Selection cSelection = ((Reading) (praxisTO.cPraxisCont)).getSubPraxis(l);
					answer[l - 1] = ((Selection) cSelection).findAnswer();
				}
				break;
			}
			}
			oneAnswer.setAnswer(answer);
			oneAnswer.setIsobject(praxisTO.nIsObjective);
			oneAnswer.setTypeId(praxisTO.nPraxisTypeID);
			oneAnswer.setKnowId(praxisTO.nKnowID);
			oneAnswer.setKnowName(praxisTO.strKnowName);
			questionAnswer.add(oneAnswer);
		}
		result.setList(questionAnswer);
		return result;
	}

	/**
	 * @param path
	 *            存放JSON数据的路径
	 * @param name
	 *            JSON数据的名字;
	 * @param answerTo
	 *            代表答案本身 function: 将答案对象本身转化为path/name的对象;
	 */
	public static boolean converterAnswerToJSON(String path, String name, List<PraxisTO> praxisToList) throws Exception {
		File filepath = new File(path);
		if (!filepath.exists())
			filepath.mkdirs();
		File file = new File(path, name);
		AnswerToPaper answerTo = fromPaperToAnswer(praxisToList);
		Map answerMap = converterAnswerToMap(answerTo);
		JSONObject serial = JSONObject.fromObject(answerMap);
		try {
			FileUtils.writeStringToFile(file, serial.toString(), "UTF8");
		} catch (Exception e) {
			System.out.println("在导出答案时发生错误" + e);
			throw new RuntimeException(e);
		}

		/*
		 * File filepath=new File(path); if(!filepath.exists())
		 * filepath.mkdir(); File file = new File(path, name); AnswerToPaper
		 * answerTo=fromPaperToAnswer(paperTO);
		 * System.out.println(answerTo.toString()); String
		 * serial=JSONUtil.serialize(answerTo);
		 * System.out.println(serial.toString());
		 * System.out.println(file.getAbsolutePath());
		 * FileUtils.writeStringToFile(file, serial,"UTF8");
		 */
		return true;
	}

	/**
	 * @param answerTo
	 * @return 将答案转化为map
	 */
	public static Map converterAnswerToMap(AnswerToPaper answerTo) {
		Map eachTAnswer = new HashMap();
		for (int i = 0; i < answerTo.getList().size(); i++) {
			AnswerToQuestion answerToQuestion = (AnswerToQuestion) answerTo.getList().get(i);
			eachTAnswer.put(new Integer(answerToQuestion.getId()), answerToQuestion);
		}
		return eachTAnswer;
	}

	/**
	 * 将文件中的答案读出来，反序列化成map；
	 * 
	 * @param path
	 * @param name
	 * @return
	 */
	public static HashMap converterJsonToAnswer(String path, String name) {
		HashMap answerMap = new HashMap();
		File file = new File(path, name);
		if (!file.exists())
			return null;
		else {
			try {
				String answer = FileUtils.readFileToString(file, "UTF8");
				answerMap = (HashMap) JSONUtil.deserialize(answer);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} catch (JSONException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		return answerMap;
	}
}
