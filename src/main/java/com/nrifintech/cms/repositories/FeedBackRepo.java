package com.nrifintech.cms.repositories;

import java.util.List;

import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nrifintech.cms.entities.FeedBack;

public interface FeedBackRepo extends JpaRepository<FeedBack, Integer>{

    @Query(value = "select count(id) , avg(rating) from feed_back" , nativeQuery = true )
    public Tuple feedbackStats();

    @Query(value = "select rating , count(id) from feed_back group by rating" , nativeQuery = true )
    public List<Tuple> feedbackRatingStats();
}
