package com.example.demo.service.common;

import com.example.demo.dto.RegisterRequestDTO;

import com.example.demo.entity.Project;
import com.example.demo.entity.Worker;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.UserRepository;

import com.example.demo.config.JwtProvider;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.Optional;

@Service
public class CommonUserServiceImpl implements CommonUserService {
	private static final ThreadLocal<Boolean> fromDB = ThreadLocal.withInitial(() -> false);
	private final UserRepository userRepository;
	private final ProjectRepository projectRepository;
	private final PasswordEncoder passwordEncoder;
	@SuppressWarnings("unused")
	private final JwtProvider jwtProvider;

	public CommonUserServiceImpl(UserRepository userRepository, ProjectRepository projectRepository,
			PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
		this.userRepository = userRepository;
		this.projectRepository = projectRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtProvider = jwtProvider;

	}

	// ThreadLocal variable to store flag per request
	public static boolean isFromDB() {
		return fromDB.get();
	}

	@Cacheable(value = "id", key = "#id")
	public Worker getUserById(Long id) {
		fromDB.set(true); // Only gets called on cache miss
		System.out.println("Fetching from DB for ID: " + id);
		return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
	}

	@Cacheable(value = "email", key = "#email")
	public Optional<Worker> getUserByEmail(String email) {
	    fromDB.set(true);  // Only sets when DB is accessed
	    return userRepository.findByEmail(email);
	}

	public static void clearFlag() {
	    fromDB.remove();
	}
	
	@Override
	public Worker updateUser(Long id, RegisterRequestDTO req) {
		Worker worker = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

		worker.setName(req.getName());
		worker.setEmail(req.getEmail());
		worker.setRole(req.getRole());

		if (req.getProjectId() != null) {
			Project project = projectRepository.findById(req.getProjectId())
					.orElseThrow(() -> new ResourceNotFoundException("Project not found"));
			worker.setProject(project);
		}

		if (req.getManagerId() != null) {
			Worker manager = userRepository.findById(req.getManagerId())
					.orElseThrow(() -> new ResourceNotFoundException("Manager not found"));
			worker.setManager(manager);
		}

		if (req.getPassword() != null && !req.getPassword().isEmpty()) {
			worker.setPassword(passwordEncoder.encode(req.getPassword()));
		}

		return userRepository.save(worker);
	}

	public List<Worker> getManagerTeam(Long managerId) {

		return userRepository.findByManagerId(managerId);

		// Other Way
//    	return userRepository.findAll()
//                .stream()
//                .filter(worker -> worker.getManager() != null && managerId.equals(worker.getManager().getId()))
//                .collect(Collectors.toList());
	}

	public Long getCount(Long managerId) {
		return userRepository.findAll().stream()
				.filter(worker -> worker.getManager() != null && managerId.equals(worker.getManager().getId())).count();
	}

	@Override
	public void createWorker(RegisterRequestDTO req) {
		Worker worker = new Worker();
		worker.setName(req.getName());
		worker.setEmail(req.getEmail());
		worker.setRole(req.getRole());
		worker.setPassword(passwordEncoder.encode(req.getPassword()));

		if (req.getProjectId() != null) {
			Project project = projectRepository.findById(req.getProjectId())
					.orElseThrow(() -> new ResourceNotFoundException("Project not found"));
			worker.setProject(project);
		}

		if (req.getManagerId() != null) {
			Worker manager = userRepository.findById(req.getManagerId())
					.orElseThrow(() -> new ResourceNotFoundException("Manager not found"));
			worker.setManager(manager);
		}

		userRepository.save(worker);
	}

	@Override
	public Optional<Worker> getManagerById(Long managerid) {
		return userRepository.findById(managerid).filter(worker -> "manager".equalsIgnoreCase(worker.getRole()));
	}

}
