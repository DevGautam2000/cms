package com.nrifintech.cms.events;

import java.util.List;

import org.springframework.context.ApplicationEvent;

public class LunchStartEvent extends ApplicationEvent {

    public LunchStartEvent(List<String> recipients) {
        super(recipients);
    }
    
}
