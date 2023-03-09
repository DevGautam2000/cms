package com.nrifintech.cms.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ErrorHandlerImplemented {
	Class<? extends Throwable> handler() default Exception.class;
	Class<? extends Throwable>[] handlers() default {};

	/*
	 
	 This annotation states that a error handler has been written for an 
	 entity if not found at : basepackage.errorhandler
	 
	 */
}

