package com.nrifintech.cms.entities;


import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nrifintech.cms.types.EmailStatus;
import com.nrifintech.cms.types.Role;
import com.nrifintech.cms.types.UserStatus;

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
	
	private String avatar;

	@Column(unique = true)
	private String email;
	private String password;
	private String phoneNumber;
	
	private Role role = Role.User;

	private UserStatus status = UserStatus.InActive;

	private EmailStatus emailStatus = EmailStatus.subscribed;
  
	private Timestamp created = new Timestamp(System.currentTimeMillis());
	
	@Column(unique=true)
	@OneToMany(fetch=FetchType.LAZY)
	private List<Order> records;
	
	@OneToOne
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Wallet wallet;
	
	@OneToOne(fetch=FetchType.EAGER)
	private Cart cart;
	
	public String getUsername(){
		return this.email;
	}

}
