package com.nrifintech.cms.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nrifintech.cms.dtos.CartItemUpdateRequest;
import com.nrifintech.cms.entities.Cart;
import com.nrifintech.cms.entities.CartItem;
import com.nrifintech.cms.errorhandler.NotFoundException;
import com.nrifintech.cms.repositories.CartRepo;
import com.nrifintech.cms.utils.Validator;

/**
 * It's a service class that handles the business logic of the cart
 */
@Service
public class CartService implements Validator {

	@Autowired
	private CartRepo cartRepo;

	@Autowired
	private CartItemService cartItemService;

	/**
	 * The function takes a cart object as a parameter and saves it to the database
	 * 
	 * @param cart The cart object that you want to save.
	 * @return The cart object is being returned.
	 */
	public Cart saveCart(Cart cart) {
		return cartRepo.save(cart);
	}

	/**
	 * // Java
	 * public Cart addNewCart() {
	 * 		return cartRepo.save(new Cart());
	 * 	}
	 * 
	 * @return A new cart is being returned.
	 */
	public Cart addNewCart() {
		return cartRepo.save(new Cart());
	}

	/**
	 * If the cart with the given id exists, return it, otherwise throw a NotFoundException
	 * 
	 * @param id The id of the cart you want to retrieve.
	 * @return A cart object
	 */
	public Cart getCart(Integer id) {
//		return cartRepo.findById(id).orElse(null);
		return cartRepo.findById(id).orElseThrow(() -> new NotFoundException("Cart"));
	}

	/**
	 * It takes a list of CartItemUpdateRequest objects, and a Cart object, and returns a Cart object
	 * 
	 * @param reqs List of CartItemUpdateRequest
	 * @param cart The cart object that is being updated
	 * @return A cart object with a list of cart items.
	 */
	public Cart addToCart(List<CartItemUpdateRequest> reqs, Cart cart) {

		if (isNotNull(cart)) {

			List<CartItem> items = cartItemService.getCartItems(reqs);

			List<CartItem> exItems = cart.getCartItems();

			// TODO: filter duplicates in items

			if (isNull(exItems))
				exItems = new ArrayList<>();

			if (isNotNull(items) || !items.isEmpty())
				items.forEach(i -> {
					i.setMealType(reqs.get(items.indexOf(i)).getMealType());
				});
			exItems.addAll(items);
			cartItemService.saveItems(exItems);

			cart.setCartItems(exItems);
		}

		return cart;
	}

	/**
	 * It clears the cart by deleting all the items in the cart and then saving the cart
	 * 
	 * @param cart The cart object that is being cleared
	 * @return A Cart object
	 */
	public Cart clearCart(Cart cart) {
		List<CartItem> cartItems = cart.getCartItems();
		List<Integer> ids = cartItems.stream().map(i -> i.getId()).collect(Collectors.toList());

		cartItems.clear();

		ids.forEach(id -> cartItemService.deleteItem(id));
		cart.getCartItems().clear();
		cart = this.saveCart(cart);

		return cart;
	}

}
