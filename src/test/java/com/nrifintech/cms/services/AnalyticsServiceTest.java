package com.nrifintech.cms.services;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.nrifintech.cms.MockMvcSetup;
import com.nrifintech.cms.dtos.AnalyticsResponseDate;
import com.nrifintech.cms.dtos.BestSellerResponse;
import com.nrifintech.cms.dtos.FeedBackRatingStats;
import com.nrifintech.cms.dtos.FeedbackStats;
import com.nrifintech.cms.dtos.OrderMealtypeReport;
import com.nrifintech.cms.dtos.OrderStatusReport;
import com.nrifintech.cms.dtos.UsersByRole;
import com.nrifintech.cms.entities.FeedBack;
import com.nrifintech.cms.repositories.CartItemRepo;
import com.nrifintech.cms.repositories.FeedBackRepo;
import com.nrifintech.cms.repositories.OrderRepo;
import com.nrifintech.cms.repositories.PurchaseRepo;
import com.nrifintech.cms.repositories.UserRepo;
import com.nrifintech.cms.types.Feedback;
import com.nrifintech.cms.types.MealType;
import com.nrifintech.cms.types.Role;
import com.nrifintech.cms.types.Status;

@RunWith(MockitoJUnitRunner.class)
public class AnalyticsServiceTest {
    private List<BestSellerResponse> bestSellerResponse = new ArrayList<>();
    private FeedbackStats feedbackStats;
    private List<UsersByRole> usersByRoleResponse = new ArrayList<>();
    private List<OrderMealtypeReport> orderMealtypeReportResponse = new ArrayList<>();
    private List<OrderStatusReport> orderStatusReportResponse = new ArrayList<>();
    private List<AnalyticsResponseDate> analyticsResponseDate = new ArrayList<>();
    private List<FeedBackRatingStats> feedBackRatingResponse= new ArrayList<>();

    private Map<Integer , String> validDates;
    private Map<Integer, String> invalidDates;
    
    @Mock
    private PurchaseRepo purchaseRepo;

    @Mock
    private OrderRepo orderRepo;

    @Mock
    private CartItemRepo cartItemRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private FeedBackRepo feedBackRepo;

    @InjectMocks
    private AnalyticsService analyticsService;
    
    @Before
    public void loadData() {

        this.validDates = new HashMap<Integer, String>();
        this.validDates.put(0, "2023-03-11");
        this.validDates.put(1, "2023-03-16");

        this.invalidDates = new HashMap<Integer, String>();
        this.invalidDates.put(0, "2023-04-11");
        this.invalidDates.put(1, "2023-04-16");

        this.bestSellerResponse.add(new BestSellerResponse("Chicken" ,"", 100));
        this.bestSellerResponse.add(new BestSellerResponse("Paneer" ,"", 99));
        this.bestSellerResponse.add(new BestSellerResponse("Roti" ,"", 76));

        this.usersByRoleResponse.add( new UsersByRole(Role.Admin , 3));
        this.usersByRoleResponse.add( new UsersByRole(Role.Canteen, 2));
        this.usersByRoleResponse.add( new UsersByRole(Role.User, 1));

        this.orderMealtypeReportResponse.add(new OrderMealtypeReport(MealType.Breakfast, 100));
        this.orderMealtypeReportResponse.add(new OrderMealtypeReport(MealType.Lunch, 99));

        this.orderStatusReportResponse.add(new OrderStatusReport(Status.Pending , 100));
        this.orderStatusReportResponse.add(new OrderStatusReport(Status.Delivered , 99));
        this.orderStatusReportResponse.add(new OrderStatusReport(Status.Cancelled , 98));
        this.orderStatusReportResponse.add(new OrderStatusReport(Status.NotDelivered , 34));

        this.analyticsResponseDate.add( new AnalyticsResponseDate("2023-03-11" , 34));
        this.analyticsResponseDate.add( new AnalyticsResponseDate("2023-03-12" , 38));
        this.analyticsResponseDate.add( new AnalyticsResponseDate("2023-03-13" , 30));
        this.analyticsResponseDate.add( new AnalyticsResponseDate("2023-03-14" , 35));
        this.analyticsResponseDate.add( new AnalyticsResponseDate("2023-03-15" , 90));
        this.analyticsResponseDate.add( new AnalyticsResponseDate("2023-03-16" , 89));

        this.feedBackRatingResponse.add(new FeedBackRatingStats(Feedback.Five , 1));
        this.feedBackRatingResponse.add(new FeedBackRatingStats(Feedback.Four , 1));
        this.feedBackRatingResponse.add(new FeedBackRatingStats(Feedback.Three , 1));
        this.feedBackRatingResponse.add(new FeedBackRatingStats(Feedback.Two , 1));
        this.feedBackRatingResponse.add(new FeedBackRatingStats(Feedback.One , 1));

        this.feedbackStats = new FeedbackStats( 5 , 3 , feedBackRatingResponse);
    }

