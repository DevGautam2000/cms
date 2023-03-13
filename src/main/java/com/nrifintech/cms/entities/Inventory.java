package com.nrifintech.cms.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;

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
	private Double quantityRequested;

	@OneToMany(mappedBy = "inventoryRef",cascade = CascadeType.MERGE)
	@JsonManagedReference
	private List<Purchase> purchases;
	
}
