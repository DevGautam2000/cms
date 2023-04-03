package com.nrifintech.cms.repositories;

import java.util.List;

import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nrifintech.cms.entities.FeedBack;

public interface FeedBackRepo extends JpaRepository<FeedBack, Integer>{

   /**
    * It returns a tuple of the count of the id column and the average of the rating column from the
    * feed_back table
    * 
    * @return Tuple is a class that contains a list of Object arrays. Each Object array represents a
    * "row" of data.
    */
    @Query(value = "select count(id) , avg(rating) from feed_back" , nativeQuery = true )
    public Tuple feedbackStats();

   /**
    * It returns a list of tuples, each tuple contains the rating and the number of feedbacks with that
    * rating
    * 
    * @return A list of tuples.
    */
    @Query(value = "select rating , count(id) from feed_back group by rating" , nativeQuery = true )
    public List<Tuple> feedbackRatingStats();
}
