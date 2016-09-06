package com.bupticet.paperadmin.model;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import com.bupticet.paperadmin.common.Constants;
import com.bupticet.paperadmin.common.ItemInfo;
import com.bupticet.paperadmin.common.PaperException;
import com.bupticet.paperadmin.schema.model.SchemaContent;
import com.bupticet.paperadmin.service.PaperAdminDAO;
import com.bupticet.paperadmin.service.imp.PaperAdminDAOImp;

public class Publisher {

	private TempData data = new TempData();

	private SchemaContent schema = null;

	private int totleNum = 0; // 用于组卷的题目总数

	public Publisher() {

	}

	// 分析整型数值,include=true则包含integer本身

	private Vector intParse(int integer, boolean include) {

		Vector value = new Vector();
		int i = 1;
		do {
			if (integer == i) {
				value.addElement(new Integer(i));
				return value;
			} else if ((integer & i) > 0)
				value.addElement(new Integer(i));
			i <<= 1;
		} while (integer >= i);
		if (include)
			value.addElement(new Integer(integer));
		return value;
	}

	// ljw加方法，用来计算试题用途
	private ArrayList intParseLjw(int integer) {

		ArrayList value = new ArrayList();
		HashSet setLjw = new HashSet();
		ArrayList valueLjw = new ArrayList();
		int i = 1;
		int j = 0;
		while (integer >= i) {
			if ((integer & i) > 0)
				value.add(new Integer(i));
			i <<= 1;
		}
		// System.out.println("aaaa1111111111:;"+value);
		Iterator it = value.iterator();
		while (it.hasNext()) {
			j = ((Integer) it.next()).intValue();
			// System.out.println("aaaa22222222:;"+j);
			switch (j) {
			case 1:
				setLjw.add(new Integer(1));
				setLjw.add(new Integer(3));
				setLjw.add(new Integer(5));
				break;
			case 2:
				setLjw.add(new Integer(2));
				setLjw.add(new Integer(3));
				setLjw.add(new Integer(6));
				break;
			case 4:
				setLjw.add(new Integer(4));
				setLjw.add(new Integer(5));
				setLjw.add(new Integer(6));
				break;
			}
		}
		setLjw.add(new Integer(7));
		valueLjw.addAll(setLjw);
		// System.out.println("aaaa333333333:::::::;"+valueLjw);
		return valueLjw;
	}

	// 分析Hashtable中的Vector类的key值，并把他们无重复的添加到一个新的Hashtable中

