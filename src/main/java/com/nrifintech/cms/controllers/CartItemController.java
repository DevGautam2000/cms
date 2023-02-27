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

import com.nrifintech.cms.entities.CartItem;
import com.nrifintech.cms.entities.Item;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.CartItemService;
import com.nrifintech.cms.types.Response;

@CrossOrigin
@RestController
@RequestMapping(Route.CartItem.prefix)
public class CartItemController {

	@Autowired
	private CartItemService cartItemService;

//	@GetMapping(Route.CartItem.getItems + "/{cartId}")
//	public Response getItems() {
//		return Response.set(cartItemService.getItems(cartid), HttpStatus.OK);
//	}


//	@PostMapping(Route.CartItem.addItem)
//	public Response addItem(@RequestBody CartItem item) {
//		CartItem i = cartItemService.addItem(item);
//		if (cartItemService.isNotNull(i))
//			return Response.set("Item added.", HttpStatus.OK);
//		
//		return Response.set("Error adding item.", HttpStatus.INTERNAL_SERVER_ERROR);
//	}
	
//	@PostMapping(Route.CartItem.addItems + "/{cartId}/{itemIds}")
//	public Response addItems(@PathVariable Integer cartId, @PathVariable List<String> itemIds) {
//		List<CartItem> i = cartItemService.addItems(itemsIds);
//		if (!i.isEmpty())
//			return Response.set("Items added.", HttpStatus.OK);
//		
//		return Response.set("Error adding item.", HttpStatus.INTERNAL_SERVER_ERROR);
//	}

}
