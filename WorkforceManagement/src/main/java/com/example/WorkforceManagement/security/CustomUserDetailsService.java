package com.example.WorkforceManagement.security;

import com.example.WorkforceManagement.entity.Employee;
import com.example.WorkforceManagement.repository.EmployeeRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final EmployeeRepository employeeRepository;

    public CustomUserDetailsService(EmployeeRepository repo) { this.employeeRepository = repo; }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee e = employeeRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found by email"));
        return User.builder()
                .username(e.getEmail())
                .password(e.getPassword())
                .roles(e.getRole())
                .build();
    }

    public UserDetails loadById(Long id) {
        Optional<Employee> opt = employeeRepository.findById(id);
        Employee e = opt.orElseThrow(() -> new UsernameNotFoundException("User not found by id"));
        return User.builder()
                .username(e.getEmail())
                .password(e.getPassword())
                .roles(e.getRole())
                .build();
    }
}