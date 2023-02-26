package com.nrifintech.cms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nrifintech.cms.entities.CartItem;

public interface CartItemRepo extends JpaRepository<CartItem, Integer>{

}
