package com.nrifintech.cms.services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.nrifintech.cms.dtos.MenuUpdateRequest;
import com.nrifintech.cms.entities.Item;
import com.nrifintech.cms.entities.Menu;
import com.nrifintech.cms.errorhandler.NotFoundException;
import com.nrifintech.cms.repositories.MenuRepo;
import com.nrifintech.cms.types.Approval;
import com.nrifintech.cms.types.Response;
import com.nrifintech.cms.types.WeekDay;
import com.nrifintech.cms.utils.SameRoute;
import com.nrifintech.cms.utils.Validator;

@Service
public class MenuService implements Validator {

	@Autowired
	private MenuRepo menuRepo;

	@Autowired
	private ItemService itemService;

	public Menu addMenu(Menu menu) {

		return menuRepo.save(menu);

	}

	public Menu getMenu(Integer menuId) {

		Menu m = menuRepo.findById(menuId).orElseThrow(() -> new NotFoundException("Menu"));
		return m;
	}

	public List<Menu> getAllMenu() {
		return menuRepo.findAll();
	}

	public Optional<Menu> removeMenu(Integer menuId) {
		Menu menu = this.getMenu(menuId);
		Optional<Menu> m = Optional.ofNullable(menu);

		if (m.isPresent()) {
			menuRepo.deleteById(menuId);
		}

		return m;
	}

	public boolean approveMenu(Menu m, Integer approvalStatusId) {

		if (m.getApproval().equals(Approval.Pending)) {

			m.setApproval(Approval.values()[approvalStatusId]);
			menuRepo.save(m);
			return true;

		}
		return false;
	}

	public Menu addItemToMenu(Integer menuId, Integer itemId) {

		Menu m = menuRepo.findById(menuId).orElse(null);

		if (isNotNull(m)) {
			// get the food using the id
			Item f = itemService.getItem(itemId);

			if (isNotNull(f)) {
				// instead get the food id of the food as a request as add the food to the menu
				if (!m.getItems().contains(f)) {
					m.getItems().add(f);

					// m.getFoods().add(menu.getFoods().get(0));
					menuRepo.save(m);
				} else
					return null;
			}
			return null;

		}

		return m;
	}

	@SameRoute
	public Menu addItemToMenu(MenuUpdateRequest menuUpdateRequest) {

		Menu m = menuRepo.findById(menuUpdateRequest.getMenuId()).orElse(null);
		if (isNotNull(m)) {
			// get the food using the id
			Item f = itemService.getItem(menuUpdateRequest.getItemId());

			if (isNotNull(f)) {
				// instead get the food id of the food as a request as add the food to the menu
				if (!m.getItems().contains(f)) {
					m.getItems().add(f);

					// m.getFoods().add(menu.getFoods().get(0));
					menuRepo.save(m);
				}
			}

		}

		return m;
	}

	public Menu removeItemFromMenu(Integer menuId, Integer itemId) {
		Menu m = menuRepo.findById(menuId).orElse(null);

		if (isNotNull(m)) {

			Item item = itemService.getItem(itemId);

			if (isNotNull(item)) {
				if (!m.getItems().isEmpty()) {
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

	public Menu addItemsToMenu(Integer menuId, List<String> itemIds) {

		Menu m = menuRepo.findById(menuId).orElse(null);

		if (isNotNull(m)) {

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

	public List<Menu> getMenuByDate(Date date) {
		return menuRepo.findMenuByDate(date);
	}

	public Boolean isServingToday() {
		Date date = new Date(System.currentTimeMillis());

		if (date.getDay() == WeekDay.Saturday.ordinal() || date.getDay() == WeekDay.Sunday.ordinal()) {
			return false;
		}

		return true;
	}
	
	public Boolean isServingToday(Date date ) {
		if (date.getDay() == WeekDay.Saturday.ordinal() || date.getDay() == WeekDay.Sunday.ordinal()) {
			return false;
		}

		return true;
	}

}
