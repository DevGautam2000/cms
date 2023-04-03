package com.nrifintech.cms.events;

import org.springframework.context.ApplicationEvent;

import com.nrifintech.cms.entities.User;

/**
 * The `AddedNewUserEvent` class extends the `ApplicationEvent` class and is used to publish an event
 * when a new user is added.
 */
public class AddedNewUserEvent extends ApplicationEvent {

    public AddedNewUserEvent(User user) {
        super(user);
    }
    
}
