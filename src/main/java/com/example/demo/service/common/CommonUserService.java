package com.example.demo.service.common;

import java.util.List;
import java.util.Optional;

import com.example.demo.dto.RegisterRequestDTO;
import com.example.demo.entity.Worker;

public interface CommonUserService {
	void createWorker(RegisterRequestDTO request);
	Worker getUserById(Long id);
	Optional<Worker> getManagerById(Long managerid);
    Optional<Worker> getUserByEmail(String email);
    List<Worker>  getManagerTeam(Long managerid); 
    Worker updateUser(Long id, RegisterRequestDTO request); 
    Long getCount(Long managerid); 
}
