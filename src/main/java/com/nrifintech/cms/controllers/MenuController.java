package com.nrifintech.cms.controllers;

import java.security.Principal;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nrifintech.cms.entities.Menu;
import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.errorhandler.NotFoundException;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.MenuService;
import com.nrifintech.cms.services.UserService;
import com.nrifintech.cms.types.Approval;
import com.nrifintech.cms.types.Response;
import com.nrifintech.cms.types.Role;
import com.nrifintech.cms.utils.ErrorHandlerImplemented;

@CrossOrigin
@RestController
@RequestMapping(Route.Menu.prefix)
public class MenuController {

	@Autowired
	private MenuService menuService;

	@Autowired
	private UserService userService;

	@PostMapping(Route.Menu.addMenu)
	public Response newMenu(@RequestBody Menu menu) {

		if (menuService.isNotNull(menuService.addMenu(menu))) {
			return Response.setMsg("Added new menu.", HttpStatus.OK);
		}

		else
			return Response.setErr("Error creating menu.", HttpStatus.INTERNAL_SERVER_ERROR);

	}

	// TODO: route for menu submission use principal also add to sec config
	@PostMapping(Route.Menu.submitMenu + "/{id}")
	public Response sumitMenu(@PathVariable Integer id, Principal principal) {

		User user = userService.getuser(principal.getName());

		if (user.getRole().equals(Role.Canteen)) {

			Menu menu = menuService.getMenu(id);

			if (menuService.isNotNull(menu)) {
				
				if(!menu.getApproval().equals(Approval.Incomplete))
					return Response.setErr("Menu already "+menu.getApproval().toString().toLowerCase() + ".", HttpStatus.INTERNAL_SERVER_ERROR);

				menu.setApproval(Approval.Pending);
				menu = menuService.saveMenu(menu);

				if (menuService.isNotNull(menu))
					return Response.setMsg("Menu added for review.", HttpStatus.OK);

				return Response.setErr("Error adding menu.", HttpStatus.INTERNAL_SERVER_ERROR);

			}

			return Response.setErr("Menu not found.", HttpStatus.NOT_FOUND);

		}

		return Response.setErr("Menu cannot be added by user.", HttpStatus.NOT_FOUND);
	}

	@ErrorHandlerImplemented(handler = NotFoundException.class)
	@GetMapping(Route.Menu.getMenu + "/{id}")
	public Response getMenu(@PathVariable Integer id) {
		Menu m = menuService.getMenu(id);
		return Response.set(m, HttpStatus.OK);
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
			return Response.setMsg("Menu removed.", HttpStatus.OK);

		return Response.setErr("Error removing menu.", HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@PostMapping(Route.Menu.approveMenu + "/{menuId}/{approvalStatusId}")
	public Response approveMenu(@PathVariable Integer menuId, @PathVariable Integer approvalStatusId) {

		Menu m = menuService.getMenu(menuId);

		if (menuService.isNotNull(m)) {

			String menuStatus = m.getApproval().toString();
			if (!menuStatus.equalsIgnoreCase(Approval.Pending.toString()))
				return Response.setErr("Menu already " + menuStatus.toLowerCase() + ".", HttpStatus.BAD_REQUEST);

			if (menuService.approveMenu(m, approvalStatusId))
				return Response.setMsg("Menu " + Approval.values()[approvalStatusId].toString().toLowerCase() + ".",
						HttpStatus.OK);

		}

		return Response.setErr("Menu does not exist.", HttpStatus.BAD_REQUEST);

	}

	@PostMapping(Route.Item.addItems + "tomenu/{menuId}/{itemIds}")
	public Response addItemsToMenu(@PathVariable Integer menuId, @PathVariable List<String> itemIds) {

		Menu m = menuService.addItemsToMenu(menuId, itemIds);
		if (menuService.isNotNull(m))
			return Response.setMsg("Added items to menu.", HttpStatus.OK);

		return Response.setErr("Error adding items to menu.", HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@PostMapping(Route.Menu.removeFromMenu + "/{menuId}/{itemId}")
	public Response removeItemFromMenu(@PathVariable Integer menuId, @PathVariable Integer itemId) {
		Menu m = menuService.removeItemFromMenu(menuId, itemId);

		if (menuService.isNotNull(m)) {
			return Response.setMsg("Item removed from menu.", HttpStatus.OK);
		}

		return Response.setErr("Error removing item from menu.", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@SuppressWarnings("deprecation")
	@GetMapping(Route.Menu.getByDate + "/{date}")
	public Response getMenuByDate(@PathVariable Date date) {

		if (!menuService.isServingToday(date))
			return Response.setErr("No food will be served today.", HttpStatus.NOT_ACCEPTABLE);

		List<Menu> menus = menuService.getMenuByDate(date);

		if (!menus.isEmpty())
			return Response.set(menus, HttpStatus.OK);

		return Response.setErr("Menu(s) with particular date not found.", HttpStatus.BAD_REQUEST);
	}

}
