package com.nrifintech.cms.entities;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.nrifintech.cms.config.Authority;
import com.nrifintech.cms.types.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Entity
public class User implements UserDetails {

	@Id
	@GeneratedValue
	private Integer id;

	private String username;

	@Column(unique = true)
	private String email;
	private String password;
	private String phoneNumber;
	
	private Role role = Role.User;

	
	@Column(unique=true)
	@OneToMany(fetch=FetchType.LAZY)
	private List<Order> records;
	
	@OneToOne(fetch=FetchType.EAGER)
	private Cart cart;
	


	
	// func. impl. left
	public String getUsername(){
		return this.email;
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		Set<GrantedAuthority> authorities=new HashSet();
        authorities.add(new SimpleGrantedAuthority(this.role.toString()));
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
