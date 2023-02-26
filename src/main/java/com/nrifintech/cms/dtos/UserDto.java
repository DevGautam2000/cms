package com.nrifintech.cms.dtos;

import com.nrifintech.cms.entities.Cart;
import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.types.Role;

import lombok.Data;



public class UserDto{

	@Data
	public static class Privileged{
		
		private Integer id;
		private String username;
		private Role role;	
		
		public Privileged(User user) {

			this.id = user.getId();
			this.username = user.getUsername();
			this.role = user.getRole();
		}
		
	}
	
	@Data
	public static class Unprivileged{
		private Integer id;
		private String username;
		private Role role;
		private Integer cartCount=0;
		
		public Unprivileged(User user) {

			this.id = user.getId();
			this.username = user.getUsername();
			this.role = user.getRole();
			
			Cart c = user.getCart();
			
			if(c != null) {
				this.cartCount = c.getItems().size();
			}
		}
	}
}
