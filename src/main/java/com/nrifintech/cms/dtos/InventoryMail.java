package com.nrifintech.cms.dtos;

import com.nrifintech.cms.entities.Inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class InventoryMail {

    private Integer id;
    private String name;
    private Double quantityInHand;
    private Double quantityRequested;
    private Double quantityPurchased;

    public InventoryMail(Inventory i , Double quantityPurchased){
        this.id = i.getId();
        this.name = i.getName();
        this.quantityInHand = i.getQuantityInHand();
        this.quantityRequested = i.getQuantityRequested();
        this.quantityPurchased = quantityPurchased;
    }

    public InventoryMail(Inventory i){
        this.id = i.getId();
        this.name = i.getName();
        this.quantityInHand = i.getQuantityInHand();
        this.quantityRequested = i.getQuantityRequested();
    }
}
