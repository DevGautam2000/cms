package com.nrifintech.cms.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartItemUpdateRequest {

	private String itemId;
	private String quantity;
	
}
