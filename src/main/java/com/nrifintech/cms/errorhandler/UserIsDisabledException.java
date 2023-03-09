package com.nrifintech.cms.errorhandler;


public class UserIsDisabledException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private static final String message = "User is disabled.";

	public UserIsDisabledException() {
		super(message);
	}
	public UserIsDisabledException(String errMessage) {
		super(errMessage);
	}

}
