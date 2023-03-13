package com.nrifintech.cms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nrifintech.cms.entities.Purchase;

public interface PurchaseRepo extends JpaRepository<Purchase,Integer> {
}
