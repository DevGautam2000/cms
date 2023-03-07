package com.nrifintech.cms.events;

import org.springframework.context.ApplicationEvent;

import com.nrifintech.cms.dtos.EmailModel;

public class ForgotPasswordEvent extends ApplicationEvent {
    public ForgotPasswordEvent(EmailModel source){
        super(source);
    }
    
}
