package com.nrifintech.cms.services;

import java.sql.Timestamp;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nrifintech.cms.entities.Inventory;
import com.nrifintech.cms.entities.Purchase;
import com.nrifintech.cms.repositories.InventoryRepo;
import com.nrifintech.cms.repositories.PurchaseRepo;

/**
 * This class is a service class that provides the business logic for the purchase entity
 */
@Service
public class PurchaseService {
    
    @Autowired
    PurchaseRepo purchaseRepo;

    @Autowired
    InventoryRepo inventoryRepo;

   /**
    * It takes a Purchase object, checks to see if the Inventory object it references is valid, and if
    * so, saves the Purchase object to the database, and updates the Inventory object's quantity in
    * hand and quantity requested fields
    * 
    * @param purchase the purchase object that is being saved
    * @return A Purchase object.
    */
    @Transactional
    public Purchase initiateNewPurchase(Purchase purchase)throws IllegalArgumentException{
        Inventory i = purchase.getInventoryRef();
        if( i == null ){
            return(null);
        }
        purchase.setInventoryRef(inventoryRepo.findById(i.getId()).orElse(null));
        if(purchase.getInventoryRef() == null){
            return(null);
        }
        purchase.setTime(new Timestamp(System.currentTimeMillis()));
        Purchase purchaseRef = purchaseRepo.save(purchase);
        inventoryRepo.updateQtyInHand( purchaseRef.getQuantity() , purchaseRef.getInventoryRef().getId() );
        inventoryRepo.updateQtyRequested( (-purchaseRef.getQuantity()) , purchaseRef.getInventoryRef().getId() );
        return(purchaseRef);
    }

   /**
    * It deletes a purchase record from the database, and updates the inventory record to reflect the
    * change
    * 
    * @param purchaseID The ID of the purchase to be rolled back.
    * @return The purchase object that was deleted.
    */
    @Transactional
    public Purchase rollbackPurchase(Integer purchaseID)throws IllegalArgumentException{
        if(purchaseID == null){
            return(null);
        }
        Purchase purchase = purchaseRepo.findById(purchaseID).orElse(null);
        if( purchase==null ){
            return(null);
        }
        if( inventoryRepo.findById(purchase.getInventoryRef().getId()).isPresent() ){
            return(null);
        }
        purchaseRepo.delete(purchase);
        inventoryRepo.updateQtyInHand( (-purchase.getQuantity()) , purchase.getInventoryRef().getId() );
        inventoryRepo.updateQtyRequested( (purchase.getQuantity()) , purchase.getInventoryRef().getId() );
        return(purchase);
    }

   /**
    * This function returns a purchase object from the database, given the id of the purchase
    * 
    * @param id The id of the purchase to be retrieved.
    * @return The purchase object with the id that was passed in.
    */
    public Purchase getPurchaseById(Integer id)throws IllegalArgumentException{
       return( this.purchaseRepo.findById(id).orElse(null) );
    }

   /**
    * This function returns a list of all purchases in the database
    * 
    * @return A list of all the purchases in the database.
    */
    public List<Purchase> getAllPurchase()throws IllegalArgumentException{
        return( this.purchaseRepo.findAll() );
     }
}
