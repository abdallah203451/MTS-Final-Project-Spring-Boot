package com.example.WorkforceManagement.service.impl;

import com.example.WorkforceManagement.dto.workorder.*;
import com.example.WorkforceManagement.entity.WorkOrder;
import com.example.WorkforceManagement.entity.Employee;
import com.example.WorkforceManagement.exception.ResourceNotFoundException;
import com.example.WorkforceManagement.mapper.WorkOrderMapper;
import com.example.WorkforceManagement.repository.WorkOrderRepository;
import com.example.WorkforceManagement.repository.EmployeeRepository;
import com.example.WorkforceManagement.service.WorkOrderService;
import com.example.WorkforceManagement.specification.WorkOrderSpecification;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;

@Service
public class WorkOrderServiceImpl implements WorkOrderService {

    private final WorkOrderRepository repo;
    private final EmployeeRepository employeeRepository;
    private final WorkOrderMapper mapper;

    public WorkOrderServiceImpl(WorkOrderRepository repo, EmployeeRepository employeeRepository, WorkOrderMapper mapper) {
        this.repo = repo;
        this.employeeRepository = employeeRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public WorkOrderDTO createWorkOrder(WorkOrderCreateDTO dto, Long createdById) {
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
}