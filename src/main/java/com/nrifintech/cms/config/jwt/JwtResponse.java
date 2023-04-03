package com.nrifintech.cms.config.jwt;

public class JwtResponse {
    String token;

   
     public JwtResponse(String token) {
         this.token = token;
     }

     // A default constructor.
     public JwtResponse() {
     }

     /**
      * This function returns the token
      * 
      * @return The token is being returned.
      */
     public String getToken() {
         return token;
     }

     /**
      * This function sets the token to the token passed in as a parameter
      * 
      * @param token The token that you received from the server.
      */
     public void setToken(String token) {
         this.token = token;
     }
}