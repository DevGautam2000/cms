package com.nrifintech.cms.listeners;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.nrifintech.cms.dtos.EmailModel;
import com.nrifintech.cms.events.BreakfastStartEvent;
import com.nrifintech.cms.events.LunchStartEvent;
import com.nrifintech.cms.events.PromotionalEvent;
import com.nrifintech.cms.services.SMTPservices;
import com.nrifintech.cms.services.UserService;

@Component
public class CRONListeners {

    @Autowired
    UserService userService;

    @Autowired
    SMTPservices smtpServices;

    @EventListener
    @Async
    public void onPromotionalEvent(PromotionalEvent event){
        List<String> recipients = (List<String>) event.getSource();
        HashMap<String,String> body = new HashMap<>();
        body.put("content", "Have you ordered for tomorrow");
        body.put("timestamp", LocalTime.now(ZoneId.of("GMT+05:30")).truncatedTo(ChronoUnit.MINUTES).toString());
        EmailModel emailModel = new EmailModel(recipients,"Canteen Management System",body,"promotion.flth");
        this.smtpServices.sendMail(emailModel);
    }

    @EventListener
    @Async
    public void onBreakfastStart(BreakfastStartEvent event){
        List<String> recipients = (List<String>) event.getSource();
        System.out.println(recipients.size());
        HashMap<String,String> body = new HashMap<>();
        body.put("content", "Breakfast");
        body.put("timestamp", LocalTime.now(ZoneId.of("GMT+05:30")).truncatedTo(ChronoUnit.MINUTES).toString());
        EmailModel emailModel = new EmailModel(recipients,"Canteen Management System",body,"service-start.flth");
        this.smtpServices.sendMail(emailModel);
    }

    @EventListener
    @Async
    public void onLunchStart(LunchStartEvent event){
        List<String> recipients = (List<String>) event.getSource();
        System.out.println(recipients.size());
        HashMap<String,String> body = new HashMap<>();
        body.put("content", "Lunch");
        body.put("timestamp", LocalTime.now(ZoneId.of("GMT+05:30")).truncatedTo(ChronoUnit.MINUTES).toString());
        EmailModel emailModel = new EmailModel(recipients,"Canteen Management System",body,"service-start.flth");
        this.smtpServices.sendMail(emailModel);
    }

}
