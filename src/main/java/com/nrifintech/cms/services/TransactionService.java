package com.nrifintech.cms.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nrifintech.cms.entities.Transaction;
import com.nrifintech.cms.repositories.TransactionRepo;
import com.nrifintech.cms.utils.Validator;

@Service
public class TransactionService implements Validator{
	
	@Autowired
	private TransactionRepo transactionRepo;
	
	
	public Transaction save(Transaction t) {
		return transactionRepo.save(t);
	}
	
	public Transaction add(Transaction t) {
		return transactionRepo.save(t);
	}
	
	
	
	

}
