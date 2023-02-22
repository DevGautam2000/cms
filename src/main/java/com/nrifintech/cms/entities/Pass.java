package com.nrifintech.cms.entities;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.nrifintech.cms.types.MealType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Pass {

	@Id
	@GeneratedValue
	private Integer id;
	
	private static Integer TOTAL_PASSES = 30;
	
	private Integer lunchLeft = Pass.TOTAL_PASSES;
	private Integer breakfastLeft = Pass.TOTAL_PASSES;
	private Date expiryDate;
	
	public void decrease(MealType mealType) {
		if (mealType.equals(MealType.Breakfast)) {
			this.setBreakfastLeft(breakfastLeft - 1);
		} else {
			this.setLunchLeft(lunchLeft - 1);
		}
	}

	public void decrease(MealType mealType, Integer factor) {
		if (mealType.equals(MealType.Breakfast)) {
			this.setBreakfastLeft(breakfastLeft - factor);
		} else {
			this.setLunchLeft(lunchLeft - factor);
		}
	}

	public void increase(MealType mealType) {
		if (mealType.equals(MealType.Breakfast)) {
			this.setBreakfastLeft(breakfastLeft + 1);
		} else {
			this.setLunchLeft(lunchLeft + 1);
		}
	}

	public void increase(MealType mealType, Integer factor) {
		if (mealType.equals(MealType.Breakfast)) {
			this.setBreakfastLeft(breakfastLeft + factor);
		} else {
			this.setLunchLeft(lunchLeft + factor);
		}
	}

}
