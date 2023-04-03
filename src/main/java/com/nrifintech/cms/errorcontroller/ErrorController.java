package com.nrifintech.cms.errorcontroller;

import java.io.IOException;

import javax.mail.MessagingException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.nrifintech.cms.errorhandler.NotFoundException;
import com.nrifintech.cms.errorhandler.UserIsDisabledException;
import com.nrifintech.cms.errorhandler.UserIsEnabledException;
import com.nrifintech.cms.types.Response;
import com.stripe.exception.StripeException;

import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import freemarker.core.ParseException;

/**
 * > This class is a controller advice handles exceptions globally across the
 * whole application
 */
@ControllerAdvice
@RestController
public class ErrorController extends ResponseEntityExceptionHandler {

  /**
   * > If the exception is a NotFoundException, return a response with the error
   * message and a 404
   * status code
   * 
   * @param ex The exception object that was thrown.
   * @return Response object
   */
  @ExceptionHandler(NotFoundException.class)
  public Response handleNotFoundException(NotFoundException ex) {
    return Response.setErr(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  /**
   * > This function is called when the request payload is not found
   * 
   * @param ex      The exception that was thrown
   * @param headers HttpHeaders object that can be used to set the response
   *                headers.
   * @param status  The HTTP status code to return.
   * @param request The current request.
   * @return A Response object with the error message and the status code.
   */
  @Override
  protected Response handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {

    return Response.setErr(ErrorMessages.PAYLOADNOTFOUND, headers, status);

  }

  /**
   * If the path variable is missing, then return a response with the error
   * message
   * "PATHVARIABLENOTFOUND"
   * 
   * @param ex      The exception that was thrown
   * @param headers The headers of the response.
   * @param status  The HTTP status code to return.
   * @param request The request that caused the error.
   * @return A Response object with the error message and the status code.
   */
  @Override
  protected Response handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers,
      HttpStatus status, WebRequest request) {
    return Response.setErr(ErrorMessages.PATHVARIABLENOTFOUND, headers, status);
  }

  /**
   * > This function is called when the request method is not supported by the
   * server
   * 
   * @param ex      The exception that was thrown
   * @param headers The headers to be written to the response
   * @param status  The status code of the response.
   * @param request The current request.
   * @return A Response object with the error message and the
   *         HttpStatus.METHOD_NOT_ALLOWED
   */
  @Override
  protected Response handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {

    super.handleHttpRequestMethodNotSupported(ex, headers, status, request);
    return Response.setErr(ex.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
  }

  /**
   * > If the user is not authenticated, return a 401 Unauthorized response
   * 
   * @param ex The exception object
   * @return A Response object with the error message and the HTTP status code.
   */
  @ExceptionHandler({ AccessDeniedException.class })
  public Response handleAccessDeniedException(
      Exception ex) {
    return Response.setErr(
        ex.getMessage(),
        HttpStatus.FORBIDDEN);
  }

  /**
   * If the JWT token is expired, then the server will not send any response to
   * the client
   * 
   * @param ex      The exception object
   * @param request The request that triggered the exception.
   */
  @ExceptionHandler({ io.jsonwebtoken.ExpiredJwtException.class })
  public void expiredJwtException(
      Exception ex, WebRequest request) {
    Response.setErr("session is terminated", HttpStatus.REQUEST_TIMEOUT);
    // handle the error to bypass and do not send any response
  }

  /**
   * > If the user is not authorized, return a 403 error
   * 
   * @param ex      The exception object
   * @param request The request that triggered the exception.
   * @return A Response object with an error message and a status code.
   */
  @ExceptionHandler({ io.jsonwebtoken.JwtException.class })
  public Response jwtException(
      Exception ex, WebRequest request) {
    return Response.setErr(
        ex.getMessage(),
        HttpStatus.FORBIDDEN);
  }

  /**
   * > If the user is not found, return a 401 error with the message "User not
   * found"
   * 
   * @param ex      The exception object
   * @param request The request that triggered the exception
   * @return A Response object with the error message and the HttpStatus.
   */
  @ExceptionHandler({ UsernameNotFoundException.class })
  public Response userNameNotFoundException(
      Exception ex, WebRequest request) {
    return Response.setErr(
        ex.getMessage(),
        HttpStatus.UNAUTHORIZED);
  }

  /**
   * > If the user is disabled, return a 401 Unauthorized response
   * 
   * @param ex      The exception object
   * @param request The request that triggered the exception
   * @return A Response object with an error message and a status code of 401.
   */

  @ExceptionHandler({ UserIsDisabledException.class, DisabledException.class })
  public Response userIsDisabledException(
      Exception ex, WebRequest request) {
    return Response.setErr(
        ex.getMessage(),
        HttpStatus.UNAUTHORIZED);
  }

  /**
   * > If the user is enabled, return a 401 error
   * 
   * @param ex      The exception object
   * @param request The request that triggered the exception
   * @return A Response object with a message and a status code.
   */
  @ExceptionHandler({ UserIsEnabledException.class })
  public Response userIsEnabledException(
      Exception ex, WebRequest request) {
    return Response.setErr(
        ex.getMessage(),
        HttpStatus.UNAUTHORIZED);
  }

  /**
   * If the token is malformed, the server will return a 400 error
   * 
   * @param ex      The exception object
   * @param request The request that triggered the exception
   */
  @ExceptionHandler({ MalformedJwtException.class, SignatureException.class })
  public void malformedToken(
      Exception ex, WebRequest request) {
    Response.setErr("Invalid token.", HttpStatus.BAD_REQUEST);
    // handle the error to bypass and do not send any response
  }

  /**
   * It handles the StripeException and sets the error message and status code.
   * 
   * @param ex      The exception object
   * @param request The request that triggered the exception
   */
  @ExceptionHandler({ StripeException.class })
  public void stripeException(
      Exception ex, WebRequest request) {
    Response.setErr(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    // handle the error to bypass and do not send any response
  }

  /**
   * > This function will catch all the exceptions that are thrown by the mail
   * service and will return
   * a response with the error message and the status code
   * 
   * @param ex      The exception object
   * @param request The request object.
   */
  @ExceptionHandler({ MessagingException.class, TemplateNotFoundException.class, MalformedTemplateNameException.class,
      ParseException.class, IOException.class, TemplateException.class })
  public void smtpException(
      Exception ex, WebRequest request) {
    Response.setErr(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    // handle the error to bypass and do not send any response
  }

  /**
   * It handles the exception and returns a response with the error message and
   * the status code.
   * 
   * @param ex      The exception object
   * @param request The request object.
   */
  @ExceptionHandler({ javax.servlet.ServletException.class })
  public void servletException(
      Exception ex, WebRequest request) {
    Response.setErr(ex.getMessage(), HttpStatus.NOT_FOUND);
    // handle the error to bypass and do not send any response
  }

  @ExceptionHandler({ImageFailureException.class})
  public Response ImageException(Exception e){
    return Response.setErr("Image is corrupt or file/format not supported", HttpStatus.BAD_REQUEST);
  }
}
