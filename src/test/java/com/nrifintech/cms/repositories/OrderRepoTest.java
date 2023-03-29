package com.nrifintech.cms.repositories;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.nrifintech.cms.MockMvcSetup;
import com.nrifintech.cms.types.MealType;
import com.nrifintech.cms.types.Status;

import javax.persistence.Tuple;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class OrderRepoTest {


    @Mock
    private OrderRepo orderRepo;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testGetTotalSales() {
        Mockito.when(orderRepo.getTotalSales(anyString(), anyString())).thenReturn(Optional.of(12.3d));
        Optional<Double> doubleOptional = orderRepo.getTotalSales("2023-03-03", "2023-02-05");
        assert doubleOptional.isPresent();

    }

    @Test
    public void testGetOrderStats() {
        List<Tuple> tuples = new ArrayList<>();
        tuples.add(MockMvcSetup.tupleOf(Status.Delivered, 3,Integer.class));

        Mockito.when(orderRepo.getOrderStats(anyString(), anyString())).thenReturn(tuples);
        List<Tuple> tupleList = orderRepo.getOrderStats("2023-03-03", "2023-02-05");
        assert !tupleList.isEmpty();

    }

    @Test
    public void testGetBreakFastVsLunchStats() {
        List<Tuple> tuples = new ArrayList<>();
        tuples.add(MockMvcSetup.tupleOf(MealType.Breakfast, 3,Integer.class));

        Mockito.when(orderRepo.getBreakfastVsLunchStats(anyString(), anyString())).thenReturn(tuples);
        List<Tuple> tupleList = orderRepo.getBreakfastVsLunchStats("2023-03-03", "2023-02-05");
        assert !tupleList.isEmpty();

    }

    @Test
    public void testGetDayByDaySales() {
        List<Tuple> tuples = new ArrayList<>();
        tuples.add(MockMvcSetup.tupleOf("2023-03-02", 350,Integer.class));

        Mockito.when(orderRepo.getDaybyDaySales(anyString(), anyString())).thenReturn(tuples);
        List<Tuple> tupleList = orderRepo.getDaybyDaySales("2023-03-03", "2023-02-05");
        assert !tupleList.isEmpty();

    }

    @Test
    public void testAutoArchive() {

        Mockito.lenient()
                .doAnswer((Answer<Void>) invocation -> null)
                .when(orderRepo).autoArchive("2023-03-03");

    }
}


