package com.nrifintech.cms.listeners;

import java.io.IOException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;

import javax.mail.MessagingException;

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

import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

@Component
public class CRONListeners {

    @Autowired
    UserService userService;

    @Autowired
    SMTPservices smtpServices;

    private String timezone = "GMT+05:30";
    private String subject = "Canteen Management System NRI Fintech India Pvt.Ltd.";

    @EventListener
    @Async
    public void onPromotionalEvent(PromotionalEvent event) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, MessagingException, IOException, TemplateException{
        List<String> recipients = (List<String>) event.getSource();
        HashMap<String,String> body = new HashMap<>();
        body.put("content", "Have you ordered for tomorrow");
        body.put("timestamp", LocalTime.now(ZoneId.of(timezone)).truncatedTo(ChronoUnit.MINUTES).toString());
        EmailModel emailModel = new EmailModel(recipients,subject,body,"promotion.flth");
        this.smtpServices.sendMail(emailModel);
    }

    @EventListener
    @Async
    public void onBreakfastStart(BreakfastStartEvent event) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, MessagingException, IOException, TemplateException{
        List<String> recipients = (List<String>) event.getSource();
        HashMap<String,String> body = new HashMap<>();
        body.put("content", "Breakfast");
        body.put("timestamp", LocalTime.now(ZoneId.of(timezone)).truncatedTo(ChronoUnit.MINUTES).toString());
        EmailModel emailModel = new EmailModel(recipients,subject,body,"service-start.flth");
        this.smtpServices.sendMail(emailModel);
    }

    @EventListener
    @Async
    public void onLunchStart(LunchStartEvent event) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, MessagingException, IOException, TemplateException{
        List<String> recipients = (List<String>) event.getSource();
        HashMap<String,String> body = new HashMap<>();
        body.put("content", "Lunch");
        body.put("timestamp", LocalTime.now(ZoneId.of(timezone)).truncatedTo(ChronoUnit.MINUTES).toString());
        EmailModel emailModel = new EmailModel(recipients,subject,body,"service-start.flth");
        this.smtpServices.sendMail(emailModel);
    }

}
