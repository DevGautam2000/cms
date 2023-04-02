package com.nrifintech.cms.config;

import org.springframework.security.core.GrantedAuthority;

/**
 * The Authority class implements the GrantedAuthority interface and is used to
 * store the authority
 * name
 */
public class Authority implements GrantedAuthority {
    private String authorityName;

   
    public Authority(String authority) {
        this.authorityName = authority;
    }

   /**
    * The getAuthority() function returns the name of the authority
    * 
    * @return The authority name.
    */
    @Override
    public String getAuthority() {
        return this.authorityName;
    }

    /**
     * The toString() method returns a string representation of the object
     * 
     * @return The toString() method is being overridden to return the superclass's toString() method
     * and the authorityName.
     */
    @Override
    public String toString() {
        return super.toString() + authorityName;
    }

}
