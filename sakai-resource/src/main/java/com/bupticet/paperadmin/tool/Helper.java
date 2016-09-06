package com.bupticet.paperadmin.tool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Hashtable;

import com.bupticet.paperadmin.common.DAOSysException;
import com.bupticet.paperadmin.service.imp.PaperAdminDAOImp;

public class Helper {
	private Hashtable condition = new Hashtable();
	public static final String HomeWorkPrefix = "H";
	public static final String TestPrefix = "T";
	public static final String SelfTestPrefix = "S";
	public static final String Answer = "_A";
	public static final String EndTag = ".json";

	public static String getAnswerName(String paperName) {
		return getAnswerNameById(getPaperId(paperName));
	}

	public static String getAnswerNameById(String paperId) {
		return paperId + Answer + EndTag;
	}

	public static String getStuAnswerName(String paperId, String attemptId) {
		return paperId + "_" + attemptId + EndTag;
	}

	public static String getPaperName(String paperId) {
		return paperId + EndTag;
	}

	public static String getPaperId(String paperName) {
		return paperName.substring(0, paperName.lastIndexOf(EndTag));
	}

	// public static String htmlSub="_html";
	public static Hashtable getAllCondition() {
		Hashtable result = new Hashtable();
		result.put(1, "一");
		result.put(2, "二");
		result.put(3, "三");
		result.put(4, "四");
		result.put(5, "五");
		result.put(6, "六");
		result.put(7, "七");
		result.put(8, "八");
		result.put(9, "九");
		result.put(10, "十");
		result.put(11, "十一");
		result.put(12, "十二");
		result.put(13, "十三");
		result.put(14, "十四");
		result.put(15, "十五");
		result.put(16, "十六");
		result.put(17, "十七");
		result.put(18, "十八");
		result.put(19, "十九");
		result.put(20, "二十");
		result.put(21, "二十一");
		result.put(22, "二十二");
		return result;
	}

	public static int genIdByAuto() {
		int result = -1;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = PaperAdminDAOImp.getDataSource().getConnection();
			ps = conn.prepareStatement("select seqmaxtt.nextval id from dual");
			rs = ps.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}

		} catch (DAOSysException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}

		}
		return result;
	}

	/**
	 * @return 找到当前的时间；
	 */
	public static long findNow() {
		Date d = new Date();
		long longtime = d.getTime();
		return longtime;
	}

	// ljw添加，去掉题目中的<p></p>
	public static String parseString(String strPraxis) throws DAOSysException {

		StringBuffer strTempBuf = new StringBuffer(strPraxis);

		try {
			int nBegin1 = strPraxis.indexOf("<p");
			if (nBegin1 == -1) {
				int NBegin1 = strPraxis.indexOf("<P");
				if (NBegin1 == -1) {
					return strPraxis;
				}
				int nEnd1 = strPraxis.indexOf(">", NBegin1);
				strTempBuf = strTempBuf.replace(NBegin1, nEnd1 + 1, "");
				String str = strTempBuf.toString();
				int nBegin2 = str.lastIndexOf("</P");
				int nEnd2 = str.indexOf(">", nBegin2);
				strTempBuf = strTempBuf.replace(nBegin2, nEnd2 + 1, "");
			} else {
				int nEnd1 = strPraxis.indexOf(">", nBegin1);
				strTempBuf = strTempBuf.replace(nBegin1, nEnd1 + 1, "");
				String str = strTempBuf.toString();
				int nBegin2 = str.lastIndexOf("</p");
				int nEnd2 = str.indexOf(">", nBegin2);
				strTempBuf = strTempBuf.replace(nBegin2, nEnd2 + 1, "");
			}
		} catch (Exception se) {
			throw new DAOSysException("SQL Exception occurring: " + se.getMessage(), "数据库操作失败：获取作业列表时出错！");
		}

		return strTempBuf.toString();
	}

}
