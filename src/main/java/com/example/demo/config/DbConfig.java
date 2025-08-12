package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;




@Component
public class DbConfig {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }
    
    
}

//use in any class after autowired application.properties values fetch kar lega
//@Autowired
//private DbConfig dbConfig;
//
//public void connect() {
//	System.out.println("URL: " + dbConfig.getDbUrl());
//	System.out.println("User: " + dbConfig.getDbUser());
//}
