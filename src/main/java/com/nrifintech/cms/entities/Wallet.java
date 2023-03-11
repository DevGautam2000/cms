package com.nrifintech.cms.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Wallet {

	@Id
	@GeneratedValue
	private Integer id;

	private Double balance = 0.0;
	
	@OneToMany(fetch=FetchType.EAGER)
	private List<Transaction> transactions;
	
	public void transact() {
		this.balance = this.getBalance() - 100.0;
	}
}
