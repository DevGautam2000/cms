package com.nrifintech.cms.controllers;

import java.util.List;

import javax.net.ssl.SSLEngineResult.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nrifintech.cms.entities.FeedBack;
import com.nrifintech.cms.entities.Item;
import com.nrifintech.cms.entities.Order;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.OrderService;
import com.nrifintech.cms.types.Response;
import com.nrifintech.cms.utils.SameRoute;

@CrossOrigin
@RestController
@RequestMapping(Route.Order.prefix)
public class OrderController {

	@Autowired
	private OrderService orderService;

	@PostMapping(Route.Order.addOrders)
	public Response addOrders(@RequestBody List<Order> orders) {

		List<Order> o = orderService.addOrders(orders);

		if (orderService.isNotNull(o)) {
			return Response.set("Orders added.", HttpStatus.OK);
		}

		return Response.set("Error adding orders.", HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@GetMapping(Route.Order.getOrders)
	public Response getOrders() {
		List<Order> o = orderService.getOrders();

		if (orderService.isNotNull(o)) {
			return Response.set(o, HttpStatus.OK);
		}

		return Response.set("Error getting orders.", HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@SameRoute
	@GetMapping(Route.Order.getOrders + "/{orderIds}")
	public Response getOrders(@PathVariable List<String> orderIds) {

		List<Order> o = orderService.getOrders(orderIds);

		if (orderService.isNotNull(o)) {
			return Response.set(o, HttpStatus.OK);
		}

		return Response.set("Error getting orders.", HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@PostMapping(Route.FeedBack.addFeedback + "/{orderId}")
	public Response addFeedback(@PathVariable Integer orderId, @RequestBody FeedBack feedBack) {

		Object obj = orderService.addFeedBackToOrder(orderId, feedBack);

		if (orderService.isNotNull(obj) && obj instanceof FeedBack)
			return Response.set("Feedback already exists.", HttpStatus.BAD_REQUEST);

		if (orderService.isNotNull(obj)) {
			return Response.set("Feedback added.", HttpStatus.OK);
		}

		return Response.set("Order not found.", HttpStatus.BAD_REQUEST);

	}

	@PostMapping(Route.CartItem.addItems + "/{orderId}/{itemIds}")
	public Response addItems(@PathVariable Integer orderId, @PathVariable List<String> itemIds,
			@RequestBody List<String> quantities) {

		Object obj = orderService.addItemsToOrder(orderId, itemIds, quantities);

		if (orderService.isNotNull(obj) && obj instanceof Item)
			return Response.set("Items already exist.", HttpStatus.BAD_REQUEST);

		if (orderService.isNotNull(obj)) {
			return Response.set("Items added.", HttpStatus.OK);
		}

		return Response.set("Order not found.", HttpStatus.BAD_REQUEST);

	}
	
	@PostMapping(Route.Order.updateStatus + "/{orderId}/{statusId}")
	public Response updateOrderStatus(@PathVariable Integer orderId,@PathVariable Integer statusId) {
		
		Order order = orderService.getOrder(orderId);
		
		//TODO:
//		if(orderService.isNotNull(order)) {
//			
//			order.setStatus(Status.values()[statusId]);
//			
//			
//		}
		
		
		return Response.set("Order not found.", HttpStatus.BAD_REQUEST);
	}

}
