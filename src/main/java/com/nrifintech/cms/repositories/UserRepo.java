package com.nrifintech.cms.repositories;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nrifintech.cms.entities.User;

public interface UserRepo extends JpaRepository<User, Integer> {

	/**
	 * Find a user by email, and return an Optional that contains the user if found,
	 * or an empty Optional
	 * if not found.
	 * 
	 * @param email The email of the user to find.
	 * @return Optional<User>
	 */
	Optional<User> findByEmail(String email);

	/**
	 * Get all the users who have placed an order on a particular date
	 * 
	 * @param date The date for which you want to get the users.
	 * @return List of email addresses of users who have placed an order on the
	 *         given date.
	 */
	@Query(value = "select email from user where id in (select distinct user_id from user_records where records_id in (select id from orders where date(order_placed) = :date))", nativeQuery = true)
	List<String> getUserByOrderDate(@Param("date") Date date);

	/**
	 * It returns the list of emails of users who have placed an order on a
	 * particular date and of a
	 * particular type.
	 * 
	 * @param date  The date for which you want to get the users.
	 * @param otype 1 for order placed, 2 for order delivered, 3 for order cancelled
	 * @return List of email addresses
	 */
	@Query(value = "select email from user where id in (select distinct user_id from user_records where records_id in (select id from orders where date(order_placed) = :date and order_type = :otype) and email_status = 0)", nativeQuery = true)
	List<String> getUserByOrderDateAndType(@Param("date") Date date, @Param("otype") Integer otype);

	/**
	 * It returns the email of the user who placed the order.
	 * 
	 * @param orderId The name of the parameter.
	 * @return A String
	 */
	@Query(value = "select email from user where id = (select user_id from user_records where records_id = :orderId)", nativeQuery = true)
	String getUserByOrderId(@Param("orderId") Integer orderId);

	/**
	 * It checks if the user has the cart item in his cart.
	 * 
	 * @param em         email of the user
	 * @param cartItemId The id of the cart item to be checked.
	 * @return A boolean value.
	 */
	@Query(value = "select (:cartItemId in (select cart_items_id from cart_cart_items where cart_id = (select user.cart_id from user where email = :em) )) as 'val' from dual", nativeQuery = true)
	Integer hasUserCartitem(@Param("em") String em, @Param("cartItemId") Integer cartItemId);

	/**
	 * It returns a list of all the consumers who have not verified their email
	 * address
	 * 
	 * @return List of all consumers who have not verified their email.
	 */
	@Query(value = "select email from user where role = 2 and email_status = 0", nativeQuery = true)
	Optional<List<String>> getAllConsumers();

	/**
	 * > Get all users grouped by role
	 * 
	 * @return A list of tuples.
	 */
	@Query(value = "select role,count(id) from user where status = 0 group by role", nativeQuery = true)
	List<Tuple> getAllUserGroup();

	/**
	 * > Get all the user emails by role
	 * 
	 * @param role The role of the user.
	 * @return List of Tuple
	 */
	@Query(value = "select email from user where role = :role and email_status = 0", nativeQuery = true)
	public List<Tuple> getUserEmailsByRole(@Param("role") Integer role);
}
