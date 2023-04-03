package com.nrifintech.cms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nrifintech.cms.entities.MyUserDetails;
import com.nrifintech.cms.repositories.UserRepo;

@Service
/**
 * It implements the UserDetailsService interface, which is a Spring Security interface that allows us
 * to load a user's data given their username
 */
public class MyUserDetailsService implements UserDetailsService {
	@Autowired 
    UserRepo userRepo;

	/**
	 * It takes a username as input and returns a UserDetails object
	 * 
	 * @param username The username of the user to load.
	 * @return MyUserDetails object
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
		com.nrifintech.cms.entities.User user=userRepo.findByEmail(username)
			.orElseThrow(()->new  UsernameNotFoundException("USERNAME '"+username+"'' NOT FOUND"));
		
		return new MyUserDetails(user);
	}

}

