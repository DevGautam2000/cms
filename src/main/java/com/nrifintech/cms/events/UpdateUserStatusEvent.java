package com.nrifintech.cms.events;

import org.springframework.context.ApplicationEvent;

import com.nrifintech.cms.entities.User;

/**
 * The UpdateUserStatusEvent class is a custom event that extends the ApplicationEvent class
 */
public class UpdateUserStatusEvent extends ApplicationEvent {

    public UpdateUserStatusEvent(User user) {
        super(user);
    } 
}
