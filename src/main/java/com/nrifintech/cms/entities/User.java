package com.nrifintech.cms.entities;


import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.nrifintech.cms.types.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Entity
public class User {

	@Id
	@GeneratedValue
	private Integer id;

	private String username;

	@Column(unique = true)
	private String email;
	private String password;
	private String phoneNumber;
	private Role role = Role.User;

	
	@Column(unique=true)
	@OneToMany(fetch=FetchType.LAZY)
	private List<Order> records;
	
	@OneToOne(fetch=FetchType.EAGER)
	private Cart cart;
	
	

}
