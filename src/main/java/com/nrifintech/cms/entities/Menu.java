package com.nrifintech.cms.entities;

import java.sql.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.nrifintech.cms.dtos.MenuDto;
import com.nrifintech.cms.types.Approval;
import com.nrifintech.cms.types.MealType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Menu {

	@Id
	@GeneratedValue
	private Integer id;
	@Builder.Default
	private Approval approval = Approval.Incomplete;

	private Date date;
	
	private MealType menuType;

	@ManyToMany(fetch = FetchType.EAGER)
	private List<Item> items;

	public Menu(MenuDto menu) {
		this.id=menu.getId();
		this.approval=menu.getApproval();
		this.date=menu.getDate();
		this.menuType=menu.getMenuType();
		this.items=menu.getItems();
	}
}
