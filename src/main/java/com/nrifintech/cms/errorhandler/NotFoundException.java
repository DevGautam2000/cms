package com.nrifintech.cms.errorhandler;

public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = " not found.";

	public NotFoundException(String entity) {
		// A static variable.
		super(entity + MESSAGE);
	}

}
