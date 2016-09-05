package com.bupticet.paperadmin.common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OracleForBinary {

	private String strQueryEmpty = null;

	private String strQueryOperate = null;

	private int nType = 0;

	// Constructor

	public OracleForBinary(String strQueryEmpty, String strQueryOperate,
			int nType) {

		this.strQueryEmpty = strQueryEmpty;

		this.strQueryOperate = strQueryOperate;

		this.nType = nType;

	}

	/**
	 * 
	 * 
	 * @param in
	 *            a inputstream
	 * 
	 * 
	 * @return int the size inserted into the data field
	 * 
	 * 
	 * @throws WriteBinaryException
	 * 
	 * 
	 */

	public int writeForBlob(Connection conn, InputStream in, boolean isUseByEJB)

	throws Exception {

		int binarySize = 0;

		PreparedStatement ps = null;

		ResultSet rs = null;

		try {

			if (!isUseByEJB)

				conn.setAutoCommit(false);

			// String strQuery="INSERT INTO praxismaterial(PM_PM_ID,PM_PM_BODY)
			// values(?,EMPTY_BLOB())";

			ps = conn.prepareStatement(strQueryEmpty);

			ps.setInt(1, nType);

			ps.executeUpdate();

			ps.clearParameters();

			// strQuery="select praxismaterial.PM_PM_BODY from praxismaterial
			// where PM_PM_ID=? for update";

			ps = conn.prepareStatement(strQueryOperate);

			ps.setInt(1, nType);

			rs = ps.executeQuery();

			if (rs.next()) {

				java.sql.Blob javabb = rs.getBlob(1);

				OutputStream os = ((oracle.sql.BLOB) javabb)
						.getBinaryOutputStream();

				byte[] buf = new byte[10240];

				int len = in.read(buf);

				while (len > 0) {

					os.write(buf, 0, len);

					len = in.read(buf);

				}

				in.close();

				os.close();

			}

			if (!isUseByEJB) {

				conn.commit();

				conn.setAutoCommit(true);

			}

		} catch (SQLException se) {

			throw new Exception(
					"Exception occurring during close().."

					+ se.getMessage());

		} catch (IOException ie) {

			throw new Exception(
					"Exception occurring during close().."

					+ ie.getMessage());

		}

		return binarySize;

	}

	/**
	 * 
	 * 
	 * @param filePath
	 *            a string representating a binary file
	 * 
	 * 
	 * @param conn
	 *            a database connection object
	 * 
	 * 
	 * @param table
	 *            a database table name
	 * 
	 * 
	 * @param field
	 *            a field name in a table
	 * 
	 * 
	 * @param subQuery
	 *            a sub select command
	 * 
	 * 
	 * @return boolean
	 * 
	 * 
	 * @throws FileNotFoundException
	 * 
	 * 
	 * @throws SQLException
	 * 
	 * 
	 * @throws IOException
	 * 
	 * 
	 */

	public static boolean writeForLongRaw(String filePath, Connection conn,
			String table, String field, String subQuery)

	throws FileNotFoundException, SQLException, IOException {

		PreparedStatement ps = null;

		boolean flag = false;

		File file = null;

		FileInputStream fin = null;

		try {

			file = new File(filePath);

			fin = new FileInputStream(file);

			// String sqlStr="UPDATE "+table+" SET "+field+"=? WHERE
			// account_id=?;";

			String sqlStr = "UPDATE student SET picture=? WHERE account_id="

			+ "(" + subQuery + ")";

			ps = conn.prepareStatement(sqlStr);

			ps.setBinaryStream(1, fin, fin.available());

			int re = ps.executeUpdate();

			if (re > 0) {

				flag = true;

			}

			fin.close();

			ps.close();

		} catch (FileNotFoundException fe) {

			throw fe;

		} catch (SQLException se) {

			throw se;

		} catch (IOException ie) {

			throw ie;

		}

		// return boolean value

		return flag;

	}

	/**
	 * 
	 * 
	 * @param in
	 *            a InputStream subclass object such as FileInputStream
	 * 
	 * 
	 * @param conn
	 *            a database connection object
	 * 
	 * 
	 * @param table
	 *            a database table name
	 * 
	 * 
	 * @param field
	 *            a field name in a table
	 * 
	 * 
	 * @param subQuery
	 *            a sub select command
	 * 
	 * 
	 * @return boolean
	 * 
	 * 
	 * @throws SQLException
	 * 
	 * 
	 * @throws IOException
	 * 
	 * 
	 */

	public static boolean writeForLongRaw(InputStream in, Connection conn,
			String table, String field, String subQuery)

	throws SQLException, IOException {

		PreparedStatement ps = null;

		boolean flag = false;

		try {

			// String sqlStr="UPDATE "+table+" SET "+field+"=? WHERE
			// account_id=?;";

			String sqlStr = "UPDATE student SET picture=? WHERE account_id="

			+ "(" + subQuery + ")";

			ps = conn.prepareStatement(sqlStr);

			ps.setBinaryStream(1, in, in.available());

			int re = ps.executeUpdate();

			if (re > 0) {

				flag = true;

			}

			in.close();

			ps.close();

		} catch (SQLException se) {

			throw se;

		} catch (IOException ie) {

			throw ie;

		}

		// return boolean value

		return flag;

	}

	/**
	 * 
	 * 
	 * @param rs
	 *            a ResultSet created by a Statement object
	 * 
	 * 
	 * @param field
	 *            a field name in a table as desired
	 * 
	 * 
	 * @return ImputStream
	 * 
	 * 
	 * @throws SQLException
	 * 
	 * 
	 * @throws IOException
	 * 
	 * 
	 */

	public static InputStream readForLongRaw(ResultSet rs, String field)

	throws SQLException, IOException {

		InputStream in = null;

		BufferedInputStream bufIn = null;

		try {

			in = rs.getBinaryStream(field);

			bufIn = new BufferedInputStream(in);

			bufIn.close();

		} catch (SQLException se) {

			throw se;

		} catch (IOException ie) {

			throw ie;

		}

		return bufIn;

	}

}