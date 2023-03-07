package com.nrifintech.cms.config;

import org.springframework.security.core.GrantedAuthority;

public class Authority implements GrantedAuthority{
    private String authority;
    public Authority(String authority){
        this.authority=authority;
    }
    @Override
    public String getAuthority() {
        return this.authority;
    }
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return super.toString()+authority;
    }
    
}