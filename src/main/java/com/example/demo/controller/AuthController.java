package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LoginRequestDTO;
import com.example.demo.dto.RegisterRequestDTO;
import com.example.demo.entity.Worker;
import com.example.demo.logger.AppLogger;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.auth.AuthService;



@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	private AuthService authService;
	@Autowired
	private UserRepository userRepo;

	@Transactional
	@PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> register(@RequestBody RegisterRequestDTO req) {
		AppLogger.log(AppLogger.Level.INFO,
				"Registration Request: " + req.getEmail() + " " + req.getName() + " " + req.getRole());
		String result = authService.register(req); 
		AppLogger.log(AppLogger.Level.INFO, "Registration Response: " + result);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDTO req) {
		AppLogger.log(AppLogger.Level.INFO,
				"Login Request: " + req.getEmail() + " " + req.getEmail() + " " + req.getPassword());
		String result=authService.login(req);
		AppLogger.log(AppLogger.Level.INFO, "Login Response: " + result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/verify")
	public ResponseEntity<String> verifyWorker(@RequestParam String email) {
		Worker worker = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Invalid email"));
		worker.setStatus("VERIFIED");
		userRepo.save(worker);
		return ResponseEntity.ok("Email verified successfully.");
	}
}
