package com.nrifintech.cms.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nrifintech.cms.dtos.CartItemUpdateRequest;
import com.nrifintech.cms.entities.CartItem;
import com.nrifintech.cms.entities.Item;
import com.nrifintech.cms.errorhandler.NotFoundException;
import com.nrifintech.cms.repositories.CartItemRepo;
import com.nrifintech.cms.utils.Validator;

@Service
public class CartItemService implements Validator {

	@Autowired
	private CartItemRepo cartItemRepo;

	@Autowired
	private ItemService itemService;

	public CartItem addItem(CartItem item) {
		return cartItemRepo.save(item);
	}

	public List<CartItem> saveItems(List<CartItem> items) {
		return cartItemRepo.saveAll(items);
	}

	public List<CartItem> addItems(List<CartItemUpdateRequest> reqs) {

//		for each itemid get the item ,  , then save it ;

		List<String> itemIds = reqs.stream().map(r -> r.getItemId()).collect(Collectors.toList());
		List<Item> items = itemService.getItems(itemIds);

		// then cart item
		List<CartItem> cartItems = items.stream()
				.map(item -> new CartItem(item, Integer.valueOf(reqs.get(items.indexOf(item)).getQuantity())))
				.collect(Collectors.toList());

		cartItemRepo.saveAll(cartItems);
		return cartItems;

	}

	public CartItem saveItem(CartItem item) {
		return cartItemRepo.save(item);
	}

	public CartItem getItem(Integer id) {
		return cartItemRepo.findById(id).orElseThrow(()-> new NotFoundException("CartItem"));
	}

	public Boolean deleteItem(Integer id) {

		CartItem c = this.getItem(id);

		if (isNotNull(c)) {
			cartItemRepo.deleteById(id);
			return true;
		}

		return false;
	}

	public List<CartItem> getCartItems() {
		return cartItemRepo.findAll();
	}

	public List<CartItem> getCartItems(List<CartItemUpdateRequest> reqs) {
		List<Item> allItems = itemService.getItems();

		List<Integer> itemIds = reqs.stream().map(r -> Integer.valueOf(r.getItemId())).collect(Collectors.toList());

		List<Item> items = allItems.stream().filter(item -> itemIds.contains(item.getId()))
				.collect(Collectors.toList());

		List<CartItem> cartItems = items.stream()
				.map(item -> new CartItem(item, Integer.valueOf(reqs.get(items.indexOf(item)).getQuantity())))
				.collect(Collectors.toList());

		return cartItems;
	}
}
