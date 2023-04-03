package com.nrifintech.cms.controllers;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nrifintech.cms.dtos.ItemDto;
import com.nrifintech.cms.entities.Item;
import com.nrifintech.cms.errorhandler.ImageFailureException;
import com.nrifintech.cms.errorhandler.NotFoundException;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.ItemService;
import com.nrifintech.cms.types.Response;
import com.nrifintech.cms.utils.ErrorHandlerImplemented;
/**
 * It's a Spring controller that handles requests to the `/item` endpoint
 */

@RestController
@RequestMapping(Route.Item.prefix)
public class ItemController {

	@Autowired
	private ItemService itemService;

	/**
	 * > This function returns a list of items
	 * 
	 * @return A Response object with a list of items and a status of OK.
	 */
	@GetMapping(Route.Item.getItems)
	public Response getItems() {
		return Response.set(itemService.getItems(), HttpStatus.OK);
	}

	/**
	 * > This function returns an item with the given itemId
	 * 
	 * @param itemId The id of the item to be retrieved.
	 * @return Response object
	 */
	@ErrorHandlerImplemented(handler = NotFoundException.class)
	@GetMapping(Route.Item.getItem + "/{itemId}")
	public Response getItem(@PathVariable Integer itemId) {

		Item item = itemService.getItem(itemId);

		return Response.set(item, HttpStatus.OK);

	}

	/**
	 * > This function adds an item to the database
	 * 
	 * @param itemDto The itemDto is the object that is sent from the frontend.
	 * @return A Response object.
	 * @throws ImageFailureException
	 */
	@PostMapping(Route.Item.addItem)
	public Response addItem(@RequestBody ItemDto itemDto) throws IOException, NoSuchAlgorithmException, ImageFailureException {
		Item item = new Item(itemDto);
		Item i = itemService.addItem(item);
		if (itemService.isNotNull(i))
			return Response.setMsg("Item added.", HttpStatus.OK);

		return Response.setErr("Item exists.", HttpStatus.BAD_REQUEST);
	}

	/**
	 * > This function adds a list of items to the database
	 * 
	 * @param items The list of items to be added.
	 * @return A list of items.
	 */
	@PostMapping(Route.Item.addItems)
	public Response addItems(@RequestBody List<Item> items) {
		List<Item> i = itemService.addItems(items);

		if (!i.isEmpty())
			return Response.setMsg("Items added.", HttpStatus.OK);

		return Response.setErr("Error adding item.", HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
