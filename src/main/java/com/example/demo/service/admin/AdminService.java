package com.example.demo.service.admin;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.demo.entity.Worker;



public interface AdminService {
    Page<Worker> getAllUsers(Pageable pageable);
    void deleteWorker(Long id);
}