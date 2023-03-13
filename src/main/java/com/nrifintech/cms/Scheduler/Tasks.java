package com.nrifintech.cms.Scheduler;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nrifintech.cms.events.BreakfastStartEvent;
import com.nrifintech.cms.events.LunchStartEvent;
import com.nrifintech.cms.events.PromotionalEvent;
import com.nrifintech.cms.services.OrderService;
import com.nrifintech.cms.services.UserService;
import com.nrifintech.cms.types.MealType;


//@Component
public class Tasks {

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;

    @Scheduled( fixedDelay = 10000)
    public void promotionalEvent(){
        List<String> recipients = userService.getAllConsumers();
        if( recipients.size()!=0 ){
            applicationEventPublisher.publishEvent(new PromotionalEvent(recipients));
        }
    }

    @Scheduled(fixedDelay = 10000)
    public void BreakfastStart(){
        DateTimeFormatter dtf =  DateTimeFormatter.ofPattern("yyyy-MM-dd");  
        LocalDateTime now = LocalDateTime.now();  
        List<String> recipients = userService.getOrdersByDateAndOrderType( Date.valueOf(dtf.format(now).toString()) , MealType.Breakfast.ordinal());
        if(recipients.size()>0){
            applicationEventPublisher.publishEvent(new BreakfastStartEvent(recipients));
        } 
    }
    @Scheduled(fixedDelay = 10000)
    public void LunchStart(){
        DateTimeFormatter dtf =  DateTimeFormatter.ofPattern("yyyy-MM-dd");  
        LocalDateTime now = LocalDateTime.now();  
        List<String> recipients = userService.getOrdersByDateAndOrderType( Date.valueOf(dtf.format(now).toString()) , MealType.Lunch.ordinal());
        if( recipients.size()>0 ){
            applicationEventPublisher.publishEvent(new LunchStartEvent(recipients));
        } 
    }

    @Scheduled(fixedDelay = 10000)
    public void autoArchive(){
        orderService.autoArchive();
    }
    
}
