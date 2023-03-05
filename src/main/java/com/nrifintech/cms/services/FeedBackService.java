package com.nrifintech.cms.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nrifintech.cms.entities.FeedBack;
import com.nrifintech.cms.errorhandler.NotFoundException;
import com.nrifintech.cms.repositories.FeedBackRepo;

@Service
public class FeedBackService {

	@Autowired
	private FeedBackRepo feedBackRepo;

	public FeedBack addFeedBack(FeedBack feedBack) {
		return feedBackRepo.save(feedBack);
	}
	
	public FeedBack getFeedBack(Integer id) {
		return feedBackRepo.findById(id).orElseThrow(()->new NotFoundException("Feedback"));
	}
}
