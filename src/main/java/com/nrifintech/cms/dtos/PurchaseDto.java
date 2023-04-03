package com.nrifintech.cms.dtos;

import java.sql.Timestamp;
import com.nrifintech.cms.entities.Inventory;

import lombok.*;

/**
 * PurchaseDto is a class that contains the following fields: refId, quantity, amount, time, and
 * inventoryRef
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PurchaseDto {

    private int refId;
    private double quantity;
    private double amount;
    private Timestamp time;

    private Inventory inventoryRef;
}
