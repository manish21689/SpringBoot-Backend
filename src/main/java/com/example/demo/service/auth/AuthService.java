package com.example.demo.service.auth;

import com.example.demo.dto.LoginRequestDTO;
import com.example.demo.dto.RegisterRequestDTO;
public interface AuthService {
    String register(RegisterRequestDTO request);
    String login(LoginRequestDTO request);
}