package com.nrifintech.cms.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.Tuple;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.nrifintech.cms.MockMvcSetup;

@RunWith(MockitoJUnitRunner.class)
public class PurchaseRepoTest {

    @Mock
    private PurchaseRepo purchaseRepo;
    

    @Before
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetExpDate() {

        Optional<Double> totalExp = Optional.of(10000d);

        Mockito.when(purchaseRepo.getTotalExp(anyString(), anyString()))
        .thenReturn(totalExp);

        Optional<Double> expectedExp = purchaseRepo.getTotalExp("2023-03-04", "2023-03-01");

        assertEquals(totalExp.get() , expectedExp.get());

    }

    @Test
    public void testGetTotalExp() {

        List<Tuple> tuples = new ArrayList<>();
        tuples.add(MockMvcSetup.tupleOf
        ("2023-03-04", 3, String.class));

        Mockito.when(purchaseRepo.getExpDate(anyString(), anyString()))
        .thenReturn(tuples);

        List<Tuple> expectedTuples = purchaseRepo.getExpDate("2023-03-04", "2023-03-01");

        assertNotNull(expectedTuples);
        assertEquals(expectedTuples.get(0).get(0,String.class) , tuples.get(0).get(0,String.class) );
    }
}
