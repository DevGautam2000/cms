package com.nrifintech.cms.dtos;

import com.nrifintech.cms.types.MealType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemUpdateRequest {

	private String itemId;
	private String quantity;
	private MealType mealType;
	
}
