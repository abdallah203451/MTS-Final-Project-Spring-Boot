package com.example.WorkforceManagement.service.impl;

import com.example.WorkforceManagement.dto.assignment.*;
import com.example.WorkforceManagement.dto.employee.AvailableEmployeeDTO;
import com.example.WorkforceManagement.dto.timeslot.*;
import com.example.WorkforceManagement.entity.*;
import com.example.WorkforceManagement.exception.ConflictException;
import com.example.WorkforceManagement.exception.ResourceNotFoundException;
import com.example.WorkforceManagement.mapper.AssignmentMapper;
import com.example.WorkforceManagement.mapper.AvailableEmployeeMapper;
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
    private final AvailableEmployeeMapper availableEmployeeMapper;

    public AssignmentServiceImpl(WorkOrderAssignmentRepository assignmentRepo,
                                 EmployeeRepository employeeRepo,
                                 WorkOrderRepository workOrderRepo,
                                 IntervalRepository intervalRepo,
                                 AssignmentMapper mapper,
                                 AvailableEmployeeMapper availableEmployeeMapper) {
        this.assignmentRepo = assignmentRepo;
        this.employeeRepo = employeeRepo;
        this.workOrderRepo = workOrderRepo;
        this.intervalRepo = intervalRepo;
        this.mapper = mapper;
        this.availableEmployeeMapper = availableEmployeeMapper;
    }

    @Override
    @Transactional
    public AssignmentDTO assignWorkOrder(AssignmentCreateDTO dto, Long assignedById) {
        // Validate technician
        Employee tech = employeeRepo.findById(dto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Technician not found"));
        if (!"Technician".equals(tech.getRole())) {
            throw new ResourceNotFoundException("Employee is not a technician");
        }

        // Validate work order
        WorkOrder wo = workOrderRepo.findById(dto.getWorkOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("WorkOrder not found"));

        // Check if work order is already assigned
        if ("Assigned".equals(wo.getStatus())) {
            throw new ConflictException("WorkOrder is already assigned or completed");
        }

        // Get assigned by employee
        Employee assignedBy = employeeRepo.findById(assignedById)
                .orElseThrow(() -> new ResourceNotFoundException("AssignedBy not found"));

        // Find an available interval for the proposed date
        LocalDate workDate = wo.getProposedSchedulingDate();
        List<Interval> allIntervals = intervalRepo.findAll();

        Interval availableInterval = null;
        for (Interval interval : allIntervals) {
            if (!assignmentRepo.existsByEmployeeAndWorkDateAndInterval(tech, workDate, interval)) {
                availableInterval = interval;
                break;
            }
        }

        if (availableInterval == null) {
            throw new ConflictException("Technician has no available intervals for the proposed date");
        }

        // Create assignment
        WorkOrderAssignment assignment = new WorkOrderAssignment();
        assignment.setWorkOrder(wo);
        assignment.setEmployee(tech);
        assignment.setWorkDate(workDate);
        assignment.setInterval(availableInterval);
        assignment.setAssignedAt(OffsetDateTime.now());
        assignment.setAssignedBy(assignedBy);

        WorkOrderAssignment saved = assignmentRepo.save(assignment);

        // Update work order status
        wo.setStatus("Assigned");
        workOrderRepo.save(wo);

        return mapper.toDto(saved);
    }

    @Override
    @Transactional
    public AssignmentDTO reassignWorkOrder(ReassignmentDTO dto, Long assignedById) {
        // Validate work order
        WorkOrder wo = workOrderRepo.findById(dto.getWorkOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("WorkOrder not found"));

        // Validate new technician
        Employee newTech = employeeRepo.findById(dto.getNewEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("New technician not found"));
        if (!"Technician".equals(newTech.getRole())) {
            throw new ResourceNotFoundException("Employee is not a technician");
        }

        // Get assigned by employee
        Employee assignedBy = employeeRepo.findById(assignedById)
                .orElseThrow(() -> new ResourceNotFoundException("AssignedBy not found"));

        // Remove existing assignments for this work order
        List<WorkOrderAssignment> existingAssignments = assignmentRepo.findByWorkOrder(wo);
        assignmentRepo.deleteAll(existingAssignments);

        // Find an available interval for the new technician
        LocalDate workDate = wo.getProposedSchedulingDate();
        List<Interval> allIntervals = intervalRepo.findAll();

        Interval availableInterval = null;
        for (Interval interval : allIntervals) {
            if (!assignmentRepo.existsByEmployeeAndWorkDateAndInterval(newTech, workDate, interval)) {
                availableInterval = interval;
                break;
            }
        }

        if (availableInterval == null) {
            throw new ConflictException("New technician has no available intervals for the proposed date");
        }

        // Create new assignment
        WorkOrderAssignment newAssignment = new WorkOrderAssignment();
        newAssignment.setWorkOrder(wo);
        newAssignment.setEmployee(newTech);
        newAssignment.setWorkDate(workDate);
        newAssignment.setInterval(availableInterval);
        newAssignment.setAssignedAt(OffsetDateTime.now());
        newAssignment.setAssignedBy(assignedBy);

        WorkOrderAssignment saved = assignmentRepo.save(newAssignment);

        // Keep work order status as "Assigned"
        wo.setStatus("Assigned");
        workOrderRepo.save(wo);

        return mapper.toDto(saved);
    }

    @Override
    public List<AvailableEmployeeDTO> getAvailableEmployeesForDate(LocalDate date) {
        List<Employee> availableEmployees = assignmentRepo.findTechniciansWithAvailableIntervals(date);
        long totalIntervals = intervalRepo.count();

        return availableEmployees.stream()
                .map(employee -> {
                    AvailableEmployeeDTO dto = availableEmployeeMapper.toDto(employee);
                    long assignedIntervals = assignmentRepo.countAssignedIntervals(employee.getEmployeeId(), date);
                    dto.setAvailableSlots((int)(totalIntervals - assignedIntervals));
                    return dto;
                })
                .collect(Collectors.toList());
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

    @Override
    public List<LocalDate> getAvailableDates(int days) {
        if (days <= 0) return Collections.emptyList();

        LocalDate start = LocalDate.now().plusDays(1); // tomorrow
        LocalDate end = start.plusDays(days - 1);

        long techCount = employeeRepo.countByRole("Technician");
        long intervalCount = intervalRepo.count();

        // If no technicians or no intervals, there are no available days
        if (techCount <= 0 || intervalCount <= 0) return Collections.emptyList();

        long totalSlotsPerDay = techCount * intervalCount;

        // Query assignments grouped by date (single DB call)
        List<Object[]> rows = assignmentRepo.countTechnicianAssignmentsGroupedByDate(start, end);
        Map<LocalDate, Long> assignedByDate = new HashMap<>();
        for (Object[] r : rows) {
            LocalDate d = (LocalDate) r[0];
            Long cnt = (Long) r[1];
            assignedByDate.put(d, cnt);
        }

        List<LocalDate> result = new ArrayList<>();
        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
            long assigned = assignedByDate.getOrDefault(d, 0L);
            if (assigned < totalSlotsPerDay) {
                result.add(d);
            }
        }

        return result;
    }
}