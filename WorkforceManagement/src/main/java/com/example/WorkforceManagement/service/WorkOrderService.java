package com.example.WorkforceManagement.service;

import com.example.WorkforceManagement.dto.workorder.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WorkOrderService {
    WorkOrderDTO createWorkOrder(WorkOrderCreateDTO dto, Long createdById);
    Page<WorkOrderDTO> getAllWorkOrders(Pageable pageable);
    WorkOrderDTO getWorkOrderById(Long id);
    Page<WorkOrderDTO> searchWorkOrders(WorkOrderSearchCriteria criteria, Pageable pageable);
    WorkOrderDTO updateWorkOrder(Long id, WorkOrderUpdateDTO dto);
    WorkOrderDTO updateStatusToCancelled(Long id);
}