package com.nrifintech.cms.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nrifintech.cms.dtos.MenuDto;
import com.nrifintech.cms.dtos.MenuUpdateRequest;
import com.nrifintech.cms.entities.Item;
import com.nrifintech.cms.entities.Menu;
import com.nrifintech.cms.repositories.MenuRepo;
import com.nrifintech.cms.utils.Validator;

@Service
public class MenuService implements Validator {

	@Autowired
	private MenuRepo menuRepo;

	@Autowired
	private ItemService itemService;

	public void addMenu() {

		Menu m = new Menu();
		menuRepo.save(m);

	}

	public void addItemToMenu(MenuUpdateRequest menuUpdateRequest) {

		Menu m = menuRepo.findById(menuUpdateRequest.getMenuId()).orElse(null);
		if (isNotNull(m)) {
			// get the food using the id
			Item f =  itemService.getItem(menuUpdateRequest.getFoodId());

			if (isNotNull(f)) {
				// instead get the food id of the food as a request as add the food to the menu
				m.getItems().add(f);

//				m.getFoods().add(menu.getFoods().get(0));
				menuRepo.save(m);
			}

		}
	}
	
	public void addItemToMenu(Integer menuId, Integer itemId) {

		Menu m = menuRepo.findById(menuId).orElse(null);
		if (isNotNull(m)) {
			// get the food using the id
			Item f =  itemService.getItem(itemId);

			if (isNotNull(f)) {
				// instead get the food id of the food as a request as add the food to the menu
				m.getItems().add(f);

//				m.getFoods().add(menu.getFoods().get(0));
				menuRepo.save(m);
			}

		}
	}

	public MenuDto getMenu(Integer id) {
		Menu m = menuRepo.findById(id).orElse(null);
		
		if(isNotNull(m)) {
			return new MenuDto(m);
		}
	
		return null;
	}

	public Menu updateMenu(Integer menuId, Item food) {
		Menu m = menuRepo.findById(menuId).orElse(null);

		if (isNotNull(m)) {
			int index = m.getItems().indexOf(food);
			m.getItems().remove(index);
			menuRepo.save(m);
		}
		return m;
	}

}
