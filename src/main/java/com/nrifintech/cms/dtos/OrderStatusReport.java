package com.nrifintech.cms.dtos;

import com.nrifintech.cms.types.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderStatusReport {
    private Status status;
    private int count;
}
