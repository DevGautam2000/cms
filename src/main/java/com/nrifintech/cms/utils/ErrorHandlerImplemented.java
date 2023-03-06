package com.nrifintech.cms.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)  
@Target(ElementType.METHOD)  
public @interface ErrorHandlerImplemented{  
	Class<?> handler();  
	
	/*
	 
	 This annotation states that a error handler has been written for an 
	 entity if not found at : basepackage.errorhandler
	 
	 */
}  