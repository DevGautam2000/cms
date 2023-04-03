package com.nrifintech.cms.events;

import org.springframework.context.ApplicationEvent;

import com.nrifintech.cms.dtos.WalletEmailResponse;

/**
 * This class is an event that is published when a wallet refund is successful
 */
public class WalletRefundEvent extends ApplicationEvent {
    public WalletRefundEvent(WalletEmailResponse response) {
        super(response);
    }
}
