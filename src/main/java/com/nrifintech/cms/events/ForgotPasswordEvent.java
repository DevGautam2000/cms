package com.nrifintech.cms.events;

import java.util.HashMap;

import org.springframework.context.ApplicationEvent;

public class ForgotPasswordEvent extends ApplicationEvent {
    public ForgotPasswordEvent(HashMap<String,String> info){
        super(info);
    }
    
}
