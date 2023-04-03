package com.nrifintech.cms.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nrifintech.cms.entities.Inventory;
import com.nrifintech.cms.repositories.InventoryRepo;

/**
 * This class is used to perform CRUD operations on the Inventory table.
 */
@Service
public class InventoryService {

    @Autowired
    InventoryRepo inventoryRepo;
    
   /**
    * If the inventory name is not in the database, add it
    * 
    * @param inventory The inventory object that is being added to the database.
    * @return The inventory object is being returned.
    */
    public Inventory addToInventory(Inventory inventory){
        List<Inventory> i = this.inventoryRepo.findByName(inventory.getName());
        if( i.size() > 0 ){
            return( null );
        }
        return(this.inventoryRepo.save(inventory));
    }

   /**
    * This function takes a list of inventory objects and saves them to the database
    * 
    * @param inventory This is the list of inventory objects that we want to add to the database.
    * @return A list of Inventory objects.
    */
    public List<Inventory> addAlltoInventory(List<Inventory> inventory){
        return( this.inventoryRepo.saveAll(inventory) );
    }

   /**
    * If the inventoryRepo has an inventory with the id of the id parameter, return that inventory,
    * otherwise return null.
    * 
    * @param id The id of the inventory item to be retrieved.
    * @return The inventory object with the id that was passed in.
    */
    public Inventory getInventoryById(Integer id){
        return( this.inventoryRepo.findById(id).orElse(null) );
    }


   /**
    * This function returns a list of inventory objects that have the name passed in as a parameter
    * 
    * @param name The name of the inventory item
    * @return A list of inventory objects.
    */
    public List<Inventory> getInventoryByName(String name){
        return( this.inventoryRepo.findByName(name) );
    }

    /**
     * This function returns a list of all the inventory items in the database
     * 
     * @return A list of all the inventory items in the database.
     */
    public List<Inventory> getAllInventory(){
        return( this.inventoryRepo.findAll() );
    }

   /**
    * If the id is null or the inventoryRepo.findById(id) is present, return false. Otherwise, delete
    * the inventoryRepo by id and return true
    * 
    * @param id The id of the inventory item to be deleted.
    * @return A boolean value.
    */
    public boolean removeInventoryById(Integer id){
        if( id == null || this.inventoryRepo.findById(id).isPresent() ){
            return(false);
        }
        this.inventoryRepo.deleteById(id);
        return(true);
    }

   /**
    * It updates the quantity in hand of a particular item in the inventory table
    * 
    * @param qty the new quantity
    * @param id the id of the item
    */
    public void updateQtyInHand(Double qty,int id){
        this.inventoryRepo.updateQtyInHand(qty, id);
    }

   /**
    * It updates the quantity requested of a particular item in the inventory table
    * 
    * @param qty the quantity that will be added to the current quantity
    * @param id the id of the inventory item
    */
    public void updateQtyRequested(Double qty,int id){
        this.inventoryRepo.updateQtyRequested(qty, id);
    }

}
