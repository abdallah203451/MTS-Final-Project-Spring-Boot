package com.example.WorkforceManagement.controller;

import com.example.WorkforceManagement.dto.assignment.*;
import com.example.WorkforceManagement.dto.timeslot.DaySlotsDTO;
import com.example.WorkforceManagement.security.SecurityUtils;
import com.example.WorkforceManagement.service.AssignmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.security.Principal;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AssignmentService service;
    public AssignmentController(AssignmentService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<AssignmentDTO> assign(@Valid @RequestBody AssignmentCreateDTO dto) {
        Long assignedById = SecurityUtils.getCurrentUserId();
        AssignmentDTO created = service.assignWorkOrder(dto, assignedById);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping("/technicians/{techId}/timeslots")
    public ResponseEntity<List<DaySlotsDTO>> timeslots(@PathVariable Long techId,
                                                       @RequestParam(defaultValue = "14") int days) {
        return ResponseEntity.ok(service.getTimeSlotsForTechnician(techId, days));
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