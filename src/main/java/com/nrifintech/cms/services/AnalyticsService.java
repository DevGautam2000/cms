package com.nrifintech.cms.services;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.persistence.Tuple;

import org.hibernate.BaseSessionEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nrifintech.cms.dtos.AnalyticsResponseDate;
import com.nrifintech.cms.dtos.BestSellerResponse;
import com.nrifintech.cms.dtos.FeedBackRatingStats;
import com.nrifintech.cms.dtos.FeedbackStats;
import com.nrifintech.cms.dtos.OrderMealtypeReport;
import com.nrifintech.cms.dtos.OrderStatusReport;
import com.nrifintech.cms.dtos.UsersByRole;
import com.nrifintech.cms.repositories.CartItemRepo;
import com.nrifintech.cms.repositories.FeedBackRepo;
import com.nrifintech.cms.repositories.OrderRepo;
import com.nrifintech.cms.repositories.PurchaseRepo;
import com.nrifintech.cms.repositories.UserRepo;
import com.nrifintech.cms.types.Feedback;
import com.nrifintech.cms.types.MealType;
import com.nrifintech.cms.types.Role;
import com.nrifintech.cms.types.Status;

@Service
public class AnalyticsService {
    
    @Autowired
    private PurchaseRepo purchaseRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private CartItemRepo cartItemRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private FeedBackRepo feedBackRepo;

    //get expenes day by day
    public List<AnalyticsResponseDate> getExpDate(String date1 , String date2){
        List<Tuple> resTup = ( this.purchaseRepo.getExpDate(date1, date2) );
        List<AnalyticsResponseDate> result = resTup.stream().map(t->new AnalyticsResponseDate(t.get(0, String.class), new BigDecimal( t.get(1, Double.class) ) )).collect(Collectors.toList());
        return( result );
    }

    //get total expenses
    public Double getTotalExp(String date1 , String date2){
        return( this.purchaseRepo.getTotalExp(date1, date2).orElse(0.0) );
    }

    //get day by day sales
    public List<AnalyticsResponseDate> getSalesDate(String date1 , String date2){
        List<Tuple> resTup = ( this.orderRepo.getDaybyDaySales(date1, date2) );
        // System.out.println( resTup.get(0).get(1).getClass().getSimpleName() );
        List<AnalyticsResponseDate> result = resTup.stream().map(t->new AnalyticsResponseDate(t.get(0, String.class), new BigDecimal( t.get(1, Double.class) ) )).collect(Collectors.toList());
        return( result );
    }

    //get total sales
    public Double getTotalSales(String date1 , String date2){
        return( this.orderRepo.getTotalSales(date1, date2).orElse(0.0) );
    }

     //get bestSeller
     public List<BestSellerResponse> getBestSeller(String date1 , String date2){
        List<Tuple> resTup = ( this.cartItemRepo.getBestSeller(date1, date2) );
        List<BestSellerResponse> result = resTup.stream().map(t->new BestSellerResponse(t.get(0, String.class), t.get(1, BigDecimal.class))).collect(Collectors.toList());
        return( result );
    }

    //get order status stats
    public List<OrderStatusReport> getStatusReport(String date1 , String date2){
        List<Tuple> resTup = this.orderRepo.getOrderStats(date1, date2);
        List<OrderStatusReport> result = resTup.stream().map(t->new OrderStatusReport( Status.values()[(t.get(0,Integer.class))] , t.get(1,BigInteger.class) )).collect(Collectors.toList());
        return(result);
    }

    //get breakfast vs lunch stats
    public List<OrderMealtypeReport> getOrderMealTypeReport(String date1 , String date2){
        List<Tuple> resTup = this.orderRepo.getBreakfastVsLunchStats(date1, date2);
        List<OrderMealtypeReport> result = resTup.stream().map(t->new OrderMealtypeReport( MealType.values()[(t.get(0,Integer.class))] , t.get(1,BigInteger.class) )).collect(Collectors.toList());
        return(result);
    }

    //users by role
    public List<UsersByRole> getUserGroup(){
        List<Tuple> resTup = this.userRepo.getAllUserGroup();
        List<UsersByRole> result = resTup.stream().map(u->new UsersByRole( Role.values()[u.get(0 , Integer.class)]  , u.get(1,BigInteger.class) ) ).collect(Collectors.toList());
        return(result);
    }

    //Complete Feedback report
    public FeedbackStats getFeedBackReport(){
        if(this.feedBackRepo.findAll().size()==0){
            return(null);
        }
        Tuple resTup1 = this.feedBackRepo.feedbackStats();
        List<Tuple> resTup2 = this.feedBackRepo.feedbackRatingStats();
        List<FeedBackRatingStats> result1 = resTup2.stream().map( f-> new FeedBackRatingStats( Feedback.values()[f.get(0,Integer.class)] , f.get(1,BigInteger.class) )).collect(Collectors.toList());
        FeedbackStats result = new FeedbackStats( resTup1.get(0,BigInteger.class) , resTup1.get(1,BigDecimal.class) , result1 );
        return(result); 
    }

}
