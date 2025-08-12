package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.apputil.AppUtils;

@Configuration
public class UtilityConfig {

    @Bean
    public AppUtils appUtils() {
        return new AppUtils();
    }
}

