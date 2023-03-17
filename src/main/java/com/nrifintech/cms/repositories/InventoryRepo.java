package com.nrifintech.cms.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nrifintech.cms.entities.Inventory;

public interface InventoryRepo extends JpaRepository<Inventory,Integer>{

    public List<Inventory> findByName(String name);

    @Modifying
    @Transactional
    @Query(value = "update inventory set quantity_in_hand =  Greatest((quantity_in_hand + :qty),0) where id = :id",nativeQuery = true)
    public void updateQtyInHand(@Param("qty") double qty1,@Param("id") Integer id);

    @Modifying
    @Transactional
    @Query(value = "update inventory set quantity_requested = Greatest((quantity_requested + :qty),0) where id = :id",nativeQuery = true)
    public void updateQtyRequested(@Param("qty") double qty1,@Param("id") Integer id);
}
