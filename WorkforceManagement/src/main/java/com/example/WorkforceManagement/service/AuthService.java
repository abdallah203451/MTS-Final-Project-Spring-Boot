package com.example.WorkforceManagement.service;

import com.example.WorkforceManagement.dto.auth.*;

public interface AuthService {
    AuthResponse login(LoginRequest req);
    AuthResponse refreshToken(RefreshRequest req);
    void logout(String refreshToken);
    void registerTeamLeader(RegisterRequest req);
}