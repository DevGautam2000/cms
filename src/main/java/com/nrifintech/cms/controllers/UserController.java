package com.nrifintech.cms.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nrifintech.cms.dtos.UserDto;
import com.nrifintech.cms.dtos.UserDto.Privileged;
import com.nrifintech.cms.dtos.UserDto.Unprivileged;
import com.nrifintech.cms.entities.Order;
import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.OrderService;
import com.nrifintech.cms.services.UserService;
import com.nrifintech.cms.types.MealType;
import com.nrifintech.cms.types.Response;
import com.nrifintech.cms.types.Role;
import com.nrifintech.cms.utils.ForDevelopmentOnly;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private OrderService orderService;

	@ForDevelopmentOnly
	@PostMapping(Route.User.addUser)
	public Response addUser(@RequestBody User user) {
		User u = userService.addUser(user);

		if (userService.isNotNull(u))
			return Response.set("User already exists.", HttpStatus.BAD_REQUEST);

		return Response.set("User added.", HttpStatus.OK);
	}
	
	
	@GetMapping(Route.User.getUser)
	public Response getUser(@RequestBody User user) {

		User exUser = userService.getuser(user.getEmail());

		if (userService.isNotNull(exUser)) {

			// check password
			if (userService.checkPassword(user, exUser)) {
				
				if(exUser.getRole().equals(Role.User)) {
					
					Unprivileged userDto = new UserDto.Unprivileged(exUser);
					return Response.set( userDto, HttpStatus.OK);
					
				}
				
				Privileged userDto = new UserDto.Privileged(exUser);
				return Response.set( userDto, HttpStatus.OK);
			}
			else
				return Response.set("Incorrect Password.", HttpStatus.BAD_REQUEST);
		}

		return Response.set("User does not exist.", HttpStatus.BAD_REQUEST);

	}

	

	@PostMapping(Route.User.removeUser)
	public Response removeUser(@RequestBody User user) {

		User u = userService.removeUser(user.getEmail());

		if (userService.isNotNull(u)) {
			return Response.set("User removed.", HttpStatus.OK);
		}

		return Response.set("Error removing user.", HttpStatus.INTERNAL_SERVER_ERROR);

	}

	// for Canteen users to add a new order for a normal user
	@PostMapping(Route.User.placeOrder + "/{userId}/{mealId}")
	public Response placeOrder(@PathVariable Integer userId, @PathVariable Integer mealId) {

		if (mealId > 1)
			return Response.set("Invalid meal type requested.", HttpStatus.BAD_REQUEST);

		User user = userService.getuser(userId);
		
		

		if (userService.isNotNull(user)) {
			
			Order order = orderService.addNewOrder(MealType.values()[mealId]);
			
			if(orderService.isNotNull(order)) {
				user.getRecords().add(order);
				User exUser = userService.saveUser(user);
				
				if(userService.isNotNull(exUser))
					return Response.set("Added new order for user.", HttpStatus.OK);
			}
				
			
		}

		return Response.set("User does not exist.", HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping(Route.Order.getOrders+"/user/{userId}")
	public Response getOrders(@PathVariable Integer userId) {
		
		User user = userService.getuser(userId);
		List<Order> orders = user.getRecords();
		
		if(!orders.isEmpty())
			return Response.set(orders , HttpStatus.OK); 
		
		return Response.set("Error getting orders.", HttpStatus.INTERNAL_SERVER_ERROR);
		
	}

	
}
