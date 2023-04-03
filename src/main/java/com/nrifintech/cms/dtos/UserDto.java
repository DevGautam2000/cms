package com.nrifintech.cms.dtos;

import com.nrifintech.cms.entities.Cart;
import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.entities.Wallet;
import com.nrifintech.cms.types.Role;
import com.nrifintech.cms.types.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



/**
 * This class is used to convert the User entity to a DTO
 */
public class UserDto{

	private UserDto() {
		throw new IllegalStateException("Utility class");
	  }
	  
	/**
	 * This class is a wrapper for the User class. It is used to return a subset of the User class to the
	 * client
	 */
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
	/**
	 * It's a class that contains the fields that are to be returned to the client
	 */
	@NoArgsConstructor
	@AllArgsConstructor
	@Data
	public static class Unprivileged{
		private Integer id;
		private String email;
		private String avatar;
		private Role role;
		private UserStatus status;
		private Integer cartCount=0;
		private Integer cartId;
		private Integer walletId;
		
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
			
			Wallet w = user.getWallet();
			
			if(w != null) {
				this.walletId = w.getId();
			}
		}
	}
}
