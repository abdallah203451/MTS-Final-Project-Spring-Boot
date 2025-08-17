package com.example.WorkforceManagement.service.impl;

import com.example.WorkforceManagement.dto.auth.*;
import com.example.WorkforceManagement.entity.Employee;
import com.example.WorkforceManagement.entity.RefreshToken;
import com.example.WorkforceManagement.repository.EmployeeRepository;
import com.example.WorkforceManagement.repository.RefreshTokenRepository;
import com.example.WorkforceManagement.security.JwtUtil;
import com.example.WorkforceManagement.service.AuthService;
import com.example.WorkforceManagement.exception.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final EmployeeRepository employeeRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final long refreshExpiryDays;

    public AuthServiceImpl(EmployeeRepository employeeRepository,
                           RefreshTokenRepository refreshTokenRepository,
                           JwtUtil jwtUtil,
                           PasswordEncoder passwordEncoder,
                           org.springframework.core.env.Environment env) {
        this.employeeRepository = employeeRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.refreshExpiryDays = Long.parseLong(env.getProperty("jwt.refresh-expiration-days", "7"));
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest req) {
        Employee e = employeeRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid credentials"));
        if (!passwordEncoder.matches(req.getPassword(), e.getPassword())) {
            throw new ResourceNotFoundException("Invalid credentials");
        }
        String access = jwtUtil.generateAccessToken(e.getEmployeeId(), e.getRole(), e.getEmail());
        String refresh = createRefreshToken(e);
        long expiresInSeconds = Long.parseLong(String.valueOf( jwtUtil == null ? 0 : 60 * 15 )); // for client
        return new AuthResponse(access, refresh, expiresInSeconds);
    }

    private String createRefreshToken(Employee e) {
        String token = UUID.randomUUID().toString();
        RefreshToken rt = new RefreshToken();
        rt.setToken(token);
        rt.setEmployee(e);
        rt.setExpiryDate(Instant.now().plusSeconds(refreshExpiryDays * 24L * 3600L));
        refreshTokenRepository.save(rt);
        return token;
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(RefreshRequest req) {
        RefreshToken rt = refreshTokenRepository.findByToken(req.getRefreshToken())
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token not found"));
        if (rt.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(rt);
            throw new ResourceNotFoundException("Refresh token expired");
        }
        Employee e = rt.getEmployee();
        String access = jwtUtil.generateAccessToken(e.getEmployeeId(), e.getRole(), e.getEmail());
        // Optionally rotate refresh token
        String newRefresh = createRefreshToken(e);
        refreshTokenRepository.delete(rt);
        return new AuthResponse(access, newRefresh, 60L * 15L);
    }

    @Override
    @Transactional
    public void logout(String refreshToken) {
        refreshTokenRepository.findByToken(refreshToken)
                .ifPresent(refreshTokenRepository::delete);
    }

    @Override
    @Transactional
    public void registerTeamLeader(RegisterRequest req) {
        if (employeeRepository.findByEmail(req.getEmail()).isPresent())
            throw new IllegalArgumentException("Email already in use");
        Employee e = new Employee();
        e.setName(req.getName());
        e.setEmail(req.getEmail());
        e.setPassword(passwordEncoder.encode(req.getPassword()));
        e.setPhoneNumber(req.getPhoneNumber());
        e.setRole("TeamLeader");
        employeeRepository.save(e);
    }
}