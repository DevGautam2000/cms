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
import com.nrifintech.cms.dtos.UserInDto;
import com.nrifintech.cms.types.EmailStatus;
import com.nrifintech.cms.types.Role;
import com.nrifintech.cms.types.UserStatus;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Builder
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

	public User(Integer id,String email, String password, Role role,Cart cart) {
		this.id=id;
		this.email = email;
		this.password = password;
		this.role = role;
		this.cart = cart;
	}

	public User(UserInDto user) {
		this.id=user.getId();
		this.avatar=user.getAvatar();
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.phoneNumber=user.getPhoneNumber();
		this.role = user.getRole();
		this.status=user.getStatus();
		this.emailStatus=user.getEmailStatus();
		this.created=user.getCreated();
		this.records=user.getRecords();
		this.wallet=user.getWallet();
		this.cart = user.getCart();
	}
}
