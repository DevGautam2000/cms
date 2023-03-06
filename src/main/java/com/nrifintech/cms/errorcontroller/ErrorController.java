package com.nrifintech.cms.errorcontroller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.nrifintech.cms.errorhandler.NotFoundException;
import com.nrifintech.cms.types.Response;

interface Message {
	String payloadNotFound = "Required payload not found or wrongly passed.";
	String pageNotFound = "Required page not found.";
	String pathVariableNotFound = "Required path variable not found.";
}

@ControllerAdvice
@RestController
public class ErrorController extends ResponseEntityExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public Response handleNotFoundException(NotFoundException ex, WebRequest request) {
		return Response.set(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		return new ResponseEntity<Object>(Message.payloadNotFound, headers, status);

	}

	@Override
	protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		return new ResponseEntity<Object>(Message.pathVariableNotFound, headers, status);
	}

//	@Override
//	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
//			HttpStatus status, WebRequest request) {	
//		super.handleNoHandlerFoundException(ex, headers, status, request);
//		return Response.set(ex.getMessage(), HttpStatus.NOT_FOUND);
//	}

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		super.handleHttpRequestMethodNotSupported(ex, headers, status, request);
		return Response.set(ex.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
	}

	
}
