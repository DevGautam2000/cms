package com.nrifintech.cms.errorhandler;


/**
 * The UserIsDisabledException class is a custom exception class that extends the RuntimeException
 * class
 */
public class UserIsDisabledException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "User is disabled.";

	// Calling the parent class constructor.
	public UserIsDisabledException() {
		super(MESSAGE);
	}
	// Calling the parent class constructor.
	public UserIsDisabledException(String errMessage) {
		super(errMessage);
	}

}
