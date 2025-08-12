package com.example.demo.helper;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.example.demo.exception.EmailException;
import com.example.demo.logger.AppLogger;

import org.springframework.beans.factory.annotation.Autowired;

@Component
public class MailService {

	@Autowired
	private JavaMailSender mailSender;

	public void sendVerificationEmail(String toEmail) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(toEmail);
			message.setSubject("Click On Link For Email Verification");
			String content = "http://localhost:8082/auth/verify?email=" + toEmail;
			message.setText(content);
			mailSender.send(message);
			System.out.println("Email sent successfully to: " + toEmail);

		} catch (MailException ex) {
			// Handle errors related to sending mail
			System.err.println("Failed to send email to: " + toEmail);
			System.out.println(ex.getMessage());

			// Optional: Throw custom exception or return error response
			throw new EmailException("Failed to send email to " + toEmail, ex);

		}
	}
	public void sendEmail(String emailAddress,String sub,String body) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(emailAddress);
			message.setSubject(sub);
			message.setText(body);
			mailSender.send(message);
			System.out.println("Email sent successfully to: " + emailAddress);
			AppLogger.log(AppLogger.Level.INFO, "Email sent successfully to: " + emailAddress);
		} catch (MailException e) {
			AppLogger.log(AppLogger.Level.INFO, e.getMessage() );
		}
		
	}
}
