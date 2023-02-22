package com.nrifintech.cms.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nrifintech.cms.entities.User;

public interface UserRepo extends JpaRepository<User, Integer> {
	@Query(value = "SELECT * from user u where u.id = : uid", nativeQuery = true)
	List<Object[]> findByUserId(@Param("uid") String uid);

	Optional<User> findByEmail(String email);
}
