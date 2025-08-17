package com.example.WorkforceManagement.repository;

import com.example.WorkforceManagement.entity.RefreshToken;
import com.example.WorkforceManagement.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    int deleteByEmployee(Employee employee);
}