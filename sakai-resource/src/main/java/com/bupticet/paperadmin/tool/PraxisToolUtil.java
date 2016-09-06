package com.bupticet.paperadmin.tool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import com.bupticet.paperadmin.common.DAOSysException;
import com.bupticet.paperadmin.common.MaterialTO;
import com.bupticet.paperadmin.model.PraxisTO;
import com.bupticet.paperadmin.service.imp.PaperAdminDAOImp;
import com.bupticet.praxisadmin.praxistype.model.PraxisContent;
import com.bupticet.praxisadmin.praxistype.modelConvertor.PraxisTOConvertor;

public class PraxisToolUtil {
	public static List getPraxis(Hashtable htPraxisID, String materialsPath) throws DAOSysException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		String strPraxisID = "";
		ArrayList list = new ArrayList();

		PraxisTO cPraxisTo = null;
		MaterialTO mto = null;
		String GET_PRAXIS_SQL = "SELECT praxispaper.PP_PP_ID as nPraxisID, praxispaper.PP_PP_ISOBJECTIVE as nIsObjective,"
				+ "praxispaper.PP_PP_TYPE as nPraxisTypeID,praxistype.PT_PT_NAME as strPraxisTypeName,praxistemplate.PTE_IMPL_CLASSNAME as nTemplateName,"
				+ "praxispaper.PP_SUGG_TIME as nSuggTime,praxispaper.PP_SUGG_SCORE as fSuggScore,"
				+ "praxisaffiliation.PA_PA_HINT as strHint,praxiscontent.PC_GRAD_APPROACH as strGradApproach,"
				+ "praxisknowledge.PK_KNOW_ID as nKnowID,knowledge.KN_KN_NAME as strKnowName,"
				+ "praxiscontent.PC_PC_BODY as strBody,praxiscontent.PC_PC_ANSWER as strAnswer "
				+ " FROM heuser.praxisaffiliation,heuser.praxiscontent,heuser.praxistype,heuser.praxispaper,heuser.praxisknowledge,heuser.knowledge,heuser.praxistemplate "
				+ " WHERE praxispaper.PP_PP_ID=praxiscontent.PC_PC_ID AND praxisaffiliation.PA_PA_ID=praxispaper.PP_PP_ID AND praxistype.PT_PT_ID=praxispaper.PP_PP_TYPE "
				+ " AND praxisknowledge.PK_PRAX_ID=praxiscontent.PC_PC_ID AND praxisknowledge.PK_KNOW_ID=knowledge.KN_KN_ID AND praxistype.PT_BELO_TEMPLATE=praxistemplate.PTE_PTE_ID AND praxispaper.PP_PP_ID in (";

