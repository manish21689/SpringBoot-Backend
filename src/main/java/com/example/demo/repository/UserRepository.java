package com.example.demo.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.entity.Worker;

@Repository
public interface UserRepository extends JpaRepository<Worker, Long> {
	Optional<Worker> findByEmail(String email);
	List<Worker> findByManagerId(Long managerId);
}
