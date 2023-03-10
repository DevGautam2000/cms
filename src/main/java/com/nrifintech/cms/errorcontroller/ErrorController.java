package com.nrifintech.cms.errorcontroller;



import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.nrifintech.cms.errorhandler.NotFoundException;
import com.nrifintech.cms.errorhandler.UserIsDisabledException;
import com.nrifintech.cms.types.Response;

import io.jsonwebtoken.ExpiredJwtException;

interface Message {
	String payloadNotFound = "Required payload not found or wrongly passed.";
	String pageNotFound = "Required page not found.";
	String pathVariableNotFound = "Required path variable not found.";
}

@CrossOrigin
@ControllerAdvice
@RestController
public class ErrorController extends ResponseEntityExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public Response handleNotFoundException(NotFoundException ex, WebRequest request) {
		return Response.setErr(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@Override
	protected Response handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		return Response.setErr(Message.payloadNotFound, headers, status);

	}

	@Override
	protected Response handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		return Response.setErr(Message.pathVariableNotFound, headers, status);
	}

//	@Override
//	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
//			HttpStatus status, WebRequest request) {	
//		super.handleNoHandlerFoundException(ex, headers, status, request);
//		return Response.set(ex.getMessage(), HttpStatus.NOT_FOUND);
//	}

	@Override
	protected Response handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		super.handleHttpRequestMethodNotSupported(ex, headers, status, request);
		return Response.setErr(ex.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
	}

	
    @ExceptionHandler({ AccessDeniedException.class })
    public Response handleAccessDeniedException(
      Exception ex) {
    	
    	System.out.println("this was printed");
        return Response.setErr(
          "Access denied", 
          HttpStatus.FORBIDDEN);
    }

    
    
    @ExceptionHandler({ io.jsonwebtoken.ExpiredJwtException.class })
    public Response expiredJwtException(
      Exception ex, WebRequest request) {
        return Response.setErr(
          "session is terminated", 
          HttpStatus.REQUEST_TIMEOUT);
    }
	

	@ExceptionHandler({ io.jsonwebtoken.JwtException.class })
    public Response jwtException(
      Exception ex, WebRequest request) {
        return Response.setErr(
          ex.getMessage(), 
          HttpStatus.FORBIDDEN);
    }
	
	
	@ExceptionHandler({ UsernameNotFoundException.class })
    public Response userNameNotFoundException(
      Exception ex, WebRequest request) {
        return Response.setErr(
          ex.getMessage(), 
          HttpStatus.UNAUTHORIZED);
    }
	

    @ExceptionHandler({ UserIsDisabledException.class, DisabledException.class })
    public Response userIsDisabledException(
      Exception ex, WebRequest request) {
        return Response.setErr(
          ex.getMessage(), 
          HttpStatus.UNAUTHORIZED);
    }
}
