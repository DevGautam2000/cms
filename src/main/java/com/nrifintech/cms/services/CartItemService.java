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

/**
 * It takes a list of CartItemUpdateRequest objects, gets the corresponding items from the ItemService,
 * and then creates CartItem objects from the Item objects and the quantity from the
 * CartItemUpdateRequest objects
 */
@Service
public class CartItemService implements Validator {

	@Autowired
	private CartItemRepo cartItemRepo;

	@Autowired
	private ItemService itemService;

	/**
	 * It takes a CartItem object, saves it to the database, and returns the saved object
	 * 
	 * @param item The item to be added to the cart.
	 * @return The item that was added to the cart.
	 */
	public CartItem addItem(CartItem item) {
		return cartItemRepo.save(item);
	}

	/**
	 * It takes a list of CartItem objects, saves them to the database, and returns the saved objects
	 * 
	 * @param items The list of items to be saved.
	 * @return A list of CartItems
	 */
	public List<CartItem> saveItems(List<CartItem> items) {
		return cartItemRepo.saveAll(items);
	}

	/**
	 * It takes a list of CartItemUpdateRequest and returns a list of CartItem.
	 * 
	 * @param reqs List of CartItemUpdateRequest objects
	 * @return A list of CartItems
	 */
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

	/**
	 * The function takes in a CartItem object, and saves it to the database
	 * 
	 * @param item The item to be saved
	 * @return The item that was saved.
	 */
	public CartItem saveItem(CartItem item) {
		return cartItemRepo.save(item);
	}

	/**
	 * If the cart item exists, return it, otherwise throw a NotFoundException.
	 * 
	 * @param id The id of the cart item to be retrieved.
	 * @return A CartItem object
	 */
	public CartItem getItem(Integer id) {
		return cartItemRepo.findById(id).orElseThrow(()-> new NotFoundException("CartItem"));
	}


/**
 * // Java
 * public Boolean deleteItem(Integer id) {
 * 	CartItem c = this.getItem(id);
 * 	if (isNotNull(c)) {
 * 		cartItemRepo.deleteById(id);
 * 		return true;
 * 	}
 * 	return false;
 * }
 * 
 * @param id The id of the item to be deleted
 * @return A boolean value.
 */
	public Boolean deleteItem(Integer id) {

		CartItem c = this.getItem(id);

		if (isNotNull(c)) {
			cartItemRepo.deleteById(id);
			return true;
		}

		return false;
	}

	/**
	 * This function returns a list of all the cart items in the database
	 * 
	 * @return A list of all the cart items in the database.
	 */
	public List<CartItem> getCartItems() {
		return cartItemRepo.findAll();
	}

	/**
	 * Get a list of items from the database, filter them by the itemIds in the request, then map the
	 * filtered items to a list of CartItems
	 * 
	 * @param reqs List of CartItemUpdateRequest objects
	 * @return A list of CartItems
	 */
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
