package com.nrifintech.cms.dtos;

import com.nrifintech.cms.entities.Item;
import com.nrifintech.cms.types.ItemType;
import com.nrifintech.cms.utils.Name;
import com.nrifintech.cms.utils.Validator;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ItemDto implements Validator{
	
	private Integer id;
	private Integer quantity;
	private Double price;
	private ItemType itemType;
	private String itemName;
	
	public ItemDto(Item item){
		
		id = item.getId();
		quantity = item.getQuantity();
		price = item.getPrice();
		itemType = item.getItemType();
		itemName = Name.list.get(item.getId());
		
	}
}
