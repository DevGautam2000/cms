package com.nrifintech.cms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.AnalyticsService;
import com.nrifintech.cms.types.Response;

@RestController
@CrossOrigin
@RequestMapping(Route.Analytics.prefix)
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping(Route.Analytics.getTotalExp + "/{date1}" + "/{date2}")
    public Response getTotalExp(@PathVariable String date1 , @PathVariable String date2){
        return( Response.set(this.analyticsService.getTotalExp(date1, date2) , HttpStatus.OK));
    }
    @GetMapping(Route.Analytics.getDateWiseExp + "/{date1}" + "/{date2}")
    public Response getDateWiseExp(@PathVariable String date1 , @PathVariable String date2){
        return( Response.set(this.analyticsService.getExpDate(date1, date2) , HttpStatus.OK));
    }
    @GetMapping(Route.Analytics.getTotalSales + "/{date1}" + "/{date2}")
    public Response getTotalSales(@PathVariable String date1 , @PathVariable String date2){
        return( Response.set(this.analyticsService.getTotalSales(date1, date2) , HttpStatus.OK));
    }
    @GetMapping(Route.Analytics.getDateWiseSales + "/{date1}" + "/{date2}")
    public Response getDateWiseSales(@PathVariable String date1 , @PathVariable String date2){
        return( Response.set(this.analyticsService.getSalesDate(date1, date2) , HttpStatus.OK));
    }
    @GetMapping(Route.Analytics.getBestSeller + "/{date1}" + "/{date2}")
    public Response getBestSeller(@PathVariable String date1 , @PathVariable String date2){
        return( Response.set(this.analyticsService.getBestSeller(date1, date2) , HttpStatus.OK));
    }
    @GetMapping(Route.Analytics.getOrderStats + "/statusstats" + "/{date1}" + "/{date2}")
    public Response getOrderStats(@PathVariable String date1 , @PathVariable String date2){
        return( Response.set(this.analyticsService.getStatusReport(date1, date2) , HttpStatus.OK));
    }
    @GetMapping(Route.Analytics.getOrderStats + "/mealtypestats" + "/{date1}" + "/{date2}")
    public Response getOrderMealtypeStats(@PathVariable String date1 , @PathVariable String date2){
        return( Response.set(this.analyticsService.getOrderMealTypeReport(date1, date2) , HttpStatus.OK));
    }
}
