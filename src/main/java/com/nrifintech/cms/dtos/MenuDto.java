package com.nrifintech.cms.dtos;

import java.sql.Date;
import java.util.List;

import com.nrifintech.cms.entities.Item;
import com.nrifintech.cms.types.Approval;
import com.nrifintech.cms.types.MealType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * MenuDto is a class that has an id, approval, date, menuType, and items
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuDto {

	private Integer id;
	@Builder.Default
	private Approval approval = Approval.Incomplete;

	private Date date;
	
	private MealType menuType;

	private List<Item> items;

}
