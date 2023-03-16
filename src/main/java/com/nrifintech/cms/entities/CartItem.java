package com.nrifintech.cms.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.nrifintech.cms.types.ItemType;
import com.nrifintech.cms.types.MealType;

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

	private Integer sourceId;

	private MealType mealType;

	private Integer quantity;
	private Double price;
	private ItemType itemType;
	private String imagePath = "";

	private String name = "";

	public CartItem(Item item, Integer quantity) {
		this.quantity = quantity;
		price = item.getPrice();
		sourceId = item.getId();
		itemType = item.getItemType();
		imagePath = item.getImagePath();
		name = item.getName();
	}

	public void increaseOne() {
		this.setQuantity(quantity + 1);
	}

	public void increaseBy(Integer factor) {
		this.setQuantity(quantity + factor);
	}

	public void decreaseOne() {
		this.setQuantity(quantity - 1);
	}

	public void decreaseBy(Integer factor) {
		this.setQuantity(quantity - factor);
	}

}
