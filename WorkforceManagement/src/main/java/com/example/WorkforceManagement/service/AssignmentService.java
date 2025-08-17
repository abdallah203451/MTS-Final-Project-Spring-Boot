package com.example.WorkforceManagement.service;

import com.example.WorkforceManagement.dto.assignment.*;
import com.example.WorkforceManagement.dto.timeslot.DaySlotsDTO;
import java.util.List;

public interface AssignmentService {
    AssignmentDTO assignWorkOrder(AssignmentCreateDTO dto, Long assignedById);
    List<DaySlotsDTO> getTimeSlotsForTechnician(Long technicianId, int days);
}