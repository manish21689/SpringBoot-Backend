package com.example.demo.dto;

import java.time.LocalDateTime;

public class ErrorResponse {
    private LocalDateTime timestamp;
    private String message;
    private String module;
    private int httpStatus;
    private int lineNumber;
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public int getHttpStatus() {
		return httpStatus;
	}
	public void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
    public ErrorResponse() {}
	public ErrorResponse(LocalDateTime timestamp, String message, String module, int httpStatus, int lineNumber) {
		super();
		this.timestamp = timestamp;
		this.message = message;
		this.module = module;
		this.httpStatus = httpStatus;
		this.lineNumber = lineNumber;
	}
    
    
    
}
