package com.nrifintech.cms.dtos;

import java.util.List;
import java.util.stream.Collectors;

import com.nrifintech.cms.entities.Menu;

import lombok.Getter;

@Getter
public class MenuDto {
	private Integer id;
	private List<ItemDto> foods;

	public MenuDto(Menu m) {
		id = m.getId();
		foods = m.getItems().stream().map(food -> new ItemDto(food)).collect(Collectors.toList());
	}
}
