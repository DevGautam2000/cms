package com.nrifintech.cms.events;

import org.springframework.context.ApplicationEvent;

import com.nrifintech.cms.dtos.InventoryMail;

public class ApprovedQtyReqEvent extends ApplicationEvent {

    public ApprovedQtyReqEvent(InventoryMail inventoryMail) {
        super(inventoryMail);
    }
    
}
