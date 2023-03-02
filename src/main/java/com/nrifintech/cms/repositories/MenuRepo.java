package com.nrifintech.cms.repositories;

import java.sql.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


import com.nrifintech.cms.entities.Menu;

public interface MenuRepo extends JpaRepository<Menu,Integer>{

	List<Menu> findMenuByDate(Date forDay);
	
}
