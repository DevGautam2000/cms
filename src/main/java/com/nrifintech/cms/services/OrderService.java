package com.nrifintech.cms.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nrifintech.cms.entities.Order;
import com.nrifintech.cms.repositories.OrderRepo;
import com.nrifintech.cms.utils.Validator;

@Service
public class OrderService implements Validator{
	
	@Autowired private OrderRepo orderRepo;
	
	
	public void addOrders(List<Order> orders) {
		
		orderRepo.saveAll(orders);
		
		//TODO: add a return type
	}

}
