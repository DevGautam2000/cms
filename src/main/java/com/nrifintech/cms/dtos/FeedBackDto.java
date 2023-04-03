package com.nrifintech.cms.dtos;

import lombok.*;

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
