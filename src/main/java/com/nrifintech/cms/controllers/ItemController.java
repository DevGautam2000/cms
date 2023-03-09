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

import com.nrifintech.cms.entities.Item;
import com.nrifintech.cms.errorhandler.NotFoundException;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.ItemService;
import com.nrifintech.cms.types.Response;
import com.nrifintech.cms.utils.ErrorHandlerImplemented;

@CrossOrigin
@RestController
@RequestMapping(Route.Item.prefix)
public class ItemController {

	@Autowired
	private ItemService itemService;

	@GetMapping(Route.Item.getItems)
	public Response getItems() {
		return Response.set(itemService.getItems(), HttpStatus.OK);
	}

	@ErrorHandlerImplemented(handler = NotFoundException.class)
	@GetMapping(Route.Item.getItem + "/{itemId}")
	public Response getItem(@PathVariable Integer itemId) {

		Item item = itemService.getItem(itemId);

		return Response.set(item, HttpStatus.OK);

	}

	@PostMapping(Route.Item.addItem)
	public Response addItem(@RequestBody Item item) {
		Item i = itemService.addItem(item);
		if (itemService.isNotNull(i))
			return Response.setMsg("Item added.", HttpStatus.OK);

		return Response.setErr("Error adding item.", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping(Route.Item.addItems)
	public Response addItems(@RequestBody List<Item> items) {
		List<Item> i = itemService.addItems(items);
		
		if (!i.isEmpty())
			return Response.setMsg("Items added.", HttpStatus.OK);

		return Response.setErr("Error adding item.", HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
