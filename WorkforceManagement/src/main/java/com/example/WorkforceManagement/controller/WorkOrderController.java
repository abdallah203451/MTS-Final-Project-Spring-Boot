package com.example.WorkforceManagement.controller;

import com.example.WorkforceManagement.dto.workorder.*;
import com.example.WorkforceManagement.service.WorkOrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import com.example.WorkforceManagement.security.SecurityUtils;

@RestController
@RequestMapping("/api/workorders")
//@CrossOrigin(origins = "http://localhost:4200")
public class WorkOrderController {

    private final WorkOrderService service;
    public WorkOrderController(WorkOrderService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<WorkOrderDTO> create(@Valid @RequestBody WorkOrderCreateDTO dto) {
        Long creatorId = SecurityUtils.getCurrentUserId();
        WorkOrderDTO created = service.createWorkOrder(dto, creatorId);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    public ResponseEntity<Page<WorkOrderDTO>> getAll(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "20") int size) {
        Page<WorkOrderDTO> p = service.getAllWorkOrders(PageRequest.of(page, size));
        return ResponseEntity.ok(p);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkOrderDTO> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(service.getWorkOrderById(id));
    }

    // Bind query params into WorkOrderSearchCriteria (q, status, fromDate, toDate, technicianId)
    @GetMapping("/search")
    public ResponseEntity<Page<WorkOrderDTO>> search(@ModelAttribute WorkOrderSearchCriteria criteria,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(service.searchWorkOrders(criteria, PageRequest.of(page, size)));
    }

    // NEW: update work order (all editable fields except 'status', 'createdAt', 'createdBy')
    @PutMapping("/{id}")
    public ResponseEntity<WorkOrderDTO> update(@PathVariable Long id,
                                               @Valid @RequestBody WorkOrderUpdateDTO dto) {
        WorkOrderDTO updated = service.updateWorkOrder(id, dto);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("forceCancel/{id}")
    public ResponseEntity<WorkOrderDTO> forceCancel(@PathVariable Long id) {
        WorkOrderDTO updated = service.updateStatusToCancelled(id);
        return ResponseEntity.ok(updated);
    }
}
