package com.example.demo.repository;




import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Worker;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Worker, Long> {
    Optional<Worker> findByEmail(String email);
    boolean existsByEmail(String email);
}

