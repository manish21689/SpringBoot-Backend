package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Worker {

	 @Id
	    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "worker_seq")
	    @SequenceGenerator(name = "worker_seq", sequenceName = "worker_seq", allocationSize = 1)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String role;
    
    private String status = "PENDING"; // default status

    @Lob
    private byte[] document; // For PDF/Image

    private String documentName;
    private String documentType;
    


    

	// Self-referencing ManyToOne (manager of this worker)
    @ManyToOne
    @JoinColumn(name = "managerid")
    private Worker manager;  //manager.getManager().getId //worker.getmanager.geId

    // ManyToOne relation with Project
    @ManyToOne
    @JoinColumn(name = "projectid")
    private Project project; //Yahan Class ka refrence variable hota hia 
   
    //=====================
    //@OneTomany worker has many daily logs 
    //mappedBy = "worker" 		This tells JPA that the DailyLog entity contains the @ManyToOne side (i.e., the foreign key is in DailyLog, not
    //@ManyToOne     @JoinColumn(name = "worker_id")    private Worker worker;
    //3. cascade = CascadeType.ALL     If you save or delete a Worker, the related DailyLog entries will be saved or deleted automatically.
    //orphanRemoval = true   If you remove a DailyLog from the dailyLogs list in Java, JPA will delete it from the database.
    //@JsonIgnore     Used to avoid infinite JSON recursion when sending data (like worker -> dailyLog -> worker -> dailyLog -
    //=================================
    //fake column
    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<DailyLog> dailyLogs;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Worker getManager() {
        return manager;
    }

    public void setManager(Worker manager) {
        this.manager = manager;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<DailyLog> getDailyLogs() {
        return dailyLogs;
    }

    public void setDailyLogs(List<DailyLog> dailyLogs) {
        this.dailyLogs = dailyLogs;
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public byte[] getDocument() {
		return document;
	}

	public void setDocument(byte[] document) {
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

	public Worker(String name, String email, String password, String role, String status, byte[] document,
			String documentName, String documentType, Worker manager, Project project, List<DailyLog> dailyLogs
			) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.status = status;
		this.document = document;
		this.documentName = documentName;
		this.documentType = documentType;
		this.manager = manager;
		this.project = project;
		this.dailyLogs = dailyLogs;
		
	}

	public Worker() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
    
}

//Name                                      Null?    Type
//		 ----------------------------------------- -------- ----------------------------
//		 ID                                        NOT NULL NUMBER(19)
//		 EMAIL                                              VARCHAR2(255 CHAR)
//		 NAME                                               VARCHAR2(255 CHAR)
//		 PASSWORD                                           VARCHAR2(255 CHAR)
//		 ROLE                                               VARCHAR2(255 CHAR)
//		 MANAGERID                                          NUMBER(19)
//		 PROJECTID                                          NUMBER(19)
//
//		SQL> describe project;
//		 Name                                      Null?    Type
//		 ----------------------------------------- -------- ----------------------------
//		 PROJECTID                                 NOT NULL NUMBER(19)
//		 DESCRIPTION                                        CLOB
//		 END_DATE                                           DATE
//		 LAUNCH_DATE                                        DATE
//		 PROJECT_NAME                                       VARCHAR2(255 CHAR)
//
//		SQL> describe dailylog;
//		 Name                                      Null?    Type
//		 ----------------------------------------- -------- ----------------------------
//		 ID                                        NOT NULL NUMBER(19)
//		 ACTIVITY                                           VARCHAR2(255 CHAR)
//		 DESCRIPTION                                        CLOB
//		 LOGDATE                                            DATE
//		 PROJECTID                                          NUMBER(19)
//		 WORKER_ID                                          NUMBER(19)
//
