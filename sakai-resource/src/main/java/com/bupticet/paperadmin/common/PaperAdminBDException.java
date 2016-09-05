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

public class PaperAdminBDException extends Exception {

	public PaperAdminBDException() {
		super();
	}

	public PaperAdminBDException(String s) {
		super(s);
	}

	public PaperAdminBDException(String str1, String m) {

		super(str1);

		message = m;

	}

	public String message() {
		return message;
	}

	private String message;

}