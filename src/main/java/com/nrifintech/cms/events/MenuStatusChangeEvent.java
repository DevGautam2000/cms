package com.nrifintech.cms.events;

import org.springframework.context.ApplicationEvent;

import com.nrifintech.cms.entities.Menu;

/**
 * This class is a custom event that is fired when the status of a menu changes
 */
public class MenuStatusChangeEvent extends ApplicationEvent {

    public MenuStatusChangeEvent(Menu menu) {
        super(menu);
    }

}
