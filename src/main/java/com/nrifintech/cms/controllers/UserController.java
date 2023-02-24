package com.nrifintech.cms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.UserService;
import com.nrifintech.cms.types.Response;
import com.nrifintech.cms.utils.ForDevelopmentOnly;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping(Route.User.getUser)
	public Response getUser(@RequestBody User user) {

		User exUser = userService.getuser(user.getEmail());

		if (userService.isNotNull(exUser)) {

			// check password
			if (userService.checkPassword(user, exUser))
				return Response.set(exUser, HttpStatus.OK);
			else
				return Response.set("Incorrect Password.", HttpStatus.BAD_REQUEST);
		}

		return Response.set("User does not exist.", HttpStatus.BAD_REQUEST);

	}

	@ForDevelopmentOnly
	@PostMapping(Route.User.addUser)
	public Response addUser(@RequestBody User user) {
		User u =  userService.addUser(user);

		if(userService.isNotNull(u))
			return Response.set("User already exists.", HttpStatus.BAD_REQUEST);
		
		return Response.set("User added.", HttpStatus.OK);
	}

	@PostMapping(Route.User.removeUser)
	public Response removeUser(@RequestBody User user) {

		User u = userService.removeUser(user.getEmail());

		if (userService.isNotNull(u)) {
			return Response.set("User removed.", HttpStatus.OK);
		}

		return Response.set("Error removing user.", HttpStatus.INTERNAL_SERVER_ERROR);

	}

}
