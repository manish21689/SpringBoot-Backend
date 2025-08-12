package com.example.demo.controller;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.config.Constants;
import com.example.demo.dto.DailyLogDTO;
import com.example.demo.dto.LoggedInUserDTO;
import com.example.demo.dto.RegisterRequestDTO;
import com.example.demo.entity.DailyLog;
import com.example.demo.entity.Worker;
import com.example.demo.response.SuccessResponse;
import com.example.demo.service.admin.AdminServiceImpl;
import com.example.demo.service.dailylog.DailyLogServiceImpl;
import com.example.demo.service.common.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

	private AdminServiceImpl asi;
	private CommonUserServiceImpl cusi;
	private DailyLogServiceImpl dlsi;

	public AdminController(AdminServiceImpl asi, CommonUserServiceImpl cusi, DailyLogServiceImpl dlsi) {
		this.asi = asi;
		this.cusi = cusi;
		this.dlsi = dlsi;
	}

	//GET http://localhost:8080/worker?page=0&size=5&sortBy=name&direction=desc
	
	@GetMapping("/worker")
	public ResponseEntity<Page<Worker>> getAllWorker(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "asc") String direction) {
		try {
			Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
			Pageable pageable = PageRequest.of(page, size, sort);
			Page<Worker> workerPage = asi.getAllUsers(pageable);
			return new ResponseEntity<>(workerPage, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@DeleteMapping("/worker/{id}")
	public ResponseEntity<String> deleteWorkerById(@PathVariable Long id) {

		try {
			asi.deleteWorker(id);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
		}
		return new ResponseEntity<>("Deleted Successfully", HttpStatus.OK);
	}

	// 1. Get user by ID
	@GetMapping("/worker/{id}")
	public ResponseEntity<SuccessResponse<Worker>> getUserById(@PathVariable Long id) {
	    Worker user = cusi.getUserById(id); // will use cache if available
	    String source = CommonUserServiceImpl.isFromDB() ? "Fetched from DB" : "Fetched from Cache";
	    CommonUserServiceImpl.clearFlag();
	    return ResponseEntity.ok(new SuccessResponse<>(Constants.FETCHED_DONE + " - " + source, user));
	}

	// 2. Get user by email
	@GetMapping("/worker/email")
	public ResponseEntity<SuccessResponse<Worker>> getUserByEmail(@RequestParam String email) {
	    try {
	        Optional<Worker> userOpt = cusi.getUserByEmail(email);
	        if (userOpt.isPresent()) {
	            String source = CommonUserServiceImpl.isFromDB() ? "Fetched from DB" : "Fetched from Cache";
	            return ResponseEntity.ok(new SuccessResponse<>("Success - " + source, userOpt.get()));
	        }
	        return ResponseEntity.notFound().build();
	    } finally {
	    	CommonUserServiceImpl.clearFlag();
	    }
	}

	// 3. Update user
	@PutMapping("/worker/{id}")
	public ResponseEntity<Worker> updateUser(@PathVariable Long id, @RequestBody RegisterRequestDTO request) {
		Worker updated = cusi.updateUser(id, request);
		return ResponseEntity.ok(updated);
	}

	// 4. Get manager's team list
	@GetMapping("/worker/manager/{managerId}/team")
	public ResponseEntity<List<Worker>> getManagerTeam(@PathVariable Long managerId) {
		List<Worker> team = cusi.getManagerTeam(managerId);
		return ResponseEntity.ok(team);
	}

	// 5. Get manager's team count
	@GetMapping("/worker/manager/{managerId}/team/count")
	public ResponseEntity<Long> getTeamCount(@PathVariable Long managerId) {
		Long count = cusi.getCount(managerId);
		return ResponseEntity.ok(count);
	}

	// 6. Create worker
	@PostMapping("/worker")
	public ResponseEntity<String> createWorker(@RequestBody RegisterRequestDTO request) {
		cusi.createWorker(request);
		return ResponseEntity.ok("Worker created successfully.");
	}

	// 7. Get manager by ID (currently returns empty in your service)
	@GetMapping("/worker/manager/{managerId}")
	public ResponseEntity<?> getManagerById(@PathVariable Long managerId) {
		return cusi.getManagerById(managerId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	// 1. Create log
	@PostMapping("/dailylog")
	public DailyLogDTO createLog(@RequestBody DailyLogDTO dto) {
		return dlsi.createLog(dto);
	}

	// 2. Update log by ID
	@PutMapping("/dailylog/{id}")
	public DailyLogDTO updateLog(@PathVariable Long id, @RequestBody DailyLogDTO dto) {
		return dlsi.updateLog(id, dto);
	}

//	// 3. Get log by ID
//	@GetMapping("/dailylog/{id}")
//	public DailyLog getLogById(@PathVariable Long id) {
//		return dlsi.getLogById(id);
//	}

	// 4. Get logs by worker/user ID
	@GetMapping("/daily/user/{workerid}")
	public List<DailyLog> getLogsByUserId(@PathVariable Long workerid) {
		return dlsi.getLogsByUserId(workerid);
	}

	// 5. Get logs for all team members under manager
	@GetMapping("/dailylog/manager/{managerId}")
	public Map<Long, List<DailyLog>> getLogsByManagerId(@PathVariable Long managerId) {
		return dlsi.getLogsByManagerId(managerId);
	}

	// 6. Get logs by date
	@GetMapping("/dailylog/date/{logdate}")
	public List<DailyLog> getLogsByDate(@PathVariable String logdate) {
		return dlsi.getLogsByDate(LocalDate.parse(logdate));
	}

	// 7. Get logs by user and date
	@PostMapping("/dailylog/user-date")

	public ResponseEntity<List<DailyLog>> getLogsByUserAndDate(@RequestParam Long id,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate logdate) {
		try {
			Worker workerOpt = cusi.getUserById(id);

			List<DailyLog> logs = dlsi.getLogsByUserAndDate(workerOpt, logdate);
			return ResponseEntity.ok(logs);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
		}
	}

//	public ResponseEntity<List<DailyLog>> getLogsByUserAndDate(@RequestParam Long id,
//			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate logdate) {
//		try {
//			Optional<Worker> workerOpt = cusi.getUserById(id);
//			if (!workerOpt.isPresent()) {
//				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
//			}
//
//			Worker user = workerOpt.get();
//			List<DailyLog> logs = dlsi.getLogsByUserAndDate(user, logdate);
//			return ResponseEntity.ok(logs);
//
//		} catch (Exception e) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
//		}
//	}

	// 1. Get all logs (admin access)
	@GetMapping("/dailylog")
	public ResponseEntity<List<DailyLog>> getAllLogs() {
		List<DailyLog> logs = dlsi.getAllLogs();
		return ResponseEntity.ok(logs);
	}

	// 2. Get a specific log by ID (admin access)
	@GetMapping("/dailylog/{id}")
	public ResponseEntity<DailyLog> getDailyLogById(@PathVariable Long id) {
		try {
			DailyLog log = dlsi.getAnyLogById(id);
			return ResponseEntity.ok(log);
		} catch (RuntimeException ex) {
			return ResponseEntity.notFound().build();
		}
	}

	// 3. Delete a specific log by ID (admin access)
	@DeleteMapping("/dailylog/{id}")
	public ResponseEntity<Void> deleteLog(@PathVariable Long id) {
		try {
			dlsi.deleteAnyLog(id);
			return ResponseEntity.noContent().build(); // 204 No Content
		} catch (Exception ex) {
			return ResponseEntity.notFound().build();
		}
	}

}
