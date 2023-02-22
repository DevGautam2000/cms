package com.nrifintech.cms.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nrifintech.cms.repositories.OrderRepo;
import com.nrifintech.cms.utils.Validator;

@Service
public class OrderService implements Validator{
	
	@Autowired private OrderRepo orderRepo;

}
