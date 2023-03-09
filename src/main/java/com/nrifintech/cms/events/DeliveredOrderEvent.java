package com.nrifintech.cms.events;

import org.springframework.context.ApplicationEvent;

import com.nrifintech.cms.dtos.OrderToken;

public class DeliveredOrderEvent extends ApplicationEvent {

    public DeliveredOrderEvent(OrderToken deliveredOrderToken) {
        super(deliveredOrderToken);
    }
    
}
