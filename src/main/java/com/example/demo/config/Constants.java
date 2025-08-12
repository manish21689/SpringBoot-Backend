package com.example.demo.config;

public class Constants {

	// JWT related constants
	public static final String SECRET_KEY = "mySuperSecretKey12345sdfsdkfsjdfksjdflskjfsd2145"; 
	public static final long EXPIRATION_TIME = 86400000; // 1 day in milliseconds
	public static String TOKEN_PREFIX = "Bearer";
	public static String HEADER_STRING = "Authorization";

	// Role constants
	public static final String ROLE_ADMIN = "ADMIN";
	public static final String ROLE_USER = "USER";

	// Custom error messages
	public static final String RESOURCE_NOT_FOUND = "required resource not found";
	public static final String INVALID_PASSWORD = "Invalid credential";
	public static final String USER_ALREADY_REGISTERRED = "User already exist with same email";
	public static final String INVALID_PERMISSION = "only authorized to perform action urself";
	public static final String INVALID_FETCH = "You cannot see details of other manager and its see team members";

	// Custome Success message
	public static final String CREATION_DONE = "created successfully";
	public static final String DELETION_DONE = "deleted successfully";
	public static final String UPDATION_DONE = "updated successfully";
	public static final String FETCHED_DONE = "fetched successfully";
	public static final String TOKEN_GEN_DONE ="loggedin & token generated successfully";

	
	private Constants() {
	}
}
