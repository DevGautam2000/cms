package com.nrifintech.cms.entities;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Purchase {

    @Id
    @GeneratedValue
    private int refId;
    private double quantity;
    private double amount;
    private Timestamp time = new Timestamp(System.currentTimeMillis());

    @ManyToOne
    @JsonBackReference
    private Inventory inventoryRef;
}
