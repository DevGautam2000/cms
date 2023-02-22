package com.nrifintech.cms.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

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
public class Menu{

	@Id
	@GeneratedValue
	private Integer id;

	@ManyToMany
//	(cascade = CascadeType.ALL, targetEntity = Item.class)
//    @JoinColumn(name = "item_id", referencedColumnName = "id")
	@ToString.Exclude
	private List<Item> items;

}
