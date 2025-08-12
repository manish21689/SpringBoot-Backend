package com.example.demo.controller;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;
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
import com.example.demo.helper.SecurityHelperPrincipal;
import com.example.demo.response.SuccessResponse;
import com.example.demo.service.common.CommonUserServiceImpl;
import com.example.demo.service.dailylog.DailyLogServiceImpl;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

	private CommonUserServiceImpl cusi;
	private DailyLogServiceImpl dlsi;
	private SecurityHelperPrincipal shp;

	public UserController(CommonUserServiceImpl cusi, DailyLogServiceImpl dlsi, SecurityHelperPrincipal shp) {
		this.shp = shp;
		this.cusi = cusi;
		this.dlsi = dlsi;

	}

	@GetMapping("/worker/{id}")
	public ResponseEntity<SuccessResponse<RegisterRequestDTO>> getUserById( @PathVariable Long id, HttpServletRequest request) {

	    LoggedInUserDTO logdto = shp.getLoggedInUser();
	    if (!shp.isSameUser(id, logdto)) {
	        throw new AccessDeniedException(Constants.INVALID_PERMISSION);
	    }

	    Worker user = cusi.getUserById(id);
	    RegisterRequestDTO dto = new RegisterRequestDTO(user);
	    // Generate dynamic base URL
	    String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
	    String imageDownloadUrl = baseUrl + "/worker/image/" + id;
	    dto.setImageDownloadUrl(imageDownloadUrl);
	    //Other Way 
	 // Build image download/display URL
//	    String imageDownloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
//	            .path("/worker/image/")
//	            .path(id.toString())
//	            .toUriString();
//	    dto.setImageDownloadUrl(imageDownloadUrl);
	    
	    return ResponseEntity.ok(new SuccessResponse<>(Constants.FETCHED_DONE, dto));
	}
     
	@GetMapping("/worker/image/{id}")
	public ResponseEntity<byte[]> getImageById(@PathVariable Long id) {
	    Worker worker = cusi.getUserById(id); // Ensure worker exists
	    byte[] imageBytes = worker.getDocument(); // Assuming image is stored as byte[] in DB
	    String docType = worker.getDocumentType(); // like "jpg", "png", "pdf"

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.parseMediaType("image/" + docType));
	    headers.setContentDisposition(ContentDisposition.inline().filename("worker_" + id + "." + docType).build());

	    return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
	}
	
//	// 1. Get user by ID Working good Previous without image
//	@GetMapping("/worker/{id}")
//	public ResponseEntity<SuccessResponse<Worker>> getUserById(@PathVariable Long id) {
//	    LoggedInUserDTO logdto = shp.getLoggedInUser();
//	    if (shp.isSameUser(id, logdto)==false) {
//	        throw new AccessDeniedException(Constants.INVALID_PERMISSION);
//	    }
//	    Worker user = cusi.getUserById(id); // Exception handled by service and caught by global handler
//	    return ResponseEntity.ok(new SuccessResponse<>(Constants.FETCHED_DONE, user));
//	    //constructur of respone entity of two type
//	    //ResponseEntity(T body, HttpStatus status)
//	    //ResponseEntity(HttpStatus status)
//	    //Option 2: Using new ResponseEntity<>(body, status)
//	    //SuccessResponse<Worker> response = new SuccessResponse<>(Constants.FETCHED_DONE, user);
//	    //return new ResponseEntity<>(response, HttpStatus.OK);
//	   
//	}

	
	
//	// 1. Get user by ID Working good 
//	@GetMapping("/worker/{id}")
//	public ResponseEntity<?> getUserById(@PathVariable Long id) {
//		// return
//		// cusi.getUserById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
//		System.out.println("/user/worker/id called");
//		LoggedInUserDTO logdto = shp.getLoggedInUser();
//		if (shp.isSameUser(id, logdto)) {
//			return cusi.getUserById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
//		} else {
//			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to get other user..!");
//		}
//	}

