package com.nrifintech.cms.listeners;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.nrifintech.cms.dtos.EmailModel;
import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.events.AddedNewUserEvent;
import com.nrifintech.cms.events.ForgotPasswordEvent;
import com.nrifintech.cms.services.SMTPservices;

@Component
public class Listeners {

    @Autowired
    private SMTPservices smtpServices;

    @EventListener
    @Async
    public void onForgotPassword(ForgotPasswordEvent event){
        EmailModel email = (EmailModel) event.getSource();
        this.smtpServices.forgotPasswordMail(email);
    }

    @EventListener
    @Async
    public void onAddedNewUser(AddedNewUserEvent event){
        User user = (User) event.getSource();
        EmailModel email = new EmailModel(user.getEmail(),"Welcome to CMS",user.getPassword(),LocalTime.now(ZoneId.of("GMT+05:30")).truncatedTo(ChronoUnit.MINUTES).toString());
        this.smtpServices.welcomeMail(email);
    }
}
