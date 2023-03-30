package com.nrifintech.cms.controllers;

import static org.mockito.ArgumentMatchers.anyList;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.nrifintech.cms.MockMvcSetup;
import com.nrifintech.cms.dtos.AnalyticsResponseDate;
import com.nrifintech.cms.dtos.BestSellerResponse;
import com.nrifintech.cms.dtos.FeedBackRatingStats;
import com.nrifintech.cms.dtos.FeedbackStats;
import com.nrifintech.cms.dtos.OrderMealtypeReport;
import com.nrifintech.cms.dtos.OrderStatusReport;
import com.nrifintech.cms.dtos.UsersByRole;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.AnalyticsService;
import com.nrifintech.cms.types.Feedback;
import com.nrifintech.cms.types.MealType;
import com.nrifintech.cms.types.Role;
import com.nrifintech.cms.types.Status;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class AnalyticsControllerTest extends MockMvcSetup{

    @Mock
    private AnalyticsService analyticsService;

    @InjectMocks
    private AnalyticsController analyticsController;

    private List<BestSellerResponse> bestSellerResponse = new ArrayList<>();
    private FeedbackStats feedbackStats;
    private List<UsersByRole> usersByRoleResponse = new ArrayList<>();
    private List<OrderMealtypeReport> orderMealtypeReportResponse = new ArrayList<>();
    private List<OrderStatusReport> orderStatusReportResponse = new ArrayList<>();
    private List<AnalyticsResponseDate> analyticsResponseDate = new ArrayList<>();
    private List<FeedBackRatingStats> feedBackRatingResponse= new ArrayList<>();

    private Map<Integer , String> validDates;
    private Map<Integer, String> invalidDates;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcSetup.setUp(Route.Analytics.prefix , this , analyticsController );
        loadData();
    }
    public void loadData() {

            this.validDates = new HashMap<Integer, String>();
            this.validDates.put(0, "2023-03-11");
            this.validDates.put(1, "2023-03-16");

            this.invalidDates = new HashMap<Integer, String>();
            this.invalidDates.put(0, "2023-04-11");
            this.invalidDates.put(1, "2023-04-16");

            this.bestSellerResponse.add(new BestSellerResponse("Chicken" , 100));
            this.bestSellerResponse.add(new BestSellerResponse("Paneer" , 99));
            this.bestSellerResponse.add(new BestSellerResponse("Roti" , 76));

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
    public void testGetBestSeller() throws UnsupportedEncodingException, Exception {
        Mockito.when(this.analyticsService.getBestSeller( this.validDates.get(0) , this.validDates.get(1) ) ).thenReturn(this.bestSellerResponse);

        String validResponse = mockMvc.perform(
            MockMvcRequestBuilders.get(prefix(Route.Analytics.getBestSeller  + "/" + this.validDates.get(0) + "/" + this.validDates.get(1)))
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        String invalidResponse1 = mockMvc.perform(
            MockMvcRequestBuilders.get(prefix(Route.Analytics.getBestSeller  + "/" + this.invalidDates.get(0) + "/" + this.invalidDates.get(1)))
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        String invalidResponse2 = mockMvc.perform(
            MockMvcRequestBuilders.get(prefix(Route.Analytics.getBestSeller  + "/" + this.validDates.get(1) + "/" + this.validDates.get(0)))
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        BestSellerResponse[] valid = mapFromJson( validResponse , BestSellerResponse[].class);
        BestSellerResponse[] invalid1 = mapFromJson( invalidResponse1, BestSellerResponse[].class);
        BestSellerResponse[] invalid2 = mapFromJson( invalidResponse2, BestSellerResponse[].class);

        assertArrayEquals(valid, this.bestSellerResponse.toArray() );
        assertTrue(invalid1.length == 0);
        assertTrue(invalid2.length == 0);
    }

    @Test
    public void testGetDateWiseExp() throws UnsupportedEncodingException, Exception {

        Mockito.when(this.analyticsService.getExpDate(this.validDates.get(0), this.validDates.get(1))).thenReturn(analyticsResponseDate);

        String validResponse = mockMvc.perform(
            MockMvcRequestBuilders.get(prefix(Route.Analytics.getDateWiseExp  + "/" + this.validDates.get(0) + "/" + this.validDates.get(1)))
           .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        String invalidResponse1 = mockMvc.perform(
            MockMvcRequestBuilders.get(prefix(Route.Analytics.getDateWiseExp  + "/" + this.invalidDates.get(0) + "/" + this.invalidDates.get(1)))
          .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        String invalidResponse2 = mockMvc.perform(
            MockMvcRequestBuilders.get(prefix(Route.Analytics.getDateWiseExp  + "/" + this.validDates.get(1) + "/" + this.validDates.get(0)))
          .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        AnalyticsResponseDate[] valid = mapFromJson( validResponse, AnalyticsResponseDate[].class);
        AnalyticsResponseDate[] invalid1 = mapFromJson( invalidResponse1, AnalyticsResponseDate[].class);
        AnalyticsResponseDate[] invalid2 = mapFromJson( invalidResponse2, AnalyticsResponseDate[].class);

        assertArrayEquals( valid , this.analyticsResponseDate.toArray() );
        assertTrue(invalid1.length == 0);
        assertTrue(invalid2.length == 0);

    }

    @Test
    public void testGetDateWiseSales() throws UnsupportedEncodingException, Exception {

        Mockito.when(this.analyticsService.getSalesDate(this.validDates.get(0), this.validDates.get(1))).thenReturn(analyticsResponseDate);

        String validResponse = mockMvc.perform(
            MockMvcRequestBuilders.get(prefix(Route.Analytics.getDateWiseSales  + "/" + this.validDates.get(0) + "/" + this.validDates.get(1)))
          .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        String invalidResponse1 = mockMvc.perform(
            MockMvcRequestBuilders.get(prefix(Route.Analytics.getDateWiseSales  + "/" + this.invalidDates.get(0) + "/" + this.invalidDates.get(1)))
         .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        String invalidResponse2 = mockMvc.perform(
            MockMvcRequestBuilders.get(prefix(Route.Analytics.getDateWiseSales  + "/" + this.validDates.get(1) + "/" + this.validDates.get(0)))
        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        AnalyticsResponseDate[] valid = mapFromJson( validResponse, AnalyticsResponseDate[].class);
        AnalyticsResponseDate[] invalid1 = mapFromJson( invalidResponse1, AnalyticsResponseDate[].class);
        AnalyticsResponseDate[] invalid2 = mapFromJson( invalidResponse2, AnalyticsResponseDate[].class);

        assertArrayEquals(valid, this.analyticsResponseDate.toArray() );
        assertTrue(invalid1.length == 0);
        assertTrue(invalid2.length == 0);

    }

    @Test
    public void testGetFeedbacksStats() throws UnsupportedEncodingException, Exception {

        Mockito.when( this.analyticsService.getFeedBackReport()).thenReturn( this.feedbackStats);

        String validResponse = mockMvc.perform(
            MockMvcRequestBuilders.get(prefix(Route.Analytics.getFeedbackStats))
          .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        FeedbackStats valid = mapFromJson( validResponse , FeedbackStats.class);
        assertEquals(valid , this.feedbackStats);
    }

    @Test
    public void testGetOrderMealtypeStats() throws UnsupportedEncodingException, Exception {

        Mockito.when( this.analyticsService.getOrderMealTypeReport(this.validDates.get(0), this.validDates.get(1))).thenReturn(orderMealtypeReportResponse);

        String validResponse = mockMvc.perform(
            MockMvcRequestBuilders.get(prefix(Route.Analytics.getOrderStats + "/mealtypestats"  + "/" + this.validDates.get(0) + "/" + this.validDates.get(1)))
          .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        String invalidResponse1 = mockMvc.perform(
            MockMvcRequestBuilders.get(prefix(Route.Analytics.getOrderStats + "/mealtypestats"  + "/" + this.invalidDates.get(0) + "/" + this.invalidDates.get(1)))
        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        String invalidResponse2 = mockMvc.perform(
            MockMvcRequestBuilders.get(prefix(Route.Analytics.getOrderStats + "/mealtypestats"  + "/" + this.validDates.get(1) + "/" + this.validDates.get(0)))
       .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        OrderMealtypeReport[] valid = mapFromJson( validResponse, OrderMealtypeReport[].class);
        OrderMealtypeReport[] invalid1 = mapFromJson( invalidResponse1, OrderMealtypeReport[].class);
        OrderMealtypeReport[] invalid2 = mapFromJson( invalidResponse2, OrderMealtypeReport[].class);

        assertArrayEquals(valid, this.orderMealtypeReportResponse.toArray() );
        assertTrue(invalid1.length == 0);
        assertTrue(invalid2.length == 0);
    }

    @Test
    public void testGetOrderStats() throws UnsupportedEncodingException, Exception {

        Mockito.when( this.analyticsService.getStatusReport(this.validDates.get(0), this.validDates.get(1))).thenReturn(orderStatusReportResponse);

        String validResponse = mockMvc.perform(
            MockMvcRequestBuilders.get(prefix(Route.Analytics.getOrderStats + "/statusstats"  + "/" + this.validDates.get(0) + "/" + this.validDates.get(1)))
          .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        String invalidResponse1 = mockMvc.perform(
            MockMvcRequestBuilders.get(prefix(Route.Analytics.getOrderStats + "/statusstats"  + "/" + this.invalidDates.get(0) + "/" + this.invalidDates.get(1)))
       .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        String invalidResponse2 = mockMvc.perform(
            MockMvcRequestBuilders.get(prefix(Route.Analytics.getOrderStats + "/statusstats"  + "/" + this.validDates.get(1) + "/" + this.validDates.get(0)))
      .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        OrderStatusReport[] valid = mapFromJson(validResponse, OrderStatusReport[].class);
        OrderStatusReport[] invalid1 = mapFromJson(invalidResponse1, OrderStatusReport[].class);
        OrderStatusReport[] invalid2 = mapFromJson(invalidResponse2, OrderStatusReport[].class);

        assertArrayEquals(valid, this.orderStatusReportResponse.toArray() );
        assertTrue(invalid1.length == 0);
        assertTrue(invalid2.length == 0);

    }

    @Test
    public void testGetTotalExp() throws UnsupportedEncodingException, Exception {

        Mockito.when(this.analyticsService.getTotalExp(this.validDates.get(0), this.validDates.get(1))).thenReturn(1000.89);
        
        String validResponse = mockMvc.perform(
            MockMvcRequestBuilders.get(prefix(Route.Analytics.getTotalExp  + "/" + this.validDates.get(0) + "/" + this.validDates.get(1)))
         .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        String invalidResponse1 = mockMvc.perform(
            MockMvcRequestBuilders.get(prefix(Route.Analytics.getTotalExp  + "/" + this.invalidDates.get(0) + "/" + this.invalidDates.get(1)))
        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        String invalidResponse2 = mockMvc.perform(
            MockMvcRequestBuilders.get(prefix(Route.Analytics.getTotalExp  + "/" + this.validDates.get(1) + "/" + this.validDates.get(0)))
      .contentType(MediaType.APPLICATION_JSON)
      ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Double valid = Double.parseDouble(validResponse);
        Double invalid1 = Double.parseDouble(invalidResponse1);
        Double invalid2 = Double.parseDouble(invalidResponse2);

        assertEquals(valid, 1000.89);
        assertTrue(invalid1 == 0.0);
        assertTrue(invalid2 == 0.0);

    }

    @Test
    public void testGetTotalSales() throws UnsupportedEncodingException, Exception {

        Mockito.when(this.analyticsService.getTotalSales(this.validDates.get(0), this.validDates.get(1))).thenReturn(1000.89);
        
        String validResponse = mockMvc.perform(
            MockMvcRequestBuilders.get(prefix(Route.Analytics.getTotalSales  + "/" + this.validDates.get(0) + "/" + this.validDates.get(1)))
         .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        String invalidResponse1 = mockMvc.perform(
            MockMvcRequestBuilders.get(prefix(Route.Analytics.getTotalSales  + "/" + this.invalidDates.get(0) + "/" + this.invalidDates.get(1)))
        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        String invalidResponse2 = mockMvc.perform(
            MockMvcRequestBuilders.get(prefix(Route.Analytics.getTotalSales  + "/" + this.validDates.get(1) + "/" + this.validDates.get(0)))
      .contentType(MediaType.APPLICATION_JSON)
      ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Double valid = Double.parseDouble(validResponse);
        Double invalid1 = Double.parseDouble(invalidResponse1);
        Double invalid2 = Double.parseDouble(invalidResponse2);

        assertEquals(valid, 1000.89);
        assertTrue(invalid1 == 0.0);
        assertTrue(invalid2 == 0.0);

    }

    @Test
    public void testGetUserStats() throws UnsupportedEncodingException, Exception {

        Mockito.when( this.analyticsService.getUserGroup() ).thenReturn(usersByRoleResponse);

        String validResponse = mockMvc.perform(
            MockMvcRequestBuilders.get(prefix(Route.Analytics.getUserStats))
         .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

       UsersByRole[] valid = mapFromJson(validResponse,  UsersByRole[].class);
       assertArrayEquals(valid, this.usersByRoleResponse.toArray() );
    }
}
