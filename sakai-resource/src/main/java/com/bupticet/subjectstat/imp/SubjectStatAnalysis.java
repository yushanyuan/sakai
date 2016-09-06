package com.bupticet.subjectstat.imp;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.bupticet.paperadmin.common.JNDINames;
import com.bupticet.paperadmin.common.ServiceLocator;
import com.googlecode.jsonplugin.JSONUtil;

public class SubjectStatAnalysis {
	private static String PREPATH = "E://";

	private static String SPLIT_CHAR = ";";

	private static int BATCH_SIZE = 1; //试卷路径的数量。

	public static float parseFloat(Object o) {
		float d = 0;
		try {
			d = Float.parseFloat(o.toString());
		} catch (Exception e) {
			System.out.println(o);
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return d;
	}

	public static long parseLong(Object o) {
		long d = 0;
		try {
			d = Long.parseLong(o.toString());
		} catch (Exception e) {
			System.out.println(o);
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return d;
	}

	/**
	 * 
	 * @param pathList:试卷或练习路径集合
	 * @return 试题统计结果（Map） 返回结果resultMap的结构如下： resultMap{id:rateMap{isobject:0,
	 *         rightRate:[{
	 *         count:n,averageScoringRate,m},{count:n,averageScoringRate,m},{}...]
	 *         averageRightRate:[{count:averageScoringRate},{count:averageScoringRate},{}...]
	 *         selectRate
	 *         :[{A:NUM,B:NUM,C:NUM,....},{A:NUM,B:NUM,C:NUM,....},{}...] } }
	 */
	public static Map doStatAnalysis(List pathList) {
		Map resultMap = new HashMap();
		for (Object path : pathList) {
			try {
				File subjectFile = new File(PREPATH + (String) path);
				if (subjectFile.exists()) {
					if (subjectFile.length() > 0) {
						Map paperAnswerMap = (Map) JSONUtil.deserialize(new java.io.FileReader(subjectFile));
						for (Object o : paperAnswerMap.values()) {
							Map praxisAnswerMap = (Map) o;
							List scores = new ArrayList();
							scores = (List) praxisAnswerMap.get("score");
							int i = scores.size();
							List studentScores = new ArrayList();
							studentScores = (List) praxisAnswerMap.get("studentScore");
							Map rateMap;
							if (resultMap.get(praxisAnswerMap.get("id")) != null) {// 是否包含该试题，如没有，添加该题的统计信息，否则更新统计信息。
								rateMap = (Map) resultMap.get(praxisAnswerMap.get("id"));
							} else {
								rateMap = new HashMap();
								rateMap.put("isobject", praxisAnswerMap.get("isobject"));
								rateMap.put("rightRate", new HashMap[i]);
								Map averageRightRate = new HashMap();
								averageRightRate.put("count", 0);
								averageRightRate.put("averageScoringRate", 0f);
								rateMap.put("averageRightRate", averageRightRate);
								resultMap.put(praxisAnswerMap.get("id"), rateMap);
							}
							// 得分率，计算公式：(a*m+b*n)/(m+n)，a是上次保存的统计最终结果，m为上次统计时的累计计数，b是新统计题目的平均得分率，n是新统计题目数
							float totalScoringRate = (float) 0.0;
							Map rightRate[] = (Map[]) rateMap.get("rightRate");
							for (int m = 0; m < i; m++) {
								Map rightRateMap;
								if (rightRate[m] == null) {
									rightRateMap = new HashMap();
									rightRateMap.put("count", 1);
									float scoringRate = parseFloat(studentScores.get(m)) / parseFloat(scores.get(m));
									rightRateMap.put("averageScoringRate", scoringRate);
									rightRate[m] = rightRateMap;
									totalScoringRate += scoringRate;
								} else {
									rightRateMap = rightRate[m];
									rightRateMap.put("count", parseLong(rightRateMap.get("count")) + 1);
									float scoringRate = (parseLong(rightRateMap.get("count"))
											* parseFloat(rightRateMap.get("averageScoringRate")) + parseFloat(studentScores
											.get(m))
											/ parseFloat(scores.get(m)))
											/ (parseLong(rightRateMap.get("count")) + 1);
									rightRateMap.put("averageScoringRate", scoringRate);
									totalScoringRate += scoringRate;
								}
							}

							// 平均正确率

							Map averageRightRate = (Map) rateMap.get("averageRightRate");
							float averageScoringRate = (parseFloat(averageRightRate.get("averageScoringRate"))
									* parseLong(averageRightRate.get("count")) + totalScoringRate / i)
									/ (parseLong(averageRightRate.get("count")) + 1);
							averageRightRate.put("count", parseLong(averageRightRate.get("count")) + 1);
							averageRightRate.put("averageScoringRate", averageScoringRate);
							// 客观题，分解答案，计算选项选择率。
							if (!praxisAnswerMap.get("isobject").equals("0")) {
								Map selectRate[];
								if (rateMap.get("selectRate") != null) {
									selectRate = (Map[]) rateMap.get("selectRate");
								} else {
									selectRate = new HashMap[i];
									rateMap.put("selectRate", selectRate);
								}
								// String[] answers = (String[])
								// praxisAnswerMap.get("answer");
								List answers = new ArrayList();
								answers = (List) praxisAnswerMap.get("answer");
								for (int j = 0; j < i; j++) {// 遍历答案数组
									Map optionMap;
									if (selectRate[j] == null) {
										optionMap = new HashMap();
										selectRate[j] = optionMap;// 将optionHashMap放入选择率数组中
									} else {
										optionMap = selectRate[j];
									}
									if (answers.get(j) != null) {
										String[] options = ((String) answers.get(j)).split(SPLIT_CHAR);// 拆分答案
										for (int k = 0; k < options.length; k++) {
											if (optionMap.get(options[k]) == null) {
												optionMap.put(options[k], 1);// 将选项及次数放到optionHashMap
											} else {
												optionMap.put(options[k], parseLong(optionMap.get(options[k])) + 1);
											}
										}
									}
								}

							}

						}
					}
				} else {
					// pathList.remove(path);
					System.out.print("文件不存在");
				}
			} catch (Exception e) {
				System.out.print("解析文件错误");
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		return resultMap;
	}

	public static boolean saveStatAnalysis() {
		Connection sakiaConn = null;
		Connection conn = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
	    //获得试题的平均正确率，数量，和序列化的选项率和正确率
		String getSubjectInfo = "select PP_STAT_RATE,PP_AVG_COUNT,PP_AVG_RATE from praxispaper where PP_PP_ID=?";
		//更新试题的平均正确率，数量，和序列化的选项率和正确率
		String updateSubjectInfo = "update praxispaper set PP_STAT_RATE=? ,PP_AVG_COUNT=?,PP_AVG_RATE=?  where PP_PP_ID=?";
		//从sakai数据库中获得已批改试卷的存储路径
		String getPathList = "select file_path from melete_answerstatsueue limit 0," + BATCH_SIZE;
		//删除sakai数据库中获得已批改试卷的存储路径
		String removePathList = "delete from melete_answerstatsueue where file_path=?";
		try {
			sakiaConn = ((DataSource) ServiceLocator.getInstance().getDataSource(JNDINames.SAKAI_DATASOURCE))
					.getConnection();//获得sakia数据源
			sakiaConn.setAutoCommit(false);
			conn = ((DataSource) ServiceLocator.getInstance().getDataSource(JNDINames.PRAXISADMIN_DATASOURCE))
					.getConnection();//获得考试系统数据源
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(getSubjectInfo);
			ps2 = conn.prepareStatement(updateSubjectInfo);
			ps3 = sakiaConn.prepareStatement(removePathList);
			boolean conti = true;
			while (conti) {
				List<String> pathList = new ArrayList<String>();
				ResultSet sakiars = sakiaConn.createStatement().executeQuery(getPathList);
				while (sakiars.next()) {
					pathList.add(sakiars.getString(1));
				}
				sakiars.close();
				if (pathList.size() == 0) {
					break;
				}
				if (pathList.size() < BATCH_SIZE) {
					conti = false;
				}

				Map resultMap = doStatAnalysis(pathList);
				for (Iterator it = resultMap.keySet().iterator(); it.hasNext();) {
					String id = (String) it.next();
					try {
						ps.setString(1, id);
						ResultSet rs = ps.executeQuery();
						ps.clearParameters();
						String oldSerializeRate = null;
						int oldCount = 0;
						float oldAverageScoringRate = 0;
						if (rs.next()) {
							oldSerializeRate = rs.getString(1);
							//System.out.println("oldSerializeRate=" + oldSerializeRate);
							oldCount = rs.getInt(2);
							oldAverageScoringRate = rs.getFloat(3);
						}
						rs.close();
						long newAverageCount = 0;
						float newAverageScoringRate = 0f;
						String serializeRate = "";

						Map newRateMap = (Map) resultMap.get(id);
						Map newAverageRightRate = (Map) newRateMap.get("averageRightRate");
						newAverageScoringRate = (oldCount * oldAverageScoringRate + parseLong(newAverageRightRate
								.get("count"))
								* parseFloat(newAverageRightRate.get("averageScoringRate")))
								/ (parseLong(newAverageRightRate.get("count")) + oldCount);

						newAverageCount = parseLong(newAverageRightRate.get("count")) + oldCount;

						if (oldSerializeRate == null || "".equals(oldSerializeRate)) {
							newRateMap.remove("isobject");
							newRateMap.remove("averageRightRate");
							serializeRate = JSONUtil.serialize(newRateMap);
						} else {

							Map oldRateMap = (Map) JSONUtil.deserialize(oldSerializeRate);

							List oldRightRateArray = (List) oldRateMap.get("rightRate");
						
							List oldSelectMapArray = (List) oldRateMap.get("selectRate");

							Map newRightRateArray[] = (Map[]) newRateMap.get("rightRate");
							
							Map newSelectMapArray[] = (Map[]) newRateMap.get("selectRate");

							if (newRightRateArray != null) {
								Map[] rightRateArray;
								if (oldRightRateArray != null) {
									if (newRightRateArray.length > oldRightRateArray.size()) {
										rightRateArray = new HashMap[newRightRateArray.length];
									} else {
										rightRateArray = new HashMap[oldRightRateArray.size()];
									}
									for (int m = 0; m < rightRateArray.length; m++) {
										Map rightRateMap = new HashMap();
										rightRateArray[m] = rightRateMap;
										long tNewCount = 0;
										float tNewAverageScoringRate = 0;
										if (newRightRateArray[m] != null) {
											tNewCount = parseLong(newRightRateArray[m].get("count"));
											tNewAverageScoringRate = parseFloat(newRightRateArray[m]
													.get("averageScoringRate"));
										}
										long tOldCount = 0;
										float tOldAverageScoringRate = 0;
										if ((Map) oldRightRateArray.get(m) != null) {
											tOldCount = parseLong(((Map) oldRightRateArray.get(m)).get("count"));
											tOldAverageScoringRate = parseFloat(((Map) oldRightRateArray.get(m))
													.get("averageScoringRate"));
										}

										rightRateMap.put("averageScoringRate",
												(tNewCount * tNewAverageScoringRate + tOldCount
														* tOldAverageScoringRate)
														/ (tNewCount + tOldCount));
										rightRateMap.put("count", tNewCount + tOldCount);
									}
								} else {
									rightRateArray = newRightRateArray;
								}
								oldRateMap.put("rightRate", rightRateArray);
							}

							if (newSelectMapArray != null) {
								Map[] selectMapArray;
								if (oldSelectMapArray != null) {
									if (newSelectMapArray.length > oldSelectMapArray.size()) {
										selectMapArray = new HashMap[newSelectMapArray.length];
									} else {
										selectMapArray = new HashMap[oldSelectMapArray.size()];
									}
									for (int m = 0; m < selectMapArray.length; m++) {
										Map newSelectRateMap = newSelectMapArray[m];

										Map oldSelectRateMap = (Map) oldSelectMapArray.get(m);
										for (Object o : newSelectRateMap.keySet()) {
											String option = (String) o;
											if (oldSelectRateMap.containsKey(option)) {
												oldSelectRateMap.put(option, (Long) oldSelectRateMap.get(option)
														+ parseLong(newSelectRateMap.get(option)));
											} else {
												oldSelectRateMap.put(option, parseLong(newSelectRateMap.get(option)));
											}
										}
										selectMapArray[m] = oldSelectRateMap;
									}
								} else {
									selectMapArray = newSelectMapArray;
								}
								oldRateMap.put("selectRate", selectMapArray);
							}

							serializeRate = JSONUtil.serialize(oldRateMap);
						}
						ps2.setString(1, serializeRate);
						ps2.setLong(2, newAverageCount);
						ps2.setFloat(3, newAverageScoringRate);
						ps2.setString(4, id);
						ps2.addBatch();
					} catch (Exception e) {
						e.printStackTrace();
						throw new RuntimeException(e);
					}
				}

				int[] result = ps2.executeBatch();
				ps2.clearParameters();
				//System.out.println("result size:" + result.length);

				for (String path : pathList) {
					ps3.setString(1, path);
					ps3.addBatch();
				}
				int[] result2 = ps3.executeBatch();
				ps3.clearParameters();
				//System.out.println("result2 size:" + result2.length);
				conn.commit();
				sakiaConn.commit();
			}
			sakiaConn.setAutoCommit(true);
			conn.setAutoCommit(true);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (conn != null) {
					conn.rollback();
				}
				if (sakiaConn != null) {
					sakiaConn.rollback();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw new RuntimeException(e);
			}
			return false;
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (ps2 != null) {
					ps2.close();
				}
				if (ps3 != null) {
					ps3.close();
				}
				if (conn != null) {
					conn.close();
				}
				if (sakiaConn != null) {
					sakiaConn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		return true;
	}
}
