package com.bupticet.paperadmin.common;

public class PaperAdminException extends Exception {

	public PaperAdminException() {
		super();
	}

	public PaperAdminException(String s) {
		super(s);
	}

	public PaperAdminException(String str1, String m) {

		super(str1);

		message = m;

	}

	public String message() {
		return message;
	}

	private String message;

}