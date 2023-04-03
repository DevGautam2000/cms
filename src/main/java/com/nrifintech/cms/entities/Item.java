package com.nrifintech.cms.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.nrifintech.cms.dtos.ItemDto;
import com.nrifintech.cms.types.ItemType;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Item {

	@Id
	@GeneratedValue
	private Integer id;

	private Integer quantity;
	private Double price;
	private ItemType itemType;
	private String imagePath = "";
	
	private String description = "";
	
	@Column(unique=true)
	private String name = "";

	public Item(ItemDto item) {
		this.id=item.getId();
		this.name=item.getName();
		this.quantity=item.getQuantity();
		this.price=item.getPrice();
		this.itemType=item.getItemType();
		this.imagePath=item.getImagePath();
		this.description=item.getDescription();
	}

	
}
