package com.example.demo.config;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.example.demo.entity.Worker;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtProvider {
    private final String SECRET = Constants.SECRET_KEY;
    private final long EXPIRATION = Constants.EXPIRATION_TIME;

    @SuppressWarnings("deprecation")
	public String generateToken(Worker worker) {
    	System.out.println("in jwtprovider " + worker.getRole());
        return Jwts.builder()
        	    .setSubject(worker.getEmail())
        	    .claim("role", "ROLE_"+worker.getRole().toUpperCase())
        	    .claim("id", worker.getId())
        	    .claim("managerid", worker.getManager().getId())
        	    .setIssuedAt(new Date())
        	    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION)) // 1 day
        	    .signWith(SignatureAlgorithm.HS256, SECRET)
        	    .compact();
    }


    @SuppressWarnings("deprecation")
	public Claims validateToken(String token) {
    	System.out.println("validate1 " +token);
    //	System.out.println("validate2" +Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody());
    	
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
    }}