package com.nrifintech.cms.dtos;

import com.nrifintech.cms.entities.Cart;
import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.types.Role;
import com.nrifintech.cms.types.UserStatus;

import lombok.Data;



public class UserDto{

	@Data
	public static class Privileged{
		
		private Integer id;
		private String email;
		private String avatar;
		private Role role;	
		private UserStatus status;
		
		public Privileged(User user) {

			this.id = user.getId();
			this.email = user.getEmail();
			this.avatar = user.getAvatar();
			this.role = user.getRole();
			this.status = user.getStatus();
		}
		
	}
	
	@Data
	public static class Unprivileged{
		private Integer id;
		private String email;
		private String avatar;
		private Role role;
		private UserStatus status;
		private Integer cartCount=0;
		private Integer cartId;
		
		public Unprivileged(User user) {

			this.id = user.getId();
			this.email = user.getEmail();
			this.avatar = user.getAvatar();
			this.role = user.getRole();
			this.status = user.getStatus();
			
			
			Cart c = user.getCart();
			
			if(c != null) {
				this.cartId = c.getId();
				this.cartCount = c.getCartItems().size();
			}
		}
	}
}
