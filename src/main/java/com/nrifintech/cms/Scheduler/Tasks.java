package com.nrifintech.cms.Scheduler;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nrifintech.cms.config.jwt.JwtUtils;
import com.nrifintech.cms.entities.TokenBlacklist;
import com.nrifintech.cms.events.BreakfastStartEvent;
import com.nrifintech.cms.events.LunchStartEvent;
import com.nrifintech.cms.events.PromotionalEvent;
import com.nrifintech.cms.repositories.TokenBlacklistRepo;
import com.nrifintech.cms.services.OrderService;
import com.nrifintech.cms.services.UserService;
import com.nrifintech.cms.types.MealType;


@Component
public class Tasks {

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    UserService userService;

    @Autowired
    TokenBlacklistRepo tokenBlacklistRepo;

    @Autowired
    JwtUtils jwtUtils;

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
    //@Scheduled(cron = "0 30 3 ? * MON,TUE,WED,THU,FRI *")
    public void BreakfastStart(){
        DateTimeFormatter dtf =  DateTimeFormatter.ofPattern("yyyy-MM-dd");  
        LocalDateTime now = LocalDateTime.now();  
        List<String> recipients = userService.getOrdersByDateAndOrderType( Date.valueOf(dtf.format(now).toString()) , MealType.Breakfast.ordinal());
        if(recipients.size()>0){
            applicationEventPublisher.publishEvent(new BreakfastStartEvent(recipients));
        } 
    }
    @Scheduled(fixedDelay = 10000)
    //@Scheduled(cron = "0 30 6 ? * MON,TUE,WED,THU,FRI *")
    public void LunchStart(){
        DateTimeFormatter dtf =  DateTimeFormatter.ofPattern("yyyy-MM-dd");  
        LocalDateTime now = LocalDateTime.now();  
        List<String> recipients = userService.getOrdersByDateAndOrderType( Date.valueOf(dtf.format(now).toString()) , MealType.Lunch.ordinal());
        if( recipients.size()>0 ){
            applicationEventPublisher.publishEvent(new LunchStartEvent(recipients));
        } 
    }

    @Scheduled(fixedDelay = 10000)
    // @Scheduled(cron = "0 30 18 ? * MON,TUE,WED,THU,FRI,SAT *" )
    public void autoArchive(){
        DateTimeFormatter dtf =  DateTimeFormatter.ofPattern("yyyy-MM-dd");  
        LocalDateTime now = LocalDateTime.now();
        orderService.autoArchive( (Date.valueOf(dtf.format(now).toString())).toString() );
    }

    @Scheduled(fixedDelay = 10000)
    // @Scheduled(cron = "0 30 18 ? * MON,TUE,WED,THU,FRI,SAT *" )
    public void autoFlush(){
        List<TokenBlacklist> operationList = this.tokenBlacklistRepo.findAll();

        List<TokenBlacklist> expiredList = operationList.stream().filter(t->{
            try{
                return(this.jwtUtils.isTokenExpired(t.getToken()));
        } catch(Exception e){ 
                return(true);} }
             ).collect(Collectors.toList());

        this.tokenBlacklistRepo.deleteAll(expiredList);
    }
    
}
