package com.nrifintech.cms.dtos;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.nrifintech.cms.types.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderStatusReport {
    private Status status;
    private BigInteger count;
}
