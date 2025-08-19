package com.example.WorkforceManagement.controller;

import com.example.WorkforceManagement.dto.assignment.*;
import com.example.WorkforceManagement.dto.employee.AvailableEmployeeDTO;
import com.example.WorkforceManagement.dto.timeslot.DaySlotsDTO;
import com.example.WorkforceManagement.security.SecurityUtils;
import com.example.WorkforceManagement.service.AssignmentService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.security.Principal;

@RestController
@RequestMapping("/api/assignments")
//@CrossOrigin(origins = "http://localhost:4200")
public class AssignmentController {

    private final AssignmentService service;
    public AssignmentController(AssignmentService service) { this.service = service; }

    @PostMapping("/assign")
    public ResponseEntity<AssignmentDTO> assign(@Valid @RequestBody AssignmentCreateDTO dto) {
        Long assignedById = SecurityUtils.getCurrentUserId();
        AssignmentDTO created = service.assignWorkOrder(dto, assignedById);
        return ResponseEntity.status(201).body(created);
    }

    @PostMapping("/reassign")
    public ResponseEntity<AssignmentDTO> reassign(@Valid @RequestBody ReassignmentDTO dto) {
        Long assignedById = SecurityUtils.getCurrentUserId();
        AssignmentDTO reassigned = service.reassignWorkOrder(dto, assignedById);
        return ResponseEntity.ok(reassigned);
    }

    @GetMapping("/available-employees")
    public ResponseEntity<List<AvailableEmployeeDTO>> getAvailableEmployees(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<AvailableEmployeeDTO> availableEmployees = service.getAvailableEmployeesForDate(date);
        return ResponseEntity.ok(availableEmployees);
    }

    @GetMapping("/technicians/{techId}/timeslots")
    public ResponseEntity<List<DaySlotsDTO>> timeslots(@PathVariable Long techId,
                                                       @RequestParam(defaultValue = "14") int days) {
        return ResponseEntity.ok(service.getTimeSlotsForTechnician(techId, days));
    }

    @GetMapping("/available-dates")
    public ResponseEntity<List<LocalDate>> availableDates(@RequestParam Long workOrderId) {
        List<LocalDate> dates = service.getAvailableDates(workOrderId);
        return ResponseEntity.ok(dates);
    }

    private Long resolveCurrentUserId(Principal principal) {
        if (principal == null) throw new RuntimeException("Unauthenticated");
        try {
            return Long.parseLong(principal.getName());
        } catch (NumberFormatException ex) {
            throw new RuntimeException("Implement mapping from principal to employee id");
        }
    }
}