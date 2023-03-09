package com.nrifintech.cms.events;

import org.springframework.context.ApplicationEvent;

import com.nrifintech.cms.dtos.PlacedOrderToken;

public class PlacedOrderEvent extends ApplicationEvent {

    public PlacedOrderEvent(PlacedOrderToken placedOrderToken) {
        super(placedOrderToken);
        
    }
    
}
