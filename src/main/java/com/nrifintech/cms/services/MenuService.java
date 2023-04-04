package com.nrifintech.cms.services;

import java.security.Principal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nrifintech.cms.dtos.MenuUpdateRequest;
import com.nrifintech.cms.entities.Item;
import com.nrifintech.cms.entities.Menu;
import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.errorhandler.NotFoundException;
import com.nrifintech.cms.repositories.MenuRepo;
import com.nrifintech.cms.types.Approval;
import com.nrifintech.cms.types.Role;
import com.nrifintech.cms.types.WeekDay;
import com.nrifintech.cms.utils.SameRoute;
import com.nrifintech.cms.utils.Validator;

@Service
public class MenuService implements Validator {

	@Autowired
	private MenuRepo menuRepo;

	@Autowired
	private ItemService itemService;

	@Autowired
	private UserService userService;

/**
 * It takes a menu object, saves it to the database, and returns the saved menu object
 * 
 * @param menu The menu object that you want to add to the database.
 * @return The menu object is being returned.
 */
	public Menu addMenu(Menu menu) {
		return menuRepo.save(menu);

	}

	/**
	 * The function takes a menu object as a parameter and saves it to the database
	 * 
	 * @param menu The menu object that is being saved.
	 * @return The menu object is being returned.
	 */
	public Menu saveMenu(Menu menu) {
		return menuRepo.save(menu);

	}

	/**
	 * If the menu exists, return it. If it doesn't, throw an exception
	 * 
	 * @param menuId The id of the menu to retrieve.
	 * @return A menu object
	 */
	public Menu getMenu(Integer menuId) {

		return menuRepo.findById(menuId)
			.orElseThrow(() -> new NotFoundException("Menu"));
	}

	/**
	 * If the user is an admin, return all menus that are not incomplete
	 * 
	 * @param principal This is the user who is logged in.
	 * @return A list of menus
	 */
	public List<Menu> getAllMenu(Principal principal) {
		
		User user = userService.getuser(principal.getName());
		List<Menu> menus = menuRepo.findAll();
		
		if(userService.isNotNull(user) && user.getRole().equals(Role.Admin)){
				menus = menus.stream().filter(m -> !m.getApproval().equals(Approval.Incomplete))
						.collect(Collectors.toList());
		}
		return menus;
	}

	/**
	 * If the menu exists, delete it and return it. Otherwise, return null
	 * 
	 * @param menuId The id of the menu to be deleted.
	 * @return Optional<Menu>
	 */
	public Optional<Menu> removeMenu(Integer menuId) {
		Menu menu = this.getMenu(menuId);
		Optional<Menu> m = Optional.ofNullable(menu);

		if (m.isPresent()) {
			menuRepo.deleteById(menuId);
		}

		return m;
	}

/**
 * If the menu is pending, set the approval status to the value of the approvalStatusId parameter
 * 
 * @param m the menu object that is being approved
 * @param approvalStatusId This is the id of the approval status that you want to set the menu to.
 */
	public Menu approveMenu(Menu m, Integer approvalStatusId) {

		if (m.getApproval().equals(Approval.Pending)) {

			m.setApproval(Approval.values()[approvalStatusId]);
			return menuRepo.save(m);

		}
		return null;
	}

// Adding an item to a menu.
	/**
	 * It adds an item to a menu
	 * 
	 * @param menuId the id of the menu
	 * @param itemId the id of the item to be added to the menu
	 * @return The menu with the item added to it.
	 */
	public Menu addItemToMenu(Integer menuId, Integer itemId) {

		Menu m = menuRepo.findById(menuId).orElse(null);

		if (isNotNull(m)) {
			// get the food using the id
			Item f = itemService.getItem(itemId);

			if (isNotNull(f)) {
				// instead get the food id of the food as a request as add the food to the menu
				if (m.getItems()!=null && !m.getItems().contains(f)) {

					m.getItems().add(f);

					menuRepo.save(m);
				}
					return null;
			}
			return null;

		}

		return m;
	}