	private String getKeys(Hashtable ht, boolean noDittograph) throws PaperException {
		Hashtable newHt = new Hashtable();
		try {
			Enumeration e = ht.keys();
			while (e.hasMoreElements()) {
				Vector vector = (Vector) (e.nextElement());
				Enumeration enum1 = vector.elements();
				while (enum1.hasMoreElements()) {
					newHt.put(enum1.nextElement(), new Integer(0));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return getKeys(newHt);
	}

	// 从Hashtable中获取String类的key值，组合成特定的用于sql的字符串

	private String getKeys(Hashtable ht) throws PaperException {
		String sTemp = "";
		try {
			Enumeration e = ht.keys();
			do {
				sTemp += "'" + e.nextElement() + "',";
			}
			while (e.hasMoreElements());
			sTemp = sTemp.substring(0, sTemp.length() - 1);
			return sTemp;
		} catch (Exception e) {
			e.printStackTrace();
			throw new PaperException(e.getMessage(), "获取组卷参数有错误!");

		}

	}

	// 根据schema生成sql语句

	private String makeSql() throws PaperException {

		try {

			StringBuffer sql = new StringBuffer("");

			sql.append("SELECT pp_pp_id,pp_pp_type,pp_pp_difficulty,pp_pp_cognizetype,");

			sql.append("pp_sugg_time,pp_sugg_score,pp_rela_modulus,pk_know_id FROM");

			sql.append(" PRAXISPAPER,PRAXISKNOWLEDGE WHERE");

			// 课程id

			sql.append(" pp_belo_course=" + schema.nCourseID);

			sql.append(" AND praxispaper.pp_pp_id=praxisknowledge.PK_PRAX_ID");

			// 最大使用次数

			if (schema.nMaxUsedTimes >= 0)

				sql.append(" AND pp_used_times<=" + schema.nMaxUsedTimes);

			// 日期限制

			if (schema.nMinTimeSlot >= 0)

				sql.append(" AND sysdate-pp_expo_time>=" + schema.nMinTimeSlot);

			Vector value = new Vector();
			Enumeration e = null;
			ArrayList value1 = new ArrayList();
			Iterator e1 = null;
			String sTemp = "";

			// 题目用途ljw修改
			//System.out.println("salljw1111111111:::" + sql.toString());

			if (schema.nPraxisUse > 0) {
				value1 = intParseLjw(schema.nPraxisUse);
				e1 = value1.iterator();
				while (e1.hasNext()) {
					sTemp += (Integer) e1.next() + ",";
				}
				sTemp = sTemp.substring(0, sTemp.length() - 1);// 去除末尾逗号
				sql.append(" AND pp_pp_use IN (" + sTemp + ")");
			}
			//System.out.println("salljw222222222222:::" + sql);

			// 题目审核情况

			if (schema.nAuditStatus > 0) {
				value = intParse(schema.nAuditStatus, false);
				//System.out.println("value ==" + value);
				e = value.elements();
				//System.out.println("e ==" + e);
				if (e.hasMoreElements()) {
					sTemp = "";
				} else {
					sTemp = ",";
				}

				while (e.hasMoreElements()) {
					//System.out.println("sTemp while  == " + sTemp);
					sTemp += (Integer) e.nextElement() + ",";
				}
				sTemp = sTemp.substring(0, sTemp.length() - 1);
				sql.append(" AND pp_audi_status IN (" + sTemp + ")");
				//System.out.println("sql----1=====" + sql);

			}

			// 类型分布

			if (schema.cTypeDist != null) {
				sTemp = getKeys(schema.cTypeDist.cDemand);
				sql.append(" AND pp_pp_type IN (" + sTemp + ")");
				//System.out.println("sql----2=====" + sql);

			}

			// 难度分布

			/*
			 * if(schema.cDiffDist!=null){ sTemp=getKeys(schema.cDiffDist.cDemand); sql.append(" AND pp_pp_difficulty IN
			 * ("+sTemp+")"); } //认知分布 if(schema.cCognDist!=null){ sTemp=getKeys(schema.cCognDist.cDemand); sql.append("
			 * AND pp_pp_cognizetype IN ("+sTemp+")"); }
			 */

			// 知识点分布
			
			if (schema.cKnowDist != null && schema.cKnowDist.cDemand != null && !schema.cKnowDist.cDemand.isEmpty()) {
				//System.out.println("知识点分布 ===" + schema.cKnowDist.cDemand);
				sTemp = getKeys(schema.cKnowDist.cDemand, true);
				sql.append(" AND pk_know_id IN (" + sTemp + ")");
			}
			//System.out.println(sql);

			return sql.toString();

		}

		catch (Exception e) {

			throw new PaperException(e.getMessage(), "生成sql语句有错误！");

		}

	}

	// 进一步处理组卷需要的参数

	private boolean dealSchema() throws PaperException {

		// 对类型的处理

		if (schema.cTypeDist != null) {

			data.nTypeNum = schema.cTypeDist.cDemand.size();

			data.pnTypeIndex = new int[data.nTypeNum];

			data.pnTypeMark = new int[data.nTypeNum];

			data.pfTypeAve = new float[data.nTypeNum];

			if (schema.cTypeDist.nMarkOrAmount == 0) {

				data.pfTypeDemand = new float[data.nTypeNum];

			}

			else

				data.pnTypeDemand = new int[data.nTypeNum];

			data.pnTypeCount = new int[data.nTypeNum];

			Enumeration e = schema.cTypeDist.cDemand.keys();

			int i = 0;

			Integer tempInteger = null;

			Float tempFloat = null;

			while (e.hasMoreElements()) {

				tempInteger = (Integer) e.nextElement();

				data.pnTypeIndex[i] = tempInteger.intValue(); // 类型索引，存储类型id

				if (schema.cTypeDist.nMarkOrAmount == 0) {

					tempFloat = (Float) schema.cTypeDist.cDemand.get(tempInteger);

					data.pfTypeDemand[i] = tempFloat.floatValue(); // 类型需求，存储分数

				}

				else {

					tempInteger = (Integer) schema.cTypeDist.cDemand.get(tempInteger);

					data.pnTypeDemand[i] = tempInteger.intValue(); // 类型需求，存储数量

				}

				i++;

			}

		}

		else

			throw new PaperException("", "处理组卷需要的参数错误:必须选择出题类型");

		// 计算重要度总和

		Float floatTemp;

		Integer integerTemp;

		try {

			if (schema.cCognDist != null) {

				if (schema.cCognDist.nImport > 0) {

					data.nValueSum += schema.cCognDist.nImport;

					// 判断认知类型中是否有未设定分数项

					Enumeration e = schema.cCognDist.cDemand.keys();

					while (e.hasMoreElements()) {

						if (schema.cCognDist.nMarkOrAmount == 0) {

							floatTemp = (Float) (schema.cCognDist.cDemand.get(e.nextElement()));

							if (floatTemp.floatValue() <= 0)

								data.bCognHasFree = true;

						}

						else {

							integerTemp = (Integer) (schema.cCognDist.cDemand.get(e.nextElement()));

							if (integerTemp.intValue() <= 0)

								data.bCognHasFree = true;

						}

					}

				}

			}

			if (schema.cDiffDist != null) {

				if (schema.cDiffDist.nImport > 0) {

					data.nValueSum += schema.cDiffDist.nImport;

					Enumeration e = schema.cDiffDist.cDemand.keys();

					while (e.hasMoreElements()) {

						if (schema.cDiffDist.nMarkOrAmount == 0) {

							floatTemp = (Float) (schema.cDiffDist.cDemand.get(e.nextElement()));

							if (floatTemp.floatValue() <= 0)

								data.bDiffHasFree = true;

						}

						else {

							integerTemp = (Integer) (schema.cDiffDist.cDemand.get(e.nextElement()));

							if (integerTemp.intValue() <= 0)

								data.bDiffHasFree = true;

						}

					}

				}

			}

			if (schema.cFullMark != null)

				data.nValueSum += schema.cFullMark.nImport;

			// System.out.println("begin");

			// System.out.println(schema.cKnowDist.cDemand);

			// System.out.println("end");

			if (schema.cKnowDist != null) {

				if (schema.cKnowDist.nImport > 0) {

					data.nValueSum += schema.cKnowDist.nImport;

					Enumeration e = schema.cKnowDist.cDemand.keys();

					while (e.hasMoreElements()) {

						if (schema.cKnowDist.nMarkOrAmount == 0) {

							floatTemp = (Float) (schema.cKnowDist.cDemand.get(e.nextElement()));

							if (floatTemp.floatValue() <= 0)

								data.bKnowHasFree = true;

						}

						else {

							integerTemp = (Integer) (schema.cKnowDist.cDemand.get(e.nextElement()));

							if (integerTemp.intValue() <= 0)

								data.bKnowHasFree = true;

						}

					}

				}

			}

			if (schema.cTotalTime != null)

				data.nValueSum += schema.cTotalTime.nImport;

			if (schema.cTypeDist != null) {

				if (schema.cTypeDist.nImport > 0) {

					data.nValueSum += schema.cTypeDist.nImport;

					Enumeration e = schema.cTypeDist.cDemand.keys();

					while (e.hasMoreElements()) {

						if (schema.cTypeDist.nMarkOrAmount == 0) {

							floatTemp = (Float) (schema.cTypeDist.cDemand.get(e.nextElement()));

							if (floatTemp.floatValue() <= 0)

								data.bTypeHasFree = true;

						}

						else {

							integerTemp = (Integer) (schema.cTypeDist.cDemand.get(e.nextElement()));

							// System.out.println("2:"+integerTemp);

							if (integerTemp.intValue() <= 0)

								data.bTypeHasFree = true;

						}

					}

				}

			}

		} catch (Exception e) {

			e.printStackTrace();
			throw new PaperException(e.getMessage(), "处理组卷需要的参数错误:计算重要度");

		}

		return true;

	}

	// 根据生成的sql语句查询数据库并对结果进行处理

	private boolean loadItems() throws PaperException {

		dealSchema();

		String sql = makeSql();

		List list = new ArrayList();

		try {

			PaperAdminDAO dao = new PaperAdminDAOImp();
			// System.out.println("sql:::"+sql);
			// list=dao.getItemInfos(sql);
			list = dao.getItemInfosLjw(sql, schema.nRelativite);
			if (list.isEmpty()) {
				throw new PaperException("", "抽取的题目数目为0，请修改组卷策略！");
			}
		} catch (Exception e) {
			throw new PaperException(e.getMessage(), "没有找到符合过滤条件的题目，请检查组卷策略的试题过滤条件！");
		}

		try {

			data.nItemTotleNum = list.size(); // 记录题目总数

			if (data.nItemTotleNum > 0) {

				for (int i = 0; i < data.nItemTotleNum; i++)
					// 生成空白染色体

					data.sNull.append('0');

				data.pItem = new ItemInfo[data.nItemTotleNum];

				Iterator iterator = list.iterator();

				int i = 0;

				while (iterator.hasNext()) {

					data.pItem[i] = (ItemInfo) (iterator.next());

					i++;

				}

				list.clear(); // 清除

				// 处理数据库，使种群的初始化更加有效

				int nAdd = 0;

				ItemInfo iTest[] = new ItemInfo[data.nItemTotleNum]; // 测试用临时数据库

				if (iTest != null && data.pfTypeAve != null) {

					int nCount = 0;

					for (int j = 0; j < data.nTypeNum; j++) {

						float fScore = 0.0f; // 类型总分数统计

						nCount = 0; // 类型总数目统计

						data.pnTypeMark[j] = 0; //

						for (i = 0; i < data.nItemTotleNum; i++)

							if (data.pItem[i].nPraxisTypeID == data.pnTypeIndex[j]) {

								iTest[nAdd] = data.pItem[i];

								nAdd++;

								data.pnTypeMark[j]++;

								fScore += data.pItem[i].fSuggScore;

								nCount++;

							}

						data.pfTypeAve[j] = fScore / nCount; // 统计类型的平均分数

						data.fScoreAve += fScore;

					}

					data.fScoreAve /= data.nItemTotleNum; // 计算总的平均分

					// 统计没有指定分数或者个数的类型

					int nNum = 0;

					if (schema.cTypeDist.nMarkOrAmount == 0) {

						for (i = 0; i < data.nTypeNum; i++)

							if (data.pfTypeDemand[i] == -1)

								nNum++;

					}

					else {

						for (i = 0; i < data.nTypeNum; i++)

							if (data.pnTypeDemand[i] == -1) // 没有指定个数的类型

								nNum++;

					}

					// 如果全部指定、有没有指定的，计算不同的情况

					if (nNum == 0) {

						if (schema.cTypeDist.nMarkOrAmount == 0) {

							for (i = 0; i < data.nTypeNum; i++) {

								float tempFloat = data.pfTypeDemand[i] / data.pfTypeAve[i];

								if (tempFloat - (int) tempFloat >= 0.5)

									data.pnTypeCount[i] = (int) tempFloat + 1;

								else

									data.pnTypeCount[i] = (int) tempFloat;

							}

						}

						else {

							for (i = 0; i < data.nTypeNum; i++)

								data.pnTypeCount[i] = data.pnTypeDemand[i];

						}

					}

					else {

						// data.bFree=true; //存在没有指定分数的类型

						float tempFloat = 0.0f;

						if (schema.cTypeDist.nMarkOrAmount == 0) {

							for (int j = 0; j < data.nTypeNum; j++) {

								if (data.pfTypeDemand[i] != -1)

									tempFloat += data.pfTypeDemand[i];

							}

							if ((float) schema.cFullMark.nDemand > tempFloat) {

								tempFloat = (schema.cFullMark.nDemand - tempFloat) / nNum;

								for (int j = 0; j < data.nTypeNum; j++) {

									float tempNumFloat;

									if (data.pfTypeDemand[j] != -1) {

										tempNumFloat = data.pfTypeDemand[j] / data.pfTypeAve[j];

										if (tempNumFloat - (int) tempNumFloat >= 0.5)

											data.pnTypeCount[j] = (int) tempNumFloat + 1;

										else

											data.pnTypeCount[j] = (int) tempNumFloat;

									}

									else {

										tempNumFloat = tempFloat / data.pfTypeAve[j];

										if (tempNumFloat - (int) tempNumFloat >= 0.5)

											data.pnTypeCount[j] = (int) tempNumFloat + 1;

										else

											data.pnTypeCount[j] = (int) tempNumFloat;

									}

								}

							}

						}

						else {

							// 已经分配分数类型的分数和

							for (int j = 0; j < data.nTypeNum; j++) {

								if (data.pnTypeDemand[j] != -1)

									tempFloat += data.pfTypeAve[j] * data.pnTypeDemand[j];

							}

							if ((float) schema.cFullMark.nDemand > tempFloat) {

								tempFloat = (schema.cFullMark.nDemand - tempFloat) / nNum; // 平均分配分数给其他类型

								// 计算分到的题目数

								for (int j = 0; j < data.nTypeNum; j++) {

									float tempNumFloat;

									if (data.pnTypeDemand[j] != -1)

										data.pnTypeCount[j] = data.pnTypeCount[j];

									else {

										tempNumFloat = tempFloat / data.pfTypeAve[j];

										if (tempNumFloat - (int) (tempNumFloat) >= 0.5)

											data.pnTypeCount[j] = (int) tempNumFloat + 1;

										else

											data.pnTypeCount[j] = (int) tempNumFloat;

									}

								}

							}

						}

					}

					// 得到重新排列过的题目

					for (i = 0; i < data.nItemTotleNum; i++)

						data.pItem[i] = iTest[i];

				}

				else {

					return false;

				}

			}

			return true;

		} catch (Exception e) {

			throw new PaperException(e.getMessage(), "抽取和处理试题错误！");

		}

	}

	// 初始化群体

	private boolean setUp(int number) throws PaperException {

		try {

			Random rand = new Random();

			if (data.pMain == null) {

				data.pMain = new StringBuffer[Constants.FarmLen];

				data.pMainFit = new float[Constants.FarmLen];

				if (data.pMain == null && data.pMainFit == null)

					return false;

			}

			int nTemp;

			// 按类型分布初始化群体

			for (int i = Constants.FarmLen - 1; i >= Constants.FarmLen - number; i--) {

				data.pMainFit[i] = 0;

				data.pMain[i] = new StringBuffer(data.sNull.toString());

				int nMark = 0;

				for (int j = 0; j < schema.cTypeDist.cDemand.size(); j++) {

					for (int k = 0; k < data.pnTypeCount[j]; k++) {

						if (data.pnTypeMark[j] > 0) {

							nTemp = nMark + (int) (data.pnTypeMark[j] * rand.nextFloat());

							if (nTemp < data.nItemTotleNum)

								data.pMain[i].setCharAt(nTemp, '1');

						}

					}

					nMark += data.pnTypeMark[j];

				}

			}

			return true;

		} catch (Exception e) {

			throw new PaperException(e.getMessage(), "初始化群体错误！");

		}

	}

	// 计算个体适应值

	private float countFit(StringBuffer seed) throws PaperException {

		float fitness = 0;

		int nPos;

		int nMark = 1; // -1为题目数

		int nId[] = new int[data.nItemTotleNum + 1]; // 内容为题目id的值

		String sSeed;

		sSeed = seed.toString();

		nId[0] = 0;

		if (nId != null) {

			// 测试种子，得到题目的id

			do {

				nPos = sSeed.indexOf('1');

				if (nPos != -1) {

					nId[nMark] = nId[nMark - 1] + nPos + 1;

					sSeed = sSeed.substring(nPos + 1);

					nMark++;

				}

			} while (nPos != -1);

			for (int i = 0; i < nMark; i++)

				nId[i] -= 1;

			// 计算个体的适应值

			/*
			 * if(schema.nRelativite==1){ boolean bIdentified=true; //是否有相同的识别号 for(int i=1;i<nMark;i++){
			 * if(data.pItem[nId[i]].nRelaModulus!=0) //默认不会重复的识别号为 0 for(int j=i+1;j<nMark;j++) if
			 * (data.pItem[nId[j]].nRelaModulus==data.pItem[nId[i]].nRelaModulus) bIdentified=false; } if(!bIdentified)
			 * return Constants.BAD; //给予最大惩罚 }
			 */

			// 总分、时间、题目数
			float score = 0.0f;

			int time = 0;

			for (int i = 1; i < nMark; i++) {

				score += data.pItem[nId[i]].fSuggScore;

				time += data.pItem[nId[i]].nSuggTime;

			}

			// 分数复合程度计算

			if (schema.cFullMark != null) {

				if (schema.cFullMark.nImport != 0) {

					if ((int) score == schema.cFullMark.nDemand)

						fitness += score;

					fitness -= (Math.abs(score - schema.cFullMark.nDemand)) * 2 *

					schema.cFullMark.nImport;

				}

			}

			// 时间符合程度计算

			if (schema.cTotalTime != null) {

				if (schema.cTotalTime.nImport != 0) {

					fitness -= (Math.abs(time - schema.cTotalTime.nDemand)) / 30 *

					schema.cTotalTime.nImport;

				}

			}

			// 能力要求分值比例计算

			// 知识点

			// 题型

			// NDXS

			Hashtable htCogn = null;

			if (schema.cCognDist != null)

				htCogn = new Hashtable();

			Hashtable htType = null;

			if (schema.cTypeDist != null)

				htType = new Hashtable();

			Hashtable htDiff = null;

			if (schema.cDiffDist != null)

				htDiff = new Hashtable();

			Hashtable htKnow = null;

			if (schema.cKnowDist != null)

				htKnow = new Hashtable();

			// /////统计各方面的数据

			float fTemp = 0;

			int nTemp;

			String sTemp;

			for (int i = 1; i < nMark; i++) {

				// 认知度

				if (schema.cCognDist != null) {

					if (schema.cCognDist.nMarkOrAmount == 0) { // 按分值

						Float tempFloat = (Float) htCogn.get(data.pItem[nId[i]].strCognizetype);

						if (tempFloat != null)

							fTemp = tempFloat.floatValue();

						else

							fTemp = 0;

						fTemp += data.pItem[nId[i]].fSuggScore;

						htCogn.put(new String(data.pItem[nId[i]].strCognizetype),

						new Float(fTemp));

					}

					else { // 按数量

						Integer tempInteger = (Integer) htCogn.get(data.pItem[nId[i]].strCognizetype);

						if (tempInteger != null)

							nTemp = tempInteger.intValue();

						else

							nTemp = 0;

						nTemp++;

						htCogn.put(new String(data.pItem[nId[i]].strCognizetype),

						new Integer(nTemp));

					}

				}

				// 难度

				if (schema.cDiffDist != null) {

					if (schema.cDiffDist.nMarkOrAmount == 0) {

						Float tempFloat = (Float) htDiff.get(data.pItem[nId[i]].strDiff);

						if (tempFloat != null)

							fTemp = tempFloat.floatValue();

						else

							fTemp = 0;

						fTemp += data.pItem[nId[i]].fSuggScore;

						htDiff.put(new String(data.pItem[nId[i]].strDiff),

						new Float(fTemp));

					}

					else {

						Integer tempInteger = (Integer) htDiff.get(data.pItem[nId[i]].strDiff);

						if (tempInteger != null)

							nTemp = tempInteger.intValue();

						else

							nTemp = 0;

						nTemp++;

						htDiff.put(new String(data.pItem[nId[i]].strDiff),

						new Integer(nTemp));

					}

				}

				if (schema.cTypeDist != null) {

					// 类型

					if (schema.cTypeDist.nMarkOrAmount == 0) {

						Float tempFloat = (Float) htType.get(new Integer(data.pItem[nId[i]].nPraxisTypeID));

						if (tempFloat != null)

							fTemp = tempFloat.floatValue();

						else

							fTemp = 0;

						fTemp += data.pItem[nId[i]].fSuggScore;

						htType.put(new Integer(data.pItem[nId[i]].nPraxisTypeID),

						new Float(fTemp));

					}

					else {

						Integer tempInteger = (Integer) htType.get(new Integer(data.pItem[nId[i]].nPraxisTypeID));

						if (tempInteger != null)

							nTemp = tempInteger.intValue();

						else

							nTemp = 0;

						nTemp++;

						htType.put(new Integer(data.pItem[nId[i]].nPraxisTypeID),

						new Integer(nTemp));

					}

				}

				// 知识点

				if (schema.cKnowDist != null) {

					if (schema.cKnowDist.nMarkOrAmount == 0) {

						Float tempFloat = (Float) htKnow.get(new Integer(data.pItem[nId[i]].nKnowID));

						if (tempFloat != null)

							fTemp = tempFloat.floatValue();

						else

							fTemp = 0;

						fTemp += data.pItem[nId[i]].fSuggScore;

						htKnow.put(new Integer(data.pItem[nId[i]].nKnowID),

						new Float(fTemp));

					}

					else {

						Integer tempInteger = (Integer) htKnow.get(new Integer(data.pItem[nId[i]].nKnowID));

						if (tempInteger != null)

							nTemp = tempInteger.intValue();

						else

							nTemp = 0;

						nTemp++;

						htKnow.put(new Integer(data.pItem[nId[i]].nKnowID),

						new Integer(nTemp));

					}

				}

			}

			// 知识点分数分布，统计后的再处理，与需求样式匹配

			/*
			 * float fTempScore; int nTempScore; Hashtable htKnowComp=null; if (schema.cKnowDist!=null){ htKnowComp=new
			 * Hashtable(); htKnowComp=(Hashtable)(schema.cKnowDist.cDemand.clone()); Enumeration e=htKnowComp.keys();
			 * while (e.hasMoreElements()){ Vector vector=(Vector)e.nextElement(); fTempScore=0; nTempScore=0;
			 * Enumeration ee=vector.elements(); while (ee.hasMoreElements()){ if (schema.cKnowDist.nMarkOrAmount==0){
			 * Float ff=(Float)htKnow.get(ee.nextElement()); if (ff!=null) fTempScore+=ff.floatValue(); } else { Integer
			 * ii=(Integer)htKnow.get(ee.nextElement()); if (ii!=null) nTempScore+=ii.intValue(); } } if
			 * (schema.cKnowDist.nMarkOrAmount==0) htKnowComp.put(vector,new Float(fTempScore)); else
			 * htKnowComp.put(vector,new Integer(nTempScore)); } }
			 */

			// System.out.println(htKnow);
			// System.out.println(htKnowComp);
			// System.out.println(schema.cKnowDist.cDemand);
			Float tempFloat = null;

			Integer tempInteger = null;

			// 认知复合程度计算

			float fCogn;

			float fTempFitness = 0;

			int nCogn;

			if (schema.cCognDist != null) {

				if (schema.cCognDist.nImport != 0) {

					Enumeration e = schema.cCognDist.cDemand.keys();

					if (schema.cCognDist.nMarkOrAmount == 0) {

						while (e.hasMoreElements()) {

							sTemp = (String) e.nextElement();

							fCogn = ((Float) schema.cCognDist.cDemand.get(sTemp)).floatValue();

							if (fCogn >= 0.0f) {

								tempFloat = (Float) htCogn.get(sTemp);

								fTemp = (tempFloat != null) ? tempFloat.floatValue() : 0;

								fTempFitness -= (Math.abs(fCogn - fTemp)) * 2;

							}

						}

					}

					else {

						while (e.hasMoreElements()) {

							sTemp = (String) e.nextElement();

							nCogn = ((Integer) schema.cCognDist.cDemand.get(sTemp)).intValue();

							if (nCogn >= 0) {

								tempInteger = (Integer) htCogn.get(sTemp);

								nTemp = (tempInteger != null) ? tempInteger.intValue() : 0;

								fCogn = (nCogn - nTemp) * data.fScoreAve;

								fTempFitness -= (Math.abs(fCogn)) * 2;

							}

						}

					}

					fitness += fTempFitness * schema.cCognDist.nImport;

				}

			}

			// 难度复合程度计算

			float fDiff;

			int nDiff;

			if (schema.cDiffDist != null) {

				if (schema.cDiffDist.nImport != 0) {

					Enumeration e = schema.cDiffDist.cDemand.keys();

					if (schema.cDiffDist.nMarkOrAmount == 0) {

						while (e.hasMoreElements()) {

							sTemp = (String) e.nextElement();

							fDiff = ((Float) schema.cDiffDist.cDemand.get(sTemp)).floatValue();

							if (fDiff >= 0.0f) {

								tempFloat = (Float) htDiff.get(sTemp);

								fTemp = (tempFloat != null) ? tempFloat.floatValue() : 0;

								fTempFitness -= (Math.abs(fTemp - fDiff)) * 2;

							}

						}

					}

					else {

						while (e.hasMoreElements()) {

							sTemp = (String) e.nextElement();

							nDiff = ((Integer) schema.cDiffDist.cDemand.get(sTemp)).intValue();

							if (nDiff >= 0) {

								tempInteger = (Integer) htDiff.get(sTemp);

								nTemp = (tempInteger != null) ? tempInteger.intValue() : 0;

								fDiff = (nTemp - nDiff) * data.fScoreAve;

								fTempFitness -= (Math.abs(fDiff)) * 2;

							}

						}

					}

					fitness += fTempFitness * schema.cDiffDist.nImport;

				}

			}

			// 类型符合程度计算

			float fType;

			int nType;

			Integer intTemp;

			if (schema.cTypeDist != null) {

				if (schema.cTypeDist.nImport != 0) {

					Enumeration e = schema.cTypeDist.cDemand.keys();

					if (schema.cTypeDist.nMarkOrAmount == 0) {

						while (e.hasMoreElements()) {

							intTemp = (Integer) e.nextElement();

							fType = ((Float) schema.cTypeDist.cDemand.get(intTemp)).floatValue();

							if (fType >= 0) {

								tempFloat = (Float) htType.get(intTemp);

								fTemp = (tempFloat != null) ? tempFloat.floatValue() : 0;

								fTempFitness -= (Math.abs(fTemp - fType)) * 2;

							}

						}

					}

					else {

						while (e.hasMoreElements()) {

							intTemp = (Integer) e.nextElement();

							nType = ((Integer) schema.cTypeDist.cDemand.get(intTemp)).intValue();

							if (nType >= 0) {

								tempInteger = (Integer) htType.get(intTemp);

								nTemp = (tempInteger != null) ? tempInteger.intValue() : 0;

								fType = (nTemp - nType) * data.fScoreAve;

								fTempFitness -= (Math.abs(fType)) * 2;

							}

						}

					}

					fitness += fTempFitness * schema.cTypeDist.nImport;

				}

			}

			// 知识点符合程度的计算

			float fKnow;

			int nKnow;

			float fHelper;

			int nHelper;

			if (schema.cKnowDist != null) {

				if (schema.cKnowDist.nImport != 0) {

					Enumeration e = schema.cKnowDist.cDemand.keys();

					if (schema.cKnowDist.nMarkOrAmount == 0) {

						while (e.hasMoreElements()) {

							Vector vector = (Vector) e.nextElement();

							fKnow = ((Float) schema.cKnowDist.cDemand.get(vector)).floatValue();

							if (fKnow >= 0) {

								fTemp = 0.0f;

								Enumeration e2 = htKnow.keys();

								while (e2.hasMoreElements()) {

									intTemp = (Integer) e2.nextElement();

									if (vector.contains(intTemp)) {

										tempFloat = (Float) htKnow.get(intTemp);

										fHelper = (tempFloat != null) ? tempFloat.floatValue() : 0;

										fTemp += fHelper;

									}

								}

								fTempFitness -= (Math.abs(fTemp - fKnow)) * 2;

							}

						}

					}

					else {

						while (e.hasMoreElements()) {

							Vector vector = (Vector) e.nextElement();

							nKnow = ((Integer) schema.cKnowDist.cDemand.get(vector)).intValue();

							if (nKnow >= 0) {

								nTemp = 0;

								Enumeration e2 = htKnow.keys();

								while (e2.hasMoreElements()) {

									intTemp = (Integer) e2.nextElement();

									if (vector.contains(intTemp)) {

										tempInteger = (Integer) htKnow.get(intTemp);

										nHelper = (tempInteger != null) ? tempInteger.intValue() : 0;

										nTemp += nHelper;

									}

								}

								fKnow = (nTemp - nKnow) * data.fScoreAve;

								fTempFitness -= (Math.abs(fKnow)) * 2;

							}

						}

					}

					fitness += fTempFitness * schema.cKnowDist.nImport;

				}

			}

			return fitness;

		}

		else {

			throw new PaperException("", "适应值函数内存错误");

		}

	}

	// 选择操作

	private StringBuffer select() throws PaperException {

		float fTemp;

		int nMark = 0;

		StringBuffer sTemp;

		StringBuffer sCopy[] = new StringBuffer[Constants.FarmLen];

		try {

			for (int i = 0; i < Constants.FarmLen; i++)

				data.pMainFit[i] = countFit(data.pMain[i]);

			// 冒泡排序，要改为快速排序

			for (int i = 0; i < Constants.FarmLen; i++) {

				for (int j = i + 1; j < Constants.FarmLen; j++) {

					if (data.pMainFit[i] < data.pMainFit[j]) {

						fTemp = data.pMainFit[i];

						sTemp = data.pMain[i];

						data.pMainFit[i] = data.pMainFit[j];

						data.pMain[i] = data.pMain[j];

						data.pMainFit[j] = fTemp;

						data.pMain[j] = sTemp;

					}

				}

			}

			int nTemp;

			sTemp = data.pMain[0];

			data.fBsfTurn = data.pMainFit[0]; // 记录本轮最佳个体

			for (int i = 0; i < Constants.FarmLen; i++) {

				nTemp = (int) (Math.exp(0.02 * i)); // 带值排列后按指数比例选择

				int j;

				for (j = 0; j < nTemp; j++) {

					if ((j + nMark) < Constants.FarmLen)

						sCopy[j + nMark] = data.pMain[i];

				}

				nMark += j;

				if (nMark >= Constants.FarmLen)

					i = Constants.FarmLen;

			}

			for (int i = 0; i < Constants.FarmLen; i++)

				data.pMain[i] = sCopy[i];

			return sTemp; // 返回最佳个体

		} catch (Exception e) {

			throw new PaperException(e.getMessage(), "选择操作发生错误！");

		}

	}

	// 交换操作

	private void crossover() throws PaperException {

		try {

			int nDouble[] = new int[Constants.FarmLen];

			String sTemp, sTemp1;

			StringBuffer sbTemp;

			int nTemp;

			// 随机配对 0-25,1-26,....24-49

			Random rand = new Random();

			for (int i = 0; i < Constants.FarmLen; i++)

				nDouble[i] = rand.nextInt();

			for (int i = 0; i < Constants.FarmLen; i++) {

				for (int j = i + 1; j < Constants.FarmLen; j++) {

					if (nDouble[i] < nDouble[j]) {

						nTemp = nDouble[i];

						sbTemp = data.pMain[i];

						nDouble[i] = nDouble[j];

						data.pMain[i] = data.pMain[j];

						nDouble[j] = nTemp;

						data.pMain[j] = sbTemp;

					}

				}

			}

			// 交换基因 考虑交换后的择优

			for (int i = 0; i < Constants.FarmLen / 2; i++) {

				if (rand.nextFloat() < Constants.PC) {

					// 交换前部分

					if (rand.nextFloat() > 0.5) {

						nTemp = (int) (data.nItemTotleNum * rand.nextFloat());

						if (nTemp != 0 && nTemp != data.nItemTotleNum) {

							sTemp = data.pMain[i].substring(nTemp);

							sbTemp = new StringBuffer(data.pMain[i].substring(0, nTemp)
									+ data.pMain[i + Constants.FarmLen / 2].substring(nTemp));

							data.pMain[i] = sbTemp;// data.pMain[i].substring(0,nTemp)+data.pMain[i+Constants.FarmLen/2].substring(nTemp);

							sbTemp = new StringBuffer(data.pMain[i + Constants.FarmLen / 2].substring(0, nTemp) + sTemp);

							data.pMain[i + Constants.FarmLen / 2] = sbTemp;// data.pMain[i+Constants.FarmLen/2].substring(0,nTemp)+sTemp;

						}

					}

					// 交换后部分

					else {

						nTemp = (int) (data.nItemTotleNum * rand.nextFloat());

						if (nTemp != 0 && nTemp != data.nItemTotleNum) {

							sTemp = data.pMain[i].substring(0, nTemp);

							sbTemp = new StringBuffer(data.pMain[i + Constants.FarmLen / 2].substring(0, nTemp)
									+ data.pMain[i].substring(nTemp));

							data.pMain[i] = sbTemp;// data.pMain[i+Constants.FarmLen/2].substring(0,nTemp)+data.pMain[i].substring(nTemp);

							sbTemp = new StringBuffer(sTemp + data.pMain[i + Constants.FarmLen / 2].substring(nTemp));

							data.pMain[i + Constants.FarmLen / 2] = sbTemp;// sTemp+data.pMain[i+Constants.FarmLen/2].substring(nTemp);

						}

					}

				}

			}

		} catch (Exception e) {

			throw new PaperException(e.getMessage(), "交换操作发生错误！");

		}

	}

	// 变异操作

	private void mutation(int nIndex) throws PaperException {

		int nPos;

		int nTemp1, nTemp2;

		int nMark = 1; // -1 为题目数

		int nId[] = new int[Constants.MAX_ITEMS + 1]; // 内容为题目的id

		String sTemp;

		StringBuffer sbSeed;

		Random rand = new Random();

		try {

			if (nId != null) {

				sTemp = data.pMain[nIndex].toString();

				nId[0] = 0;

				// 测试种子

				do {

					nPos = sTemp.indexOf('1');

					if (nPos != -1) {

						nId[nMark] = nId[nMark - 1] + nPos + 1;

						sTemp = sTemp.substring(nPos + 1);

						nMark++;

					}

				} while (nPos != -1);

				if (nMark != 1) {

					for (int i = 0; i < nMark; i++)

						nId[i] -= 1;

					// 变异在基因块内进行，为两点对换

					sbSeed = new StringBuffer(data.sNull.toString());

					nTemp1 = (int) ((nMark - 1) * rand.nextFloat()) + 1; // 1~nMark-1间的随机整数

					nPos = data.pItem[nId[nTemp1]].nPraxisTypeID;

					for (int i = 0; i < data.nTypeNum; i++)

						if (nPos == data.pnTypeIndex[i]) {

							nPos = i; // 转化为类型索引

							i = data.nTypeNum;

						}

					int nTemp3 = 0;

					for (int i = 0; i < nPos; i++)

						nTemp3 += data.pnTypeMark[i];

					nTemp2 = nTemp3 + (int) (data.pnTypeMark[nPos] * rand.nextFloat());

					if (data.pMain[nIndex].charAt(nTemp2) == '0')

						nId[nTemp1] = nTemp2;

					for (int i = 1; i < nMark; i++)

						sbSeed.setCharAt(nId[i], '1');

					if (countFit(sbSeed) > countFit(data.pMain[nIndex]))

						data.pMain[nIndex] = sbSeed;

				}

				else {// 要变异的字符串为空

				}

			}

			else {

				throw new PaperException("", "变异操作，内存错误");

				// this.ERR+="变异，内存错误";

			}

		} catch (Exception e) {

			throw new PaperException(e.getMessage(), "变异操作发生错误!");

		}

	}

	// 成卷

	private void publishPaper(StringBuffer sBestSeed) throws PaperException {

		String sPaper = new String();

		String sOK = sBestSeed.toString();

		int nPos;

		int nMark = 1; // -1 后为题目数量

		int nId[] = new int[data.nItemTotleNum + 1]; // 内容为题目的ID

		if (nId != null) {

			nId[0] = 0;

			// 测试染色体

			do {

				nPos = sOK.indexOf('1');

				if (nPos != -1) {

					nId[nMark] = nId[nMark - 1] + nPos + 1;

					sOK = sOK.substring(nPos + 1);

					nMark++;

				}

			} while (nPos != -1);

			data.result = new Hashtable();

			for (int i = 1; i < nMark; i++) {

				nId[i] -= 1;

				data.result.put(new Integer(data.pItem[nId[i]].nItemID), new Integer(data.pItem[nId[i]].nPraxisTypeID));

			}

			valuePaper(data.BestSeed); // 计算满意度

		}

		else

			throw new PaperException("", "生成试卷题目错误.");

	}

	// 评估组卷结果

	private void valuePaper(StringBuffer sBestSeed) throws PaperException {
		String sTempSeed = sBestSeed.toString();
		int nPos;
		int nMark = 1; // -1 为题目数量
		int nId[] = new int[Constants.MAX_ITEMS + 1];
		if (nId != null) {
			nId[0] = 0;
			do {
				nPos = sTempSeed.indexOf('1');
				if (nPos != -1) {
					nId[nMark] = nId[nMark - 1] + nPos + 1;
					sTempSeed = sTempSeed.substring(nPos + 1);
					nMark++;
				}
			} while (nPos != -1);
			for (int i = 0; i < nMark; i++)
				nId[i] -= 1;
			int time = 0;
			float score = 0.0f;
			for (int i = 1; i < nMark; i++) {
				score += data.pItem[nId[i]].fSuggScore;
				time += data.pItem[nId[i]].nSuggTime;
			}
			schema.cFullMark.nFact = (int) score;
			schema.cTotalTime.nFact = time;
			int nTempAccording = 0;
			// 分数满意度
			if (schema.cFullMark != null) {
				if (schema.cFullMark.nImport != 0) {
					nPos = (schema.cFullMark.nDemand >= schema.cFullMark.nFact) ? (int) ((float) schema.cFullMark.nFact
							/ schema.cFullMark.nDemand * 100) : (int) ((float) (Math.abs(2 * schema.cFullMark.nDemand
							- schema.cFullMark.nFact))
							/ schema.cFullMark.nDemand * 100);
					nTempAccording += nPos * schema.cFullMark.nImport;
					schema.cFullMark.nAccording = nPos;
				}
			}
			// 时间满意度
			if (schema.cTotalTime != null) {
				if (schema.cTotalTime.nImport != 0) {
					nPos = (schema.cTotalTime.nDemand >= schema.cTotalTime.nFact) ? (int) ((float) schema.cTotalTime.nFact
							/ schema.cTotalTime.nDemand * 100)
							: (int) ((float) (Math.abs(2 * schema.cTotalTime.nDemand - schema.cTotalTime.nFact))
									/ schema.cTotalTime.nDemand * 100);
					nTempAccording += nPos * schema.cTotalTime.nImport;
					schema.cTotalTime.nAccording = nPos;
				}
			}
			Hashtable htCogn = null;
			if (schema.cCognDist != null) {
				htCogn = new Hashtable();
				schema.cCognDist.cFact = new Hashtable();
			}
			Hashtable htType = null;
			if (schema.cTypeDist != null) {
				htType = new Hashtable();
				schema.cTypeDist.cFact = new Hashtable();
			}
			Hashtable htDiff = null;
			if (schema.cDiffDist != null) {
				htDiff = new Hashtable();
				schema.cDiffDist.cFact = new Hashtable();
			}
			Hashtable htKnow = null;
			if (schema.cKnowDist != null) {
				htKnow = new Hashtable();
				schema.cKnowDist.cFact = new Hashtable();
			}
			// /////统计各方面的数据
			float fTemp = 0;
			int nTemp = 0;
			String sTemp;
			for (int i = 1; i < nMark; i++) {
				// 认知度
				if (schema.cCognDist != null) {
					if (schema.cCognDist.nMarkOrAmount == 0) { // 按分值
						Float tempFloat = (Float) htCogn.get(data.pItem[nId[i]].strCognizetype);
						if (tempFloat != null)
							fTemp = tempFloat.floatValue();
						else
							fTemp = 0;
						fTemp += data.pItem[nId[i]].fSuggScore;
						htCogn.put(new String(data.pItem[nId[i]].strCognizetype), new Float(fTemp));
						// 写组卷的实际结果
						schema.cCognDist.cFact.put(new String(data.pItem[nId[i]].strCognizetype), new Float(fTemp));
					} else { // 按数量
						Integer tempInteger = (Integer) htCogn.get(data.pItem[nId[i]].strCognizetype);
						if (tempInteger != null)
							nTemp = tempInteger.intValue();
						else
							nTemp = 0;
						nTemp++;
						htCogn.put(new String(data.pItem[nId[i]].strCognizetype), new Integer(nTemp));
						// 写组卷的实际结果
						schema.cCognDist.cFact.put(new String(data.pItem[nId[i]].strCognizetype), new Integer(nTemp));
					}
				}
				// 难度
				if (schema.cDiffDist != null) {
					if (schema.cDiffDist.nMarkOrAmount == 0) {
						Float tempFloat = (Float) htDiff.get(data.pItem[nId[i]].strDiff);
						if (tempFloat != null)
							fTemp = tempFloat.floatValue();
						else
							fTemp = 0;
						fTemp += data.pItem[nId[i]].fSuggScore;
						htDiff.put(new String(data.pItem[nId[i]].strDiff), new Float(fTemp));
						schema.cDiffDist.cFact.put(new String(data.pItem[nId[i]].strDiff), new Float(fTemp));
					} else {
						Integer tempInteger = (Integer) htDiff.get(data.pItem[nId[i]].strDiff);
						if (tempInteger != null)
							nTemp = tempInteger.intValue();
						else
							nTemp = 0;
						nTemp++;
						htDiff.put(new String(data.pItem[nId[i]].strDiff), new Integer(nTemp));
						schema.cDiffDist.cFact.put(new String(data.pItem[nId[i]].strDiff), new Integer(nTemp));
					}
				}
				if (schema.cTypeDist != null) {
					// 类型
					if (schema.cTypeDist.nMarkOrAmount == 0) {
						Float tempFloat = (Float) htType.get(new Integer(data.pItem[nId[i]].nPraxisTypeID));
						if (tempFloat != null)
							fTemp = tempFloat.floatValue();
						else
							fTemp = 0;
						fTemp += data.pItem[nId[i]].fSuggScore;
						htType.put(new Integer(data.pItem[nId[i]].nPraxisTypeID), new Float(fTemp));
						schema.cTypeDist.cFact.put(new Integer(data.pItem[nId[i]].nPraxisTypeID), new Float(fTemp));
					} else {
						Integer tempInteger = (Integer) htType.get(new Integer(data.pItem[nId[i]].nPraxisTypeID));
						if (tempInteger != null)
							nTemp = tempInteger.intValue();
						else
							nTemp = 0;
						nTemp++;
						htType.put(new Integer(data.pItem[nId[i]].nPraxisTypeID), new Integer(nTemp));
						schema.cTypeDist.cFact.put(new Integer(data.pItem[nId[i]].nPraxisTypeID), new Integer(nTemp));
					}
				}
				// 知识点
				if (schema.cKnowDist != null) {
					if (schema.cKnowDist.nMarkOrAmount == 0) {
						Float tempFloat = (Float) htKnow.get(new Integer(data.pItem[nId[i]].nKnowID));
						if (tempFloat != null)
							fTemp = tempFloat.floatValue();
						else
							fTemp = 0;
						fTemp += data.pItem[nId[i]].fSuggScore;
						htKnow.put(new Integer(data.pItem[nId[i]].nKnowID), new Float(fTemp));
					} else {
						Integer tempInteger = (Integer) htKnow.get(new Integer(data.pItem[nId[i]].nKnowID));
						if (tempInteger != null)
							nTemp = tempInteger.intValue();
						else
							nTemp = 0;
						nTemp++;
						htKnow.put(new Integer(data.pItem[nId[i]].nKnowID), new Integer(nTemp));
					}
				}
			}

			// 知识点分数分布，统计后的再处理，与需求样式匹配
			float fTempScore;
			int nTempScore;
			if (schema.cKnowDist != null) {
				if (schema.cKnowDist.cFact == null)
					schema.cKnowDist.cFact = new Hashtable();
				schema.cKnowDist.cFact = (Hashtable) (schema.cKnowDist.cDemand.clone());
				Enumeration e = schema.cKnowDist.cFact.keys();
				while (e.hasMoreElements()) {
					Vector vector = (Vector) e.nextElement();
					fTempScore = 0;
					nTempScore = 0;
					Enumeration ee = vector.elements();
					while (ee.hasMoreElements()) {
						if (schema.cKnowDist.nMarkOrAmount == 0) {
							Float ff = (Float) htKnow.get(ee.nextElement());
							if (ff != null)
								fTempScore += ff.floatValue();
						} else {
							Integer ii = (Integer) htKnow.get(ee.nextElement());
							if (ii != null)
								nTempScore += ii.intValue();
						}
					}
					if (schema.cKnowDist.nMarkOrAmount == 0) {
						schema.cKnowDist.cFact.put(vector, new Float(fTempScore));
						// System.out.println("vector1111111::::"+vector);
						// System.out.println("fTempScore1111111::::"+fTempScore);
					} else {
						schema.cKnowDist.cFact.put(vector, new Integer(nTempScore));
						// System.out.println("vector2222::::"+vector);
						// System.out.println("fTempScore2222::::"+nTempScore);
					}
				}
			}
			Float tempFloat;
			Integer tempInteger;
			float fTempAccording = 0;
			// 认知复合程度计算
			float fCogn;
			int nCogn;
			// 决定满意度是累加还是逐减
			if (data.bCognHasFree)
				fTempAccording = schema.cFullMark.nDemand;
			else
				fTempAccording = 0;
			if (schema.cCognDist != null) {
				if (schema.cCognDist.nImport != 0) {
					Enumeration e = schema.cCognDist.cDemand.keys();
					if (schema.cCognDist.nMarkOrAmount == 0) {
						while (e.hasMoreElements()) {
							sTemp = (String) e.nextElement();
							fCogn = ((Float) schema.cCognDist.cDemand.get(sTemp)).floatValue();
							if (fCogn >= 0.0f) {
								tempFloat = (Float) htCogn.get(sTemp);
								fTemp = (tempFloat != null) ? tempFloat.floatValue() : 0;
								if (!(data.bCognHasFree))
									// fTempAccording+=(fCogn>fTemp)?fTemp:fCogn;
									fTempAccording += (fCogn > fTemp) ? (fCogn - fTemp) : 0;
								else
									fTempAccording -= (fCogn > fTemp) ? (fCogn - fTemp) : 0;
							}
						}
					} else {
						while (e.hasMoreElements()) {
							sTemp = (String) e.nextElement();
							nCogn = ((Integer) schema.cCognDist.cDemand.get(sTemp)).intValue();
							if (nCogn >= 0) {
								tempInteger = (Integer) htCogn.get(sTemp);
								nTemp = (tempInteger != null) ? tempInteger.intValue() : 0;
								if (!(data.bCognHasFree))
									// fTempAccording+=(nCogn>nTemp)?nTemp*data.fScoreAve:nCogn*data.fScoreAve;
									fTempAccording += (nCogn > nTemp) ? (nCogn - nTemp) * data.fScoreAve : 0;
								else
									fTempAccording -= (nCogn > nTemp) ? (nCogn - nTemp) * data.fScoreAve : 0;
							}
						}
					}
					/*
					 * if (fTempAccording<0) fTempAccording=0; if (fTempAccording<=schema.cFullMark.nDemand)
					 * fTempAccording=fTempAccording/schema.cFullMark.nDemand*100; else fTempAccording=100;
					 */
					if (!(data.bCognHasFree))
					// fTempAccording=(schema.cFullMark.nDemand-fTempAccording)/schema.cFullMark.nDemand*100;
					{
						if (schema.cFullMark.nDemand >= fTempAccording)
							fTempAccording = (schema.cFullMark.nDemand - fTempAccording) / schema.cFullMark.nDemand
									* 100;
						else
							fTempAccording = 0;
					} else {
						if (fTempAccording >= 0)
							fTempAccording = fTempAccording / schema.cFullMark.nDemand * 100;
						else
							fTempAccording = 0;
					}
					schema.cCognDist.nAccording = (int) fTempAccording;
					nTempAccording += (int) (fTempAccording * schema.cCognDist.nImport);
				}
			}
			// 难度复合程度计算
			float fDiff;
			int nDiff;
			if (data.bDiffHasFree)
				fTempAccording = schema.cFullMark.nDemand;
			else
				fTempAccording = 0;
			if (schema.cDiffDist != null) {
				if (schema.cDiffDist.nImport != 0) {
					Enumeration e = schema.cDiffDist.cDemand.keys();
					if (schema.cDiffDist.nMarkOrAmount == 0) {
						while (e.hasMoreElements()) {
							sTemp = (String) e.nextElement();
							fDiff = ((Float) schema.cDiffDist.cDemand.get(sTemp)).floatValue();
							if (fDiff >= 0.0f) {
								tempFloat = (Float) htDiff.get(sTemp);
								fTemp = (tempFloat != null) ? tempFloat.floatValue() : 0;
								if (!(data.bDiffHasFree))
									// fTempAccording+=(fDiff>fTemp)?fTemp:fDiff;
									fTempAccording += (fDiff > fTemp) ? (fDiff - fTemp) : 0;
								else
									fTempAccording -= (fDiff > fTemp) ? (fDiff - fTemp) : 0;
							}
						}
					} else {
						while (e.hasMoreElements()) {
							sTemp = (String) e.nextElement();
							nDiff = ((Integer) schema.cDiffDist.cDemand.get(sTemp)).intValue();
							if (nDiff >= 0) {
								tempInteger = (Integer) htDiff.get(sTemp);
								nTemp = (tempInteger != null) ? tempInteger.intValue() : 0;
								if (!(data.bDiffHasFree))
									// fTempAccording+=(nDiff>nTemp)?nTemp*data.fScoreAve:nDiff*data.fScoreAve;
									fTempAccording += (nDiff > nTemp) ? (nDiff - nTemp) * data.fScoreAve : 0;
								else
									fTempAccording -= (nDiff > nTemp) ? (nDiff - nTemp) * data.fScoreAve : 0;
							}
						}
					}
					/*
					 * if (fTempAccording<0) fTempAccording=0; if (fTempAccording<=schema.cFullMark.nDemand)
					 * fTempAccording=fTempAccording/schema.cFullMark.nDemand*100; else fTempAccording=100;
					 */
					if (!(data.bDiffHasFree))
					// fTempAccording=(schema.cFullMark.nDemand-fTempAccording)/schema.cFullMark.nDemand*100;
					{
						if (schema.cFullMark.nDemand >= fTempAccording)
							fTempAccording = (schema.cFullMark.nDemand - fTempAccording) / schema.cFullMark.nDemand
									* 100;
						else
							fTempAccording = 0;
					} else {
						if (fTempAccording >= 0)
							fTempAccording = fTempAccording / schema.cFullMark.nDemand * 100;
						else
							fTempAccording = 0;
					}
					schema.cDiffDist.nAccording = (int) fTempAccording;
					nTempAccording += (int) (fTempAccording * schema.cDiffDist.nImport);
				}
			}

			// 类型符合程度计算
			float fType;
			int nType;
			Integer intTemp;
			if (data.bTypeHasFree) {
				fTempAccording = schema.cFullMark.nDemand;
				// System.out.println("fTempAccording::::::::"+fTempAccording);
			} else
				fTempAccording = 0;
			if (schema.cTypeDist != null) {
				if (schema.cTypeDist.nImport != 0) {
					Enumeration e = schema.cTypeDist.cDemand.keys();
					// System.out.println("Enumeration::::::::"+e);
					if (schema.cTypeDist.nMarkOrAmount == 0) {// 0是分值
						while (e.hasMoreElements()) {
							intTemp = (Integer) e.nextElement();
							fType = ((Float) schema.cTypeDist.cDemand.get(intTemp)).floatValue();
							if (fType >= 0) {
								tempFloat = (Float) htType.get(intTemp);
								fTemp = (tempFloat != null) ? tempFloat.floatValue() : 0;
								if (!(data.bTypeHasFree))
									// fTempAccording+=(fType>fTemp)?fTemp:fType;
									fTempAccording += (fType > fTemp) ? (fType - fTemp) : 0;
								else
									fTempAccording -= (fType > fTemp) ? (fType - fTemp) : 0;
							}
						}
					} else {
						while (e.hasMoreElements()) {
							intTemp = (Integer) e.nextElement();
							nType = ((Integer) schema.cTypeDist.cDemand.get(intTemp)).intValue();
							if (nType >= 0) {
								tempInteger = (Integer) htType.get(intTemp);
								nTemp = (tempInteger != null) ? tempInteger.intValue() : 0;
								if (!(data.bTypeHasFree)) {
									// fTempAccording+=(nType>nTemp)?nTemp*data.fScoreAve:nType*data.fScoreAve;
									fTempAccording += (nType > nTemp) ? (nType - nTemp) * data.fScoreAve : 0;
									// System.out.println("nType***::"+nType);
									// System.out.println("nTemp***::"+nTemp);
									// System.out.println("1111111111***::"+fTempAccording);
								} else {
									fTempAccording -= (nType > nTemp) ? (nType - nTemp) * data.fScoreAve : 0;
									// System.out.println("2222222222222"+fTempAccording);
								}
							}
							// System.out.println("fTempAccording0000000000000000::::::::"+fTempAccording);
						}
					}
					// System.out.println("fTempAccording1111111111111111::::::::"+fTempAccording);
					/*
					 * if (fTempAccording<0) fTempAccording=0; if (fTempAccording<=schema.cFullMark.nDemand)
					 * fTempAccording=fTempAccording/schema.cFullMark.nDemand*100; else fTempAccording=100;
					 */
					if (!(data.bTypeHasFree)) {
						if (schema.cFullMark.nDemand >= fTempAccording)
							fTempAccording = (schema.cFullMark.nDemand - fTempAccording) / schema.cFullMark.nDemand
									* 100;
						else
							fTempAccording = 0;
					} else {
						if (fTempAccording >= 0)
							fTempAccording = fTempAccording / schema.cFullMark.nDemand * 100;
						else
							fTempAccording = 0;
					}
					// fTempAccording=(schema.cFullMark.nDemand-fTempAccording)/schema.cFullMark.nDemand*100;
					// System.out.println("fTempAccording22222222222222::::::::"+fTempAccording);
					schema.cTypeDist.nAccording = (int) fTempAccording;
					nTempAccording += (int) (fTempAccording * schema.cTypeDist.nImport);
				}
			}
			// 知识点符合程度的计算
			float fKnow;
			int nKnow;
			float fHelper;
			int nHelper;
			if (data.bKnowHasFree)
				fTempAccording = schema.cFullMark.nDemand;
			else
				fTempAccording = 0;
			if (schema.cKnowDist != null) {
				if (schema.cKnowDist.nImport != 0) {
					Enumeration e = schema.cKnowDist.cDemand.keys();
					if (schema.cKnowDist.nMarkOrAmount == 0) {
						while (e.hasMoreElements()) {
							Vector vector = (Vector) e.nextElement();
							fKnow = ((Float) schema.cKnowDist.cDemand.get(vector)).floatValue();
							if (fKnow >= 0) {
								fTemp = 0.0f;
								Enumeration e2 = htKnow.keys();
								while (e2.hasMoreElements()) {
									intTemp = (Integer) e2.nextElement();
									if (vector.contains(intTemp)) {
										tempFloat = (Float) htKnow.get(intTemp);
										fHelper = (tempFloat != null) ? tempFloat.floatValue() : 0;
										fTemp += fHelper;
									}
								}
								if (!(data.bKnowHasFree))
									// fTempAccording+=(fKnow>fTemp)?fTemp:fKnow;
									fTempAccording += (fKnow > fTemp) ? (fKnow - fTemp) : 0;
								else
									fTempAccording -= (fKnow > fTemp) ? (fKnow - fTemp) : 0;
							}
						}
					} else {
						while (e.hasMoreElements()) {
							Vector vector = (Vector) e.nextElement();
							nKnow = ((Integer) schema.cKnowDist.cDemand.get(vector)).intValue();
							if (nKnow >= 0) {
								nTemp = 0;
								Enumeration e2 = htKnow.keys();
								while (e2.hasMoreElements()) {
									intTemp = (Integer) e2.nextElement();
									if (vector.contains(intTemp)) {
										tempInteger = (Integer) htKnow.get(intTemp);
										nHelper = (tempInteger != null) ? tempInteger.intValue() : 0;
										nTemp += nHelper;
									}
								}
								/*
								 * if (!(data.bKnowHasFree))
								 * fTempAccording+=(nKnow>nTemp)?nTemp*data.fScoreAve:nKnow*data.fScoreAve; else
								 * fTempAccording-=(nKnow>nTemp)?(nKnow-nTemp)*data.fScoreAve:0;
								 */
								/* 061130修改满意度计算方法 */
								if (!(data.bKnowHasFree))
									fTempAccording += (nKnow > nTemp) ? (nKnow - nTemp) * data.fScoreAve : 0;
								else
									fTempAccording -= (nKnow > nTemp) ? (nKnow - nTemp) * data.fScoreAve : 0;
							}
						}
					}
					/*
					 * if (fTempAccording<0) fTempAccording=0; if (fTempAccording<=schema.cFullMark.nDemand)
					 * fTempAccording=fTempAccording/schema.cFullMark.nDemand*100; else fTempAccording=100;
					 */
					// fTempAccording=(schema.cFullMark.nDemand-fTempAccording)/schema.cFullMark.nDemand*100;
					if (!(data.bKnowHasFree))
					// fTempAccording=(schema.cFullMark.nDemand-fTempAccording)/schema.cFullMark.nDemand*100;
					{
						if (schema.cFullMark.nDemand >= fTempAccording)
							fTempAccording = (schema.cFullMark.nDemand - fTempAccording) / schema.cFullMark.nDemand
									* 100;
						else
							fTempAccording = 0;
					} else {
						if (fTempAccording >= 0)
							fTempAccording = fTempAccording / schema.cFullMark.nDemand * 100;
						else
							fTempAccording = 0;
					}

					schema.cKnowDist.nAccording = (int) fTempAccording;
					nTempAccording += (int) (fTempAccording * schema.cKnowDist.nImport);
				}
			}
			if (data.nValueSum != 0)
				nTempAccording = nTempAccording / data.nValueSum;
			else
				nTempAccording = 100;
			schema.nTotalAccording = nTempAccording;
		}
	}

	// 集成函数

	private boolean gaMain() throws PaperException {

		float fOld = 0, fNew;

		Random rand = new Random();

		long startTime = 0;

		startTime = System.currentTimeMillis();

		if (loadItems()) {

			if (setUp(Constants.FarmLen)) {

				for (int i = 0; i < Constants.MaxLoop; i++) {

					StringBuffer sTemp = select(); // data.BSF 为本轮最佳解

					// System.out.println(sTemp);

					// System.out.println(countFit(sTemp));

					if (data.fBsfBest < data.fBsfTurn || i == 0) {

						data.BestSeed = sTemp;

						data.fBsfBest = data.fBsfTurn;

					}

					// 判断早熟，部分替代原始种群的种子

					if (i % 30 == 1) {

						fNew = data.fBsfBest;

						if (fNew - fOld < 1) {

							setUp(Constants.FarmLen / 2);

							data.pMain[0] = data.BestSeed;

						}

						fOld = fNew;

					}

					crossover();

					boolean bExist = false;

					for (int j = 0; j < Constants.FarmLen; j++) {

						if (rand.nextFloat() < Constants.PM)

							mutation(j);

						if (data.pMain[j] == data.BestSeed)// 已经存在最佳解

							bExist = true;

					}

					if (!bExist)

						data.pMain[Constants.FarmLen - 1] = data.BestSeed;

				}

				publishPaper(data.BestSeed); // 成卷

				countFit(data.BestSeed);

				// System.out.println(data.fBsfBest);

				long endTime = 0;

				endTime = System.currentTimeMillis();

				// System.out.println("用时："+(endTime-startTime)+"ms");

			}

			else {

				throw new PaperException("", "gaMain：内存错误!");

			}

		}

		else {

			throw new PaperException("", "gaMain:数据库访问错误!");

		}

		return true;

	}

	public Hashtable generate(SchemaContent schema) throws PaperException {

		this.schema = schema;

		try {

			if (gaMain()) {

				return data.result;

			}

			else

				return null;

		} catch (PaperException e) {

			e.printStackTrace();
			throw new PaperException(e.getMessage(), e.message());

		}

	}

	public HashMap testHashMap() {
		Random rd = new Random();
		HashMap hm = new HashMap();
		hm.put(new Integer(0), "a");
		hm.put(new Integer(1), "b");
		hm.put(new Integer(2), "c");
		int a = rd.nextInt(3);
		// System.out.println("aaa::"+a);
		// System.out.println(hm.get(new Integer(a)));
		return hm;
	}

	public static void main(String[] args) {

		Publisher op = new Publisher();
		op.testHashMap();
	}

}