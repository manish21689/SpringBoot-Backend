package com.example.demo.helper;

import org.springframework.stereotype.Component;

import com.example.demo.dto.LoggedInUserDTO;

import jakarta.servlet.http.HttpSession;

@Component
public class SecurityHelperSession {

    // This method should return LoggedInUser from session or token
    public LoggedInUserDTO getLoggedInUser(HttpSession session) {
        Object obj = session.getAttribute("loggedInUser");
        if (obj instanceof LoggedInUserDTO) {
            return (LoggedInUserDTO) obj;
        }
        throw new RuntimeException("User not logged in");
    }

    public boolean isAdmin(LoggedInUserDTO user) {
        return "admin".equalsIgnoreCase(user.getRole());
    }

    public boolean isSameUser(Long idToCheck, LoggedInUserDTO user) {
        return idToCheck != null && idToCheck.equals(user.getId());
    }

    public boolean isManagerOf(Long managerIdFromRecord, LoggedInUserDTO user) {
        return managerIdFromRecord != null && managerIdFromRecord.equals(user.getId());
    }

    public boolean canDelete(Long idToBeDeleted, Long managerIdOfRecord, LoggedInUserDTO user) {
        if (isAdmin(user)) return true;
        if (isSameUser(idToBeDeleted, user)) return true;
        if (isManagerOf(managerIdOfRecord, user)) return true;
        return false;
    }

}

