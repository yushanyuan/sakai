package com.bupticet.paperadmin.tool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;

import com.bupticet.paperadmin.common.DAOSysException;
import com.bupticet.paperadmin.common.MaterialTO;
import com.bupticet.paperadmin.common.PaperAdminBDException;
import com.bupticet.paperadmin.common.PaperAdminException;
import com.bupticet.paperadmin.common.PraxisAdminBDException;
import com.bupticet.paperadmin.common.QuestionTO;
import com.bupticet.paperadmin.model.FrameTO;
import com.bupticet.paperadmin.model.PaperTO;
import com.bupticet.paperadmin.model.PraxisTO;
import com.bupticet.paperadmin.model.SchemaTO;
import com.bupticet.paperadmin.service.imp.PaperAdminDAOImp;
import com.bupticet.praxisadmin.praxistype.model.PraxisContent;

public class SchemaToolUtil {
	public static  SchemaTO getSchemaTO(int nSchemaID) throws DAOSysException {
	      Connection conn = null;
	      PreparedStatement ps = null;
	      ResultSet rs = null;
	      SchemaTO sc = null;
	     // System.out.println("nSchemaID=========="+nSchemaID);
	      try {
	       String GET_SCHEMATO_SQL =
	      "SELECT PS_SCHEMA_TO FROM PAPERSCHEMA WHERE PS_PS_ID=?";
	        conn = PaperAdminDAOImp.getDataSource().getConnection();
	        //System.out.println("conn =="+conn);
	        ps = conn.prepareStatement(GET_SCHEMATO_SQL,
	                                   ResultSet.TYPE_SCROLL_INSENSITIVE,
	                                   ResultSet.CONCUR_UPDATABLE);
	        ps.setInt(1, nSchemaID);
	        rs = ps.executeQuery();
	        if (rs.next()) {	
	        	//System.out.println("rs.getBlob(1)=========="+rs.getBlob(1).getBinaryStream());
	          ObjectInputStream in = new ObjectInputStream(rs.getBlob(1).getBinaryStream());	  
	         // System.out.println("in=========="+in);
	          sc = (SchemaTO) in.readObject();	  
	          //System.out.println(" sc=========="+ sc);
	        }
	      }
	      catch (Exception se) {
	        throw new DAOSysException("SQL Exception occurring: "
	                                  + se.getMessage(), "数据库操作失败：获取试卷信息时出错！");
	      }
	      finally {
	        try {
	          if (rs != null)
	              rs.close();
	          if (ps != null)
	            ps.close();
	          if (conn != null)
	            conn.close();
	        }
	        catch (SQLException se) {
	          throw new DAOSysException("Exception occurring during close().."
	                                    + se.getMessage(), "数据库关闭失败！");
	        }
	      }
	      return sc;
	  }
	public static  List<SchemaTO> getSchemaTOListByCourseId(int courseId) throws DAOSysException {
	     
		List  result=new ArrayList(); 
		Connection conn = null;
	      PreparedStatement ps = null;
	      ResultSet rs = null;
	      
	      try {
	       String GET_SCHEMATO_SQL =
	      "SELECT PS_PS_ID as id ,PS_PS_NAME as name FROM PAPERSCHEMA WHERE PS_BELO_COURSE=?";
	        conn = PaperAdminDAOImp.getDataSource().getConnection();
	        ps = conn.prepareStatement(GET_SCHEMATO_SQL,
	                                   ResultSet.TYPE_SCROLL_INSENSITIVE,
	                                   ResultSet.CONCUR_UPDATABLE);
	        ps.setInt(1, courseId);
	        rs = ps.executeQuery();
	        int index=1;
	        while (rs.next()) {	
	        	//System.out.println("index==========="+index);
	        	index++;
	        	SchemaTO sc = new SchemaTO();
	        	sc.nSchemaID=rs.getInt("id");
	        	sc.strSchemaName=rs.getString("name");
	        	sc.nCourseID=courseId;	        	
	        	result.add(sc);
	        	//System.out.println("result==========="+result.size());
	        }
	        //System.out.println("schematool================="+result);
	      }
	      catch (Exception se) {
	        throw new DAOSysException("SQL Exception occurring: "
	                                  + se.getMessage(), "数据库操作失败：获取试卷信息时出错！");
	      }
	      finally {
	        try {
	          if (rs != null)
	              rs.close();
	          if (ps != null)
	            ps.close();
	          if (conn != null)
	            conn.close();
	        }
	        catch (SQLException se) {
	          throw new DAOSysException("Exception occurring during close().."
	                                    + se.getMessage(), "数据库关闭失败！");
	        }
	      }
	      return result;
	  }

}
