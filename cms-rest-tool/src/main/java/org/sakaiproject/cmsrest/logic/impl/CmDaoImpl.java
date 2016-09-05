/**
 * 版权所有 北京思开科技有限公司 
 * All Rights Reserved
 */
package org.sakaiproject.cmsrest.logic.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

import org.sakaiproject.cmsrest.logic.CmDao;
import org.sakaiproject.db.api.SqlService;

/**
 * @Description: TODO
 * @author: yushanyuan
 *
 */
public class CmDaoImpl implements CmDao {

private SqlService sqlService;
	

	public SqlService getSqlService() {
		return sqlService;
	}

	public void setSqlService(SqlService sqlService) {
		this.sqlService = sqlService;
	}
	
	public void init(){
		
		System.out.println(" init cm dao -----");
		Locale defaultLocale=Locale.getDefault();
		ResourceBundle bundle = ResourceBundle.getBundle("i18n", defaultLocale);
		 
		 
		try {
			int cnt = getCountOfCategory();
			if(cnt == 0){
				insertCategory("liberal" ,bundle.getString("liberal"));
				insertCategory("science" ,bundle.getString("science"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public String getEIdByTitle(String title) throws SQLException {
		
		String id = null;
		Connection c = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		 
		c = sqlService.borrowConnection();
		
		String sql1 = "SELECT ENTERPRISE_ID FROM CM_MEMBER_CONTAINER_T WHERE TITLE = ?";
		pst = c.prepareStatement(sql1);
		pst.setString(1, title);
		rs = pst.executeQuery();
		 
		if(rs.next()){
			id = rs.getString("ENTERPRISE_ID");
		} 
		
		if (rs != null)
			rs.close();

		if (pst != null)
			pst.close();

		if (c != null)
			sqlService.returnConnection(c);
		
		return id;
	}
	
	//----------------------------------------------------------------
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	private int getCountOfCategory() throws SQLException{
		int cut = 0;
		Connection c = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		 
		c = sqlService.borrowConnection();
		
		String sql1 = "SELECT COUNT(*) AS CUT FROM CM_SEC_CATEGORY_T";
		pst = c.prepareStatement(sql1);
		rs = pst.executeQuery();
		if(rs.next()){
			cut = rs.getInt("CUT");
		} 
		
		if (rs != null)
			rs.close();

		if (pst != null)
			pst.close();

		if (c != null)
			sqlService.returnConnection(c);
		
		return cut;
	}
	
	private void insertCategory(String code, String desc) throws SQLException{
		 
		Connection c = null;
		PreparedStatement pst = null;
		 
		c = sqlService.borrowConnection();
		
		String sql1 = "INSERT INTO CM_SEC_CATEGORY_T VALUES(?,?)";
		pst = c.prepareStatement(sql1);
		pst.setString(1, code);
		pst.setString(2, desc);
		pst.executeUpdate();
		c.commit();
		if (pst != null)
			pst.close();

		if (c != null)
			sqlService.returnConnection(c);
	}

	
	
	
}
