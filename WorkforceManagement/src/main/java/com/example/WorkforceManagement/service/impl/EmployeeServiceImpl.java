package com.example.WorkforceManagement.service.impl;

import com.example.WorkforceManagement.dto.employee.*;
import com.example.WorkforceManagement.entity.Employee;
import com.example.WorkforceManagement.exception.ResourceNotFoundException;
import com.example.WorkforceManagement.mapper.EmployeeMapper;
import com.example.WorkforceManagement.repository.EmployeeRepository;
import com.example.WorkforceManagement.service.EmployeeService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository repo;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeMapper mapper;

    public EmployeeServiceImpl(EmployeeRepository repo, PasswordEncoder passwordEncoder, EmployeeMapper mapper) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public EmployeeDTO createTechnician(CreateTechnicianDTO dto, Long createdById) {
        if (repo.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already used");
        }
        Employee e = new Employee();
        e.setName(dto.getName());
        e.setEmail(dto.getEmail());
        e.setPassword(passwordEncoder.encode(dto.getPassword()));
        e.setPhoneNumber(dto.getPhoneNumber());
        e.setRole("Technician");
        Employee saved = repo.save(e);
        return mapper.toDto(saved);
    }

    @Override
    public List<EmployeeDTO> getAllTechnicians() {
        return repo.findAll().stream()
                .filter(e -> "Technician".equals(e.getRole()))
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO getTechnicianById(Long id) {
        Employee e = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Technician not found"));
        if (!"Technician".equals(e.getRole())) throw new ResourceNotFoundException("Employee is not a technician");
        return mapper.toDto(e);
    }
}