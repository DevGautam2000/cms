package com.nrifintech.cms.dtos;

import lombok.*;

/**
 * FeedBackDto is a class that has a rating and comments
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedBackDto {

	private Integer id;
	
	private Integer rating;
	private String comments;
	
}
