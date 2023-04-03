package com.nrifintech.cms.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.nrifintech.cms.dtos.FeedBackDto;

import lombok.*;


/**
 * FeedBack is a class that has a constructor that takes a FeedBackDto as a parameter
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FeedBack {

	@Id
	@GeneratedValue
	private Integer id;
	
	private Integer rating;
	private String comments;
	
	
	// A constructor that takes a FeedBackDto as a parameter
	public FeedBack(FeedBackDto feedBack) {
		this.id=feedBack.getId();
		this.rating=feedBack.getRating();
		this.comments=feedBack.getComments();
	}
}
