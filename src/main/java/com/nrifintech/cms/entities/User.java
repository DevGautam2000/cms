package com.nrifintech.cms.entities;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

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
	private String email;
	private String password;
	private String phoneNumber;
	private Role role = Role.User;

	@OneToMany
	private List<Pass> pass;

	@OneToMany
	private Set<Order> record;

}
