package com.nrifintech.cms.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MenuUpdateRequest {
		private Integer menuId;
		private Integer itemId;
}
