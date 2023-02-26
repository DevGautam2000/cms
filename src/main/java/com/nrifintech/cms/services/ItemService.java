package com.nrifintech.cms.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nrifintech.cms.entities.CartItem;
import com.nrifintech.cms.entities.Item;
import com.nrifintech.cms.repositories.CartItemRepo;
import com.nrifintech.cms.repositories.ItemRepo;
import com.nrifintech.cms.utils.Validator;

@Service
public class ItemService implements Validator {

	@Autowired
	private ItemRepo itemRepo;

	@Autowired
	private CartItemRepo cartItemRepo;
	// add a food
	public Item addItem(Item item) {
		return itemRepo.save(item);
	}

	// get a food
	public Item getItem(Integer id) {
		return itemRepo.findById(id).orElse(null);
	}

	// add foods
	public List<Item> addItems(List<Item> items) {
		return itemRepo.saveAll(items);
	}

	// get foods
	public List<Item> getItems() {
		return itemRepo.findAll();
	}

	public List<CartItem> getCartItems() {
		return cartItemRepo.findAll();
	}
	
	public List<Item> getItems(List<String> itemIds) {
		List<Item> allItems = this.getItems();
		List<Item> items = allItems.stream().filter(item -> itemIds.contains(item.getId().toString()))
				.collect(Collectors.toList());

		return items;
	}
	
	public List<CartItem> getCartItems(List<Integer> itemIds) {
		List<CartItem> allItems = this.getCartItems();
		List<CartItem> items = allItems.stream().filter(item -> itemIds.contains(item.getId()))
				.collect(Collectors.toList());

		return items;
	}

}
