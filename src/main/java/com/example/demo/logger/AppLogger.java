package com.example.demo.logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.example.demo.exception.JsonErrorException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AppLogger {

	private static final String LOG_DIR = "c:/temp/";

	public enum Level {
		INFO, ERROR, DEBUG, WARN
	}

	public static void log(Level level, String message) {
		String date = LocalDate.now().toString(); // e.g. 2025-07-22
		String fileName = LOG_DIR + "app-log-" + date + ".txt";
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));

		// Automatically get caller info
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		// Index 0: getStackTrace, 1: log(), 2: your actual caller
		StackTraceElement caller = stackTrace.length > 2 ? stackTrace[3] : null;

		String className = (caller != null) ? caller.getClassName() : "UnknownClass";
		String methodName = (caller != null) ? caller.getMethodName() : "UnknownMethod";
		int lineNumber = (caller != null) ? caller.getLineNumber() : -1;

		String msg = StringOrJson(message);

		// String logLine = String.format("%s [%s] %s.%s(%d) - %s",timestamp, level,
		// className, methodName, lineNumber, message);
		String logLine = timestamp + " [" + level + "] " + className 
				                                         + "." + methodName 
				                                         + "(" + lineNumber + ") - "
				                                         + msg;

		try (PrintWriter out = new PrintWriter(new FileWriter(fileName, true))) {
			out.println(logLine);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public static String StringOrJson(Object message) {
		String msg;
		if (message instanceof String) {
			msg = (String) message;
		} else {
			try {
				ObjectMapper mapper = new ObjectMapper();
				msg = mapper.writeValueAsString(message);
			} catch (com.fasterxml.jackson.core.JsonProcessingException e) {
	            throw new JsonErrorException("JSON conversion failed: " + e.getMessage()); // ✅ rethrow
	        }
		}
		return msg;
	}
	//method overloading...
	public static void log(Level level, Object message) {
	    log(level, StringOrJson(message));
	    // call anywhere like this AppLogger.log(Level.INFO, myObject/Worker);
	}

}

//usage start from main class
//AppLogger.log(AppLogger.Level.INFO, "Application started");
//AppLogger.log(AppLogger.Level.ERROR, "Something went wrong");

//2025-07-22 08:45:12.123 [INFO] TestApp.main(5) - Application started
//2025-07-22 08:45:13.456 [ERROR] TestApp.main(6) - Something went wrong
