package com.nrifintech.cms.entities;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.nrifintech.cms.types.MealType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

enum Status {
	Delivered, Pending, InProgress
}

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name="orders")
public class Order {

	@Id
	@GeneratedValue
	private Integer id;

	private Status status = Status.Pending;
	private MealType orderType;
	private Timestamp timeStamp;

	// map user id
}
