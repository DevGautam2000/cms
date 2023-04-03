package com.nrifintech.cms.dtos;

import com.nrifintech.cms.entities.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * OrderToken is a class that has a username and an order.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderToken {
    String username;
    Order order;
}
