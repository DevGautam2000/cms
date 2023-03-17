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

@Service
public class CartService implements Validator {

	@Autowired
	private CartRepo cartRepo;

	@Autowired
	private CartItemService cartItemService;

	public Cart saveCart(Cart cart) {
		return cartRepo.save(cart);
	}

	public Cart addNewCart() {
		return cartRepo.save(new Cart());
	}

	public Cart getCart(Integer id) {
		return cartRepo.findById(id).orElseThrow(() -> new NotFoundException("Cart"));
	}

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

	public Cart clearCart(Cart cart) {
		List<CartItem> cartItems = cart.getCartItems();

		List<Integer> ids = cartItems.stream().map(i -> i.getId()).collect(Collectors.toList());

		cartItems.clear();

		ids.forEach(id -> cartItemService.deleteItem(id));
		cart.getCartItems().clear();
		cart = this.saveCart(cart);

		System.out.println("CART CLEAR: " + cart);

		return cart;
	}

}
