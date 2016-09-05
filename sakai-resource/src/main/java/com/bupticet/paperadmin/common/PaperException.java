package com.bupticet.paperadmin.common;

public class PaperException extends Exception {

	public PaperException() {

	}

	public PaperException(String msg) {

		super(msg);

	}

	public PaperException(String str1, String m) {

		super(str1);

		message = m;

	}

	public String message() {
		return message;
	}

	private String message;

}