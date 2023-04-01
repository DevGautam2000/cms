package com.nrifintech.cms.events;

import java.util.Map;

import org.springframework.context.ApplicationEvent;

public class ForgotPasswordEvent extends ApplicationEvent {
    public ForgotPasswordEvent(Map<String,String> info){
        super(info);
    }
    
}
