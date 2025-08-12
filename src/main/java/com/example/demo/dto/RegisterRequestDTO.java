package com.example.demo.dto;



import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Worker;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequestDTO {

	private Long id;
    @NotBlank
    private String name;

    @Email
    private String email;

    @Size(min = 6)
    private String password;

    private String role;

    private Long managerId;

    private Long projectId;

    private MultipartFile document;

    private String documentName;

    private String status;
    private String documentType;

    private String imageDownloadUrl; // For response purpose only

    public RegisterRequestDTO() {
    }

    public RegisterRequestDTO(@NotBlank String name, @Email String email, @Size(min = 6) String password,
                               String role, Long managerId, Long projectId, MultipartFile document,
                               String documentName, String documentType) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.managerId = managerId;
        this.projectId = projectId;
        this.document = document;
        this.documentName = documentName;
        this.documentType = documentType;
    }

    public RegisterRequestDTO(Worker user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.managerId = user.getManager().getId();
        this.projectId = user.getProject().getProjectid();
        this.documentName = user.getDocumentName();
        this.documentType = user.getDocumentType();
        this.status=user.getStatus();
        this.id=user.getId();
    }
    
    // --- Getters & Setters ---
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    public Long getManagerId() {
        return managerId;
    }
    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public Long getProjectId() {
        return projectId;
    }
    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public MultipartFile getDocument() {
        return document;
    }
    public void setDocument(MultipartFile document) {
        this.document = document;
    }

    public String getDocumentName() {
        return documentName;
    }
    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentType() {
        return documentType;
    }
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getImageDownloadUrl() {
        return imageDownloadUrl;
    }
    public void setImageDownloadUrl(String imageDownloadUrl) {
        this.imageDownloadUrl = imageDownloadUrl;
    }
}
