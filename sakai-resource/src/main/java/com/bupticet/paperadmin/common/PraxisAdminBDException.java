package com.bupticet.paperadmin.common;

/**
 * 
 * 
 * <p>
 * Title:
 * </p>
 * 
 * 
 * <p>
 * Description:
 * </p>
 * 
 * 
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * 
 * 
 * <p>
 * Company:
 * </p>
 * 
 * 
 * @author unascribed
 * 
 * 
 * @version 1.0
 * 
 * 
 */

public class PraxisAdminBDException extends Exception {

	public PraxisAdminBDException() {
		super();
	}

	public PraxisAdminBDException(String s) {
		super(s);
	}

	public PraxisAdminBDException(String str1, String m) {

		super(str1);

		message = m;

	}

	public String message() {
		return message;
	}

	private String message;

}