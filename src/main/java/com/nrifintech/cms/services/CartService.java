package com.nrifintech.cms.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nrifintech.cms.dtos.CartItemUpdateRequest;
import com.nrifintech.cms.entities.Cart;
import com.nrifintech.cms.entities.CartItem;
import com.nrifintech.cms.repositories.CartRepo;
import com.nrifintech.cms.utils.Validator;

@Service
public class CartService implements Validator {

	@Autowired
	private CartRepo cartRepo;

	@Autowired
	private ItemService itemService;

	public Cart saveCart(Cart cart) {
		return cartRepo.save(cart);
	}

	public Cart addNewCart() {
		return cartRepo.save(new Cart());
	}

	public Cart getCart(Integer id) {
		return cartRepo.findById(id).orElse(null);
	}

	public Cart addToCart(List<CartItemUpdateRequest> reqs, Cart cart) {

		if (isNotNull(cart)) {
			

			List<Integer> itemIds = reqs.stream().map( r -> Integer.valueOf(r.getItemId())).collect(Collectors.toList());
			
			List<CartItem> items = itemService.getCartItems(itemIds);

			List<CartItem> exItems = cart.getItems();

			if (isNotNull(exItems))
				items.removeAll(exItems);
			
			if (isNull(exItems))
				exItems = new ArrayList<>();

			exItems.addAll(items);
			this.saveCart(cart);
		}

		return cart;
	}

}
