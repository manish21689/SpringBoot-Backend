package com.example.demo.apputil;

public class AppUtils {

    public String formatMobile(String number) {
        return "+91-" + number;
    }

    public boolean isValidEmail(String email) {
        return email != null && email.contains("@");
    }
}
