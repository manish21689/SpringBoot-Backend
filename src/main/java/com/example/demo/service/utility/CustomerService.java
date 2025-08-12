package com.example.demo.service.utility;

import org.springframework.stereotype.Service;

import com.example.demo.apputil.AppUtils;

@Service
public class CustomerService {

    private final AppUtils appUtils;

    public CustomerService(AppUtils appUtils) {
        this.appUtils = appUtils;
    }

    public void saveCustomer(String number) {
        String formatted = appUtils.formatMobile(number);
        System.out.println("Formatted Number: " + formatted);
    }
}

