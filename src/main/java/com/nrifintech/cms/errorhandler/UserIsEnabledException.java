package com.nrifintech.cms.errorhandler;

/**
 * The UserIsEnabledException class is a custom exception class that extends the RuntimeException class
 */
public class UserIsEnabledException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "User is Enabled.";

	// Calling the super class constructor.
	public UserIsEnabledException() {
		super(MESSAGE);
	}
	// This is a constructor that takes a String as a parameter.
	public UserIsEnabledException(String errMessage) {
		super(errMessage);
	}
}
