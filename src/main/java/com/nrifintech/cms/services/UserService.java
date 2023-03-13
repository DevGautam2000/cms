package com.nrifintech.cms.services;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.stereotype.Service;
import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.entities.Wallet;
import com.nrifintech.cms.errorhandler.NotFoundException;
import com.nrifintech.cms.repositories.UserRepo;
import com.nrifintech.cms.utils.Validator;

@Service
public class UserService implements Validator {

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private WalletService walletService;

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

	
		
		
		if (isNull(exUser)) {
			Wallet w = new Wallet();
			w = walletService.save(w);
			
			if(isNotNull(w)) {
				user.setWallet(w); 
				userRepo.save(user);
				
			}
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
		return userRepo.save(user);
	}

	public User updatePassword(User user, String password) {
		user.setPassword(password);
		return this.updateUser(user.getId(), user);
	}

	public User updateUser(int id, User user) {
		user.setId(id);
		return userRepo.save(user);
	}

	public List<User> getUsers() {
		return userRepo.findAll();
	}

	public List<String> getOrdersByDate(Date date) {

		List<String> usersEmails = userRepo.getUserByOrderDate(date);
		return usersEmails;

	}

	public List<String> getOrdersByDateAndOrderType(Date date,Integer otype) {

		List<String> usersEmails = userRepo.getUserByOrderDateAndType(date,otype);
		return usersEmails;

	}

	public String getUserByOrderId(Integer orderId) {

		String userEmail = userRepo.getUserByOrderId(orderId);
		return userEmail;

	}
	
	public boolean hasUserCartitem(String email,int cartItemId){
        return userRepo.hasUserCartitem(email, cartItemId)==1;
    }

	public List<String> getAllConsumers(){
		Optional<List<String>> result = userRepo.getAllConsumers();
		if( result.isPresent()){
			return(result.get());
		}
		return(new ArrayList<>());
	}
}
