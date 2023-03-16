package com.nrifintech.cms.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderResponseRequest {

		private Boolean isBreakFastOrdered; 
		private Boolean isLunchOrdered; 
}
