package com.nrifintech.cms.dtos;

import com.nrifintech.cms.entities.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PlacedOrderToken {
    String username;
    Order order;
}
