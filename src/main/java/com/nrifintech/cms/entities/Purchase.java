package com.nrifintech.cms.entities;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
}
