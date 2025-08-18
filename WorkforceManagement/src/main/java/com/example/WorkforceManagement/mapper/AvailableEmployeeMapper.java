package com.example.WorkforceManagement.mapper;

import com.example.WorkforceManagement.dto.employee.AvailableEmployeeDTO;
import com.example.WorkforceManagement.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AvailableEmployeeMapper {
    @Mapping(target = "availableSlots", ignore = true)
    AvailableEmployeeDTO toDto(Employee employee);
}
