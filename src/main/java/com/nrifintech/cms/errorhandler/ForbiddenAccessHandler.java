package com.nrifintech.cms.errorhandler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * This class is a handler that will be called when the user tries to access a resource that is
 * not authorized to access by the user.
 */
public class ForbiddenAccessHandler implements AccessDeniedHandler {


    /**
     * If the user is not logged in, redirect to the login page
     * 
     * @param request The request object.
     * @param response The response object.
     * @param exc The exception that was thrown.
     */
    @Override
    public void handle(
      HttpServletRequest request,
      HttpServletResponse response, 
      AccessDeniedException exc) throws IOException, ServletException {
        
        Authentication auth 
          = SecurityContextHolder.getContext().getAuthentication();
        
        if(auth != null) {
        	throw new AccessDeniedException("Access denied.");
        }
        
    }
}