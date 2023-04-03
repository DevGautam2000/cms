package com.nrifintech.cms.events;

import org.springframework.context.ApplicationEvent;

import com.nrifintech.cms.dtos.OrderToken;

/**
 * A custom event that is published when an order is delivered.
 */
public class DeliveredOrderEvent extends ApplicationEvent {

    public DeliveredOrderEvent(OrderToken deliveredOrderToken) {
        super(deliveredOrderToken);
    }
    
}
