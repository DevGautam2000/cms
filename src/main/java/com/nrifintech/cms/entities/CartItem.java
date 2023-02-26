package com.nrifintech.cms.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.nrifintech.cms.types.ItemType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class CartItem {

	
	@Id
	@GeneratedValue
	private Integer id;

	private Integer quantity;
	private Double price;
	private ItemType itemType;
	private String imagePath = "";
	
	private String name = "";
	
	
	public CartItem(Item item, Integer quantity) {
		id = item.getId();
		this.quantity = quantity;
		price = item.getPrice();
		itemType = item.getItemType();
		imagePath = item.getImagePath();
		name = item.getName();
	}

	
}
