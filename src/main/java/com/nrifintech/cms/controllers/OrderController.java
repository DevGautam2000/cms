package com.nrifintech.cms.controllers;

import java.security.Principal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nrifintech.cms.dtos.FeedBackDto;
import com.nrifintech.cms.dtos.OrderResponseRequest;
import com.nrifintech.cms.dtos.OrderToken;
import com.nrifintech.cms.dtos.WalletEmailResponse;
import com.nrifintech.cms.entities.Cart;
import com.nrifintech.cms.entities.CartItem;
import com.nrifintech.cms.entities.FeedBack;
import com.nrifintech.cms.types.Feedback;
import com.nrifintech.cms.types.Global;
import com.nrifintech.cms.entities.Order;
import com.nrifintech.cms.entities.Transaction;
import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.entities.Wallet;

import com.nrifintech.cms.events.CancelledOrderEvent;
import com.nrifintech.cms.events.DeliveredOrderEvent;
import com.nrifintech.cms.events.PlacedOrderEvent;
import com.nrifintech.cms.events.WalletDebitEvent;
import com.nrifintech.cms.events.WalletRefundEvent;

import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.CartService;
import com.nrifintech.cms.services.MenuService;
import com.nrifintech.cms.services.OrderService;
import com.nrifintech.cms.services.TransactionService;
import com.nrifintech.cms.services.UserService;
import com.nrifintech.cms.services.WalletService;
import com.nrifintech.cms.types.MealType;
import com.nrifintech.cms.types.Response;
import com.nrifintech.cms.types.Status;
import com.nrifintech.cms.utils.SameRoute;

