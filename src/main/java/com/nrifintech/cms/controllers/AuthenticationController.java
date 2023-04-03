package com.nrifintech.cms.controllers;

import java.security.Principal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nrifintech.cms.config.MyUserDetailsService;
import com.nrifintech.cms.config.jwt.JwtRequest;
import com.nrifintech.cms.config.jwt.JwtResponse;
import com.nrifintech.cms.config.jwt.JwtUtils;
import com.nrifintech.cms.dtos.UserDto;
import com.nrifintech.cms.dtos.UserDto.Privileged;
import com.nrifintech.cms.dtos.UserDto.Unprivileged;
import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.errorhandler.UserIsDisabledException;
import com.nrifintech.cms.events.ForgotPasswordEvent;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.AuthenticationService;
import com.nrifintech.cms.services.UserService;
import com.nrifintech.cms.types.Response;
import com.nrifintech.cms.types.Role;
import com.nrifintech.cms.utils.ErrorHandlerImplemented;
import io.jsonwebtoken.JwtException;
/**
 * This class handles requests to the `/authentication` endpoint
 */

@RestController
@RequestMapping(Route.Authentication.prefix)
public class AuthenticationController {
	@Autowired
	private AuthenticationService authService;

	@Autowired
	@Lazy
	private MyUserDetailsService userDetailsServiceImple;

	@Autowired
	private UserService userService;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	/**
	 * It generates a token for the user.
	 * 
	 * @param jwtRequest The request body that contains the username and password.
	 * @return A JwtResponse object with the token.
	 */
	@ErrorHandlerImplemented(
		handlers={UsernameNotFoundException.class , UserIsDisabledException.class})
	@PostMapping(Route.Authentication.generateToken)
	public Response generateToken(@RequestBody JwtRequest jwtRequest) throws Exception {
		authService.authenticate(jwtRequest.getUsername(), jwtRequest.getPassword());

		UserDetails userDetails = this.userDetailsServiceImple.loadUserByUsername(jwtRequest.getUsername());
		String token = this.jwtUtils.generateToken(userDetails);

		return Response.set(new JwtResponse(token), HttpStatus.OK);
	}

	/**
	 * It returns the current user's information
	 * 
	 * @param principal The principal object is the user object that is returned by the authentication
	 * provider.
	 * @return A Response object.
	 */
	@GetMapping(Route.Authentication.currentUser)
	public Response getCurrentUser(Principal principal) {

		if(principal==null)throw new JwtException("Invalid Token");

		User exUser = userService.getuser(principal.getName());

		if (exUser.getRole().equals(Role.User)) {

			Unprivileged userDto = new UserDto.Unprivileged(exUser);
			return Response.set(userDto, HttpStatus.OK);

		}

		Privileged userDto = new UserDto.Privileged(exUser);
		return Response.set(userDto, HttpStatus.OK);
	}

	/**
	 * It takes in a username, generates a random token, saves it in the database, and sends an email to
	 * the user with a link to reset their password
	 * 
	 * @param user The user object that contains the username of the user who forgot their password.
	 * @return A Response object with a message and a status code.
	 */
	@PostMapping(Route.Authentication.forgotPassword)
	public Response forgotPassword(@RequestBody JwtRequest user) {
		//authService.forgetPassword(user.getUsername());
		HashMap<String,String> info = new HashMap<>();
        info.put("username",user.getUsername());
        info.put("forgotlink",authService.forgetPassword(user.getUsername()));
        applicationEventPublisher.publishEvent(new ForgotPasswordEvent(info));

		return Response.setMsg("Email sent.", HttpStatus.OK);

	}

	/**
	 * It takes a token and a JwtRequest object as parameters, and then calls the authService's
	 * changePassword function, passing in the username, token, and encoded password from the JwtRequest
	 * object
	 * 
	 * @param token The token that was sent to the user's email.
	 * @param userInfo This is the object that contains the username and password.
	 * @return A Response object with a message and a status code.
	 */
	@PostMapping(Route.Authentication.changePassword)
	public Response changePassword(@RequestParam String token, @RequestBody JwtRequest userInfo) {

		authService.changePassword(userInfo.getUsername(), token, passwordEncoder.encode(userInfo.getPassword()));
		
		return Response.setMsg("Password changed Successfully", HttpStatus.OK);
	}

	
	/**
	 * > This function sends an email to the user with a link to reset their password
	 * 
	 * @param user The user object that contains the username of the user who wants to reset their
	 * password.
	 * @return A Response object with a message and a status code.
	 */
	@PostMapping(Route.Authentication.setNewPassword)
	public Response setNewPassword(@RequestBody JwtRequest user) {

		authService.setNewPassword(user.getUsername());

		return Response.setMsg("Email sent.", HttpStatus.OK);
	}

	/**
	 * It takes a token and a JwtRequest object as parameters, and then calls the authService's
	 * setNewPasswordAndActivate function, which takes the username, token, and encoded password from the
	 * JwtRequest object as parameters
	 * 
	 * @param token The token that was sent to the user's email.
	 * @param userInfo This is the object that contains the username and password.
	 * @return A Response object with a message and a status code.
	 */
	@PostMapping(Route.Authentication.activateNewPassword)
	public Response setNewPasswordAndActivate(@RequestParam String token, @RequestBody JwtRequest userInfo) {
		
		authService.setNewPasswordAndActivate(userInfo.getUsername(), token, passwordEncoder.encode(userInfo.getPassword()));
		
		return Response.setMsg("Password changed Successfully", HttpStatus.OK);
	}
	
}