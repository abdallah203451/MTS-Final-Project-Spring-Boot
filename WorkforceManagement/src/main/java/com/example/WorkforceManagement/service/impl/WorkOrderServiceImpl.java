package com.example.WorkforceManagement.service.impl;

import com.example.WorkforceManagement.dto.workorder.*;
import com.example.WorkforceManagement.entity.WorkOrder;
import com.example.WorkforceManagement.entity.Employee;
import com.example.WorkforceManagement.exception.ResourceNotFoundException;
import com.example.WorkforceManagement.mapper.WorkOrderMapper;
import com.example.WorkforceManagement.repository.WorkOrderAssignmentRepository;
import com.example.WorkforceManagement.repository.WorkOrderRepository;
import com.example.WorkforceManagement.repository.EmployeeRepository;
import com.example.WorkforceManagement.service.WorkOrderService;
import com.example.WorkforceManagement.specification.WorkOrderSpecification;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Service
public class WorkOrderServiceImpl implements WorkOrderService {

    private final WorkOrderRepository repo;
    private final EmployeeRepository employeeRepository;
    private final WorkOrderAssignmentRepository assignmentRepo;
    private final WorkOrderMapper mapper;

    public WorkOrderServiceImpl(WorkOrderRepository repo, EmployeeRepository employeeRepository, WorkOrderMapper mapper, WorkOrderAssignmentRepository assignmentRepo) {
        this.repo = repo;
        this.employeeRepository = employeeRepository;
        this.mapper = mapper;
        this.assignmentRepo = assignmentRepo;
    }

    @Override
    @Transactional
    public WorkOrderDTO createWorkOrder(WorkOrderCreateDTO dto, Long createdById) {
        LocalDate today = LocalDate.now();

        if (dto.getProposedSchedulingDate().isBefore(today)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "proposed scheduling date cannot be before today");
        }
        if (dto.getProposedSchedulingDate().isAfter(today.plusDays(13))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "proposed scheduling date must be within 14 days from today");
        }

        Employee creator = employeeRepository.findById(createdById).orElseThrow(() -> new ResourceNotFoundException("Creator not found"));
        WorkOrder w = new WorkOrder();
        w.setTitle(dto.getTitle());
        w.setDescription(dto.getDescription());
        w.setCustomerName(dto.getCustomerName());
        w.setCustomerMobile(dto.getCustomerMobile());
        w.setCustomerEmail(dto.getCustomerEmail());
        w.setCustomerAddress(dto.getCustomerAddress());
        w.setProposedSchedulingDate(dto.getProposedSchedulingDate());
        w.setCreatedAt(OffsetDateTime.now());
        w.setCreatedBy(creator);
        w.setStatus("NotAssigned");
        WorkOrder saved = repo.save(w);
        return mapper.toDto(saved);
    }

    @Override
    public Page<WorkOrderDTO> getAllWorkOrders(Pageable pageable) {
        return repo.findAll(pageable).map(mapper::toDto);
    }

    @Override
    public WorkOrderDTO getWorkOrderById(Long id) {
        WorkOrder w = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("WorkOrder not found"));
        return mapper.toDto(w);
    }

    @Override
    public Page<WorkOrderDTO> searchWorkOrders(WorkOrderSearchCriteria criteria, Pageable pageable) {
        return repo.findAll(WorkOrderSpecification.byCriteria(criteria), pageable).map(mapper::toDto);
    }

    @Override
    @Transactional
    public WorkOrderDTO updateWorkOrder(Long id, WorkOrderUpdateDTO dto) {
        WorkOrder existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WorkOrder not found with id: " + id));

        LocalDate creationDate = existing.getCreatedAt().toLocalDate(); // adapt if creationDate is LocalDateTime
        LocalDate scheduled = dto.getProposedSchedulingDate();

        if (scheduled.isBefore(creationDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "scheduledDate cannot be before creation date (" + creationDate + ")");
        }
        if (scheduled.isAfter(creationDate.plusDays(13))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "scheduledDate must be within 14 days from creation date (" + creationDate + ")");
        }

        // Update allowed fields only (do NOT change status, createdAt, createdBy)
        existing.setTitle(dto.getTitle());
        existing.setDescription(dto.getDescription());
        existing.setCustomerName(dto.getCustomerName());
        existing.setCustomerMobile(dto.getCustomerMobile());
        existing.setCustomerEmail(dto.getCustomerEmail());
        existing.setCustomerAddress(dto.getCustomerAddress());
        existing.setProposedSchedulingDate(dto.getProposedSchedulingDate());

        WorkOrder saved = repo.save(existing);
        return mapper.toDto(saved);
    }

    public WorkOrderDTO updateStatusToCancelled(Long id){
        WorkOrder existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WorkOrder not found with id: " + id));
        existing.setStatus("Cancelled");
        assignmentRepo.deleteByWorkOrder(existing);
        WorkOrder saved = repo.save(existing);
        return mapper.toDto(saved);
    }
}