package com.nrifintech.cms.controllers;

import java.util.List;

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

		menuService.addMenu();
		return Response.set("Added new menu.", HttpStatus.OK);

	}

	
	@PostMapping(Route.Menu.addToMenu + "/{menuId}/{itemId}")
	public Response addToMenu(@PathVariable Integer menuId, @PathVariable Integer itemId) {

		menuService.addItemToMenu(menuId,itemId);
		return Response.set("Added new item to menu( id : " + menuId + " )", HttpStatus.OK);

	}
	
	@SameRoute
	@PostMapping(Route.Menu.addToMenu)
	public Response addToMenu(@RequestBody MenuUpdateRequest updateRequest) {

		menuService.addItemToMenu(updateRequest);
		return Response.set("Added new item to menu( id : " + updateRequest.getMenuId() + " )", HttpStatus.OK);

	}

	@PostMapping(Route.Menu.getMenu + "/{id}")
	public Response getMenu(@PathVariable Integer id) {
		return Response.set(menuService.getMenu(id), HttpStatus.OK);
	}

	@PostMapping(Route.Menu.updateMenu)
	public Response updateMenu(@RequestBody Menu menu) {
		return Response.set(menuService.updateMenu(menu.getId(), menu.getItems().get(0)), HttpStatus.OK);
	}
	
	//TODO: get menu for the month
	@GetMapping(Route.Menu.monthMenu)
	public Response getMonthlyMenu() {
		
		List<Menu> monthlyMenu = menuService.getAllMenu();
		return Response.set(monthlyMenu, HttpStatus.OK);
	}
	

}
