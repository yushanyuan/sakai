package com.bupticet.paperadmin.tool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.bupticet.paperadmin.common.DAOSysException;
import com.bupticet.paperadmin.common.MaterialTO;
import com.bupticet.paperadmin.model.ExerciseTO;
import com.bupticet.paperadmin.model.PraxisTO;
import com.bupticet.paperadmin.service.imp.PaperAdminDAOImp;
import com.bupticet.praxisadmin.praxistype.model.PraxisContent;

public class ExerciseToolUtil {
	public static List findExerciseListByCourse(int courseId) {
		List result = new ArrayList();
		Connection connection = null;
		ResultSet rs = null;
		DataSource source = PaperAdminDAOImp.getDataSource();
		try {
			connection = source.getConnection();
			String sql = "select EX_EX_ID as id,EX_EX_NAME as name,EX_BELO_COURSE as courseId from EXERCISE where EX_BELO_COURSE="
					+ courseId + " and EX_TRY_STATE=1";
			// System.out.println("sql============="+sql);
			rs = connection.createStatement().executeQuery(sql);
			// System.out.println("rs=============="+rs);
			while (rs.next()) {
				ExerciseTO one = new ExerciseTO();
				one.setBelongCourseId(rs.getInt("courseId"));
				one.setExerciseName(rs.getString("name"));
				one.setExerciseToId(rs.getInt("id"));
				result.add(one);
			}
			// System.out.println("rulst==========="+result);
			rs.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return result;
	}

	/*
	 * 根据作业ID获取作业的所有题目信息，然后作业和答案以json文件的格式进行保存
	 */
	public static List findPraxisTOListById(int nExerciseID, String materialsPath) throws DAOSysException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		PreparedStatement ps2 = null;
		ResultSet rs2 = null;
		boolean aa = false;
		String strPraxisID = "";
		ArrayList list = new ArrayList();

		PraxisTO cPraxisTo = null;
		MaterialTO mto = null;
		String GET_PRAXIS_SQL = "SELECT praxistype.PT_IS_OBJECTIVE as nIsObjective,basepraxis.BP_ORIG_ID as nOrigPraxisID,basepraxis.BP_BP_ID as nPraxisID,"
				+ "basepraxis.BP_BP_TYPE as nPraxisTypeID,praxistype.PT_PT_NAME as strPraxisTypeName,praxistemplate.PTE_IMPL_CLASSNAME as nTemplateName,"
				+ "basepraxis.BP_SUGG_TIME as nSuggTime,basepraxis.BP_BP_MARK as fSuggScore,"
				+ "praxisaffiliation.PA_PA_HINT as strHint,basepraxis.BP_GRAD_APPROACH as strGradApproach,"
				+ "praxisknowledge.PK_KNOW_ID as nKnowID,knowledge.KN_KN_NAME as strKnowName,"
				+ "basepraxis.BP_BP_BODY as strBody,basepraxis.BP_BP_ANSWER as strAnswer "
				+ "FROM heuser.praxisaffiliation,heuser.basepraxis,heuser.praxistype,heuser.praxisknowledge,heuser.knowledge,heuser.praxistemplate "
				+ "WHERE praxisaffiliation.PA_PA_ID=basepraxis.BP_ORIG_ID AND praxistype.PT_PT_ID=basepraxis.BP_BP_TYPE "
				+ "AND praxisknowledge.PK_PRAX_ID=basepraxis.BP_ORIG_ID AND praxisknowledge.PK_KNOW_ID=knowledge.KN_KN_ID AND praxistype.PT_BELO_TEMPLATE=praxistemplate.PTE_PTE_ID"
				+ " AND basepraxis.BP_BP_ID in (";

		String GET_MATERIAL_SQL = "SELECT EXERCISEMATERIAL.EM_EM_ID AS nMaterialID,EXERCISEMATERIAL.EM_BELO_PRAXIS as nPraxisID,EXERCISEMATERIAL.EM_EM_BODY as strBody,EXERCISEMATERIAL.EM_EM_FORMAT as strFormat,"
				+ "EXERCISEMATERIAL.EM_FILE_SUFFIX as strSuffix from heuser.EXERCISEMATERIAL where EXERCISEMATERIAL.EM_BELO_PRAXIS=?";

		String GET_BASEPRAXIS_SQL = "SELECT basepraxis.BP_BP_ID AS nBasePraxisID"
				+ " from heuser.basepraxis,heuser.exercise,heuser.superpraxis "
				+ "where basepraxis.BP_BELO_SUPERPRAXIS=superpraxis.SP_SP_ID AND superpraxis.SP_BELO_EXERCISE=exercise.EX_EX_ID AND exercise.EX_EX_ID=?";

		try {
			conn = PaperAdminDAOImp.getDataSource().getConnection();
			ps = conn.prepareStatement(GET_BASEPRAXIS_SQL, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			// / System.out.println("nExerciseID=========="+nExerciseID);
			ps.setInt(1, nExerciseID);
			rs = ps.executeQuery();
			// System.out.println("rs============="+rs);
			ps.clearParameters();
			while (rs.next()) {
				// System.out.println("rs.getInt========"+rs.getInt("nBasePraxisID"));
				strPraxisID += String.valueOf(rs.getInt("nBasePraxisID")) + ",";
			}
			// System.out.println("strPraxisID=========="+strPraxisID);
			if (strPraxisID.length() != 0) {
				strPraxisID = strPraxisID.substring(0, strPraxisID.length() - 1);
			}
			GET_PRAXIS_SQL = GET_PRAXIS_SQL + strPraxisID + ")";
			// System.out.println("========="+GET_PRAXIS_SQL);
			ps1 = conn.prepareStatement(GET_PRAXIS_SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs1 = ps1.executeQuery();
			ps1.clearParameters();
			while (rs1.next()) {
				cPraxisTo = new PraxisTO();
				cPraxisTo.cPraxisCont = (PraxisContent) Class.forName(rs1.getString("nTemplateName")).newInstance();
				cPraxisTo.cPraxisCont.setXMLBody(rs1.getClob("strBody").getSubString(1,
						(int) rs1.getClob("strBody").length()));
				cPraxisTo.cPraxisCont.setXMLAnswer(rs1.getClob("strAnswer").getSubString(1,
						(int) rs1.getClob("strAnswer").length()));
				cPraxisTo.cPraxisCont.readFromXML();
				cPraxisTo.nPraxisID = rs1.getInt("nOrigPraxisID");
				cPraxisTo.nIsObjective = rs1.getInt("nIsObjective");
				cPraxisTo.nPraxisTypeID = rs1.getInt("nPraxisTypeID");
				cPraxisTo.strPraxisTypeName = rs1.getString("strPraxisTypeName");
				cPraxisTo.nSuggTime = rs1.getInt("nSuggTime");
				cPraxisTo.fSuggScore = rs1.getFloat("fSuggScore");
				cPraxisTo.strHint = rs1.getString("strHint");
				if (cPraxisTo.strHint == null)
					cPraxisTo.strHint = "";
				cPraxisTo.strGradApproach = rs1.getString("strGradApproach");
				if (cPraxisTo.strGradApproach == null)
					cPraxisTo.strGradApproach = "";
				// cPraxisTo.cMaterialIDs = cMaterialIDs;
				cPraxisTo.nKnowID = rs1.getInt("nKnowID");
				cPraxisTo.strKnowName = rs1.getString("strKnowName");
				// 取出图片等素材，存放在praxisTO中
				ps2 = conn.prepareStatement(GET_MATERIAL_SQL, ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				ps2.setInt(1, rs1.getInt("nPraxisID"));
				rs2 = ps2.executeQuery();
				ps2.clearParameters();

				ArrayList list1 = new ArrayList();
				while (rs2.next()) {
					mto = new MaterialTO(rs2.getInt("nMaterialID"), rs2.getInt("nPraxisID"),
							rs2.getString("strFormat"), rs2.getString("strSuffix"));
					if (!PraxisToolUtil
							.materialExists(materialsPath, mto.getNMaterialID() + "", mto.getStrFileSuffix())) {
						PraxisToolUtil.writeMaterialToFile(materialsPath, mto.nMaterialID + "", mto.strFileSuffix, rs2
								.getBlob("strBody").getBinaryStream());
					}
					list1.add(mto);
				}
				cPraxisTo.list = list1;
				list.add(cPraxisTo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOSysException(e.getMessage(), "数据库操作失败：设置试题内容出错！");
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (rs1 != null)
					rs1.close();
				if (ps1 != null)
					ps1.close();
				if (rs2 != null)
					rs2.close();
				if (ps2 != null)
					ps2.close();
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				throw new DAOSysException(se.getMessage(), "数据库关闭失败！");
			}
		}
		return list;

	}
}
