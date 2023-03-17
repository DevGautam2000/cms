package com.nrifintech.cms.dtos;

import com.nrifintech.cms.entities.Inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class InventoryMail {
    private String name;
    private Double quantity;
}
