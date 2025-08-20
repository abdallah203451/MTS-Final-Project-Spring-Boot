package com.example.WorkforceManagement.service;

import com.example.WorkforceManagement.dto.employee.*;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeService {
    EmployeeDTO createTechnician(CreateTechnicianDTO dto, Long createdById);
    List<EmployeeDTO> getAllTechnicians();
    EmployeeDTO getTechnicianById(Long id);
    List<EmployeeDTO> getAvailableTechniciansInDate(LocalDate date);
}
