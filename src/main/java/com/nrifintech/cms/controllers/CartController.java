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

import com.nrifintech.cms.dtos.CartItemUpdateRequest;
import com.nrifintech.cms.entities.Cart;
import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.CartService;
import com.nrifintech.cms.services.UserService;
import com.nrifintech.cms.types.Response;

@CrossOrigin
@RestController
@RequestMapping(Route.Cart.prefix)
public class CartController {

	@Autowired
	private CartService cartService;

	@Autowired
	private UserService userService;

	@PostMapping(Route.Cart.addToCart + "/{userId}")
	public Response addToCart(@PathVariable Integer userId, @RequestBody List<CartItemUpdateRequest> reqs) {

		User user = userService.getuser(userId);

		if (userService.isNotNull(user)) {

			Cart cart = user.getCart();

			if (cartService.isNull(cart))
				cart = new Cart();

			if (cartService.isNotNull(cart)) {

				cart = cartService.addToCart(reqs, cart);

				if (cartService.isNotNull(cart)) {
					user.setCart(cart);
					userService.saveUser(user);

					return Response.set("Added to cart.", HttpStatus.OK);
				}

			}
		}

		return Response.set("User not found.", HttpStatus.BAD_REQUEST);

	}

	@GetMapping(Route.Cart.getCart + "/{cartId}")
	public Response getCart(@PathVariable Integer cartId) {

		Cart cart = cartService.getCart(cartId);

		if (cartService.isNotNull(cart))
			return Response.set(cart, HttpStatus.OK);

		return Response.set("Cart not found.", HttpStatus.BAD_REQUEST);
	}

}
