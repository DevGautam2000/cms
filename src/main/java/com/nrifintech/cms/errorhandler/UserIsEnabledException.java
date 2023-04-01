package com.nrifintech.cms.errorhandler;

public class UserIsEnabledException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "User is Enabled.";

	public UserIsEnabledException() {
		super(MESSAGE);
	}
	public UserIsEnabledException(String errMessage) {
		super(errMessage);
	}
}
