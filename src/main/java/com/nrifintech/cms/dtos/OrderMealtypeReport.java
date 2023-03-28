package com.nrifintech.cms.dtos;

import java.math.BigInteger;

import com.nrifintech.cms.types.MealType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderMealtypeReport {
    private MealType mealType;
    private Object count;
}
