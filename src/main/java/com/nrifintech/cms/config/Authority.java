package com.nrifintech.cms.config;

import org.springframework.security.core.GrantedAuthority;

public class Authority implements GrantedAuthority{
    private String authorityName;
    public Authority(String authority){
        this.authorityName=authority;
    }
    @Override
    public String getAuthority() {
        return this.authorityName;
    }
    @Override
    public String toString() {
        return super.toString()+authorityName;
    }
    
}
