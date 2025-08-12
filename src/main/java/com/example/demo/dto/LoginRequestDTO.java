package com.example.demo.dto;

import jakarta.validation.constraints.*;

public class LoginRequestDTO {
    @Email private String email;
    @NotBlank private String password;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public LoginRequestDTO() {
	
	}
	public LoginRequestDTO(@Email String email, @NotBlank String password) {
		
		this.email = email;
		this.password = password;
	}
	
    
    
}
