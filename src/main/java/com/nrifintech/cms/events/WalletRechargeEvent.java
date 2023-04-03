package com.nrifintech.cms.events;

import org.springframework.context.ApplicationEvent;

import com.nrifintech.cms.dtos.WalletEmailResponse;

/**
 * This class is used to publish an event when a wallet recharge is successful
 */
public class WalletRechargeEvent extends ApplicationEvent {

    public WalletRechargeEvent(WalletEmailResponse response) {
        super(response);
    }
    
}
