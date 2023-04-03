package com.nrifintech.cms.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Tuple;

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

/**
 * This class is used to get analytics data from the database
 */
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
    /**
     * It takes two dates as input, and returns a list of objects of type AnalyticsResponseDate, which
     * contains the date and the number of purchases made on that date
     * 
     * @param date1 the start date
     * @param date2 the end date
     * @return A list of AnalyticsResponseDate objects.
     */
    public List<AnalyticsResponseDate> getExpDate(String date1 , String date2){
        List<Tuple> resTup = ( this.purchaseRepo.getExpDate(date1, date2) );
        List<AnalyticsResponseDate> result = resTup.stream().map(t->new AnalyticsResponseDate(t.get(0, String.class),t.get(1) )).collect(Collectors.toList());
        return( result );
    }

    //get total expenses
   /**
    * It returns the sum of all the expenses between two dates
    * 
    * @param date1 the start date
    * @param date2 the date of the last day of the month
    * @return The total expense of the company between two dates.
    */
    public Double getTotalExp(String date1 , String date2){
        return( this.purchaseRepo.getTotalExp(date1, date2).orElse(0.0) );
    }

    //get day by day sales
     /**
     * It takes two dates as input, and returns a list of objects of type AnalyticsResponseDate, which
     * contains the date and the number of sales made on that date
     * 
     * @param date1 the start date
     * @param date2 the end date
     * @return A list of AnalyticsResponseDate objects.
     */
    public List<AnalyticsResponseDate> getSalesDate(String date1 , String date2){
        List<Tuple> resTup = ( this.orderRepo.getDaybyDaySales(date1, date2) );
        System.out.println( resTup.get(0).get(1).getClass() );
        List<AnalyticsResponseDate> result = resTup.stream().map(t->new AnalyticsResponseDate(t.get(0, String.class), t.get(1) )).collect(Collectors.toList());
        return( result );
    }

    //get total sales
   /**
    * It returns the total sales of the company between two dates
    * 
    * @param date1 the start date
    * @param date2 the end date
    * @return The total sales for the given date range.
    */
    public Double getTotalSales(String date1 , String date2){
        return( this.orderRepo.getTotalSales(date1, date2).orElse(0.0) );
    }

     //get bestSeller
    /**
     * It takes two dates as input and returns a list of BestSellerResponse objects
     * 
     * @param date1 the start date of the period
     * @param date2 the end date of the period
     * @return A list of BestSellerResponse objects.
     */
     public List<BestSellerResponse> getBestSeller(String date1 , String date2){
        List<Tuple> resTup = ( this.cartItemRepo.getBestSeller(date1, date2) );
        List<BestSellerResponse> result = resTup.stream().map(t->new BestSellerResponse(t.get(0, String.class), t.get(1))).collect(Collectors.toList());
        return( result );
    }

    //get order status stats
   /**
    * It takes two dates as input, and returns a list of OrderStatusReport objects, each of which
    * contains a status and a count of the number of orders with that status
    * 
    * @param date1 the start date of the report
    * @param date2 the end date of the report
    * @return A list of OrderStatusReport objects.
    */
    public List<OrderStatusReport> getStatusReport(String date1 , String date2){
        List<Tuple> resTup = this.orderRepo.getOrderStats(date1, date2);
        List<OrderStatusReport> result = resTup.stream().map(t->new OrderStatusReport( Status.values()[(t.get(0,Integer.class))] , t.get(1) )).collect(Collectors.toList());
        return(result);
    }

    //get breakfast vs lunch stats
   /**
    * It takes two dates as input, and returns a list of objects of type OrderMealtypeReport, which
    * contains the meal type and the number of orders for that meal type
    * 
    * @param date1 the start date of the report
    * @param date2 the end date of the report
    * @return A list of OrderMealtypeReport objects.
    */
    public List<OrderMealtypeReport> getOrderMealTypeReport(String date1 , String date2){
        List<Tuple> resTup = this.orderRepo.getBreakfastVsLunchStats(date1, date2);
        List<OrderMealtypeReport> result = resTup.stream().map(t->new OrderMealtypeReport( MealType.values()[(t.get(0,Integer.class))] , t.get(1) )).collect(Collectors.toList());
        return(result);
    }

    //users by role
   /**
    * It returns a list of objects of type UsersByRole, which
    * contains the user type and the number of users for that user type
    * 
    * @return A list of UsersByRole objects.
    */
    public List<UsersByRole> getUserGroup(){
        List<Tuple> resTup = this.userRepo.getAllUserGroup();
        List<UsersByRole> result = resTup.stream().map(u->new UsersByRole( Role.values()[u.get(0 , Integer.class)]  , u.get(1) ) ).collect(Collectors.toList());
        return(result);
    }

    //Complete Feedback report
  /**
   * It returns a FeedbackStats object which contains the total number of feedbacks, the total number
   * of feedbacks with a rating, and a list of FeedBackRatingStats objects which contain the rating and
   * the number of feedbacks with that rating
   * 
   * @return A tuple of the number of feedbacks and the number of feedbacks with a rating.
   */
    public FeedbackStats getFeedBackReport(){
        if(this.feedBackRepo.findAll().size()==0){
            return(null);
        }
        Tuple resTup1 = this.feedBackRepo.feedbackStats();
        List<Tuple> resTup2 = this.feedBackRepo.feedbackRatingStats();
        List<FeedBackRatingStats> result1 = resTup2.stream().map( f-> new FeedBackRatingStats( Feedback.values()[f.get(0,Integer.class)] , f.get(1) )).collect(Collectors.toList());
        FeedbackStats result = new FeedbackStats( resTup1.get(0) , resTup1.get(1) , result1 );
        return(result); 
    }

}
