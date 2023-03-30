package com.nrifintech.cms.dtos;

import java.util.List;
import com.nrifintech.cms.entities.Purchase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDto {

	private Integer id;

	private String name;
	private Double quantityInHand;
	private Double quantityRequested;

	private List<Purchase> purchases;
	
}
