package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfig {

	@Bean
	 JavaMailSender javaMailSender() {
		return new JavaMailSenderImpl();
	}
}

//@Configuration
//public class UtilityConfig {
//
//    @Value("${mail.sender}")
//    private String sender;
//
//    @Bean
//    public MailUtil mailUtil() {
//        return new MailUtil(sender);
//    }
//}