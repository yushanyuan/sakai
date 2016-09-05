package com.bupticet.paperadmin.common;

public class DividedByZeroException extends RuntimeException {

	private String message;

	/**
	 * 
	 * 
	 * Default constructor. take no argument
	 * 
	 * 
	 */

	public DividedByZeroException() {

		super();

	}

	/**
	 * 
	 * 
	 * Constructor
	 * 
	 * 
	 * @param gripe
	 *            a String that explains what the condition is
	 * 
	 * 
	 */

	public DividedByZeroException(String gripe) {

		super(gripe);

	}

}