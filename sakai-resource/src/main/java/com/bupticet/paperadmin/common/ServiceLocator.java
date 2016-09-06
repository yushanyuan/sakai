package com.bupticet.paperadmin.common;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ServiceLocator {
	private ServiceLocator serviceLocator;
	private ServiceLocator()
	{}
	public static ServiceLocator getInstance()
	{
		return new ServiceLocator();
	}
	public Object getDataSource(String name) throws NamingException
	{
		Context ctx=null;
		DataSource ds=null;
		try {
			ctx = new InitialContext();
			 ds=(DataSource)ctx.lookup(name);
		} catch (NamingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
       return ds;

	}
}
