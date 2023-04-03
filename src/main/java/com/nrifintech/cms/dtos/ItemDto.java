package com.nrifintech.cms.dtos;

import com.nrifintech.cms.types.ItemType;

import lombok.*;

/**
 * ItemDto is a class that represents an item in the store.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

	private Integer id;

	private Integer quantity;
	private Double price;
	private ItemType itemType;
	@Builder.Default
	private String imagePath = "";
	@Builder.Default
	private String description = "";
	@Builder.Default
	private String name = "";
}
