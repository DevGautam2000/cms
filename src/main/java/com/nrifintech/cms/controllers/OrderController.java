package com.nrifintech.cms.controllers;

import java.security.Principal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.nrifintech.cms.dtos.OrderToken;
import com.nrifintech.cms.entities.Cart;
import com.nrifintech.cms.entities.CartItem;
import com.nrifintech.cms.entities.FeedBack;
import com.nrifintech.cms.entities.Item;
import com.nrifintech.cms.entities.Order;
import com.nrifintech.cms.entities.Transaction;
import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.entities.Wallet;
import com.nrifintech.cms.events.PlacedOrderEvent;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.CartService;
import com.nrifintech.cms.services.OrderService;
import com.nrifintech.cms.services.TransactionService;
import com.nrifintech.cms.services.UserService;
import com.nrifintech.cms.services.WalletService;
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

	@Autowired
	private WalletService walletService;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

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

		return Response.setErr("Error getting orders.", HttpStatus.INTERNAL_SERVER_ERROR);

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

			if (order.getStatus().toString().equalsIgnoreCase(Status.Delivered.toString())
					&& status[statusId].toString().equalsIgnoreCase(Status.Delivered.toString()))
				return Response.setErr("Operation not allowed.", HttpStatus.BAD_REQUEST);

			if (status[statusId].toString().equalsIgnoreCase(Status.Delivered.toString()))
				order.setOrderDelivered(new Timestamp(System.currentTimeMillis()));

			order.setStatus(status[statusId]);
			orderService.saveOrder(order);
			// email code
			if (order.getStatus().toString().equalsIgnoreCase(Status.Delivered.toString())) {
				// this.applicationEventPublisher.publishEvent(new DeliveredOrderEvent(new
				// OrderToken(user.getEmail(), order)));
			} else if (order.getStatus().toString().equalsIgnoreCase(Status.Cancelled.toString())) {
				// this.applicationEventPublisher.publishEvent(new CancelledOrderEvent(new
				// OrderToken(user.getEmail(), order)));
			}

			return Response.setMsg("Order " + status[statusId].toString() + ".", HttpStatus.OK);
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

			// get wallet

			Wallet wallet = user.getWallet();

			if (walletService.isNotNull(wallet)) {

				// check sufficient wallet amount

				Boolean isPayable = walletService.checkMinimumAmount(wallet);

				if (!isPayable)
					return Response.setErr("Low wallet balance.", HttpStatus.PAYMENT_REQUIRED);

				Order order = orderService.addNewOrder(MealType.values()[mealId]);

				if (orderService.isNotNull(order)) {

					Cart cart = user.getCart();

					if (cartService.isNull(cart))
						return Response.setErr("Empty Cart.", HttpStatus.BAD_REQUEST);

					List<CartItem> cartItems = cart.getCartItems();

					if (orderService.isNull(cartItems) || cartItems.isEmpty())
						return Response.setErr("Empty Cart.", HttpStatus.BAD_REQUEST);

					order.setCartItems(new ArrayList<>(cartItems));

					Integer orderTotal = 0;

					for (CartItem item : order.getCartItems()) {
						Integer price = (int) (item.getPrice() * item.getQuantity());
						orderTotal += price;
					}
					;

					orderService.saveOrder(order);

					user.getRecords().add(order);
					user = userService.saveUser(user);

					// clear the cart after placing the order
					if (userService.isNotNull(user)) {

						user.getCart().getCartItems().clear();
						user = userService.saveUser(user);

						List<Object> orderObjects = walletService.updateWallet(wallet, orderTotal);

						wallet = (Wallet) orderObjects.get(0);
						Transaction transaction = (Transaction) orderObjects.get(1);

						if (walletService.isNotNull(wallet) && transactionService.isNotNull(transaction)) {

							order.setTransaction(transaction);
							orderService.saveOrder(order);

							walletService.save(wallet);

							this.applicationEventPublisher
									.publishEvent(new PlacedOrderEvent(new OrderToken(user.getEmail(), order)));
							return Response.setMsg("Added new order for user.", HttpStatus.OK);
						}

						return Response.setErr("Error placing order.", HttpStatus.OK);

					}

				}

			}
			return Response.setErr("Wallet not found.", HttpStatus.BAD_REQUEST);
		}

		return Response.setErr("User does not exist.", HttpStatus.BAD_REQUEST);
	}
	
	

	@PostMapping(Route.Order.cancelOrder + "/{orderId}")
	public Response cancelOrder(Principal principal, @PathVariable Integer orderId) {

		User user = userService.getuser(principal.getName());

		Order order = orderService.getOrder(orderId);

		if (orderService.isNotNull(order)) {

			Timestamp prevTimestamp = order.getOrderPlaced();
			Timestamp currTimestamp = new Timestamp(System.currentTimeMillis());

			Boolean isServicable = orderService.getServerEpoch(prevTimestamp, currTimestamp);

			if (isServicable) {
				if (user.getRecords().contains(order)) {

					// refund back to wallet

					if (order.getStatus().equals(Status.Cancelled))
						return Response.setErr("Order already cancelled.", HttpStatus.BAD_REQUEST);

					Transaction transaction = order.getTransaction();

					Wallet wallet = user.getWallet();

					if (walletService.isNotNull(wallet)) {

						if (wallet.getTransactions().contains(transaction)) {

							wallet = walletService.refundToWallet(wallet, transaction.getAmount(), order.getId());

							// cancle the order
							order.setStatus(Status.Cancelled);
							order = orderService.saveOrder(order);

							if (orderService.isNotNull(order))
								return Response.setMsg("Order Cancelled.", HttpStatus.OK);
						}

					}

					return Response.setErr("Wallet not found.", HttpStatus.NOT_FOUND);
				}

				return Response.setErr("Order does not exist for user.", HttpStatus.UNAUTHORIZED);

			}

			return Response.setErr("Order cannot be cancelled.", HttpStatus.NOT_ACCEPTABLE);

		}

		return Response.setErr("Order not found.", HttpStatus.NOT_FOUND);

	}
}
