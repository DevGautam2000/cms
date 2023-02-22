package com.nrifintech.cms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nrifintech.cms.entities.Item;

public interface ItemRepo extends JpaRepository<Item, Integer>{

}
