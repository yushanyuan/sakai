package org.sakaiproject.resource.api.course.vo;

import java.io.Serializable;

/**
 * 题库：策略
 * 
 * @author cedarwu
 * 
 */
public class SchemaNewVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int nSchemaID = 0;

	private String strSchemaName = "";

	private String strCreator = "";

	private String cCreaDate = null;

	private String cExpoTime = null;

	private int nCourseID = 0;

	private int nUsedTimes = 0;

	private SchemaContentNew cSchemaCont;

	private int nCreatorID = 0;

	public int getnSchemaID() {
		return nSchemaID;
	}

	public void setnSchemaID(int nSchemaID) {
		this.nSchemaID = nSchemaID;
	}

	public String getStrSchemaName() {
		return strSchemaName;
	}

	public void setStrSchemaName(String strSchemaName) {
		this.strSchemaName = strSchemaName;
	}

	public String getStrCreator() {
		return strCreator;
	}

	public void setStrCreator(String strCreator) {
		this.strCreator = strCreator;
	}

	public String getcCreaDate() {
		return cCreaDate;
	}

	public void setcCreaDate(String cCreaDate) {
		this.cCreaDate = cCreaDate;
	}

	public String getcExpoTime() {
		return cExpoTime;
	}

	public void setcExpoTime(String cExpoTime) {
		this.cExpoTime = cExpoTime;
	}

	public int getnCourseID() {
		return nCourseID;
	}

	public void setnCourseID(int nCourseID) {
		this.nCourseID = nCourseID;
	}

	public int getnUsedTimes() {
		return nUsedTimes;
	}

	public void setnUsedTimes(int nUsedTimes) {
		this.nUsedTimes = nUsedTimes;
	}

	public SchemaContentNew getcSchemaCont() {
		return cSchemaCont;
	}

	public void setcSchemaCont(SchemaContentNew cSchemaCont) {
		this.cSchemaCont = cSchemaCont;
	}

	public int getnCreatorID() {
		return nCreatorID;
	}

	public void setnCreatorID(int nCreatorID) {
		this.nCreatorID = nCreatorID;
	}

}