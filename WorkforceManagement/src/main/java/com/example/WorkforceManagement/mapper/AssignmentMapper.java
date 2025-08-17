package com.example.WorkforceManagement.mapper;

import com.example.WorkforceManagement.entity.WorkOrderAssignment;
import com.example.WorkforceManagement.dto.assignment.AssignmentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AssignmentMapper {
    @Mapping(source = "workOrder.workOrderId", target = "workOrderId")
    @Mapping(source = "employee.employeeId", target = "employeeId")
    @Mapping(source = "interval.intervalId", target = "intervalId")
    @Mapping(source = "interval.label", target = "intervalLabel")
    @Mapping(source = "assignedBy.employeeId", target = "assignedById")
    AssignmentDTO toDto(WorkOrderAssignment a);
}