    @Test
    public void testGetBestSeller() {
        when(cartItemRepo.getBestSeller(any(), any())).thenReturn(
            bestSellerResponse.stream().map(e->   
                MockMvcSetup
                .tupleOf(e.getName(), "" , e.getCount().toString(), String.class)
            ).collect(Collectors.toList())
        );

        System.out.println(bestSellerResponse);
        assertEquals(bestSellerResponse.toString(), 
            analyticsService.getBestSeller("31-3-2023","31-3-2023").toString()
        );
    }

    @Test
    public void testGetExpDate() {
        when(this.purchaseRepo.getExpDate(any(), any())).thenReturn(
            analyticsResponseDate.stream().map(e->   
                MockMvcSetup
                .tupleOf(e.getDate(), e.getValue().toString(), String.class)
            ).collect(Collectors.toList())
        );
       
        assertEquals(analyticsResponseDate.size(), 
            analyticsService.getExpDate(this.validDates.get(0), this.validDates.get(1)).size()
        );
    }

    @Test
    public void testGetFeedBackReport() {
        //
        when(feedBackRepo.findAll()).thenReturn(
            Arrays.asList(new FeedBack())
        );
        when(feedBackRepo.feedbackStats()).thenReturn(
            MockMvcSetup
            .tupleOf(feedbackStats.getTotalCount(), feedbackStats.getAvgRating(), Integer.class)
        );

        when(feedBackRepo.feedbackRatingStats()).thenReturn(
             feedbackStats.getFeedbackRaingStats().stream().map(e->   
                MockMvcSetup
                .tupleOf(e.getFeedback().ordinal(), e.getCount(), Integer.class)
            ).collect(Collectors.toList())
        );

        
        assertEquals(feedbackStats, 
            analyticsService.getFeedBackReport()
        );
    }

    @Test
    public void testGetOrderMealTypeReport() {
        when(orderRepo.getBreakfastVsLunchStats(any(), any())).thenReturn(
            orderMealtypeReportResponse.stream().map(e->   
                MockMvcSetup
                .tupleOf(e.getMealType().ordinal(), e.getCount(), Integer.class)
            ).collect(Collectors.toList())
        );

        assertEquals(orderMealtypeReportResponse.size(), 
            analyticsService.getOrderMealTypeReport("20-5-2023","21-5-2023").size()
        );
    }

    @Test
    public void testGetSalesDate() {
        when(orderRepo.getDaybyDaySales(any(), any())).thenReturn(
            analyticsResponseDate.stream().map(e->   
                MockMvcSetup
                .tupleOf(e.getDate(), e.getValue().toString(), String.class)
            ).collect(Collectors.toList())
        );

        assertEquals(analyticsResponseDate.size(), 
            analyticsService.getSalesDate("20-5-2023","21-5-2023").size()
        );
    }

    @Test
    public void testGetStatusReport() {
        when(orderRepo.getOrderStats(any(), any())).thenReturn(
                orderStatusReportResponse.stream().map(e->   
                MockMvcSetup
                .tupleOf(e.getStatus().ordinal(), e.getCount(), Integer.class)
            ).collect(Collectors.toList())
        );

        assertArrayEquals(orderStatusReportResponse.toArray(), 
            analyticsService.getStatusReport("12-3-2023", "12-3-2023") .toArray()
        );
    }

    @Test
    public void testGetTotalExp() {
        when(purchaseRepo.getTotalExp(anyString(),anyString())).thenReturn(Optional.of(43.0));

        assertEquals(43.0, analyticsService.getTotalExp("12-3-2023", "12-3-2023"));
        
        
        when(purchaseRepo.getTotalExp(anyString(),anyString())).thenReturn(Optional.empty() );

        assertEquals(0.0, analyticsService.getTotalExp("12-3-2023", "12-3-2023"));
    }

    @Test
    public void testGetTotalSales() {
        when(orderRepo.getTotalSales(anyString(),anyString())).thenReturn(Optional.of(43.0));

        assertEquals(43.0, analyticsService.getTotalSales("12-3-2023", "12-3-2023"));
        
        
        when(orderRepo.getTotalSales(anyString(),anyString())).thenReturn(Optional.empty() );

        assertEquals(0.0, analyticsService.getTotalSales("12-3-2023", "12-3-2023"));
    
    }

    @Test
    public void testGetUserGroup() {
        when(userRepo.getAllUserGroup()).thenReturn(
            usersByRoleResponse.stream().map(e->   
                MockMvcSetup
                .tupleOf(e.getRole().ordinal() , e.getCount(), Integer.class)
            ).collect(Collectors.toList())
        );

        assertArrayEquals(usersByRoleResponse.toArray(), 
            analyticsService.getUserGroup().toArray()
        );
    }
}
