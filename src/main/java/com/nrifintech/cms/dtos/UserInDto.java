package com.nrifintech.cms.dtos;

import java.sql.Timestamp;
import java.util.List;

import com.nrifintech.cms.entities.Cart;
import com.nrifintech.cms.entities.Order;
import com.nrifintech.cms.entities.Wallet;
import com.nrifintech.cms.types.EmailStatus;
import com.nrifintech.cms.types.Role;
import com.nrifintech.cms.types.UserStatus;

import lombok.*;

/**
 * UserInDto is a class that contains the information of a user
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Builder
public class UserInDto {

	private Integer id;
	private String avatar;
	private String email;


	private String password;
	private String phoneNumber;
	@Builder.Default
	private Role role = Role.User;
	@Builder.Default
	private UserStatus status = UserStatus.InActive;
	@Builder.Default
	private EmailStatus emailStatus = EmailStatus.subscribed;
	@Builder.Default
	private Timestamp created = new Timestamp(System.currentTimeMillis());

	private List<Order> records;
	private Wallet wallet;	
	private Cart cart;
	
	public String getUsername(){
		return this.email;
	}

	public UserInDto(Integer id,String email, String password, Role role,Cart cart) {
		this.id=id;
		this.email = email;
		this.password = password;
		this.role = role;
		this.cart = cart;
	}

}
