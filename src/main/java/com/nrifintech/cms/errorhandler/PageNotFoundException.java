package com.nrifintech.cms.errorhandler;

public class PageNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private static final String message = "Page not found.";

	public PageNotFoundException() {
		super(message);
	}

}
