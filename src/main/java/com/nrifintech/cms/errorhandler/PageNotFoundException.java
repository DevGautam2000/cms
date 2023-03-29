package com.nrifintech.cms.errorhandler;

public class PageNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "Page not found.";

	public PageNotFoundException() {
		super(MESSAGE);
	}

}
