package com.nrifintech.cms.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.nrifintech.cms.types.UserStatus;



public class MyUserDetails implements UserDetails{
	private String username;
	private String password;
    private List<GrantedAuthority> authorities;

	private UserStatus status;
	private String phoneNumber;
	
	// private Role role = Role.User;

    public MyUserDetails(User user) {
        this.username = user.getEmail();
        this.password = user.getPassword();
		this.status = user.getStatus();
        // this.active = user.isActive();
        
        this.authorities = new ArrayList<GrantedAuthority>();
        this.authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));

    }

	


	
	@Override
	public String getUsername(){
		return this.username;
	}

    @Override
    public String getPassword() {
        return this.password;
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}
    
    // func. impl. left
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.status==UserStatus.Active;
	}
}
