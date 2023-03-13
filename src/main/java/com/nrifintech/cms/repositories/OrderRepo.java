package com.nrifintech.cms.repositories;

import javax.transaction.Transactional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.nrifintech.cms.entities.Order;

public interface OrderRepo extends JpaRepository<Order,Integer>{
    @Modifying
    @Transactional
    @Query(value = "update orders set status = 2 where status = 0 ")
    public void autoArchive();
}
