package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="dailylog")
public class DailyLog {

	 @Id
	    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dailylog_seq")
	    @SequenceGenerator(name = "dailylog_seq", sequenceName = "dailylog_seq", allocationSize = 1)
    private Long id;

    private String activity;

    @Column(columnDefinition = "CLOB")
    private String description;

    private LocalDate logdate = LocalDate.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id")
    @JsonIgnore
    private Worker worker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectid", referencedColumnName = "projectid")
    private Project project;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return logdate;
    }

    public void setDate(LocalDate logdate) {
        this.logdate = logdate;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
