package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.*;

public interface DailyLogRepository extends JpaRepository<DailyLog, Long> {

    // Get logs for a specific user
    List<DailyLog> findByWorker(Worker worker);

    // Get logs for a specific user on a specific date
    List<DailyLog> findByWorker_IdAndLogdate(Long wid, LocalDate logdate);

    // Get logs on a specific date
    List<DailyLog> findByLogdate(LocalDate logdate);
    
    List<DailyLog>  findByWorker_IdIn(Set<Long> userIds);
    List<DailyLog>  findByWorker_Id(Long wid);
}
