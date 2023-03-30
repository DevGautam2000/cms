package com.nrifintech.cms.entities;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.nrifintech.cms.dtos.PurchaseDto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class Purchase {

    @Id
    @GeneratedValue
    private int refId;
    private double quantity;
    private double amount;
    private Timestamp time;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JsonBackReference
    private Inventory inventoryRef;

    public Purchase(PurchaseDto purchase) {
        this.refId=purchase.getRefId();
        this.quantity=purchase.getQuantity();
        this.amount=purchase.getAmount();
        this.time=purchase.getTime();
    }

    
}
