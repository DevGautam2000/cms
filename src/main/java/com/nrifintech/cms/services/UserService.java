package com.nrifintech.cms.services;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.nrifintech.cms.dtos.EmailModel;
import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.errorhandler.NotFoundException;
import com.nrifintech.cms.events.AddedNewUserEvent;
import com.nrifintech.cms.events.UpdateUserStatusEvent;
import com.nrifintech.cms.repositories.UserRepo;
import com.nrifintech.cms.utils.Validator;

import eu.bitwalker.useragentutils.Application;

@Service
public class UserService implements Validator {

	@Autowired 
	UserRepo userRepo;
	@Autowired
	ApplicationEventPublisher applicationEventPublisher;

	public User getuser(String email) {
		return userRepo.findByEmail(email).orElseThrow(() -> new NotFoundException("User"));
	}

	private User getExistingUser(String email) {
		return userRepo.findByEmail(email).orElse(null);
	}

	public User getuser(Integer id) {
		return userRepo.findById(id).orElseThrow(() -> new NotFoundException("User"));
	}

	public User addUser(User user) {
		User exUser = this.getExistingUser(user.getEmail());

		if (!isNotNull(exUser)) {
			userRepo.save(user);
			//email code
			this.applicationEventPublisher.publishEvent(new AddedNewUserEvent(user));
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
		if (isNotNull(exUser)) {
			userRepo.delete(exUser);
		}

		return exUser;
	}

	public User saveUser(User user) {
		this.applicationEventPublisher.publishEvent(new UpdateUserStatusEvent(user));
		return userRepo.save(user);
	}

	public User updatePassword(User user,String password) {
		user.setPassword(password);
		return this.updateUser(user.getId(), user);
	}

	public User updateUser(int id,User user) {
		user.setId(id);
		return userRepo.save(user);
	}
	
	public List<User> getUsers() {
		return userRepo.findAll();
	}

}
