package com.nrifintech.cms.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Tuple;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.nrifintech.cms.MockMvcSetup;
import com.nrifintech.cms.entities.FeedBack;

@RunWith(MockitoJUnitRunner.class)
public class FeedBackRepoTest {

    @Mock
    private FeedBackRepo feedBackRepo;

    @Before
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFeedbackRatingStats() {


        Tuple tuple = MockMvcSetup.tupleOf(
        FeedBack.builder().id(12).comments("nice food").rating(4).build()
        , 3, Integer.class);

        List<Tuple> tuples = new ArrayList<>();
        tuples.add(tuple);

        Mockito.when(feedBackRepo.feedbackRatingStats()).thenReturn(tuples);

        List<Tuple> expectedTuples = feedBackRepo.feedbackRatingStats();

        assertNotNull(expectedTuples);
        assertEquals(expectedTuples.size(), tuples.size());
        assertEquals(expectedTuples.get(0),  tuples.get(0));
    }

    @Test
    public void testFeedbackStats() {


        Tuple tuple = MockMvcSetup.tupleOf(
           4, 3, Integer.class);
    
            List<Tuple> tuples = new ArrayList<>();
            tuples.add(tuple);
    
            Mockito.when(feedBackRepo.feedbackStats()).thenReturn(tuple);
    
            Tuple expectedTuple = feedBackRepo.feedbackStats();
    
            assertNotNull(expectedTuple);
            assertEquals(expectedTuple.get(0),  tuple.get(0));
    }
}
