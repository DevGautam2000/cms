package com.nrifintech.cms.advice;

import java.net.http.HttpHeaders;
import java.nio.file.AccessDeniedException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalRestExceptionHandler 
  extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<Object> handleAccessDeniedException(
      Exception ex, WebRequest request) {
        return new ResponseEntity<Object>(
          "Access denied message here", 
          HttpStatus.FORBIDDEN);
    }

    
    
    @ExceptionHandler({ io.jsonwebtoken.ExpiredJwtException.class })
    public ResponseEntity<Object> expiredJwtException(
      Exception ex, WebRequest request) {
        return new ResponseEntity<Object>(
          "session is terminated", 
          HttpStatus.REQUEST_TIMEOUT);
    }
    
}