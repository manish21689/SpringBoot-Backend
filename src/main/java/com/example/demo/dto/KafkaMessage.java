package com.example.demo.dto;

public class KafkaMessage {
    private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public KafkaMessage() {

	}

	public KafkaMessage(String message) {
		this.message = message;
	}


}
