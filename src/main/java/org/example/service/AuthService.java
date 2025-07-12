package org.example.service;

import org.example.model.dto.AuthResponse;
import org.example.model.dto.LoginRequest;
import org.example.model.dto.SignupRequest;

public interface AuthService {
    
    AuthResponse signup(SignupRequest signupRequest);
    
    AuthResponse login(LoginRequest loginRequest);
} 