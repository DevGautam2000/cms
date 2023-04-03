package com.nrifintech.cms.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nrifintech.cms.entities.Transaction;
import com.nrifintech.cms.repositories.TransactionRepo;
import com.nrifintech.cms.utils.Validator;

/**
 * The TransactionService class is a service class that implements the Validator interface
 */
@Service
public class TransactionService implements Validator{
	
	@Autowired
	private TransactionRepo transactionRepo;
	
	
	/**
	 * It saves a transaction to the database
	 * 
	 * @param t The transaction object that is to be saved.
	 * @return The transaction object is being returned.
	 */
	public Transaction save(Transaction t) {
		return transactionRepo.save(t);
	}
	
	/**
	 * It takes a transaction object, saves it to the database, and returns the saved object
	 * 
	 * @param t The transaction object that is to be added to the database.
	 * @return The transaction object is being returned.
	 */
	public Transaction add(Transaction t) {
		return transactionRepo.save(t);
	}

}
