package com.nrifintech.cms.entities;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.nrifintech.cms.types.Approval;
import com.nrifintech.cms.types.MealType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Menu {

	@Id
	@GeneratedValue
	private Integer id;
	private Approval approval = Approval.Incomplete;

	private Date date;
	
	private MealType menuType;

	@ManyToMany(fetch = FetchType.EAGER)
	private List<Item> items;

}
