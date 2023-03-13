package com.nrifintech.cms.events;

import java.util.List;

import org.springframework.context.ApplicationEvent;

public class PromotionalEvent extends ApplicationEvent{

    public PromotionalEvent(List<String> recipients) {
        super(recipients);
    }
    
}
