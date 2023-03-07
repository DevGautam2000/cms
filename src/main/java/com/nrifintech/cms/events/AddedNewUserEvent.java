package com.nrifintech.cms.events;

import org.springframework.context.ApplicationEvent;

import com.nrifintech.cms.entities.User;

public class AddedNewUserEvent extends ApplicationEvent {

    public AddedNewUserEvent(User user) {
        super(user);
    }
    
}