//	@GetMapping("/{id}")
//	public ResponseEntity<SuccessResponse<Worker>> getUserById(@PathVariable Long id) {
//		LoggedInUserDTO logdto = shp.getLoggedInUser();
//  		Optional<Worker> user = null;
//
//        if (id.equals(logdto.getId())==false && logdto.getRole().equals("admin")==false) {
//            throw new AccessDeniedException(Constants.INVALID_PERMISSION);
//        }
//
//		if (shp.isSameUser(id, logdto)) {
//			user = cusi.getUserById(id);
//
//		} else {
//			throw new AccessDeniedException(Constants.INVALID_PERMISSION);
//		}
//		return ResponseEntity.ok(new SuccessResponse<>(Constants.FETCHED_DONE, user.get()));
//	}
	// 2. Get user by email of Any
	@GetMapping("/worker/email")
	public ResponseEntity<Worker> getUserByEmail(@RequestParam String email) {
		LoggedInUserDTO logdto = shp.getLoggedInUser();
		return cusi.getUserByEmail(email).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	// 3. Update only itself
	@Transactional
	@PutMapping("/worker/{id}")
	public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody RegisterRequestDTO request) {
		LoggedInUserDTO logdto = shp.getLoggedInUser();
		if (shp.isSameUser(id, logdto) && shp.canUpdate(id, logdto)) {
			Worker updated = cusi.updateUser(id, request);
			return ResponseEntity.ok(updated);
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to update this user.");
		}
	}

	// 4. Get manager's team list
	@GetMapping("/worker/manager/{managerId}/team")
	public ResponseEntity<?> getManagerTeam(@PathVariable Long managerId) {
		LoggedInUserDTO logdto = shp.getLoggedInUser();
		if (shp.isReportingManager(managerId, logdto)) {
			List<Worker> team = cusi.getManagerTeam(managerId);
			return ResponseEntity.ok(team);
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to update this user.");
		}
	}

	// 5. Get manager's team count
	@GetMapping("/worker/manager/{managerId}/team/count")
	public ResponseEntity<?> getTeamCount(@PathVariable Long managerId) {
		LoggedInUserDTO logdto = shp.getLoggedInUser();
		if (shp.isReportingManager(managerId, logdto)) {
			Long count = cusi.getCount(managerId);
			return ResponseEntity.ok(count);
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to update this user.");
		}
	}

	// 6. Create worker
	@Transactional
	@PostMapping("/worker")
	public ResponseEntity<String> createWorker(@RequestBody RegisterRequestDTO request) {
		cusi.createWorker(request);
		return ResponseEntity.ok("Worker created successfully.");
	}

	// 7. Get his manager details by ID ((currently returns empty in your service)
	@GetMapping("/worker/manager/{managerId}")
	public ResponseEntity<?> getManagerById(@PathVariable Long managerId) {
		// return
		// cusi.getManagerById(managerId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
		LoggedInUserDTO logdto = shp.getLoggedInUser();
		if (shp.isReportingManager(managerId, logdto)) {
			return cusi.getManagerById(managerId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can see details only Yours Manager..");
		}
	}
	// DailyLogService

	// 1. Create log
	@PostMapping("/dailylog")
	public DailyLogDTO createLog(@RequestBody DailyLogDTO dto) {
		return dlsi.createLog(dto);
	}

	// 2. Update log by ID
	@Transactional
	@PutMapping("/dailylog/{id}")
	public DailyLogDTO updateLog(@PathVariable Long id, @RequestBody DailyLogDTO dto) {
		return dlsi.updateLog(id, dto);
	}

	// 3. Get log by ID
	@GetMapping("/dailylog/{id}")
	public DailyLog getLogById(@PathVariable Long id) {
		return dlsi.getLogById(id);
	}

	// 4. Get logs by worker/user ID
	@GetMapping("/dailylog/{userId}")
	public List<DailyLog> getLogsByUserId(@PathVariable Long userId) {
		return dlsi.getLogsByUserId(userId);
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

}
