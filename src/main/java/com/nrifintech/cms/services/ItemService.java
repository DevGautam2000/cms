package com.nrifintech.cms.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nrifintech.cms.entities.Item;
import com.nrifintech.cms.repositories.ItemRepo;
import com.nrifintech.cms.utils.Validator;

@Service
public class ItemService implements Validator {

	@Autowired
	private ItemRepo itemRepo;

	// add a food
	public void addItem(Item item) {
		itemRepo.save(item);
	}

	// get a food
	public Item getItem(Integer id) {
		return itemRepo.findById(id).orElse(null);

	}

	// add foods
	public void addItems(List<Item> items) {
		itemRepo.saveAll(items);
	}

	// get foods
	public List<Item> getItems() {
		return itemRepo.findAll();
	}

	public List<Item> getItemsRaw() {
		return itemRepo.findAll();

	}
}
