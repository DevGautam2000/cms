package com.nrifintech.cms.events;

import org.springframework.context.ApplicationEvent;

import com.nrifintech.cms.dtos.InventoryMail;

/**
 * This class is a custom event that is fired when the approved quantity is requested
 */
public class ApprovedQtyReqEvent extends ApplicationEvent {

    public ApprovedQtyReqEvent(InventoryMail inventoryMail) {
        super(inventoryMail);
    }
    
}
