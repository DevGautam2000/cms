package com.nrifintech.cms.events;

import org.springframework.context.ApplicationEvent;

import com.nrifintech.cms.dtos.InventoryMail;

public class UpdateQtyReqEvent extends ApplicationEvent{

    public UpdateQtyReqEvent(InventoryMail inventoryMail) {
        super(inventoryMail);
    }
    
}
