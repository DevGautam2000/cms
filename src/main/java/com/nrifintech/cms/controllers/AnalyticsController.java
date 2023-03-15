package com.nrifintech.cms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.AnalyticsService;
import com.nrifintech.cms.types.Response;

@RestController
@RequestMapping(Route.Analytics.prefix)
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping(Route.Analytics.getTotalExp + "/{date1}" + "/{date2}")
    public Response getTotalExp(@PathVariable String date1 , @PathVariable String date2){
        return( Response.set(this.analyticsService.getTotalExp(date1, date2) , HttpStatus.OK));
    }
    @GetMapping(Route.Analytics.getDateWise + "/{date1}" + "/{date2}")
    public Response getDateWise(@PathVariable String date1 , @PathVariable String date2){
        return( Response.set(this.analyticsService.getExpDate(date1, date2) , HttpStatus.OK));
    }
}
