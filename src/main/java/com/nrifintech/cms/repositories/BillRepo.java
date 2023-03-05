package com.nrifintech.cms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nrifintech.cms.entities.Bill;

public interface BillRepo extends JpaRepository<Bill, Integer>{

}
