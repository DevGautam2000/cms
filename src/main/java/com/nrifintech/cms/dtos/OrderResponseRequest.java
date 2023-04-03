package com.nrifintech.cms.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * This class is a POJO that is used to send a response to the client
 */
@Getter
@Setter
@AllArgsConstructor
public class OrderResponseRequest {

		private Boolean isBreakFastOrdered; 
		private Boolean isLunchOrdered; 
}
