package com.nrifintech.cms.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nrifintech.cms.entities.Order;
import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.repositories.UserRepo;
import com.nrifintech.cms.utils.Validator;

@Service
public class UserService implements Validator {

	@Autowired
	UserRepo userRepo;

	public User getuser(String email) {
		return userRepo.findByEmail(email).orElse(null);
	}

	public User addUser(User user) {
		User exUser  = this.getuser(user.getEmail());
		
		if(!isNotNull(exUser)) {
			userRepo.save(user);
		}
		
		return exUser;
	}

	public boolean checkPassword(User... users) {
		if (users[0].getPassword().equals(users[1].getPassword())) {
			return true;
		}

		return false;
	}

	public User removeUser(String email) {
		
		User exUser = this.getuser(email);
		if(isNotNull(exUser)) {
			userRepo.delete(exUser);
		}
		
		return exUser;
	}
	
	public void addOrders(Integer userId, List<String> orderIds) {
		User exUser = userRepo.findById(userId).orElse(null);
		
//		??TODO:  complete the orders part
//		if(isNotNull(exUser)){
//			
//			List<Order> orders = new ArrayList<>();
//			
//			orderIds.forEach(o -> {
//				
//				orders
//				
//				//TODO : add orders
//				
//			});
//			
//		}
	}

}
