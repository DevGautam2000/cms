package com.nrifintech.cms.events;

import java.util.List;

import org.springframework.context.ApplicationEvent;

/**
 * A custom event that is published when breakfast starts.
 */
public class BreakfastStartEvent extends ApplicationEvent {

    public BreakfastStartEvent(List<String> recipients) {
        super(recipients);
    }
    
}
