package com.nrifintech.cms.Scheduler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nrifintech.cms.events.BreakfastStartEvent;
import com.nrifintech.cms.repositories.UserRepo;
import com.nrifintech.cms.services.UserService;


@Component
public class Tasks {

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    UserService userService;

    @Scheduled(fixedDelay = 10000)
    public void BreakfastStart(){
        //List<String> recipients = userService.getOrdersByDateAndOrderType("date", "ordertype");
        List<String> recipients = new ArrayList<>();
        recipients.add("sagnik938@gmail.com");
        recipients.add("mayukhbarmanray3@gmail.com");

        applicationEventPublisher.publishEvent(new BreakfastStartEvent(recipients));
        System.out.println("mails sent");

    }
    
}
