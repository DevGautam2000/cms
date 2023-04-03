package com.nrifintech.cms.dtos;

import com.nrifintech.cms.types.MealType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is used to update the quantity of a cart item
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemUpdateRequest {

	private String itemId;
	private String quantity;
	private MealType mealType;
	
}
