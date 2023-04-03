package com.nrifintech.cms.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.nrifintech.cms.types.ItemType;
import com.nrifintech.cms.types.MealType;

import lombok.*;

/**
 * CartItem is a class that represents an item in a cart
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
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

	// A constructor.
	public CartItem(Item item, Integer quantity) {
		this.quantity = quantity;
		price = item.getPrice();
		sourceId = item.getId();
		itemType = item.getItemType();
		imagePath = item.getImagePath();
		name = item.getName();
	}

	/**
	 * This function increases the quantity of the item by one
	 */
	public void increaseOne() {
		this.setQuantity(quantity + 1);
	}

	/**
	 * > This function increases the quantity of a product by a given factor
	 * 
	 * @param factor The amount to increase the quantity by.
	 */
	public void increaseBy(Integer factor) {
		this.setQuantity(quantity + factor);
	}

	/**
	 * Decrease the quantity of the item by one
	 */
	public void decreaseOne() {
		this.setQuantity(quantity - 1);
	}

	/**
	 * This function decreases the quantity of a product by a given factor
	 * 
	 * @param factor The amount to decrease the quantity by.
	 */
	public void decreaseBy(Integer factor) {
		this.setQuantity(quantity - factor);
	}

}
