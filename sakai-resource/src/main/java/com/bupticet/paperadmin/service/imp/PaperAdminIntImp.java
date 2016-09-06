package com.bupticet.paperadmin.service.imp;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.bupticet.paperadmin.common.DAOSysException;
import com.bupticet.paperadmin.common.PaperAdminBDException;
import com.bupticet.paperadmin.common.PaperAdminException;
import com.bupticet.paperadmin.common.PaperException;
import com.bupticet.paperadmin.common.PraxisAdminBDException;
import com.bupticet.paperadmin.model.AnswerToPaper;
import com.bupticet.paperadmin.model.Publisher;
import com.bupticet.paperadmin.model.SchemaTO;
import com.bupticet.paperadmin.schema.model.PraxisDistribute;
import com.bupticet.paperadmin.schema.model.SchemaContent;
import com.bupticet.paperadmin.service.PaperAdminDAO;
import com.bupticet.paperadmin.service.PaperAdminInt;
import com.bupticet.paperadmin.tool.AnswerToolUtil;
import com.bupticet.paperadmin.tool.PaperToolUtil;
import com.bupticet.paperadmin.tool.PraxisToolUtil;
import com.bupticet.paperadmin.tool.SchemaToolUtil;

public class PaperAdminIntImp implements PaperAdminInt {
	public Hashtable autoPaperGen(int cSchemaContId)
			throws PaperAdminException {
		// TODO 自动生成方法存根
		SchemaTO cSchema = SchemaToolUtil.getSchemaTO(cSchemaContId);
		SchemaContent cSchemaCont = cSchema.cSchemaCont;
		PaperAdminDAO paperAdminDao = new PaperAdminDAOImp();
		Publisher PaperGenerator = new Publisher();
		Hashtable htPraxisIDs = null;
		// 如果是分数将整型转为浮点型
		exchangeIntAndFloat(cSchemaCont.cKnowDist, true);
		exchangeIntAndFloat(cSchemaCont.cCognDist, true);
		exchangeIntAndFloat(cSchemaCont.cDiffDist, true);
		exchangeIntAndFloat(cSchemaCont.cTypeDist, true);
		// 将String转换为Integer
		if (cSchemaCont.cTypeDist != null) {
			cSchemaCont.cTypeDist.cDemand = exchangeIntAndStr(
					cSchemaCont.cTypeDist.cDemand, false);
		}
		// 将知识点及其子节点ID封装到Vector中

		try {
			if (cSchemaCont.cKnowDist != null) {
				cSchemaCont.cKnowDist.cDemand = processKnowID(
						cSchemaCont.cKnowDist, cSchemaCont.nCourseID);
			}
			// System.out.println("cSchemaCont.cKnowDist.cDemand::::"+cSchemaCont.cKnowDist.cDemand);
			// 将时间由分钟转换为秒
			cSchemaCont.cTotalTime.nDemand = (int) cSchemaCont.cTotalTime.nDemand * 60;
			// 将cSchemaCont传给组卷算法并获得返回值题目Hashtable,Key=PraxisID,value=PraxisTypeID
			htPraxisIDs = PaperGenerator.generate(cSchemaCont);
			/*
			 * System.out.println("时间满意度："+cSchemaCont.cTotalTime.nAccording);
			 * System.out.println("分数满意度："+cSchemaCont.cFullMark.nAccording);
			 * if(cSchemaCont.cTypeDist!=null)
			 * System.out.println("题型满意度："+cSchemaCont.cTypeDist.nAccording);
			 * if(cSchemaCont.cKnowDist!=null)
			 * System.out.println("知识点满意度："+cSchemaCont.cKnowDist.nAccording);
			 * if(cSchemaCont.cTypeDist!=null)
			 * System.out.println("认知满意度："+cSchemaCont.cTypeDist.nAccording);
			 * if(cSchemaCont.cDiffDist!=null)
			 * System.out.println("难度满意度："+cSchemaCont.cDiffDist.nAccording);
			 * System.out.println("总满意度："+cSchemaCont.nTotalAccording);
			 * System.out.println("知识点实际(no)：");
			 */
		} catch (PaperException pe) {
			pe.printStackTrace();
			// System.out.println(pe.getMessage());
			throw new PaperAdminException("组卷算法出错" + pe.getMessage(), pe
					.message());
		}
		// 保存试卷
		if (htPraxisIDs == null) {
			throw new PaperAdminException("组卷算法无题目输出", "组卷算法无题目输出");
		}
		//System.out.println("htPraxisIDs=========="+htPraxisIDs);
		return htPraxisIDs;
	}
	public void genJsonPaper(int cSchemaContId) throws Exception
	{
		Hashtable questionList=this.autoPaperGen(cSchemaContId);
		//System.out.println("questionList========"+questionList);
		List questions=PraxisToolUtil.getPraxis(questionList,"D:\\workspace\\StrutsSpringHibernate\\WebContent\\");
		//System.out.println("questions==========="+questions);
		//PaperToolUtil.converterPaperToHTML("D:\\workspace\\StrutsSpringHibernate\\WebContent","paper",questions);
		PaperToolUtil.converterPaperToJson("D:\\workspace\\StrutsSpringHibernate\\WebContent", "paperjson", questions);
	}
	public void getPaperFromJson() throws DAOSysException, PraxisAdminBDException, IOException, PaperAdminBDException, SQLException
	{
		try {
			List result=PaperToolUtil.converterJsonToPaper("D:\\workspace\\StrutsSpringHibernate\\WebContent", "paperjson");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		//System.out.println("second............"+result);
		//PaperToolUtil.converterPaperToHTML("D:\\workspace\\StrutsSpringHibernate\\WebContent","secondpaper",result);
		
	}
	private void exchangeIntAndFloat(PraxisDistribute dist, boolean isIntToFloat) {
		if (dist != null) {
			Enumeration enKeys = null;
			Object key = null;
			// 如果是分数将整型与浮点型互换
			if (dist.nMarkOrAmount == 0) {
				// 处理需求
				if (dist.cDemand != null) {
					enKeys = dist.cDemand.keys();
					while (enKeys.hasMoreElements()) {
						key = enKeys.nextElement();
						if (isIntToFloat) {
							dist.cDemand.put(key, new Float(
									(float) (((Integer) dist.cDemand.get(key))
											.intValue())));
						} else {
							dist.cDemand.put(key, new Integer(
									(int) (((Float) dist.cDemand.get(key))
											.floatValue())));
						}
					}
				}
				// 处理实际
				if (dist.cFact != null) {
					enKeys = dist.cFact.keys();
					while (enKeys.hasMoreElements()) {
						key = enKeys.nextElement();
						if (isIntToFloat) {
							dist.cFact.put(key, new Float(
									(float) (((Integer) dist.cFact.get(key))
											.intValue())));
						} else {
							dist.cFact.put(key, new Integer(
									(int) (((Float) dist.cFact.get(key))
											.floatValue())));
						}
					}
				}
			}
		}
	}

	private Hashtable exchangeIntAndStr(Hashtable ht, boolean isIntToStr) {
		if (ht != null) {

			Hashtable htTmp = new Hashtable();

			Enumeration enKeys = null;

			Object key = null;

			enKeys = ht.keys();
			// System.out.println("enKeys.hasMoreElements()="+enKeys.hasMoreElements());
			int i = 0;
			while (enKeys.hasMoreElements()) {

				key = enKeys.nextElement();
				if (isIntToStr) {

					htTmp.put(((Integer) key).toString(), ht.get(key));

				}

				else {

					htTmp.put(new Integer((String) key), ht.get(key));

				}

			}
			return htTmp;

		}

		return null;

	}

	// 将知识点及其子节点ID封装到Vector中

	private Hashtable processKnowID(PraxisDistribute cKnowDist, int nCourseID) {
		Hashtable htKnowDistNew = new Hashtable();
		try {
			Hashtable htKnowDist = cKnowDist.cDemand;
			// System.out.println("htKnowDist::::"+htKnowDist);
			// System.out.println("htKnowDistNew::::"+htKnowDistNew);
			// Integer cCourseAsKnowID=
			// (Integer)getKnowledgeRemoteHome().findCourseAsKnow(nCourseID).getPrimaryKey();
			Hashtable htAllKnowIDs = getAllKnowID(new Integer(nCourseID));
			Enumeration enKeys = htKnowDist.keys();
			String key = "";
			Integer key1 = null;
			enKeys = htKnowDist.keys();
			while (enKeys.hasMoreElements()) {
				key = (String) enKeys.nextElement();
				htKnowDistNew.put(htAllKnowIDs.get(new Integer(key)),
						htKnowDist.get(key));
			}

			// System.out.println("htKnowDistNew:::"+htKnowDistNew);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return htKnowDistNew;
	}

	// 获得所有节点及其某节点所有子节点ID向量Vector对

	private Hashtable getAllKnowID(Integer cCourseID)
			throws PaperAdminException {
		try {
			Hashtable htAllKnowIDs = new Hashtable();// key=节点ID,value=所有子节点ID向量Vector

			Hashtable htSubSupIDs = getSubSupKnowID(cCourseID);
			// 初始化htAllKnowIDs
			Object key = null;
			Enumeration enKeys = htSubSupIDs.keys();
			while (enKeys.hasMoreElements()) {
				key = enKeys.nextElement();
				htAllKnowIDs.put(key, new Vector());
			}
			// 遍历所有节点以获得所有节点及其某节点所有子节点ID向量Vector对
			Object keyLoop = null;
			enKeys = htSubSupIDs.keys();
			while (enKeys.hasMoreElements()) {

				key = enKeys.nextElement();
				keyLoop = key;
				while (!keyLoop.equals(new Integer(0))) {
					((Vector) htAllKnowIDs.get(keyLoop)).add(key);
					keyLoop = htSubSupIDs.get(keyLoop);
				}
			}
			return htAllKnowIDs;
		} catch (PaperAdminException re) {
			throw new PaperAdminException(
					"courseadminejb deleteknowledge error:RemoveException"
							+ re.getMessage(), "删除知识点时出错");
		}
	}

	// 将知识点及其父节点ID对封装到Hashtable中

	private Hashtable getSubSupKnowID(Integer cCourseID)
			throws PaperAdminException {
		// Iterator cKnowIt=null;
		Hashtable htSubSupIDs = new Hashtable();// key=子节点ID,value=父节点ID
		// KnowledgeRemote knowledge=null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List items = null;
		String GET_KNOWS_SQL = "SELECT knowledge.KN_KN_ID as nKnowledgeID,knowledge.KN_PARE_ID as nParentKnowID"
				+ " FROM heuser.knowledge" + " WHERE KN_BELO_COURSE=?";
		// cKnowIt=getKnowledgeRemoteHome().findByCourseID(cCourseID.intValue()).iterator();

		try {
			// ======Make a connection to the specific database======
			// Create a connection object
			conn = PaperAdminDAOImp.getDataSource().getConnection();
			// Create a PreparedStatemen
			ps = conn.prepareStatement(GET_KNOWS_SQL,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			ps.setInt(1, cCourseID.intValue());
			// Execute some SQL statement
			// Get a ResultSet object
			rs = ps.executeQuery();
			// Obtain the data you wish to access and encapsulate it to the List
			// structure
			// Obtain items
			while (rs.next()) {
				htSubSupIDs.put(new Integer(rs.getInt("nKnowledgeID")),
						new Integer(rs.getInt("nParentKnowID")));
			}
		} catch (SQLException se) {
			throw new DAOSysException("SQL Exception occurring: "
					+ se.getMessage(), "数据库操作失败:获取知识点子节点时出错!");
		}
		// Free resource for other process
		finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				throw new DAOSysException(
						"Exception occurring during close().."
								+ se.getMessage(), "数据库关闭失败!");
			}
		}
		return htSubSupIDs;

	}

	// ljw添加2007.5.20，用于从多知识点的vector中拆分出最高级的父知识点
	private Vector processVector(Vector cV, int nCourseID) {
		Integer key1 = null;
		Object keyLoop = null;
		Object keyLoop1 = null;
		Vector v1 = new Vector();
		try {

			Hashtable htSubSupIDs = getSubSupKnowID(new Integer(nCourseID));
			key1 = (Integer) cV.get(0);
			if (htSubSupIDs != null) {
				keyLoop = key1;
				while (cV.contains(keyLoop)) {
					keyLoop1 = keyLoop;
					keyLoop = htSubSupIDs.get(keyLoop);
				}
				// System.out.println("htKnowDistNew:::"+htKnowDistNew);
			}
			v1.add(keyLoop1);
			// System.out.println("v1:::"+v1);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return v1;
	}

}
