package com.nrifintech.cms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nrifintech.cms.entities.Cart;

public interface CartRepo extends JpaRepository<Cart,Integer>{

}
