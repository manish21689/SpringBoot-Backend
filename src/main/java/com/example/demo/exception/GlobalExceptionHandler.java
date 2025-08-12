package com.example.demo.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.demo.logger.AppLogger;
import com.example.demo.response.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
		ErrorResponse err = new ErrorResponse(LocalDateTime.now(), ex.getMessage(), req.getRequestURI(),
				HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex, HttpServletRequest req) {
		return new ResponseEntity<>(new ErrorResponse(LocalDateTime.now(), ex.getMessage(), req.getRequestURI(),
				HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(JsonErrorException.class)
    public ResponseEntity<?> handleJson(JsonErrorException ex,HttpServletRequest req) {
        AppLogger.log(AppLogger.Level.ERROR, "JsonProcessingException: " + ex.getMessage());
        ErrorResponse error = new ErrorResponse(LocalDateTime.now(), 
        		ex.getMessage(), req.getRequestURI(),
				HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
	
	@ExceptionHandler(EmailException.class)
	public ResponseEntity<ErrorResponse> handleEmailError(EmailException ex, HttpServletRequest request) {
	    ErrorResponse error = new ErrorResponse(LocalDateTime.now(), ex.getMessage(), request.getRequestURI(), 500);
	    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest req) {
		return new ResponseEntity<>(new ErrorResponse(LocalDateTime.now(), ex.getMessage(), req.getRequestURI(), 500),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
}

//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex) {
//        Map<String, Object> error = new HashMap<>();
//        error.put("timestamp", LocalDateTime.now());
//        error.put("status", HttpStatus.NOT_FOUND.value());
//        error.put("error", "Not Found");
//        error.put("message", ex.getMessage());
//        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(BadCredentialsException.class)
//    public ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialsException ex) {
//        Map<String, Object> error = new HashMap<>();
//        error.put("timestamp", LocalDateTime.now());
//        error.put("status", HttpStatus.UNAUTHORIZED.value());
//        error.put("error", "Unauthorized");
//        error.put("message", ex.getMessage());
//        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
//        Map<String, Object> error = new HashMap<>();
//        error.put("timestamp", LocalDateTime.now());
//        error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
//        error.put("error", "Internal Server Error");
//        error.put("message", ex.getMessage());
//        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//    
////    @ExceptionHandler(Exception.class)
////    public ResponseEntity<ErrorResponse> handle(Exception ex) {
////        ErrorResponse err = new ErrorResponse(LocalDateTime.now(), ex.getMessage(), "GLOBAL", 500);
////        return ResponseEntity.status(500).body(err);
////    }
//}
//
