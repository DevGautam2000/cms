package com.nrifintech.cms.listeners;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.nrifintech.cms.dtos.EmailModel;
import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.events.AddedNewUserEvent;
import com.nrifintech.cms.events.ForgotPasswordEvent;
import com.nrifintech.cms.events.UpdateUserStatusEvent;
import com.nrifintech.cms.services.SMTPservices;
import com.nrifintech.cms.types.UserStatus;

@Component
public class Listeners {

    @Autowired
    private SMTPservices smtpServices;

    @EventListener
    @Async
    public void onForgotPassword(ForgotPasswordEvent event){
        HashMap<String,String> body = (HashMap<String, String>) event.getSource();
        body.put("timestamp", LocalTime.now(ZoneId.of("GMT+05:30")).truncatedTo(ChronoUnit.MINUTES).toString());
        body.put("link","<pass-forgot-link>");
        List<String> recipients = new ArrayList<>();
        recipients.add(body.get("username"));
        EmailModel email = new EmailModel(recipients , "Forgot-password-request" , body , "forgot-password.flth");
        this.smtpServices.sendMail(email);
    }

    @EventListener
    @Async
    public void onAddedNewUser(AddedNewUserEvent event){
        User user = (User) event.getSource();

        List<String> recipients = new ArrayList<>();
        recipients.add(user.getEmail());
        HashMap<String,String> body = new HashMap<>();
        body.put("link", "<pass-reset-link>");
        body.put("username",user.getEmail());
        body.put("timestamp",LocalTime.now(ZoneId.of("GMT+05:30")).truncatedTo(ChronoUnit.MINUTES).toString());

        EmailModel email = new EmailModel(recipients,"Welcome to Canteen Management System NRI Fintech India Pvt.Ltd." , body,"welcome-email.flth");
        this.smtpServices.sendMail(email);
    }

    @EventListener
    @Async
    public void onUpdateUserStatus(UpdateUserStatusEvent event){
        User user = (User) event.getSource();
        String template = "inactive-user.flth";
        if(user.getStatus() == UserStatus.Active){
            template = "active-user.flth";
        }
        List<String> recipients = new ArrayList<>();
        recipients.add(user.getEmail());
        HashMap<String,String> body = new HashMap<>();
        body.put("status", user.getStatus().toString());
        body.put("username",user.getEmail());
        body.put("timestamp",LocalTime.now(ZoneId.of("GMT+05:30")).truncatedTo(ChronoUnit.MINUTES).toString());

        EmailModel email = new EmailModel(recipients,"Welcome to Canteen Management System NRI Fintech India Pvt.Ltd." , body,template);
        this.smtpServices.sendMail(email); 
    }
}
