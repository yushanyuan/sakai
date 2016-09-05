package com.bupticet.paperadmin.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

import javax.sql.DataSource;

public class Setting {

	private static Setting me;

	private Hashtable htPath;

	public static final String GET_PATH_SQL =

	"SELECT SE_SE_VALUE FROM setting WHERE SE_SE_NAME=?";

	public static final String GET_DTD_SQL =

	"SELECT SE_SE_VALUE FROM setting WHERE SE_SE_NAME=?";

	public Setting() {

		htPath = new Hashtable();

	}

	public static Setting getInstance() {

		if (me == null) {

			me = new Setting();

		}

		return me;

	}

	public String getPath(String strSettingName) throws DAOSysException {

		try {

			strSettingName = strSettingName.trim();

			if (htPath.containsKey(strSettingName)) {

				return (String) htPath.get(strSettingName);

			}

			else {

				String str = getPathFromDB(strSettingName);

				htPath.put(strSettingName, str);

				return str;

			}

		} catch (DAOSysException de) {

			throw new DAOSysException("Excetion occur when getPath"
					+ de.getMessage());

		} catch (NullPointerException ne) {

			throw new DAOSysException("Excetion occur when getPath:没有此项配置"

			+ ne.getMessage());

		}

	}

	public String getdir(String strDtdName) throws DAOSysException {

		String dtddir = null;

		Connection conn = null;

		PreparedStatement ps = null;

		ResultSet rs = null;

		try {

			// ========get connection to the database=========

			conn = getDataSource().getConnection();

			// create a prepareStatement

			ps = conn.prepareStatement(GET_DTD_SQL,

			ResultSet.TYPE_SCROLL_INSENSITIVE,

			ResultSet.CONCUR_READ_ONLY);

			ps.setString(1, strDtdName);

			// execute some sql statement

			// get a resultset

			rs = ps.executeQuery();

			// obtain the data you want to access

			if (rs.next()) {

				dtddir = rs.getString(1);

			}

		} catch (SQLException se) {

			throw new DAOSysException("error:" + se.getMessage());

		}

		finally {

			try {

				if (rs != null)
					rs.close();

				if (ps != null)
					ps.close();

				if (conn != null)
					conn.close();

			} catch (SQLException se) {

				throw new DAOSysException("Excetion occur when closing"
						+ se.getMessage());

			}

		}

		return dtddir;

	}

	private String getPathFromDB(String str) throws DAOSysException {

		// String GET_PATH_SQL="SELECT SE_SE_VALUE FROM setting WHERE
		// SE_SE_NAME=?";

		String filedir = null;

		Connection conn = null;

		PreparedStatement ps = null;

		ResultSet rs = null;

		// get a directory from db that xml file save at

		try {

			// ========get connection to the database=========

			conn = getDataSource().getConnection();

			// create a prepareStatement

			ps = conn.prepareStatement(GET_PATH_SQL,

			ResultSet.TYPE_SCROLL_INSENSITIVE,

			ResultSet.CONCUR_READ_ONLY);

			ps.setString(1, str);

			// execute some sql statement

			// get a resultset

			rs = ps.executeQuery();

			// obtain the data you want to access

			if (rs.next()) {

				filedir = rs.getString(1);

			}

		} catch (SQLException se) {

			throw new DAOSysException("error:" + se.getMessage());

		}

		finally {

			try {

				if (rs != null)
					rs.close();

				if (ps != null)
					ps.close();

				if (conn != null)
					conn.close();

			} catch (SQLException se) {

				throw new DAOSysException("Excetion occur when closing"
						+ se.getMessage());

			}

		}

		return filedir;

	}

	protected static DataSource getDataSource() throws DAOSysException {

		try {

			return (DataSource) ServiceLocator.getInstance().getDataSource(
					JNDINames.SETTING_DATASOURCE);

		} catch (Exception sle) {

			throw new DAOSysException(
					"NamingException while looking up DB context : "

					+ sle.getMessage());

		}

	}

}