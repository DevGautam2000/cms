package com.nrifintech.cms.repositories;

import java.sql.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


import com.nrifintech.cms.entities.Menu;

public interface MenuRepo extends JpaRepository<Menu,Integer>{

	/**
	 * Find all menus for a given day.
	 * 
	 * @param forDay The date for which you want to find the menu.
	 * @return A list of Menu objects.
	 */
	List<Menu> findMenuByDate(Date forDay);
	
}
