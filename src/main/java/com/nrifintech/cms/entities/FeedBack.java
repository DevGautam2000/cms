package com.nrifintech.cms.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.*;


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
	
	
}
