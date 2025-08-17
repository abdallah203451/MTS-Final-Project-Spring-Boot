package com.example.WorkforceManagement.controller;

import com.example.WorkforceManagement.dto.employee.*;
import com.example.WorkforceManagement.security.SecurityUtils;
import com.example.WorkforceManagement.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.security.Principal;

@RestController
@RequestMapping("/api/technicians")
public class TechnicianController {

    private final EmployeeService service;
    public TechnicianController(EmployeeService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<EmployeeDTO> create(@Valid @RequestBody CreateTechnicianDTO dto, Principal principal) {
        Long createdById = SecurityUtils.getCurrentUserId();
        EmployeeDTO created = service.createTechnician(dto, createdById);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> list() {
        return ResponseEntity.ok(service.getAllTechnicians());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(service.getTechnicianById(id));
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