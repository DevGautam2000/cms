package com.nrifintech.cms.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.nrifintech.cms.types.Approval;

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
	private Approval approval = Approval.Pending;

	@ManyToMany
	@ToString.Exclude
	private List<Item> items;

}
