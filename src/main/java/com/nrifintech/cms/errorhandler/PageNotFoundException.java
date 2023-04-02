package com.nrifintech.cms.errorhandler;

/**
 * It's a RuntimeException that's thrown when a page is not found
 */
public class PageNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "Page not found.";

	// It's calling the constructor of the super class (RuntimeException) and passing the message to it.
	public PageNotFoundException() {
		super(MESSAGE);
	}

}
