package com.nrifintech.cms.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Inventory {

	@Id
	@GeneratedValue
	private Integer id;
	private String name;
	private Double quantityInHand;
	private double unitPrice;
	private String unitDesc;

	@OneToMany(mappedBy = "inventoryRef")
	private List<Purchase> purchases;
	
}
