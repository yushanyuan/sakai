package com.bupticet.paperadmin.common;

public class DAOSysException extends RuntimeException {

	/**
	 * 
	 * 
	 * Default constructor. Takes no arguments
	 * 
	 * 
	 */

	private String message;

	public DAOSysException() {

		super();

	}

	public DAOSysException(String gripe) {

		super(gripe);

	}

	public DAOSysException(String str1, String m) {

		super(str1);

		message = m;

	}

	public String message() {
		return message;
	}

}