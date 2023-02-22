package com.nrifintech.cms.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	public void addUser(User user) {
		userRepo.save(user);
	}

	public boolean checkPassword(User... users) {
		if (users[0].getPassword().equals(users[1].getPassword())) {
			return true;
		}

		return false;
	}

}
