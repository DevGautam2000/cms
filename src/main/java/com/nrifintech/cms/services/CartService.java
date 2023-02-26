package com.nrifintech.cms.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nrifintech.cms.entities.Cart;
import com.nrifintech.cms.entities.Item;
import com.nrifintech.cms.repositories.CartRepo;

@Service
public class CartService {

	@Autowired 
	private CartRepo cartRepo;
	
	public Cart saveCart(Cart cart) {
		return cartRepo.save(cart);
	}
	
	
	public Cart addNewCart(Cart cart) {
		return cartRepo.save(new Cart());
	}
	
	
	
	public void addToCart(List<Item> items) {
		
		
		
	}
	
}
