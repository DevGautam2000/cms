package com.nrifintech.cms.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nrifintech.cms.entities.FeedBack;
import com.nrifintech.cms.errorhandler.NotFoundException;
import com.nrifintech.cms.repositories.FeedBackRepo;

/**
 * FeedBackService is a class that has a method addFeedBack that takes a FeedBack object and returns a
 * FeedBack object
 */
@Service
public class FeedBackService {

	@Autowired
	private FeedBackRepo feedBackRepo;

	/**
	 * It takes a FeedBack object as a parameter and saves it to the database
	 * 
	 * @param feedBack The object that you want to save.
	 * @return The feedBack object is being returned.
	 */
	public FeedBack addFeedBack(FeedBack feedBack) {
		return feedBackRepo.save(feedBack);
	}
	
	/**
	 * It returns a feedback object with the given id, or throws a NotFoundException if the feedback
	 * object is not found
	 * 
	 * @param id The id of the feedback you want to get.
	 * @return A feedback object
	 */
	public FeedBack getFeedBack(Integer id) {
		return feedBackRepo.findById(id).orElseThrow(()->new NotFoundException("Feedback"));
	}
}
