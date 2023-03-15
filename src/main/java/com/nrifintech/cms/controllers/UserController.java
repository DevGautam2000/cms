package com.nrifintech.cms.controllers;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nrifintech.cms.entities.Order;
import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.events.AddedNewUserEvent;
import com.nrifintech.cms.events.UpdateUserStatusEvent;
import com.nrifintech.cms.repositories.UserRepo;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.UserService;
import com.nrifintech.cms.types.EmailStatus;
import com.nrifintech.cms.types.Response;
import com.nrifintech.cms.types.Role;
import com.nrifintech.cms.types.UserStatus;
import com.nrifintech.cms.utils.ForDevelopmentOnly;

import eu.bitwalker.useragentutils.Application;

@CrossOrigin
@RestController
@RequestMapping(Route.User.prefix)
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@ForDevelopmentOnly
	@PostMapping(Route.User.addUser)
	public Response addUser(@RequestBody User user) {

		if (user.getRole().ordinal() > Role.values().length)
			return Response.setErr("Invalid role for user.", HttpStatus.BAD_REQUEST);

		
		User u = userService.addUser(user);
	

		if (userService.isNotNull(u))
			return Response.setErr("User already exists.", HttpStatus.BAD_REQUEST);
		this.applicationEventPublisher.publishEvent(new AddedNewUserEvent(user));
		
		
		
		return Response.setMsg("User added.", HttpStatus.OK);
	}

	@GetMapping(Route.User.getUsers)
	public Response getUsers() {
		List<User> users = userService.getUsers();

		if (!users.isEmpty())
			return Response.set(users, HttpStatus.OK);

		return Response.setErr("No users found.", HttpStatus.BAD_REQUEST);
	}

	@PostMapping(Route.User.removeUser)
	public Response removeUser(@RequestBody User user) {

		User u = userService.removeUser(user.getEmail());

		if (userService.isNotNull(u)) {
			return Response.setMsg("User removed.", HttpStatus.OK);
		}

		return Response.setErr("Error removing user.", HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@GetMapping(Route.User.getOrders + "/{userId}")
	public Response getOrders(@PathVariable Integer userId) {
		
		boolean i = userService.hasUserCartitem("aniket@3.com", 27);
		System.out.println(i);

		User user = userService.getuser(userId);
		List<Order> orders = user.getRecords();

		if (!orders.isEmpty())
			return Response.set(orders, HttpStatus.OK);

		return Response.setMsg("Error getting orders.", HttpStatus.INTERNAL_SERVER_ERROR);

	}

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

	@GetMapping(Route.User.getEmailStatus + "/{userId}")
	public Response getEmailStatus(@PathVariable Integer userId) {

		User user = userService.getuser(userId);

		if (userService.isNotNull(user)) {
			return Response.set(user, HttpStatus.OK);
		}

		return Response.setErr("Invalid user", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
//	@GetMapping(Route.User.getAllUsersForOrderByDate + "/{date}")
//	public List<String> getAllUsersForOrderByDate(@PathVariable Date date){
//		
//		List<String> users = userService.getOrdersByDate(date);
//		return users;
//		
//		
//	}
	
//	@GetMapping("uboi/{oId}")
//	public Response getAllUsersForOrderByDate(@PathVariable Integer oId){
//		
//		String userEmail = userService.getUserByOrderId(oId);
//		return Response.setMsg(userEmail, HttpStatus.OK);
//		
//		
//	}



}
