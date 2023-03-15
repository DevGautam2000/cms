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
}
