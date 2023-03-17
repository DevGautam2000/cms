package com.nrifintech.cms.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nrifintech.cms.entities.Inventory;
import com.nrifintech.cms.repositories.InventoryRepo;

@Service
public class InventoryService {

    @Autowired
    InventoryRepo inventoryRepo;
    
    public Inventory addToInventory(Inventory inventory){
        List<Inventory> i = this.inventoryRepo.findByName(inventory.getName());
        if( i.size() > 0 ){
            return( null );
        }
        return(this.inventoryRepo.save(inventory));
    }

    public List<Inventory> addAlltoInventory(List<Inventory> inventory){
        return( this.inventoryRepo.saveAll(inventory) );
    }

    public Inventory getInventoryById(Integer id){
        return( this.inventoryRepo.findById(id).orElse(null) );
    }
    public List<Inventory> getInventoryByName(String name){
        return( this.inventoryRepo.findByName(name) );
    }
    public List<Inventory> getAllInventory(){
        return( this.inventoryRepo.findAll() );
    }
    public void removeInventoryById(Integer id){
        this.inventoryRepo.deleteById(id);
    }

    public void updateQtyInHand(Double qty,int id){
        this.inventoryRepo.updateQtyInHand(qty, id);
    }
    public void updateQtyRequested(Double qty,int id){
        this.inventoryRepo.updateQtyRequested(qty, id);
    }

}
