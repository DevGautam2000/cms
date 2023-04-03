package com.nrifintech.cms.config.jwt;

/**
 * It's a POJO that contains the username and password that will be sent to the
 * server
 */
public class JwtRequest {
    String username;
    String password;

    // It's a default constructor.
    public JwtRequest() {
    }

   // It's a constructor that takes username and password as parameters and sets them to the class
   // variables.
    public JwtRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * This function returns the username of the user
     * 
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * This function sets the username to the value of the parameter username.
     * 
     * @param username The username of the user to be created.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * This function returns the password of the user
     * 
     * @return The password
     */
    public String getPassword() {
        return password;
    }

    /**
     * This function sets the password of the user.
     * 
     * @param password The password to use for the connection.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
