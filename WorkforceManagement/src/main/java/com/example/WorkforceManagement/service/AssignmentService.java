package com.example.WorkforceManagement.service;

import com.example.WorkforceManagement.dto.assignment.*;
import com.example.WorkforceManagement.dto.employee.AvailableEmployeeDTO;
import com.example.WorkforceManagement.dto.timeslot.DaySlotsDTO;

import java.time.LocalDate;
import java.util.List;

public interface AssignmentService {
    AssignmentDTO assignWorkOrder(AssignmentCreateDTO dto, Long assignedById);
    AssignmentDTO reassignWorkOrder(ReassignmentDTO dto, Long assignedById);
    List<AvailableEmployeeDTO> getAvailableEmployeesForDate(LocalDate date);
    List<DaySlotsDTO> getTimeSlotsForTechnician(Long technicianId, int days);
    List<java.time.LocalDate> getAvailableDates(Long workOrderId);
}