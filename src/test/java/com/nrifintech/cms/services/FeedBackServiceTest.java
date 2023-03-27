package com.nrifintech.cms.services;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nrifintech.cms.entities.FeedBack;
import com.nrifintech.cms.errorhandler.NotFoundException;
import com.nrifintech.cms.repositories.FeedBackRepo;

@ExtendWith(MockitoExtension.class)
public class FeedBackServiceTest {
    
	@Mock
	private FeedBackRepo feedBackRepo;
    @InjectMocks
    private FeedBackService feedBackService;
    FeedBack f = new FeedBack(1,3,"okeish");

    @Test
    void testAddFeedBack() {
        when(feedBackRepo.save(f)).thenReturn(f);
        assertEquals(f.toString() , feedBackService.addFeedBack(f).toString());

    }

    @Test
    void testGetFeedBack() {

        when(feedBackRepo.findById(f.getId())).thenReturn(Optional.ofNullable(f));
        assertEquals(f.toString() , feedBackService.getFeedBack(f.getId()).toString());
        when(feedBackRepo.findById(f.getId())).thenReturn(Optional.ofNullable(null));
        assertThrows(NotFoundException.class , ()-> feedBackService.getFeedBack(f.getId()).toString());
    }
}
