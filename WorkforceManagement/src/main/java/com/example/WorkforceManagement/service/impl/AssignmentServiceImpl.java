package com.example.WorkforceManagement.service.impl;

import com.example.WorkforceManagement.dto.assignment.*;
import com.example.WorkforceManagement.dto.timeslot.*;
import com.example.WorkforceManagement.entity.*;
import com.example.WorkforceManagement.exception.ConflictException;
import com.example.WorkforceManagement.exception.ResourceNotFoundException;
import com.example.WorkforceManagement.mapper.AssignmentMapper;
import com.example.WorkforceManagement.repository.*;
import com.example.WorkforceManagement.service.AssignmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
import java.util.*;
import java.util.stream.*;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    private final WorkOrderAssignmentRepository assignmentRepo;
    private final EmployeeRepository employeeRepo;
    private final WorkOrderRepository workOrderRepo;
    private final IntervalRepository intervalRepo;
    private final AssignmentMapper mapper;

    public AssignmentServiceImpl(WorkOrderAssignmentRepository assignmentRepo,
                                 EmployeeRepository employeeRepo,
                                 WorkOrderRepository workOrderRepo,
                                 IntervalRepository intervalRepo,
                                 AssignmentMapper mapper) {
        this.assignmentRepo = assignmentRepo;
        this.employeeRepo = employeeRepo;
        this.workOrderRepo = workOrderRepo;
        this.intervalRepo = intervalRepo;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public AssignmentDTO assignWorkOrder(AssignmentCreateDTO dto, Long assignedById) {
        Employee tech = employeeRepo.findById(dto.getEmployeeId()).orElseThrow(() -> new ResourceNotFoundException("Technician not found"));
        if (!"Technician".equals(tech.getRole())) throw new ResourceNotFoundException("Employee is not a technician");
        Interval interval = intervalRepo.findById(dto.getIntervalId()).orElseThrow(() -> new ResourceNotFoundException("Interval not found"));
        boolean exists = assignmentRepo.existsByEmployeeAndWorkDateAndInterval(tech, dto.getWorkDate(), interval);
        if (exists) throw new ConflictException("Technician already assigned for that date & interval");

        WorkOrder wo = workOrderRepo.findById(dto.getWorkOrderId()).orElseThrow(() -> new ResourceNotFoundException("WorkOrder not found"));
        Employee assignedBy = employeeRepo.findById(assignedById).orElseThrow(() -> new ResourceNotFoundException("AssignedBy not found"));

        WorkOrderAssignment a = new WorkOrderAssignment();
        a.setWorkOrder(wo);
        a.setEmployee(tech);
        a.setWorkDate(dto.getWorkDate());
        a.setInterval(interval);
        a.setAssignedAt(OffsetDateTime.now());
        a.setAssignedBy(assignedBy);
        WorkOrderAssignment saved = assignmentRepo.save(a);

        wo.setStatus("Assigned");
        workOrderRepo.save(wo);

        return mapper.toDto(saved);
    }

    @Override
    public List<DaySlotsDTO> getTimeSlotsForTechnician(Long technicianId, int days) {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(days - 1);
        List<Interval> intervals = intervalRepo.findAll();
        // load assignments in range
        List<WorkOrderAssignment> assignments = assignmentRepo.findByEmployeeEmployeeIdAndWorkDateBetween(technicianId, start, end);
        // map by date+intervalId
        Set<String> taken = assignments.stream()
                .map(a -> a.getWorkDate().toString() + "|" + a.getInterval().getIntervalId())
                .collect(Collectors.toSet());

        List<DaySlotsDTO> result = new ArrayList<>();
        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
            LocalDate current = d; // <-- make a final/effectively-final copy for lambdas

            DaySlotsDTO day = new DaySlotsDTO();
            day.setDate(current);

            List<IntervalSlotDTO> slots = intervals.stream().map(iv -> {
                IntervalSlotDTO s = new IntervalSlotDTO();
                s.setIntervalId(iv.getIntervalId());
                s.setLabel(iv.getLabel());
                s.setAvailable(!taken.contains(current.toString() + "|" + iv.getIntervalId()));
                return s;
            }).collect(Collectors.toList());

            day.setIntervals(slots);
            result.add(day);
        }
        return result;
    }

}