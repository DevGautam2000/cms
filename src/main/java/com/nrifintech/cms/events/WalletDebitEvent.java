package com.nrifintech.cms.events;

import org.springframework.context.ApplicationEvent;

import com.nrifintech.cms.dtos.WalletEmailResponse;

public class WalletDebitEvent extends ApplicationEvent {

    public WalletDebitEvent(WalletEmailResponse response) {
        super(response);
    }
    
}
