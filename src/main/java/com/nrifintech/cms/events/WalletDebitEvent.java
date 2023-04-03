package com.nrifintech.cms.events;

import org.springframework.context.ApplicationEvent;

import com.nrifintech.cms.dtos.WalletEmailResponse;

/**
 * This class is an event that is published when a wallet debit is successful
 */
public class WalletDebitEvent extends ApplicationEvent {

    public WalletDebitEvent(WalletEmailResponse response) {
        super(response);
    }
    
}
