package com.nrifintech.cms.services;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.nrifintech.cms.repositories.FeedBackRepo;

public class FeedBackServiceTest {
    
	@Mock
	private FeedBackRepo feedBackRepo;
    @InjectMocks
    private FeedBackService feedBackService;
    @Test
    void testAddFeedBack() {
        // when(feedBackRepo.save(any())).

    }

    @Test
    void testGetFeedBack() {

    }
}
