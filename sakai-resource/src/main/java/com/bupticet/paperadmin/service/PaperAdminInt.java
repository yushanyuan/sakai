package com.bupticet.paperadmin.service;

import java.util.Hashtable;


import com.bupticet.paperadmin.common.PaperAdminException;
import com.bupticet.paperadmin.schema.model.SchemaContent;

public interface PaperAdminInt {
	public Hashtable autoPaperGen(int cSchemaContId) throws PaperAdminException;
}
