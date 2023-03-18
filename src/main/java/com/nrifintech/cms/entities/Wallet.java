package com.nrifintech.cms.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
@Entity
public class Wallet {

	@Id
	@GeneratedValue
	private Integer id;

	private Double balance = 0.0;
	
	@OneToMany
	private List<Transaction> transactions;
	
	
}
