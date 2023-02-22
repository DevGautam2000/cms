package com.nrifintech.cms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nrifintech.cms.dtos.MenuUpdateRequest;
import com.nrifintech.cms.entities.Menu;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.MenuService;
import com.nrifintech.cms.types.Response;

@RestController
public class MenuController {

	@Autowired
	private MenuService menuService;

	@PostMapping(Route.Menu.addMenu)
	public Response newMenu() {

		menuService.addMenu();
		return Response.set("Added new menu.", HttpStatus.OK);

	}

	@PostMapping(Route.Menu.addToMenu)
	public Response addToMenu(@RequestBody  MenuUpdateRequest menuUpdateRequest) {

		menuService.addItemToMenu(menuUpdateRequest);
		return Response.set("Added new item to menu( id : " + menuUpdateRequest.getMenuId() + " )", HttpStatus.OK);

	}

	@PostMapping(Route.Menu.getMenu)
	public Response getMenu(@RequestParam(name="id") Integer id) {
		return Response.set(menuService.getMenu(id), HttpStatus.OK);
	}

	@PostMapping(Route.Menu.updateMenu)
	public Response updateMenu(@RequestBody Menu menu) {
		return Response.set(menuService.updateMenu(menu.getId(), menu.getItems().get(0)), HttpStatus.OK);
	}
	
	//TODO: get menu for the month

}
