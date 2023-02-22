package com.nrifintech.cms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nrifintech.cms.entities.Item;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.ItemService;
import com.nrifintech.cms.types.Response;

@RestController
public class ItemController {

	@Autowired
	private ItemService itemService;

	@GetMapping(Route.Item.getItems)
	public Response getItems() {
		return Response.set(itemService.getItems(), HttpStatus.OK);
	}

	@PostMapping(Route.Item.getItem)
	public Response getItem(@PathVariable Integer itemId) {
		return Response.set(itemService.getItem(itemId), HttpStatus.OK);
	}

	@PostMapping(Route.Item.addItem)
	public Response addItem(@RequestBody Item item) {
		itemService.addItem(item);
		return Response.set("Food added.", HttpStatus.OK);
	}

}
