package com.nrifintech.cms.errorhandler;

public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private static final String message = " not found.";

	public NotFoundException(String entity) {
		super(entity + message);
	}

}
