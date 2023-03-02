package com.nrifintech.cms.entities;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="timestamps")
public class TimeStamp {

	@Id
	@GeneratedValue
	private Integer id;
	
	private Timestamp pending = new Timestamp(System.currentTimeMillis());
	private Timestamp delivered;
	private Timestamp notDelivered;
	private Timestamp cancelled;
	
	
}
