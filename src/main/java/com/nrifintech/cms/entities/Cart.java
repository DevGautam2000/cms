package com.nrifintech.cms.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.*;

/**
 * Cart Entity
 * A cart has an id
 * A cart has many cart items.
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cart {

	@Id
	@GeneratedValue
	private Integer id;
	
	@ManyToMany(fetch=FetchType.EAGER)
	private List<CartItem> cartItems;
}
