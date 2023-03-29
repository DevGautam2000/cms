package com.nrifintech.cms.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nrifintech.cms.entities.MyUserDetails;
import com.nrifintech.cms.repositories.UserRepo;

@Service
public class MyUserDetailsService implements UserDetailsService {
	@Autowired 
    UserRepo userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
		com.nrifintech.cms.entities.User user=userRepo.findByEmail(username)
			.orElseThrow(()->new  UsernameNotFoundException("USERNAME '"+username+"'' NOT FOUND"));
		
		return new MyUserDetails(user);
	}

}

