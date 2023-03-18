package com.itexc.dom.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(0)
public class MyApplicationListener implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private Setup setup;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        setup.init();
    }
}
