package com.nrifintech.cms.events;

import org.springframework.context.ApplicationEvent;

import com.nrifintech.cms.dtos.WalletEmailResponse;
import com.stripe.model.Application;

public class WalletRefundEvent extends ApplicationEvent {
    public WalletRefundEvent(WalletEmailResponse response) {
        super(response);
    }
}
