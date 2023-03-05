package com.nrifintech.cms.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
	
	private Double quantityInHand;
	private Double quantityRequired;
	private Double totalExpenditure;
	private Double monthlyExpenditure;
	
}
