package com.example.WorkforceManagement.mapper;

import com.example.WorkforceManagement.entity.WorkOrder;
import com.example.WorkforceManagement.dto.workorder.WorkOrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WorkOrderMapper {
    @Mapping(source = "createdBy.employeeId", target = "createdById")
    @Mapping(source = "createdBy.name", target = "createdByName")
    @Mapping(target = "status", source = "status")
    WorkOrderDTO toDto(WorkOrder w);

//    @Mapping(target = "createdById", source = "createdBy.employeeId")
//    @Mapping(target = "createdByName", source = "createdBy.fullName") // adapt field name
//    WorkOrderDTO toDto(WorkOrder w);
}