package com.nrifintech.cms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nrifintech.cms.entities.Order;

public interface OrderRepo extends JpaRepository<Order,Integer>{

}
