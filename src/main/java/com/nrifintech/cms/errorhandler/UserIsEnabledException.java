package com.nrifintech.cms.errorhandler;

public class UserIsEnabledException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private static final String message = "User is Enabled.";

	public UserIsEnabledException() {
		super(message);
	}
	public UserIsEnabledException(String errMessage) {
		super(errMessage);
	}
}
