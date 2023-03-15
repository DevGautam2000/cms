package com.nrifintech.cms.entities;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.nrifintech.cms.types.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Transaction {

	@Id
	@GeneratedValue
	private Integer id;

	private Timestamp timestamp = new Timestamp(System.currentTimeMillis());

	private Integer amount = 0;
	
	private TransactionType type;
	
	private String referenceNumber = "";
	
	private String chargeId = "";
	
	private String remarks = "-";
	
	public Transaction(Integer amount,TransactionType type) {
		this.amount = amount;
		this.type = type;
	}

}
