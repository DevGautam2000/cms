package com.nrifintech.cms.events;

import org.springframework.context.ApplicationEvent;

import com.nrifintech.cms.dtos.InventoryMail;

/**
 * This class is an event that is published by the InventoryService class when it receives a request to
 * update the quantity of an item in the inventory
 */
public class UpdateQtyReqEvent extends ApplicationEvent{

    public UpdateQtyReqEvent(InventoryMail inventoryMail) {
        super(inventoryMail);
    }
    
}
