package com.nrifintech.cms.events;

import org.springframework.context.ApplicationEvent;

import com.nrifintech.cms.dtos.OrderToken;

/**
 * A custom event that is published when an order is cancelled.
 */
public class CancelledOrderEvent extends ApplicationEvent {

    public CancelledOrderEvent(OrderToken cancelledOrderToken) {
        super(cancelledOrderToken);
    }
    
}
