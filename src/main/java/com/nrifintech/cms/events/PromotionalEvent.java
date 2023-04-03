package com.nrifintech.cms.events;

import java.util.List;

import org.springframework.context.ApplicationEvent;

/**
 * The PromotionalEvent class extends the ApplicationEvent class and is used to send promotional emails
 * to a list of recipients.
 */
public class PromotionalEvent extends ApplicationEvent{

    public PromotionalEvent(List<String> recipients) {
        super(recipients);
    }
    
}
