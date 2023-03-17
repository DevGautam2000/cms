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

	Optional<User> findByEmail(String email);
	
	@Query(value ="select email from user where id in (select distinct user_id from user_records where records_id in (select id from orders where date(order_placed) = :date))",nativeQuery = true)
	List<String> getUserByOrderDate(@Param("date") Date date);

	@Query(value ="select email from user where id in (select distinct user_id from user_records where records_id in (select id from orders where date(order_placed) = :date and order_type = :otype) and email_status = 0)",nativeQuery = true)
	List<String> getUserByOrderDateAndType(@Param("date") Date date,@Param("otype") Integer otype);
	
	@Query(value="select email from user where id = (select user_id from user_records where records_id = :orderId)",nativeQuery = true)
	String getUserByOrderId(@Param("orderId") Integer orderId);

	
	@Query(value ="select (:cartItemId in (select cart_items_id from cart_cart_items where cart_id = (select user.cart_id from user where email = :em) )) as 'val' from dual",nativeQuery = true)
    Integer hasUserCartitem(@Param("em") String em,@Param("cartItemId") Integer cartItemId);

	@Query(value ="select email from user where role = 2 and email_status = 0",nativeQuery = true)
	Optional<List<String>> getAllConsumers();

	@Query(value = "select role,count(id) from user where status = 0 group by role",nativeQuery = true)
	List<Tuple> getAllUserGroup();
}
