package com.nrifintech.cms.events;

import org.springframework.context.ApplicationEvent;

import com.nrifintech.cms.dtos.OrderToken;

public class CancelledOrderEvent extends ApplicationEvent {

    public CancelledOrderEvent(OrderToken cancelledOrderToken) {
        super(cancelledOrderToken);
    }
    
}
