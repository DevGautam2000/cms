package com.nrifintech.cms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nrifintech.cms.entities.Transaction;

public interface TransactionRepo extends JpaRepository<Transaction, Integer>{

}
