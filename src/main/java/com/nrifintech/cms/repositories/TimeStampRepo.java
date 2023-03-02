package com.nrifintech.cms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nrifintech.cms.entities.TimeStamp;

public interface TimeStampRepo extends JpaRepository<TimeStamp, Integer>{

}
