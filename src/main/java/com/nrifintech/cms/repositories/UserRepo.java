package com.nrifintech.cms.repositories;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nrifintech.cms.entities.User;

public interface UserRepo extends JpaRepository<User, Integer> {

	Optional<User> findByEmail(String email);
	
	@Query(value ="select email from user where id in (select distinct user_id from user_records where records_id in (select id from orders where date(order_placed) = :date))",nativeQuery = true)
	List<String> getUserByOrderDate(@Param("date") Date date);

	@Query(value ="select email from user where id in (select distinct user_id from user_records where records_id in (select id from orders where date(order_placed) = :date and order_type = :otype))",nativeQuery = true)
	List<String> getUserByOrderDateAndType(@Param("date") Date date,@Param("otype") Integer otype);
	
	@Query(value="select email from user where id = (select user_id from user_records where records_id = :orderId)",nativeQuery = true)
	String getUserByOrderId(@Param("orderId") Integer orderId);
}
