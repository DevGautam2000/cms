package com.nrifintech.cms.utils;

public interface Validator {

	default boolean isNotNull(Object o) {
		return o != null;
	}
	
}
