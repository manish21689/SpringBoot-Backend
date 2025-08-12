package com.example.demo.response;

import java.time.LocalDateTime;

//com.example.demo.dto
public class ErrorResponse {
	private LocalDateTime timestamp;
	private String message;
	private String path;
	private int status;

	public ErrorResponse(LocalDateTime timestamp, String message, String path, int status) {
		this.timestamp = timestamp;
		this.message = message;
		this.path = path;
		this.status = status;
	}

	public ErrorResponse() {
	}

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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
