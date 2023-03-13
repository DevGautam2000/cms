package com.nrifintech.cms.entities;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Purchase {

    @Id
    @GeneratedValue
    private String refId;
    private double quantity;
    private double amount;
    private Timestamp time = new Timestamp(System.currentTimeMillis());

    @ManyToOne
    private Inventory inventoryRef;
}
