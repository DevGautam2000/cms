package com.nrifintech.cms.events;

import java.util.Map;

import org.springframework.context.ApplicationEvent;

/**
 * The ForgotPasswordEvent class extends the ApplicationEvent class and is used to send an email to the
 * user who forgot their password
 */
public class ForgotPasswordEvent extends ApplicationEvent {
    public ForgotPasswordEvent(Map<String,String> info){
        super(info);
    }
    
}
