
package com.example.demo.service.dailylog;

import com.example.demo.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.DailyLogDTO;
import com.example.demo.entity.DailyLog;
import com.example.demo.entity.Worker;
import com.example.demo.repository.DailyLogRepository;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DailyLogServiceImpl implements DailyLogService {

	private final DailyLogRepository dailyLogRepository;
	private final UserRepository userRepo;
	private final ProjectRepository projectRepo;

	
	public DailyLogServiceImpl(DailyLogRepository dailyLogRepository, UserRepository userRepo,
			ProjectRepository projectRepo) {
		this.dailyLogRepository = dailyLogRepository;
		this.userRepo = userRepo;
		this.projectRepo = projectRepo;
	}

	// COMMON METHODS
	@Override
	public DailyLogDTO createLog(DailyLogDTO dto) {
		DailyLog log = new DailyLog();

		log.setActivity(dto.getActivity());
		log.setDescription(dto.getDescription());
		log.setDate(dto.getDate());

		// Set worker by ID
		Worker worker = userRepo.findById(dto.getWorkerId())
				.orElseThrow(() -> new RuntimeException("Worker not found with ID: " + dto.getWorkerId()));
		log.setWorker(worker);

		// Set project by ID
		Project project = projectRepo.findById(dto.getProjectId())
				.orElseThrow(() -> new RuntimeException("Project not found with ID: " + dto.getProjectId()));
		log.setProject(project);

		DailyLog saved = dailyLogRepository.save(log);

		// Convert saved entity to DTO for response
		DailyLogDTO response = new DailyLogDTO();
		response.setId(saved.getId());
		response.setActivity(saved.getActivity());
		response.setDescription(saved.getDescription());
		response.setDate(saved.getDate());
		response.setWorkerId(saved.getWorker().getId());
		response.setProjectId(saved.getProject().getProjectid());
		return response;

	}

	@Override
	public DailyLogDTO updateLog(Long id, DailyLogDTO updatedDto) {
		return dailyLogRepository.findById(id).map(existing -> {
			existing.setDate(updatedDto.getDate());
			existing.setActivity(updatedDto.getActivity());
			existing.setDescription(updatedDto.getDescription());

			// Optionally update worker or project if needed
			// Worker and Project should be fetched and set only if allowed

			DailyLog saved = dailyLogRepository.save(existing);

			// Convert back to DTO
			DailyLogDTO responseDto = new DailyLogDTO();
			responseDto.setId(saved.getId());
			responseDto.setActivity(saved.getActivity());
			responseDto.setDescription(saved.getDescription());
			responseDto.setDate(saved.getDate());
			responseDto.setWorkerId(saved.getWorker().getId());
			responseDto.setProjectId(saved.getProject().getProjectid());

			return responseDto;
		}).orElseThrow(() -> new RuntimeException("Log not found"));
	}

	@Override
	public DailyLog getLogById(Long id) {
		return dailyLogRepository.findById(id).orElseThrow(() -> new RuntimeException("Log not found"));
	}

	@Override
	public List<DailyLog> getLogsByUserId(Long userid) {
		return dailyLogRepository.findByWorker_Id(userid);
	}

	@Override
	public Map<Long, List<DailyLog>> getLogsByManagerId(Long managerId) {
		List<Worker> teamMembers = userRepo.findByManagerId(managerId);
		Set<Long> memberIds = teamMembers.stream().map(Worker::getId).collect(Collectors.toSet());
		List<DailyLog> logs = dailyLogRepository.findByWorker_IdIn(memberIds);
		return logs.stream().collect(Collectors.groupingBy(log -> (Long) log.getWorker().getId()));
	}

	@Override
	public List<DailyLog> getLogsByDate(LocalDate logdate) {
		return dailyLogRepository.findByLogdate(logdate);
	}

	@Override
	public List<DailyLog> getLogsByUserAndDate(Worker worker, LocalDate logdate) {
		if (worker.getId() != null) {
			return dailyLogRepository.findByWorker_IdAndLogdate(worker.getId(), logdate);
		} else {
			return dailyLogRepository.findByLogdate(logdate);
		}
	}

	// USER METHODS
	@Override
	public void deleteLog(Long id) {
		dailyLogRepository.deleteById(id);
	}

	@Override
	public List<DailyLog> getLogs() {
		return dailyLogRepository.findAll(); // Will filter at controller based on user
	}

	// ADMIN METHODS
	@Override
	public List<DailyLog> getAllLogs() {
		return dailyLogRepository.findAll(); // Admin gets everything
	}

	@Override
	public void deleteAnyLog(Long id) {
		dailyLogRepository.deleteById(id);
	}

	@Override
	public DailyLog getAnyLogById(Long id) {
		return dailyLogRepository.findById(id).orElseThrow(() -> new RuntimeException("Log not found"));
	}
	
	

}



