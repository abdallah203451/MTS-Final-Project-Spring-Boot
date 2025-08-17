package com.example.WorkforceManagement.mapper;

import com.example.WorkforceManagement.entity.Employee;
import com.example.WorkforceManagement.dto.employee.EmployeeDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    EmployeeDTO toDto(Employee e);
}