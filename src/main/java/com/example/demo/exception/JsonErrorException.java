package com.example.demo.exception;

//@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //yahn bhi use kar sakte hai.
public class JsonErrorException extends RuntimeException {
	private static final long serialVersionUID = 1L;
    public JsonErrorException(String message) {
        super(message);
    }
}
