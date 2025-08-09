package org.example.serviceImpl;

import org.example.model.User;
import org.example.model.dto.AuthResponse;
import org.example.model.dto.LoginRequest;
import org.example.model.dto.SignupRequest;
import org.example.repo.UserRepository;
import org.example.service.AuthService;
import org.example.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public AuthResponse signup(SignupRequest signupRequest) {
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new RuntimeException("User already exists with email: " + signupRequest.getEmail());
        }

        User user = new User();
        user.setName(signupRequest.getFullName());
        user.setEmail(signupRequest.getEmail());
        user.setPhone(signupRequest.getPhone() != null ? signupRequest.getPhone() : "");
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

        User savedUser = userRepository.save(user);

        String token = jwtUtil.generateToken(savedUser.getEmail(), savedUser.getId().toString());

        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
            savedUser.getId().toString(),
            savedUser.getName(),
            savedUser.getEmail()
        );

        return new AuthResponse(
            "Signup successful",
            userInfo,
            "Bearer " + token
        );
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmailAndIsActiveTrue(loginRequest.getEmail())
            .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getId().toString());

        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
            user.getId().toString(),
            user.getName(),
            user.getEmail()
        );

        return new AuthResponse(
            "Login successful",
            userInfo,
            "Bearer " + token
        );
    }
} 