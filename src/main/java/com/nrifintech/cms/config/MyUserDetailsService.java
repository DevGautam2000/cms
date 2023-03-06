package com.nrifintech.cms.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nrifintech.cms.entities.MyUserDetails;
import com.nrifintech.cms.repositories.UserRepo;



@Service
public class MyUserDetailsService implements UserDetailsService {
	List<User> users=new ArrayList();
	@Autowired //@Lazy
    UserRepo userRepo;

	//remove this constructor before deployment
	public MyUserDetailsService() {
		super();
		users.add((User)User
	            .builder()
				.username("user")
	            .password("password")
	            .roles("USER")
	            .build());

		users.add((User)User
	            .builder()
				.username("admin")
	            .password("password")
	            .roles("ADMIN")
	            .build());
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		for(User u : users) {
		System.out.println(u.getUsername()+u.getPassword()+" @new");
			if(u.getUsername().equals(username)) {
				System.out.println(u);
				return u;
			}
		}


        Optional<com.nrifintech.cms.entities.User> user=userRepo.findByEmail(username);
		user.orElseThrow(()->new  UsernameNotFoundException("USERNAME '"+username+"'' NOT FOUND"));
		
		return user.map(MyUserDetails::new).get();
	}

}

