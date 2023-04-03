package com.nrifintech.cms.errorhandler;

/**
 * It's a custom exception class that extends the RuntimeException class
 */
public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = " not found.";

	// Calling the super class constructor.
	public NotFoundException(String entity) {
		// A static variable.
		super(entity + MESSAGE);
	}

}