		String GET_MATERIAL_SQL = "SELECT PRAXISMATERIAL.PM_PM_ID AS nMaterialID,PRAXISMATERIAL.PM_BELO_PRAXIS as nPraxisID,PRAXISMATERIAL.PM_PM_BODY as strBody,PRAXISMATERIAL.PM_PM_FORMAT as strFormat,"
				+ "PRAXISMATERIAL.PM_FILE_SUFFIX as strSuffix from heuser.PRAXISMATERIAL where PRAXISMATERIAL.PM_BELO_PRAXIS=?";
		try {
			Enumeration en = htPraxisID.keys();
			while (en.hasMoreElements()) {
				strPraxisID += ((Integer) en.nextElement()).toString() + ",";
			}
			if (strPraxisID.length() != 0) {
				strPraxisID = strPraxisID.substring(0, strPraxisID.length() - 1);
			}

			conn = PaperAdminDAOImp.getDataSource().getConnection();

			GET_PRAXIS_SQL = GET_PRAXIS_SQL + strPraxisID + ")";

			ps = conn.prepareStatement(GET_PRAXIS_SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();

			ps.clearParameters();
			while (rs.next()) {
				cPraxisTo = new PraxisTO();
				cPraxisTo.cPraxisCont = (PraxisContent) Class.forName(rs.getString("nTemplateName")).newInstance();
				cPraxisTo.cPraxisCont.setXMLBody(rs.getClob("strBody").getSubString(1,
						(int) rs.getClob("strBody").length()));
				cPraxisTo.cPraxisCont.setXMLAnswer(rs.getClob("strAnswer").getSubString(1,
						(int) rs.getClob("strAnswer").length()));
				cPraxisTo.cPraxisCont.readFromXML();
				// System.out.println("cPraxisTo.cPraxisCont===========");
				cPraxisTo.nPraxisID = rs.getInt("nPraxisID");
				cPraxisTo.nIsObjective = rs.getInt("nIsObjective");
				cPraxisTo.nPraxisTypeID = rs.getInt("nPraxisTypeID");
				cPraxisTo.strPraxisTypeName = rs.getString("strPraxisTypeName");
				cPraxisTo.nSuggTime = rs.getInt("nSuggTime");
				cPraxisTo.fSuggScore = rs.getFloat("fSuggScore");
				cPraxisTo.strHint = rs.getString("strHint");
				if (cPraxisTo.strHint == null)
					cPraxisTo.strHint = "";
				cPraxisTo.strGradApproach = rs.getString("strGradApproach");
				if (cPraxisTo.strGradApproach == null)
					cPraxisTo.strGradApproach = "";
				// cPraxisTo.cMaterialIDs = cMaterialIDs;
				cPraxisTo.nKnowID = rs.getInt("nKnowID");
				cPraxisTo.strKnowName = rs.getString("strKnowName");
				// 取出图片等素材，存放在praxisTO中
				ps1 = conn.prepareStatement(GET_MATERIAL_SQL, ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				ps1.setInt(1, rs.getInt("nPraxisID"));
				rs1 = ps1.executeQuery();
				ps1.clearParameters();

				ArrayList list1 = new ArrayList();
				while (rs1.next()) {
					mto = new MaterialTO(rs1.getInt("nMaterialID"), rs1.getInt("nPraxisID"),
							rs1.getString("strFormat"), rs1.getString("strSuffix"));
					if (!materialExists(materialsPath, mto.getNMaterialID() + "", mto.getStrFileSuffix())) {
						writeMaterialToFile(materialsPath, mto.nMaterialID + "", mto.strFileSuffix, rs1.getBlob(
								"strBody").getBinaryStream());
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
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				throw new DAOSysException(se.getMessage(), "数据库关闭失败！");
			}
		}
		return list;
	}

	public static boolean materialExists(final String path, final String id, String filesuffix) {
		File p = new File(path + "material");
		if (!p.exists()) {
			return false;
		}
		filesuffix = filesuffix.substring(filesuffix.indexOf("/") + 1).toLowerCase();
		String fn = id + "." + filesuffix;
		File f = new File(p, fn);
		if (f.exists()) {
			return true;
		}
		// String[] files = p.list(new FilenameFilter() {
		// public boolean accept(File dir, String name) {
		// if (name.startsWith(id)) {
		// return true;
		// }
		// return false;
		// }
		// });
		// if (files.length > 0) {
		// return true;
		// }
		return false;
	}

	public static void writeMaterialToFile(final String filePath, final String fileName, String filesuffix,
			InputStream stream) {
		try {
			File path = new File(filePath + "material");
			if (!path.exists()) {
				path.mkdirs();
			}
			if (filesuffix.indexOf("/") >= 0) {
				filesuffix = filesuffix.substring(filesuffix.indexOf("/") + 1).toLowerCase();
			}
			String strFileName = fileName + "." + filesuffix;
			File materialFile = new File(path, strFileName);
			if (!materialFile.exists()) {
				FileOutputStream out = new FileOutputStream(materialFile);
				InputStream in = stream;
				byte[] buf = new byte[1024];
				int len = 0;
				len = in.read(buf);
				while (len > 0) {
					out.write(buf, 0, len);
					len = in.read(buf);
				}
				in.close();
				out.flush();
				out.close();
			}
		} catch (FileNotFoundException fnfe) {
			throw new DAOSysException("Exception occurring during get FileOutputStream" + fnfe.getMessage(), "文件未找到！");
		} catch (IOException ioe) {
			throw new DAOSysException("Exception occurring during write data" + ioe.getMessage(), "写数据时失败！");
		}
	}

	public static Hashtable findAllPRAXISTEMPLATE() throws SQLException {
		Hashtable result = new Hashtable();
		//Connection conn = null;
		try {
			/*PreparedStatement ps = null;
			ResultSet rs = null;
			String GET_PRAXISTEMPLATE_SQL = "SELECT PTE_PTE_ID, PTE_IMPL_CLASSNAME as classname FROM PRAXISTEMPLATE ";
			conn = PaperAdminDAOImp.getDataSource().getConnection();
			ps = conn.prepareStatement(GET_PRAXISTEMPLATE_SQL, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			rs = ps.executeQuery();
			while (rs.next()) {
				
				 * if(rs.getString(1).equals("com.bupticet.praxisadmin.praxistype.model.Selection"))
				 * result.put(rs.getInt(1), com.bupticet.praxisadmin.praxistype.model.Selection.class); else
				 * if(rs.getString(1).equals("com.bupticet.praxisadmin.praxistype.model.Reading"))
				 * result.put(rs.getInt(1), com.bupticet.praxisadmin.praxistype.model.Reading.class); else
				 * result.put(rs.getInt(1),com.bupticet.praxisadmin.praxistype.model.FillingBlank.class);
				 
				result.put(rs.getInt(1), rs.getString("classname"));
			}*/

			String values = "[{\"PTE_PTE_ID\":1,\"CLASSNAME\":\"com.bupticet.praxisadmin.praxistype.model.Selection\"},{\"PTE_PTE_ID\":2,\"CLASSNAME\":\"com.bupticet.praxisadmin.praxistype.model.Selection\"},{\"PTE_PTE_ID\":3,\"CLASSNAME\":\"com.bupticet.praxisadmin.praxistype.model.Selection\"},{\"PTE_PTE_ID\":4,\"CLASSNAME\":\"com.bupticet.praxisadmin.praxistype.model.Reading\"},{\"PTE_PTE_ID\":5,\"CLASSNAME\":\"com.bupticet.praxisadmin.praxistype.model.FillingBlank\"},{\"PTE_PTE_ID\":6,\"CLASSNAME\":\"com.bupticet.praxisadmin.praxistype.model.FillingBlank\"},{\"PTE_PTE_ID\":7,\"CLASSNAME\":\"com.bupticet.praxisadmin.praxistype.model.Selection\"},{\"PTE_PTE_ID\":8,\"CLASSNAME\":\"com.bupticet.praxisadmin.praxistype.model.Reading\"}]";
			JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON(values);
			if (jsonArray != null) {
				List list = (List) JSONSerializer.toJava(jsonArray);
				for (Object o : list) {
					JSONObject jsonObject = JSONObject.fromObject(o);
					result.put(jsonObject.get("PTE_PTE_ID"),jsonObject.get("CLASSNAME"));
				}
			}
		} catch (Exception e) {
			System.out.println("在取题目模版ddddd时发生如下错误：" + e);
			throw new RuntimeException(e);
		} finally {
			//conn.close();
		}
		return result;
	}

	public static List convertToPraxisToConvertor(List praxisTo, Hashtable oneparamter, Hashtable twoparamter) {
		List result = new ArrayList();
		for (int i = 0; i < praxisTo.size(); i++) {
			result.add(((PraxisTO) praxisTo.get(i)).convertToPraxisTOConvertor(oneparamter, twoparamter));
		}
		return result;
	}

	public static List convertorConvertorToPraxisTo(List praxisToConvertor, Hashtable oneparamter, Hashtable twoparamter) {
		List result = new ArrayList();
		for (int i = 0; i < praxisToConvertor.size(); i++) {
			result.add(((PraxisTOConvertor) praxisToConvertor.get(i)).convertToPraxisTO(oneparamter, twoparamter));
		}
		return result;
	}
}
