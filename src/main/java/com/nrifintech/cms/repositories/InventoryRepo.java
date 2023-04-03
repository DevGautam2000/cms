package com.nrifintech.cms.repositories;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nrifintech.cms.entities.Inventory;

public interface InventoryRepo extends JpaRepository<Inventory,Integer>{

    /**
     * Find an inventory by id, and return it as an Optional.
     * 
     * @param id The id of the inventory item you want to find.
     * @return An Optional object that contains an Inventory object.
     */
    public Optional<Inventory> findById(Integer id);

    /**
     * Find all inventory items with the given name.
     * 
     * @param name The name of the method.
     * @return A list of Inventory objects
     */
    public List<Inventory> findByName(String name);

   /**
    * It updates the quantity in hand of a particular item in the inventory table
    * 
    * @param qty1 The quantity to be added to the existing quantity in hand.
    * @param id The id of the inventory item
    */
    @Modifying
    @Transactional
    @Query(value = "update inventory set quantity_in_hand =  Greatest((quantity_in_hand + :qty),0) where id = :id",nativeQuery = true)
    public void updateQtyInHand(@Param("qty") double qty1,@Param("id") Integer id);

    /**
     * This function updates the quantity requested of a particular inventory item
     * 
     * @param qty1 the quantity to be added to the quantity_requested column
     * @param id The id of the inventory item
     */
    @Modifying
    @Transactional
    @Query(value = "update inventory set quantity_requested = Greatest((quantity_requested + :qty),0) where id = :id",nativeQuery = true)
    public void updateQtyRequested(@Param("qty") double qty1,@Param("id") Integer id);
}
