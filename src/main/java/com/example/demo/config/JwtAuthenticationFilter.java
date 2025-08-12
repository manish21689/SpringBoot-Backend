package com.example.demo.config;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.dto.LoggedInUserDTO;

import io.jsonwebtoken.Claims;

import java.io.IOException;
import java.util.List;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	@Autowired
	private JwtProvider jwtProvider;
	 

	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		     System.out.println("Filter "+request);
		String token = getJwtFromRequest(request);
		System.out.println("Filter#####" +token);
		if (token != null) {
			Claims claims = jwtProvider.validateToken(token);
			System.out.println("filterClain "+claims.getSubject() +" "+claims.get("role", String.class));
			String username = claims.getSubject(); // From JWT
			String role = claims.get("role", String.class);
			Long id = claims.get("id", Long.class); // Add `id` claim during token creation
			Long managerId = claims.get("managerid", Long.class);
			//Reason passing password as " "
			//This part of code runs after the user is already authenticated using the JWT.
			//At this point, we are not verifying the password again.
			//The JWT token was already created after login and password validation.
			LoggedInUserDTO logdetails = new LoggedInUserDTO(id, username, role, "", managerId);
	//UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(logdetails, null,List.of(new SimpleGrantedAuthority(role)));
			
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(logdetails, null,logdetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authToken);
			System.out.println(" JwtFilterAuthorities: " + logdetails.getAuthorities());
			
			System.out.println("Principal: " + authToken.getPrincipal() +" "+authToken.getAuthorities());
//            Generalized pattern
//            UserDetails userDetails = userDetailsService.loadUserByUsername(username); //GET EMAIL 
//            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                    userDetails, null, userDetails.getAuthorities());
//            SecurityContextHolder.getContext().setAuthentication(authToken);
		}
		chain.doFilter(request, response);
	}

	private String getJwtFromRequest(HttpServletRequest request) {
	    String bearerToken = request.getHeader(Constants.HEADER_STRING);
	    System.out.println("token# " + bearerToken);
	    if (bearerToken != null && bearerToken.startsWith(Constants.TOKEN_PREFIX)) { // agar postman main small bearer hai toh yahan bhi s
	        return bearerToken.substring(6).trim(); // Removes "Bearer" and trims any space
	    }
	    return null;
	}
}
