package com.nrifintech.cms.dtos;

import com.nrifintech.cms.types.MealType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is a data transfer object that holds the meal type and the count of orders for that meal
 * type.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderMealtypeReport {
    private MealType mealType;
    private Object count;
}
