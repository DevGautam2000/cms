package com.nrifintech.cms.dtos;

import java.util.Set;

import com.nrifintech.cms.entities.Order;
import com.nrifintech.cms.types.Role;
import com.nrifintech.cms.utils.Validator;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data

public class UserDto implements Validator{

	private Integer id;
	private String username;
	private Role role = Role.User;
	private Set<Order> record;

}
