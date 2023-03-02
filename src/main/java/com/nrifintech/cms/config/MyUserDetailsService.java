package com.nrifintech.cms.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nrifintech.cms.repositories.UserRepo;



@Service
public class MyUserDetailsService implements UserDetailsService {
	List<User> users=new ArrayList();
	@Autowired @Lazy
    UserRepo userRepo;

	public MyUserDetailsService() {
		super();
		users.add((User)User//.withDefaultPasswordEncoder()
	            .builder()
				.username("user")
	            .password("password")
	            .roles("USER")
	            .build());

		users.add((User)User//.withDefaultPasswordEncoder()
	            .builder()
				.username("admin")
	            .password("password")
	            .roles("ADMIN")
	            .build());
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		System.out.println(username+"  :::::in loadUserByUsername");
//		UserDetails us= User//.withDefaultPasswordEncoder()
//	            .builder()
//				.username("user")
//	            .password("password")
//	            .roles("USER")
//	            .build();
//		System.out.println("US: "+us);
//		return us;
		for(User u : users) {
		System.out.println(u.getUsername()+u.getPassword()+" @new");
			if(u.getUsername().equals(username)) {
				System.out.println(u);
				return u;
			}
		}
        UserDetails user=(UserDetails)userRepo.findByEmail(username).orElse(null);
		user.getAuthorities().forEach(System.out::println);
        System.out.println("DBUSER *********:"+user.getUsername()+" "+user.getPassword());
        if(user!=null)return user;
		throw new UsernameNotFoundException("USERNAME NOT FOUnD");
	}





	
	// @Autowired 
	// UserRepo userRepo;

	// public User getuser(String email) {
	// 	return userRepo.findByEmail(email).orElse(null);
	// }
	
	// public User getuser(Integer id) {
	// 	return userRepo.findById(id).orElse(null);
	// }

	// public User addUser(User user) {
	// 	User exUser  = this.getuser(user.getEmail());
		
	// 	if(!isNotNull(exUser)) {
	// 		userRepo.save(user);
	// 	}
		
	// 	return exUser;
	// }

	// public boolean checkPassword(User... users) {
	// 	if (users[0].getPassword().equals(users[1].getPassword())) {
	// 		return true;
	// 	}

	// 	return false;
	// }

	// public User removeUser(String email) {
		
	// 	User exUser = this.getuser(email);
	// 	if(isNotNull(exUser)) {
	// 		userRepo.delete(exUser);
	// 	}
		
	// 	return exUser;
	// }
	
	// public User saveUser(User user) {
	// 	return userRepo.save(user);
	// }
}

