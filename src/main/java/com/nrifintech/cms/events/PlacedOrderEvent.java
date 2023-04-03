package com.nrifintech.cms.events;

import org.springframework.context.ApplicationEvent;

import com.nrifintech.cms.dtos.OrderToken;

/**
 * The `PlacedOrderEvent` class is a subclass of the `ApplicationEvent` class
 */
public class PlacedOrderEvent extends ApplicationEvent {

    public PlacedOrderEvent(OrderToken placedOrderToken) {
        super(placedOrderToken);
        
    }
    
}
