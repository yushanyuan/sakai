package com.bupticet.paperadmin.model;

import java.util.Date;

public class SchemaListTO {

	public int nSchemaID;

	public String strSchemaName;

	public String strCreator;

	public Date cCreaDate;

	public SchemaListTO() {

		nSchemaID = 0;

		strSchemaName = "";

		strCreator = "";

		cCreaDate = null;

	}

	public SchemaListTO(int nSchemaID, String strSchemaName, String strCreator,
			Date cCreaDate) {

		this.nSchemaID = nSchemaID;

		this.strSchemaName = strSchemaName;

		this.strCreator = strCreator;

		this.cCreaDate = cCreaDate;

	}

}