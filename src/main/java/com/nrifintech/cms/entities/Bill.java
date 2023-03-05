package com.nrifintech.cms.entities;

import java.sql.Date;

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
public class Bill {

	@Id
	@GeneratedValue
	private Integer id;
	
	private Double due;
	private Double totalExpenditure;
	private Date nextCycle;
	
}
