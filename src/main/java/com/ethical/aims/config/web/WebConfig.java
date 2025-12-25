package com.ethical.aims.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        System.out.println("Reached to WebConfig addViewControllers() method........");
        registry.addViewController("/{spring:[^\\.]*}")
                .setViewName("forward:/index.html");
        registry.addViewController("/**/{spring:[^\\.]*}")
                .setViewName("forward:/index.html");
    }
}
