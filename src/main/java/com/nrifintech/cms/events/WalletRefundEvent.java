package com.nrifintech.cms.events;

import org.springframework.context.ApplicationEvent;

import com.nrifintech.cms.dtos.WalletEmailResponse;

public class WalletRefundEvent extends ApplicationEvent {
    public WalletRefundEvent(WalletEmailResponse response) {
        super(response);
    }
}
