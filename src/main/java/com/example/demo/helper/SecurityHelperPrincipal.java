package com.example.demo.helper;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.demo.dto.LoggedInUserDTO;

@Component
public class SecurityHelperPrincipal {

	// This method fetches LoggedInUser from Spring Security context
	public LoggedInUserDTO getLoggedInUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof LoggedInUserDTO) {

			LoggedInUserDTO user = (LoggedInUserDTO) authentication.getPrincipal();
			Long id = user.getId();
			String email = user.getEmail();
			String role = user.getRole();
			Long managerId = user.getManagerid();
//			Map<String, Object> profile = new HashMap<>();
//	        profile.put("id", id);
//	        profile.put("email", email);
//	        profile.put("role", role);
//	        profile.put("managerId", managerId);
            System.out.println("Security Helper" +email +" "+role);
			return (LoggedInUserDTO) authentication.getPrincipal();
		}
		throw new RuntimeException("User not logged in");
	}

	public boolean isAdmin(LoggedInUserDTO user) {
		return "admin".equalsIgnoreCase(user.getRole());
	}

	public boolean isSameUser(Long idToCheck, LoggedInUserDTO user) {
		return idToCheck != null && idToCheck.equals(user.getId());
	}

	public boolean isReportingManager(Long managerIdFromRecord, LoggedInUserDTO user) {
		return managerIdFromRecord != null && managerIdFromRecord.equals(user.getManagerid());
	}

	public boolean canDelete(Long idToBeDeleted, LoggedInUserDTO user) {
		if (isSameUser(idToBeDeleted, user)) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean canUpdate(Long idToBeDeleted, LoggedInUserDTO user) {
		if (isSameUser(idToBeDeleted, user)) {
			return true;
		} else {
			return false;
		}
	}
	public boolean canAccessUser(Long targetId, LoggedInUserDTO user) {
	    return isAdmin(user) || isSameUser(targetId, user) || isReportingManager(targetId, user);
	}
	
	public boolean canDeleteForBoth(Long idToBeDeleted, LoggedInUserDTO user) {
		return isSameUser(idToBeDeleted, user) || isAdmin(user);
	}
	
	public boolean canUpdateBoth(Long idToBeDeleted, LoggedInUserDTO user) {
		return isSameUser(idToBeDeleted, user) || isAdmin(user);
	}
	
//	public Long getLoggedInUserId() {
//	    return getLoggedInUser().getId();
//	}
}


//If you want to keep CustomUserDetails for some reason:
//if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
//    Worker worker = ((CustomUserDetails) authentication.getPrincipal()).getWorker();
//    return new LoggedInUserDTO(
//        worker.getId(),
//        worker.getEmail(),
//        worker.getRole(),
//        worker.getPassword(),
//        worker.getManager().getId()
//    );
//}
//But then you lose the benefit of directly accessing LoggedInUserDTO.


