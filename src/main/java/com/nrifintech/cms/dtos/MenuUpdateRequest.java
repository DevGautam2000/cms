package com.nrifintech.cms.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * This class is used to update a menu item
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MenuUpdateRequest {
		private Integer menuId;
		private Integer itemId;
}
