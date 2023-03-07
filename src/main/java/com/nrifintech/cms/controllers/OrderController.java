package com.nrifintech.cms.controllers;

import java.sql.Timestamp;
import java.util.ArrayList;
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

import com.nrifintech.cms.entities.Cart;
import com.nrifintech.cms.entities.CartItem;
import com.nrifintech.cms.entities.FeedBack;
import com.nrifintech.cms.entities.Item;
import com.nrifintech.cms.entities.Order;
import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.CartService;
import com.nrifintech.cms.services.OrderService;
import com.nrifintech.cms.services.UserService;
import com.nrifintech.cms.types.MealType;
import com.nrifintech.cms.types.Response;
import com.nrifintech.cms.types.Status;
import com.nrifintech.cms.utils.SameRoute;

@CrossOrigin
@RestController
@RequestMapping(Route.Order.prefix)
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private CartService cartService;
	
	@PostMapping(Route.Order.addOrders)
	public Response addOrders(@RequestBody List<Order> orders) {

		List<Order> o = orderService.addOrders(orders);

		if (orderService.isNotNull(o)) {
			return Response.setMsg("Orders added.", HttpStatus.OK);
		}

		return Response.setErr("Error adding orders.", HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@GetMapping(Route.Order.getOrders)
	public Response getOrders() {
		List<Order> o = orderService.getOrders();

		if (orderService.isNotNull(o)) {
			return Response.set(o, HttpStatus.OK);
		}

		return Response.setErr("Error getting orders.", HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@SameRoute
	@GetMapping(Route.Order.getOrders + "/{orderIds}")
	public Response getOrders(@PathVariable List<String> orderIds) {

		List<Order> o = orderService.getOrders(orderIds);

		if (orderService.isNotNull(o)) {
			return Response.set(o, HttpStatus.OK);
		}

		return Response.setMsg("Error getting orders.", HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@PostMapping(Route.FeedBack.addFeedback + "/{orderId}")
	public Response addFeedback(@PathVariable Integer orderId, @RequestBody FeedBack feedBack) {

		Object obj = orderService.addFeedBackToOrder(orderId, feedBack);

		if (orderService.isNotNull(obj) && obj instanceof FeedBack)
			return Response.setErr("Feedback already exists.", HttpStatus.BAD_REQUEST);

		if (orderService.isNotNull(obj)) {
			return Response.setMsg("Feedback added.", HttpStatus.OK);
		}

		return Response.setErr("Order not found.", HttpStatus.BAD_REQUEST);

	}

	@PostMapping(Route.CartItem.addItems + "/{orderId}/{itemIds}")
	public Response addItems(@PathVariable Integer orderId, @PathVariable List<String> itemIds,
			@RequestBody List<String> quantities) {

		Object obj = orderService.addItemsToOrder(orderId, itemIds, quantities);

		if (orderService.isNotNull(obj) && obj instanceof Item)
			return Response.setErr("Items already exist.", HttpStatus.BAD_REQUEST);

		if (orderService.isNotNull(obj)) {
			return Response.setMsg("Items added.", HttpStatus.OK);
		}

		return Response.setErr("Order not found.", HttpStatus.BAD_REQUEST);

	}

	@PostMapping(Route.Order.updateStatus + "/{orderId}/{statusId}")
	public Response updateOrderStatus(@PathVariable Integer orderId, @PathVariable Integer statusId) {

		Status[] status = Status.values();
		if (status[statusId].toString().equalsIgnoreCase(Status.Pending.toString()))
			return Response.setErr("Operation not allowed.", HttpStatus.BAD_REQUEST);

		Order order = orderService.getOrder(orderId);

		if (orderService.isNotNull(order)) {
			
			if (order.getStatus().toString().equalsIgnoreCase(Status.Delivered.toString()) &&
				status[statusId].toString().equalsIgnoreCase(Status.Delivered.toString())
					)
				return Response.setErr("Operation not allowed.", HttpStatus.BAD_REQUEST);

			if (status[statusId].toString().equalsIgnoreCase(Status.Delivered.toString()))
				order.setOrderDelivered(new Timestamp(System.currentTimeMillis()));
			
			order.setStatus(status[statusId]);
			orderService.saveOrder(order);

			return Response.setMsg("Order " + status[statusId].toString()  + ".", HttpStatus.OK);
		}

		return Response.setErr("Order not found.", HttpStatus.BAD_REQUEST);
	}
	
	// for Canteen users to add a new order for a normal user
		@PostMapping(Route.Order.placeOrder + "/{userId}/{mealId}")
		public Response placeOrder(@PathVariable Integer userId, @PathVariable Integer mealId) {

			if (mealId > 1)
				return Response.setErr("Invalid meal type requested.", HttpStatus.BAD_REQUEST);

			User user = userService.getuser(userId);

			if (userService.isNotNull(user)) {

				Order order = orderService.addNewOrder(MealType.values()[mealId]);

				if (orderService.isNotNull(order)) {

					Cart cart = user.getCart();

					if (cartService.isNull(cart))
						return Response.setErr("Empty Cart.", HttpStatus.BAD_REQUEST);

					List<CartItem> cartItems = cart.getCartItems();

					if (orderService.isNull(cartItems) || cartItems.isEmpty())
						return Response.setErr("Empty Cart.", HttpStatus.BAD_REQUEST);

					
					order.setCartItems(new ArrayList<>(cartItems));
					orderService.saveOrder(order);

					user.getRecords().add(order);
					user = userService.saveUser(user);

					// clear the cart after placing the order
					if (userService.isNotNull(user)) {

						user.getCart().getCartItems().clear();
						user = userService.saveUser(user);

						return Response.setMsg("Added new order for user.", HttpStatus.OK);
					}

				}

			}

			return Response.setErr("User does not exist.", HttpStatus.BAD_REQUEST);
		}


}
