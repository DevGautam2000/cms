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

@Service
public class PurchaseService {
    
    @Autowired
    PurchaseRepo purchaseRepo;

    @Autowired
    InventoryRepo inventoryRepo;

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

    public Purchase getPurchaseById(Integer id)throws IllegalArgumentException{
       return( this.purchaseRepo.findById(id).orElse(null) );
    }

    public List<Purchase> getAllPurchase()throws IllegalArgumentException{
        return( this.purchaseRepo.findAll() );
     }
}
