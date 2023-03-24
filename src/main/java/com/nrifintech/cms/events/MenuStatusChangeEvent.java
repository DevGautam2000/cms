package com.nrifintech.cms.events;

import org.springframework.context.ApplicationEvent;

import com.nrifintech.cms.entities.Menu;

public class MenuStatusChangeEvent extends ApplicationEvent {

    public MenuStatusChangeEvent(Menu menu) {
        super(menu);
    }

}
