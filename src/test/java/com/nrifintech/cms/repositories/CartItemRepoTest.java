package com.nrifintech.cms.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;
import javax.persistence.TupleElement;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.nrifintech.cms.MockMvcSetup;


public class CartItemRepoTest {

    
    @Mock
    private CartItemRepo cartItemRepo;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testGetBestSeller() {
        
      Tuple t = MockMvcSetup.tupleOf("dahi",3,String.class);
      List<Tuple> tuples = new ArrayList<>();
      tuples.add(t);

      Mockito.when(cartItemRepo.getBestSeller(anyString(), anyString())).thenReturn(tuples);
     
      List<Tuple> expectedTuples = cartItemRepo.getBestSeller("2023-03-03", "2023-03-23");


      assertNotNull(expectedTuples);
      assertEquals(expectedTuples.size(), tuples.size());
      assertEquals(expectedTuples.get(0).get(0, String.class), tuples.get(0).get(0, String.class));
    }
}
