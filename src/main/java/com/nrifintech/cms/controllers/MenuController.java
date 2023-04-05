package com.nrifintech.cms.controllers;

import java.security.Principal;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nrifintech.cms.dtos.MenuDto;
import com.nrifintech.cms.entities.Menu;
import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.errorhandler.NotFoundException;
import com.nrifintech.cms.events.MenuStatusChangeEvent;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.MenuService;
import com.nrifintech.cms.services.UserService;
import com.nrifintech.cms.types.Approval;
import com.nrifintech.cms.types.Response;
import com.nrifintech.cms.types.Role;
import com.nrifintech.cms.utils.ErrorHandlerImplemented;

/**
 * > This class is a controller that handles requests to the `/menu` endpoint
 */
@RestController
@RequestMapping(Route.Menu.prefix)
public class MenuController {

	@Autowired
	private MenuService menuService;

	@Autowired
	private UserService userService;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	/**
	 * It creates a new menu.
	 * 
	 * @param menuDto This is the object that will be sent to the server.
	 * @return A Response object.
	 */
	@PostMapping(Route.Menu.addMenu)
	public Response newMenu(@RequestBody MenuDto menuDto) {
		Menu menu = new Menu(menuDto);
		if (menuService.isNotNull(menuService.addMenu(menu))) {
			return Response.setMsg("Added new menu.", HttpStatus.OK);
		}

		else
			return Response.setErr("Error creating menu.", HttpStatus.INTERNAL_SERVER_ERROR);

	}

	/**
	 * > Submit a menu for review
	 * 
	 * @param id        The id of the menu to be submitted.
	 * @param principal This is the user who is logged in.
	 * @return A Response object.
	 */
	@PostMapping(Route.Menu.submitMenu + "/{id}")
	public Response submitMenu(@PathVariable Integer id, Principal principal) {

		User user = userService.getuser(principal.getName());

		if (user.getRole().equals(Role.Canteen)) {

			Menu menu = menuService.getMenu(id);

			if (menuService.isNotNull(menu)) {

				if (menu.getItems().isEmpty())
					return Response.setErr("Menu has no items added.", HttpStatus.BAD_REQUEST);

				if (menu.getApproval().equals(Approval.Rejected)) {

					menu.setApproval(Approval.Pending);
					menu = menuService.saveMenu(menu);

					if (menuService.isNotNull(menu)) {
						this.applicationEventPublisher.publishEvent(new MenuStatusChangeEvent(menu));
						return Response.setMsg("Menu added for review.", HttpStatus.OK);
					}
				}

				if (!menu.getApproval().equals(Approval.Incomplete))
					return Response.setErr("Menu already " + menu.getApproval().toString().toLowerCase() + ".",
							HttpStatus.INTERNAL_SERVER_ERROR);

				menu.setApproval(Approval.Pending);
				menu = menuService.saveMenu(menu);

				if (menuService.isNotNull(menu)) {
					this.applicationEventPublisher.publishEvent(new MenuStatusChangeEvent(menu));
					return Response.setMsg("Menu added for review.", HttpStatus.OK);
				}

				return Response.setErr("Error adding menu.", HttpStatus.INTERNAL_SERVER_ERROR);

			}

			return Response.setErr("Menu not found.", HttpStatus.NOT_FOUND);

		}

		return Response.setErr("Menu cannot be added by user.", HttpStatus.NOT_FOUND);
	}

	/**
	 * > This function returns a menu with the given id
	 * 
	 * @param id The id of the menu to be retrieved.
	 * @return A Response object.
	 */
	@ErrorHandlerImplemented(handler = NotFoundException.class)
	@GetMapping(Route.Menu.getMenu + "/{id}")
	public Response getMenu(@PathVariable Integer id) {
		Menu m = menuService.getMenu(id);
		return Response.set(m, HttpStatus.OK);
	}

	/**
	 * It returns the monthly menu for the user.
	 * 
	 * @param principal This is the user object that is passed to the controller.
	 * @return A list of Menu objects.
	 */
	@GetMapping(Route.Menu.getMonthMenu)
	public Response getMonthlyMenu(Principal principal) {

		List<Menu> monthlyMenu = menuService.getAllMenu(principal);
		return Response.set(monthlyMenu, HttpStatus.OK);
	}

