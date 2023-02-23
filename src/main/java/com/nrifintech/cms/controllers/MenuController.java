package com.nrifintech.cms.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nrifintech.cms.dtos.MenuUpdateRequest;
import com.nrifintech.cms.entities.Menu;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.MenuService;
import com.nrifintech.cms.types.Response;
import com.nrifintech.cms.utils.SameRoute;

@RestController
public class MenuController {

	@Autowired
	private MenuService menuService;

	@PostMapping(Route.Menu.addMenu)
	public Response newMenu() {

		if (menuService.isNotNull(menuService.addMenu())) {
			return Response.set("Added new menu.", HttpStatus.OK);
		}

		else
			return Response.set("Error creating menu.", HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@GetMapping(Route.Menu.getMenu + "/{id}")
	public Response getMenu(@PathVariable Integer id) {
		Optional<Menu> m = Optional.ofNullable(menuService.getMenu(id));
		if (m.isPresent())
			return Response.set(m.get(), HttpStatus.OK);

		return Response.set("Menu does not exist.", HttpStatus.BAD_REQUEST);
	}

	@GetMapping(Route.Menu.getMonthMenu)
	public Response getMonthlyMenu() {

		List<Menu> monthlyMenu = menuService.getAllMenu();
		return Response.set(monthlyMenu, HttpStatus.OK);
	}

	@PostMapping(Route.Menu.removeMenu + "/{menuId}")
	public Response removeMenu(@PathVariable Integer menuId) {

		Optional<Menu> m = menuService.removeMenu(menuId);

		if (m.isPresent())
			return Response.set("Menu removed.", HttpStatus.OK);

		return Response.set("Error removing menu.", HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@PostMapping(Route.Menu.approveMenu + "/{menuId}")
	public Response approveMenu(@PathVariable Integer menuId) {

		Menu m = menuService.approveMenu(menuId);

		if (menuService.isNotNull(m))
			return Response.set("Menu approved.", HttpStatus.OK);

		return Response.set("Menu already approved.", HttpStatus.BAD_REQUEST);

	}

	@PostMapping(Route.Menu.addToMenu + "/{menuId}/{itemId}")
	public Response addToMenu(@PathVariable Integer menuId, @PathVariable Integer itemId) {

		Menu m = menuService.addItemToMenu(menuId, itemId);
		if (menuService.isNotNull(m))
			return Response.set("Added new item to menu( id : " + menuId + " )", HttpStatus.OK);

		return Response.set("Error adding item to menu.", HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@SameRoute
	@PostMapping(Route.Menu.addToMenu)
	public Response addToMenu(@RequestBody MenuUpdateRequest updateRequest) {

		Menu m = menuService.addItemToMenu(updateRequest);
		if (menuService.isNotNull(m))
			return Response.set("Added new item to menu( id : " + updateRequest.getMenuId() + " )", HttpStatus.OK);

		return Response.set("Error adding item to menu.", HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@PostMapping(Route.Menu.addItemsToMenu + "/{menuId}/{itemIds}")
	public Response addItemsToMenu(@PathVariable Integer menuId, @PathVariable List<String> itemIds) {

		for(Object s : itemIds) {
			System.out.println(s);
		}
		
		Menu m = menuService.addItemsToMenu(menuId, itemIds);
		if (menuService.isNotNull(m))
			return Response.set("Added items to menu.", HttpStatus.OK);

		return Response.set("Error adding items to menu.", HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@PostMapping(Route.Menu.removeFromMenu + "/{menuId}/{itemId}")
	public Response removeItemFromMenu(@PathVariable Integer menuId, @PathVariable Integer itemId) {
		Menu m = menuService.removeItemFromMenu(menuId, itemId);

		if (menuService.isNotNull(m)) {
			return Response.set("Item removed from menu.", HttpStatus.OK);
		}

		return Response.set("Error removing item from menu.", HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
