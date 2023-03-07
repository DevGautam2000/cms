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
        EmailModel email = (EmailModel) event.getSource();
        this.smtpServices.forgotPasswordMail(email);
    }

    @EventListener
    @Async
    public void onAddedNewUser(AddedNewUserEvent event){
        User user = (User) event.getSource();
        EmailModel email = new EmailModel(user.getEmail(),"Welcome to CMS","pass-create-link",LocalTime.now(ZoneId.of("GMT+05:30")).truncatedTo(ChronoUnit.MINUTES).toString());
        this.smtpServices.welcomeMail(email);
    }

    @EventListener
    @Async
    public void onUpdateUserStatus(UpdateUserStatusEvent event){
        User user = (User) event.getSource();
        EmailModel email = new EmailModel(user.getEmail(),"Change of account status",user.getPassword(),LocalTime.now(ZoneId.of("GMT+05:30")).truncatedTo(ChronoUnit.MINUTES).toString());
        if(user.getStatus() == UserStatus.Active){
            this.smtpServices.statusMail(email,"active-user.flth");
        }
        this.smtpServices.statusMail(email,"inactive-user.flth");
    }
}
