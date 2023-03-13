package com.nrifintech.cms.entities;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.nrifintech.cms.types.MealType;
import com.nrifintech.cms.types.Status;

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
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue
	private Integer id;

	private Status status = Status.Pending;
	private MealType orderType;

	private Timestamp orderPlaced = new Timestamp(System.currentTimeMillis());

	private Timestamp orderDelivered;

	@OneToOne
	private FeedBack feedBack;
	
	@OneToOne
	private Transaction transaction ;

	@ManyToMany(fetch = FetchType.LAZY)
	private List<CartItem> cartItems;

}
