package com.nrifintech.cms.services;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.JsonObject;
import com.nrifintech.cms.entities.TokenBlacklist;
import com.nrifintech.cms.repositories.TokenBlacklistRepo;

import lombok.RequiredArgsConstructor;


/**
 * This class is responsible for adding the JWT to the blacklist when the user logs out.
 */
@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler{
    @Autowired
    private TokenBlacklistRepo tokenRepository;
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;
    

   /**
    * If the token is not in the blacklist, add it to the blacklist and clear the security context
    * 
    * @param request The request object.
    * @param response The response object that will be sent to the client.
    * @param authentication The Authentication object that was created during the login process.
    */
    @Override
    public void logout(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            return;
        
        jwt = authHeader.substring(7);
        TokenBlacklist storedToken = tokenRepository.findById(jwt).orElse(null);
        
        if(storedToken == null) {
            tokenRepository.save(new TokenBlacklist(jwt));
            SecurityContextHolder.clearContext();
      }

      
      ObjectMapper mapper = new ObjectMapper();
      mapper.enable(SerializationFeature.INDENT_OUTPUT);
      String msg="";
        try {
            response.setContentType("application/json");
            JsonObject json = new JsonObject();
            json.addProperty("success", true);
            json.addProperty("status", HttpStatus.OK.value());
            json.addProperty("message", "Your success message here");
            response.getOutputStream().write(json.toString().getBytes());
        } catch (JsonProcessingException e) {
			resolver.resolveException(request, response, null, e);
        }catch (IOException e) {
			resolver.resolveException(request, response, null, e);
        }
        


    }
}
