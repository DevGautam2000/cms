package com.nrifintech.cms.events;

import java.util.List;

import org.springframework.context.ApplicationEvent;

public class BreakfastStartEvent extends ApplicationEvent {

    public BreakfastStartEvent(List<String> recipients) {
        super(recipients);
    }
    
}
