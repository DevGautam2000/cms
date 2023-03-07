package com.nrifintech.cms.events;

import org.springframework.context.ApplicationEvent;

import com.nrifintech.cms.entities.User;

public class UpdateUserStatusEvent extends ApplicationEvent {

    public UpdateUserStatusEvent(User user) {
        super(user);
    } 
}
