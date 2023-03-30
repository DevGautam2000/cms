package com.nrifintech.cms.dtos;

import java.sql.Timestamp;
import com.nrifintech.cms.entities.Inventory;

import lombok.*;

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
