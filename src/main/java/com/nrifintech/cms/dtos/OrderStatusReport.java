package com.nrifintech.cms.dtos;

import com.nrifintech.cms.types.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The `OrderStatusReport` class is a data transfer object (DTO) that represents the status of an order
 * and the count of orders with that status
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderStatusReport {
    private Status status;
    private Object count;
}
