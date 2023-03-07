package com.nrifintech.cms.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.UserService;
import com.nrifintech.cms.types.Response;
import com.nrifintech.cms.types.Role;
import com.nrifintech.cms.types.UserStatus;
import com.nrifintech.cms.utils.ForDevelopmentOnly;

@CrossOrigin
@RestController
@RequestMapping(Route.User.prefix)
public class UserController {

	@Autowired
	private UserService userService;

	@ForDevelopmentOnly
	@PostMapping(Route.User.addUser)
	public Response addUser(@RequestBody User user) {

		if (user.getRole().ordinal() > Role.values().length)
			return Response.set("Invalid role for user.", HttpStatus.BAD_REQUEST);

		User u = userService.addUser(user);

		if (userService.isNotNull(u))
			return Response.set("User already exists.", HttpStatus.BAD_REQUEST);

		return Response.set("User added.", HttpStatus.OK);
	}

	@GetMapping(Route.User.getUsers)
	public Response getUsers() {
		List<User> users = userService.getUsers();

		if (!users.isEmpty())
			return Response.set(users, HttpStatus.OK);

		return Response.set("No users found.", HttpStatus.BAD_REQUEST);
	}

	@PostMapping(Route.User.removeUser)
	public Response removeUser(@RequestBody User user) {

		User u = userService.removeUser(user.getEmail());

		if (userService.isNotNull(u)) {
			return Response.set("User removed.", HttpStatus.OK);
		}

		return Response.set("Error removing user.", HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@GetMapping(Route.User.getOrders + "/{userId}")
	public Response getOrders(@PathVariable Integer userId) {

		User user = userService.getuser(userId);
		List<Order> orders = user.getRecords();

		if (!orders.isEmpty())
			return Response.set(orders, HttpStatus.OK);

		return Response.set("Error getting orders.", HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@PostMapping(Route.User.updateStatus + "/{userId}/{statusId}")
	public Response updateUserStatus(@PathVariable Integer userId, @PathVariable Integer statusId) {

		if(statusId > 1) 
			return Response.set("Invalid status code.",HttpStatus.BAD_REQUEST);
		
		User user = userService.getuser(userId);

		if (userService.isNotNull(user)) {

			user.setStatus(UserStatus.values()[statusId]);
			userService.saveUser(user);
			
		}
		
		return Response.set("User status updated to: " + user.getStatus().toString().toLowerCase() + " ",
				HttpStatus.OK);
	}

}
