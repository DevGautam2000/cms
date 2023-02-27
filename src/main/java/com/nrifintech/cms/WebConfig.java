package com.nrifintech.cms;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
 
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/profile/{id}").setViewName("../template2.html");
        registry.addViewController("/dashboard").setViewName("/template2.html");
        registry.addViewController("/dashboard2").setViewName("/template2.html");
    }
 
}