	/**
	 * It adds an item to a menu
	 * 
	 * @param menuUpdateRequest 
	 * @return The menu object is being returned.
	 */
	@SameRoute
	public Menu addItemToMenu(MenuUpdateRequest menuUpdateRequest) {

		Menu m = menuRepo.findById(menuUpdateRequest.getMenuId()).orElse(null);
		if (isNotNull(m)) {
			// get the food using the id
			Item f = itemService.getItem(menuUpdateRequest.getItemId());

			// instead get the food id of the food as a request as add the food to the menu
			if (isNotNull(f) && !m.getItems().contains(f)) {
					m.getItems().add(f);

					menuRepo.save(m);
			}

		}

		return m;
	}

	/**
	 * If the menu exists, and the item exists, and the menu has items, and the item is in the menu, then
	 * remove the item from the menu
	 * 
	 * @param menuId the id of the menu
	 * @param itemId The id of the item to be removed from the menu
	 * @return A Menu object
	 */
	public Menu removeItemFromMenu(Integer menuId, Integer itemId) {
		Menu m = menuRepo.findById(menuId).orElse(null);

		if (isNotNull(m)) {

			Item item = itemService.getItem(itemId);

			if (isNotNull(item)) {
				if (m.getItems()!=null && !m.getItems().isEmpty()) {
					int index = m.getItems().indexOf(item);

					m.getItems().remove(index);
					menuRepo.save(m);
				} else
					m = null;
			} else
				m = null;

		}
		return m;
	}

/**
 * It takes a menuId and a list of itemIds, finds the menu, adds the items to the menu, and returns the
 * menu
 * 
 * @param menuId The id of the menu to which the items are to be added.
 * @param itemIds [1,2,3]
 * @return A Menu object
 */
	public Menu addItemsToMenu(Integer menuId, List<String> itemIds) {

		Menu m = menuRepo.findById(menuId).orElse(null);

		if (isNotNull(m) && m.getItems()!=null) {

			List<Item> exItems = m.getItems();
			List<Item> items = new ArrayList<>();

			itemIds.forEach(itemId -> {
				Item item = itemService.getItem(Integer.valueOf(itemId));

				if (isNotNull(item) && !exItems.contains(item))
					items.add(item);

			});

			exItems.addAll(items);
			menuRepo.save(m);

		}

		return m;

	}

/**
 * It returns a list of menus for a given date, but the list is filtered based on the user's role
 * 
 * @param date the date of the menu
 * @param principal The principal object is used to get the currently logged in user.
 * @return A list of menus
 */
	public List<Menu> getMenuByDate(Date date, Principal principal) {

		User user = userService.getuser(principal.getName());
		List<Menu> menus = menuRepo.findMenuByDate(date);

		if (userService.isNotNull(user)) {

			if (user.getRole().equals(Role.Admin))
				menus = menus.stream().filter(m -> !m.getApproval().equals(Approval.Incomplete))
						.collect(Collectors.toList());
			
			if (user.getRole().equals(Role.User))
				menus = menus.stream().filter(m -> m.getApproval().equals(Approval.Approved))
						.collect(Collectors.toList());

		}
		return menus;
	}

/**
 * "If today is Saturday or Sunday, return false, otherwise return true."
 * 
 * The above function is a good example of a function that is hard to read. It's hard to read because
 * it's hard to understand what it's doing
 * 
 * @return A boolean value.
 */
	public boolean isServingToday() {
		Date date = new Date(System.currentTimeMillis());

		if (date.getDay() == WeekDay.Saturday.ordinal() || date.getDay() == WeekDay.Sunday.ordinal()) {
			return false;
		}

		return true;
	}

	/**
	 * > If the date is a Saturday or Sunday, return false, otherwise return true
	 * 
	 * @param date The date to check
	 * @return A boolean value.
	 */
	public boolean isServingToday(Date date) {
		if (date.getDay() == WeekDay.Saturday.ordinal() || date.getDay() == WeekDay.Sunday.ordinal()) {
			return false;
		}

		return true;
	}

}