@RestController
@RequestMapping(Route.Order.prefix)
public class OrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private MenuService menuService;

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

	private String orderNotFoundMessage = "Order not found.";

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
	public Response addFeedback(@PathVariable Integer orderId, @RequestBody FeedBackDto feedBackDto) {
		FeedBack feedBack = new FeedBack(feedBackDto);
		Object obj = orderService.addFeedBackToOrder(orderId, feedBack);

		if (orderService.isNotNull(obj) && obj instanceof FeedBack)
			return Response.setErr("Feedback already exists.", HttpStatus.BAD_REQUEST);

		if (orderService.isNotNull(obj)) {
			return Response.setMsg("Feedback added.", HttpStatus.OK);
		}

		return Response.setErr(orderNotFoundMessage, HttpStatus.BAD_REQUEST);

	}

	@PostMapping(Route.Order.updateStatus + "/{orderId}/{statusId}")
	public Response updateOrderStatus(@PathVariable Integer orderId, @PathVariable Integer statusId , Principal principal) {

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
			order = orderService.saveOrder(order);
			System.out.println(order); // invokes lazy init...
			// email 
			this.applicationEventPublisher.publishEvent(new DeliveredOrderEvent(new OrderToken(userService.getUserByOrderId(orderId) , order)));
			System.out.println(principal.getName());
			return Response.setMsg("Order " + status[statusId].toString() + ".", HttpStatus.OK);
		}

		return Response.setErr(orderNotFoundMessage, HttpStatus.BAD_REQUEST);
	}

	// for Canteen users to add a new order for a normal user
	@PostMapping(Route.Order.placeOrder + "/{id}")
	public Response placeOrder(@PathVariable Integer id) {

		if (Boolean.FALSE.equals(menuService.isServingToday()) )
			return Response.setErr("No food will be served today.", HttpStatus.NOT_ACCEPTABLE);
		//		***********************************************

		// Get the current date and time
		LocalDateTime currentDateTime = LocalDateTime.now();

		// Get the time at 8:00 PM | 20:00 on the same day
		LocalDateTime cutoffDateTime = LocalDateTime.of(currentDateTime.toLocalDate(),
				LocalTime.of(Global.PLACE_ORDER_FREEZE_TIME, 0));

		// Check if the current time is before 8:00 PM on the same day
		if (!currentDateTime.isBefore(cutoffDateTime)) {
			return Response.setErr("Order cannot be placed after " + Global.PLACE_ORDER_FREEZE_TIME + ".",
					HttpStatus.NOT_ACCEPTABLE);
		}

        //		************************************************

		User user = userService.getuser(id);

		if (userService.isNotNull(user)) {

			// get wallet

			Wallet wallet = user.getWallet();

			if (walletService.isNotNull(wallet)) {

				// check sufficient wallet amount
				Boolean isPayable = walletService.checkMinimumAmount(wallet);

				if (Boolean.FALSE.equals(isPayable))
					return Response.setErr("Low wallet balance.", HttpStatus.NOT_ACCEPTABLE);

				Order lunchOrder = orderService.addNewOrder(MealType.Lunch);
				Order breakfastOrder = orderService.addNewOrder(MealType.Breakfast);

				if (orderService.isNotNull(lunchOrder) || orderService.isNotNull(breakfastOrder)) {

					Cart cart = user.getCart();

					if (cartService.isNull(cart))
						return Response.setErr("Empty Cart.", HttpStatus.BAD_REQUEST);

					List<CartItem> cartItems = cart.getCartItems();

					if (orderService.isNull(cartItems) || cartItems.isEmpty())
						return Response.setErr("Empty Cart.", HttpStatus.BAD_REQUEST);

					// filter lunch and bf cart items and separately get the orderTotals

					List<CartItem> lunchItems = cartItems.stream().filter(ci -> ci.getMealType().equals(MealType.Lunch))
							.collect(Collectors.toList());

					List<CartItem> breakfastItems = cartItems.stream()
							.filter(ci -> ci.getMealType().equals(MealType.Breakfast)).collect(Collectors.toList());

					lunchOrder.setCartItems(new ArrayList<>(lunchItems));
					breakfastOrder.setCartItems(new ArrayList<>(breakfastItems));

					Integer lunchOrderTotal = 0;
					Integer breakfastOrderTotal = 0;

					for (CartItem item : lunchOrder.getCartItems()) {
						Integer price = (int) (item.getPrice() * item.getQuantity());
						lunchOrderTotal += price;
					}

					for (CartItem item : breakfastOrder.getCartItems()) {
						Integer price = (int) (item.getPrice() * item.getQuantity());
						breakfastOrderTotal += price;
					}

					if (lunchOrderTotal > wallet.getBalance() + WalletService.LIMIT
							|| breakfastOrderTotal > wallet.getBalance() + WalletService.LIMIT) {
						return Response.setErr("Low wallet balance.", HttpStatus.NOT_ACCEPTABLE);
					}

					if (!lunchItems.isEmpty()) {

						orderService.saveOrder(lunchOrder);
						user.getRecords().add(lunchOrder);
					}

					if (!breakfastItems.isEmpty()) {

						orderService.saveOrder(breakfastOrder);
						user.getRecords().add(breakfastOrder);
					}

					user = userService.saveUser(user);

					// clear the cart after placing the order
					if (userService.isNotNull(user)) {

						user.getCart().getCartItems().clear();
						user = userService.saveUser(user);

						OrderResponseRequest orderResponseRequest = new OrderResponseRequest(false, false);

						if (!lunchItems.isEmpty()) {

							List<Object> lunchOrderObjects = walletService.updateWallet(wallet, lunchOrderTotal);

							wallet = (Wallet) lunchOrderObjects.get(0);
							Transaction transaction = (Transaction) lunchOrderObjects.get(1);

							if (walletService.isNotNull(wallet) && transactionService.isNotNull(transaction)) {

								lunchOrder.setTransaction(transaction);
								orderService.saveOrder(lunchOrder);

								walletService.save(wallet);

								this.applicationEventPublisher.publishEvent(
										new PlacedOrderEvent(new OrderToken(user.getEmail(), lunchOrder)));

								this.applicationEventPublisher.publishEvent(new WalletDebitEvent(
									new WalletEmailResponse(user.getEmail(), wallet.getBalance() , transaction.getAmount() , transaction.getReferenceNumber() )
									)
								);

								orderResponseRequest.setIsLunchOrdered(true);
							}

						}

						if (!breakfastItems.isEmpty()) {

							List<Object> breakfastOrderObjects = walletService.updateWallet(wallet,
									breakfastOrderTotal);

							wallet = (Wallet) breakfastOrderObjects.get(0);
							Transaction transaction = (Transaction) breakfastOrderObjects.get(1);

							if (walletService.isNotNull(wallet) && transactionService.isNotNull(transaction)) {

								breakfastOrder.setTransaction(transaction);
								orderService.saveOrder(breakfastOrder);

								walletService.save(wallet);

								this.applicationEventPublisher.publishEvent(
										new PlacedOrderEvent(new OrderToken(user.getEmail(), breakfastOrder)));

								this.applicationEventPublisher.publishEvent(new WalletDebitEvent(
										new WalletEmailResponse(user.getEmail(), wallet.getBalance() , transaction.getAmount() , transaction.getReferenceNumber() )
										)
									);

								orderResponseRequest.setIsBreakFastOrdered(true);
							}

						}

						if (orderResponseRequest.getIsBreakFastOrdered() && orderResponseRequest.getIsLunchOrdered())
							return Response.setMsg("Added 2 new orders.", HttpStatus.OK);
						else if (orderResponseRequest.getIsBreakFastOrdered()
								|| orderResponseRequest.getIsLunchOrdered())
							return Response.setMsg("Added new order.", HttpStatus.OK);

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

		// order cancellation is 12 pm

		if (orderService.isNotNull(order)) {

			if (user.getRecords().contains(order)) {

				if (order.getStatus().equals(Status.Cancelled))
					return Response.setErr("Order already cancelled.", HttpStatus.BAD_REQUEST);

				if (!order.getStatus().equals(Status.Pending))
					return Response.setErr("Order cannot be cancelled.", HttpStatus.BAD_REQUEST);

//					***********************************************

				// Get the creation date and time from the database table or file
				Timestamp creationDateTime = order.getOrderPlaced();

				// Get the current date and time
				LocalDateTime currentDateTime = LocalDateTime.now();

				// Add one day to the creation date and time and set the time to 12:00 AM
				LocalDateTime nextDay = creationDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
						.plusDays(1).with(Global.CANCEL_ORDER_FREEZE_TIME);

				// Check if the current date and time is after 12:00 AM on the next day
				if (currentDateTime.isAfter(nextDay)) {
					return Response.setErr("Order cannot be cancelled.", HttpStatus.NOT_ACCEPTABLE);
				}

//					************************************************

				// refund back to wallet

				Transaction transaction = order.getTransaction();

				Wallet wallet = user.getWallet();

				if (walletService.isNotNull(wallet) && wallet.getTransactions().contains(transaction)) {

					wallet = walletService.refundToWallet(wallet, transaction.getAmount(), order.getId());

						// cancle the order
						order.setStatus(Status.Cancelled);
						order = orderService.saveOrder(order);

						if (orderService.isNotNull(order)){
							this.applicationEventPublisher.publishEvent(new WalletRefundEvent(new WalletEmailResponse(principal.getName() , wallet.getBalance() , transaction.getAmount() , transaction.getReferenceNumber())));
							System.out.println(order); //Warning: Invokes lazy init..
							this.applicationEventPublisher.publishEvent(new CancelledOrderEvent(new OrderToken(principal.getName(), order)));
							return Response.setMsg("Order Cancelled.", HttpStatus.OK);
					}
				}
				return Response.setErr("Wallet not found.", HttpStatus.NOT_FOUND);
            } 
			return Response.setErr("Order does not exist for user.", HttpStatus.UNAUTHORIZED);
		}
        return Response.setErr(orderNotFoundMessage, HttpStatus.NOT_FOUND);
    }

	@GetMapping(Route.Order.getOrderQuantity + "/{date}")
	public Response getOrderQuantity(@PathVariable Date date){
		Map<String,Integer> responseMap = this.orderService.getOrderQuantity(date);
		if(responseMap.isEmpty()){
			return( Response.setErr("Sorry no orders as of now", HttpStatus.NOT_FOUND));
		}
		return( Response.set(responseMap,HttpStatus.OK) );
	}

}
