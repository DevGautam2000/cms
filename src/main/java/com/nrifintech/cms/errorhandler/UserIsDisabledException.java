package com.nrifintech.cms.errorhandler;


public class UserIsDisabledException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "User is disabled.";

	public UserIsDisabledException() {
		super(MESSAGE);
	}
	public UserIsDisabledException(String errMessage) {
		super(errMessage);
	}

}
