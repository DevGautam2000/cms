package com.nrifintech.cms.controllers;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nrifintech.cms.dtos.UserInDto;
import com.nrifintech.cms.entities.Order;
import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.errorhandler.ImageFailureException;
import com.nrifintech.cms.events.AddedNewUserEvent;
import com.nrifintech.cms.events.UpdateUserStatusEvent;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.ImageService;
import com.nrifintech.cms.services.UserService;
import com.nrifintech.cms.types.EmailStatus;
import com.nrifintech.cms.types.Response;
import com.nrifintech.cms.types.Role;
import com.nrifintech.cms.types.UserStatus;

/**
 * > This class is a controller that handles requests to the `/user` endpoint
 */
@RestController
@RequestMapping(Route.User.prefix)
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private ImageService imageService;


	/**
	 * It adds a new user to the database.
	 * 
	 * @param userDto The user object that is sent from the frontend.
	 * @return Response object
	 * @throws ImageFailureException
	 */
	@PostMapping(Route.User.addUser)
	public Response addUser(@RequestBody UserInDto userDto) throws IOException, NoSuchAlgorithmException, ImageFailureException {
		User user= new User(userDto);
		if (user.getRole().ordinal() > Role.values().length)
			return Response.setErr("Invalid role for user.", HttpStatus.BAD_REQUEST);

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		User u = userService.addUser(user);
	

		if (userService.isNotNull(u))
			return Response.setErr("User already exists.", HttpStatus.BAD_REQUEST);

		this.applicationEventPublisher.publishEvent(new AddedNewUserEvent(user));
		
		
		return Response.setMsg("User added.", HttpStatus.OK);
	}
	
	
	
	

	/**
	 * > This function returns a list of all users in the database
	 * 
	 * @return A Response object.
	 */
	@GetMapping(Route.User.getUsers)
	public Response getUsers() {
		List<User> users = userService.getUsers();

		if (!users.isEmpty())
			return Response.set(users, HttpStatus.OK);

		return Response.setErr("No users found.", HttpStatus.BAD_REQUEST);
	}

	/**
	 * > This function updates the user information and saves it to the database
	 * 
	 * @return A Response object.
	 */
	@PostMapping(Route.User.updateUser)
	public Response updateUser(@RequestBody UserInDto userDto) {
		User user = new User(userDto);
		
		
		
		User exUser = userService.getuser(user.getId());

		if (userService.isNotNull(exUser)){
			
			exUser.setPhoneNumber(user.getPhoneNumber());
			if(user.getStatus()!= null)
				exUser.setStatus(user.getStatus());
			
			
			userService.saveUser(exUser);
			
			return Response.setMsg("User updated.", HttpStatus.OK);
		}

		return Response.setErr("User not found.", HttpStatus.NOT_FOUND);
	}
	
	
	/**
	 * It removes a user from the database.
	 * 
	 * @param userDto This is the object that will be sent to the server.
	 * @return A Response object.
	 */
	@PostMapping(Route.User.removeUser)
	public Response removeUser(@RequestBody UserInDto userDto) throws IOException, NoSuchAlgorithmException {
		
		User u = userService.removeUser(userDto.getEmail());

		if (userService.isNotNull(u)) {
			return Response.setMsg("User removed.", HttpStatus.OK);
		}

		return Response.setErr("Error removing user.", HttpStatus.INTERNAL_SERVER_ERROR);

	}

	/**
	 * > Get all orders for a user
	 * 
	 * @param userId The id of the user whose orders you want to get.
	 * @return A list of orders.
	 */
	@GetMapping(Route.User.getOrders + "/{userId}")
	public Response getOrders(@PathVariable Integer userId) {

		User user = userService.getuser(userId);
		List<Order> orders = user.getRecords();

		if (!orders.isEmpty())
			return Response.set(orders, HttpStatus.OK);

		return Response.setMsg("Error getting orders.", HttpStatus.INTERNAL_SERVER_ERROR);

	}

	/**
	 * It updates the status of a user
	 * 
	 * @param userId The id of the user to update.
	 * @param statusId 0 = active, 1 = inactive
	 * @return A Response object.
	 */
	@PostMapping(Route.User.updateStatus + "/{userId}/{statusId}")
	public Response updateUserStatus(@PathVariable Integer userId, @PathVariable Integer statusId) {

		if(statusId > 1) 
			return Response.setErr("Invalid status code.",HttpStatus.BAD_REQUEST);
		
		User user = userService.getuser(userId);

		if (userService.isNotNull(user)) {

			user.setStatus(UserStatus.values()[statusId]);
			userService.saveUser(user);
			
		}
		this.applicationEventPublisher.publishEvent(new UpdateUserStatusEvent(user));
		return Response.setMsg("User status updated to: " + user.getStatus().toString().toLowerCase() + " ",
				HttpStatus.OK);
	}

	/**
	 * It toggles the subscription status of a user
	 * 
	 * @param id The id of the user
	 * @param subStatusId 0 = unsubscribed, 1 = subscribed
	 * @return A Response object.
	 */
	@GetMapping(Route.User.subscriptionToggler + "/{id}/{subStatusId}")
	public Response subsciptionToggler(@PathVariable int id, @PathVariable int subStatusId){
		if(subStatusId > 1) 
			return Response.setErr("Invalid status code.",HttpStatus.BAD_REQUEST);
		
		User user = userService.getuser(id);

		if (userService.isNotNull(user)) {

			user.setEmailStatus(EmailStatus.values()[subStatusId]);
			userService.saveUser(user);
			
		}
		return Response.set("User " + user.getEmailStatus().toString().toLowerCase() + " ",
				HttpStatus.OK);
	}

	/**
	 * > This function returns the email status of a user
	 * 
	 * @param userId The user's id
	 * @return A Response object is being returned.
	 */
	@GetMapping(Route.User.getEmailStatus + "/{userId}")
	public Response getEmailStatus(@PathVariable Integer userId) {

		User user = userService.getuser(userId);

		if (userService.isNotNull(user)) {
			return Response.set(user, HttpStatus.OK);
		}

		return Response.setErr("Invalid user", HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
