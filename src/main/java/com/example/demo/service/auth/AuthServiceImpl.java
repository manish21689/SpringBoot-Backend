package com.example.demo.service.auth;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.config.Constants;
import com.example.demo.config.JwtProvider;
import com.example.demo.dto.LoginRequestDTO;
import com.example.demo.dto.RegisterRequestDTO;
import com.example.demo.entity.Project;
import com.example.demo.entity.Worker;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.helper.MailService;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.UserRepository;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ProjectRepository projectRepo;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private JwtProvider jwtProvider;
	@Autowired
	private MailService mailService;

	public String registerWorker(RegisterRequestDTO req) {
	    if (userRepo.findByEmail(req.getEmail()).isPresent()) {
	        throw new RuntimeException("Email already exists");
	    }

	    Worker worker = new Worker();
	    worker.setName(req.getName());
	    worker.setEmail(req.getEmail());
	    worker.setRole(req.getRole());
	    worker.setPassword(encoder.encode(req.getPassword()));
	    worker.setStatus("PENDING");

	    if (req.getManagerId() != null) {
	        Worker manager = userRepo.findById(req.getManagerId())
	            .orElseThrow(() -> new RuntimeException("Manager not found"));
	        worker.setManager(manager);
	    }

	    if (req.getProjectId() != null) {
	        Project project = projectRepo.findById(req.getProjectId())
	            .orElseThrow(() -> new RuntimeException("Project not found"));
	        worker.setProject(project);
	    }

	    if (req.getDocument() != null && !req.getDocument().isEmpty()) {
	        try {
	            worker.setDocument(req.getDocument().getBytes());
	            worker.setDocumentName(req.getDocument().getOriginalFilename());
	            worker.setDocumentType(req.getDocument().getContentType());
	        } catch (IOException e) {
	            throw new RuntimeException("Document processing failed");
	        }
	    }

	    userRepo.save(worker);
  
	    mailService.sendVerificationEmail(req.getEmail());

	    return "User Registered Successfully. Please check your email to verify.";
	} 
	public String login(LoginRequestDTO request) {
		Worker worker = userRepo.findByEmail(request.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException(Constants.USER_ALREADY_REGISTERRED));
		if (!encoder.matches(request.getPassword(), worker.getPassword())) {
			throw new BadCredentialsException(Constants.INVALID_PASSWORD);
		}
		return jwtProvider.generateToken(worker); 
		//String token = Constants.TOKEN_PREFIX + jwtProvider.generateToken(worker);
	}
	@Override
	public String register(RegisterRequestDTO request) {
		// TODO Auto-generated method stub
		return null;
	}
}