	/**
	 * It removes a menu from the database.
	 * 
	 * @param menuId The id of the menu to be removed.
	 * @return A Response object.
	 */
	@PostMapping(Route.Menu.removeMenu + "/{menuId}")
	public Response removeMenu(@PathVariable Integer menuId) {

		Menu menu = menuService.getMenu(menuId);
		if (menu == null || menu.getApproval() != Approval.Incomplete) {
			return Response.setErr("Error removing menu.", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		Optional<Menu> m = menuService.removeMenu(menuId);

		if (m.isPresent())
			return Response.setMsg("Menu removed.", HttpStatus.OK);

		return Response.setErr("Error removing menu.", HttpStatus.INTERNAL_SERVER_ERROR);

	}

	/**
	 * > Approve a menu by setting its approval status to either `Approved` or
	 * `Rejected`
	 * 
	 * @param menuId           The id of the menu to be approved.
	 * @param approvalStatusId The status of the menu.
	 * @return A Response object.
	 */
	@PostMapping(Route.Menu.approveMenu + "/{menuId}/{approvalStatusId}")
	public Response approveMenu(@PathVariable Integer menuId, @PathVariable Integer approvalStatusId) {

		if (approvalStatusId == Approval.Incomplete.ordinal() || approvalStatusId == Approval.Pending.ordinal())
			return Response.setErr("Operation not allowed.", HttpStatus.BAD_REQUEST);

		Menu m = menuService.getMenu(menuId);

		if (menuService.isNotNull(m)) {

			String menuStatus = m.getApproval().toString();

			if (!menuStatus.equals(Approval.Pending.toString())) //!menuStatus.equals(Approval.Rejected.toString())
				return Response.setErr("Menu already " + menuStatus.toLowerCase() + ".", HttpStatus.BAD_REQUEST);

			m = menuService.approveMenu(m, approvalStatusId);
			if (menuService.isNotNull(m)) {
				this.applicationEventPublisher.publishEvent(new MenuStatusChangeEvent(m));
				return Response.setMsg("Menu " + m.getApproval().toString().toLowerCase() + ".", HttpStatus.OK);
			}

		}

		return Response.setErr("Menu does not exist.", HttpStatus.BAD_REQUEST);

	}

	/**
	 * > Add items to a menu
	 * 
	 * @param menuId  The id of the menu you want to add items to.
	 * @param itemIds a list of item ids to add to the menu
	 * @return A Response object.
	 */
	@PostMapping(Route.Item.addItems + "tomenu/{menuId}/{itemIds}")
	public Response addItemsToMenu(@PathVariable Integer menuId, @PathVariable List<String> itemIds) {

		Menu m = menuService.addItemsToMenu(menuId, itemIds);
		if (menuService.isNotNull(m))
			return Response.setMsg("Added items to menu.", HttpStatus.OK);

		return Response.setErr("Error adding items to menu.", HttpStatus.INTERNAL_SERVER_ERROR);

	}

	/**
	 * > Removes an item from a menu
	 * 
	 * @param menuId The id of the menu you want to remove an item from.
	 * @param itemId The id of the item to be removed from the menu.
	 * @return A Response object.
	 */
	@PostMapping(Route.Menu.removeFromMenu + "/{menuId}/{itemId}")
	public Response removeItemFromMenu(@PathVariable Integer menuId, @PathVariable Integer itemId) {
		Menu m = menuService.removeItemFromMenu(menuId, itemId);

		if (menuService.isNotNull(m)) {
			return Response.setMsg("Item removed from menu.", HttpStatus.OK);
		}

		return Response.setErr("Error removing item from menu.", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * It returns a list of menus for a particular date
	 * 
	 * @param date      The date for which you want to get the menu.
	 * @param principal This is the user who is logged in.
	 * @return A list of menus
	 */
	@GetMapping(Route.Menu.getByDate + "/{date}")
	public Response getMenuByDate(@PathVariable Date date, Principal principal) {

		if (!menuService.isServingToday(date))
			return Response.setErr("No food will be served tomorrow.", HttpStatus.NOT_ACCEPTABLE);

		List<Menu> menus = menuService.getMenuByDate(date, principal);

		if (!menus.isEmpty())
			return Response.set(menus, HttpStatus.OK);

		return Response.setErr("Menu(s) with particular date not found.", HttpStatus.BAD_REQUEST);
	}

}
