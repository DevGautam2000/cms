package com.nrifintech.cms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nrifintech.cms.dtos.FeedbackStats;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.AnalyticsService;
import com.nrifintech.cms.types.Response;

/**
 * This class handles requests to the `/analytics` endpoint
 */

@RestController
@RequestMapping(Route.Analytics.prefix)
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    /**
     * This function returns the total expense between two dates
     * 
     * @param date1 The start date of the period you want to get the total expenses for.
     * @param date2 The end date of the range
     * @return A response object with the total expense between the two dates.
     */
    @GetMapping(Route.Analytics.getTotalExp + "/{date1}" + "/{date2}")
    public Response getTotalExp(@PathVariable String date1 , @PathVariable String date2){
        return( Response.set(this.analyticsService.getTotalExp(date1, date2) , HttpStatus.OK));
    }

    /**
     * This function is used to get the expense between two dates
     * 
     * @param date1 The start date of the range
     * @param date2 The end date of the range
     * @return A response object is being returned.
     */
    @GetMapping(Route.Analytics.getDateWiseExp + "/{date1}" + "/{date2}")
    public Response getDateWiseExp(@PathVariable String date1 , @PathVariable String date2){
        return( Response.set(this.analyticsService.getExpDate(date1, date2) , HttpStatus.OK));
    }

    /**
     * This function returns the total sales between two dates
     * 
     * @param date1 The start date of the period you want to get the total sales for.
     * @param date2 The end date of the range
     * @return A response object with the total sales and a status of OK.
     */
    @GetMapping(Route.Analytics.getTotalSales + "/{date1}" + "/{date2}")
    public Response getTotalSales(@PathVariable String date1 , @PathVariable String date2){
        return( Response.set(this.analyticsService.getTotalSales(date1, date2) , HttpStatus.OK));
    }

    /**
     * This function takes two dates as parameters and returns the sales between those two dates
     * 
     * @param date1 The start date of the range
     * @param date2 The end date of the range
     * @return A response object is being returned.
     */
    @GetMapping(Route.Analytics.getDateWiseSales + "/{date1}" + "/{date2}")
    public Response getDateWiseSales(@PathVariable String date1 , @PathVariable String date2){
        return( Response.set(this.analyticsService.getSalesDate(date1, date2) , HttpStatus.OK));
    }

    /**
     * This function returns the best seller of the given date range
     * 
     * @param date1 The start date of the period you want to check
     * @param date2 The end date of the period you want to get the best seller for.
     * @return A list of the best selling products
     */
    @GetMapping(Route.Analytics.getBestSeller + "/{date1}" + "/{date2}")
    public Response getBestSeller(@PathVariable String date1 , @PathVariable String date2){
        return( Response.set(this.analyticsService.getBestSeller(date1, date2) , HttpStatus.OK));
    }

    /**
     * This function returns a list of order status statistics for a given date range
     * 
     * @param date1 The start date of the report
     * @param date2 The end date of the range of dates you want to get the report for.
     * @return A list of order status stats
     */
    @GetMapping(Route.Analytics.getOrderStats + "/statusstats" + "/{date1}" + "/{date2}")
    public Response getOrderStats(@PathVariable String date1 , @PathVariable String date2){
        return( Response.set(this.analyticsService.getStatusReport(date1, date2) , HttpStatus.OK));
    }

    /**
     * This function returns a list of meal types and the number of orders for each meal type between
     * two dates
     * 
     * @param date1 The start date of the report
     * @param date2 The end date of the report
     * @return A list of OrderMealTypeStats
     */
    @GetMapping(Route.Analytics.getOrderStats + "/mealtypestats" + "/{date1}" + "/{date2}")
    public Response getOrderMealtypeStats(@PathVariable String date1 , @PathVariable String date2){
        return( Response.set(this.analyticsService.getOrderMealTypeReport(date1, date2) , HttpStatus.OK));
    }

    /**
     * > This function returns the feedback stats of the application
     * 
     * @return FeedbackStats object
     */
    @GetMapping(Route.Analytics.getFeedbackStats)
    public Response getFeedbacksStats(){
        FeedbackStats f = this.analyticsService.getFeedBackReport();
        if( f == null ){
            return( Response.setMsg("No feedbacks as of now", HttpStatus.BAD_REQUEST));
        }
        return( Response.set( f, HttpStatus.OK));  
    }

    /**
     * > This function returns a list of all the users in the database, along with their respective
     * number of posts, comments, and likes
     * 
     * @return A list of user groups
     */
    @GetMapping(Route.Analytics.getUserStats)
    public Response getUserStats(){
        return( Response.set(this.analyticsService.getUserGroup() , HttpStatus.OK));
    }
}
