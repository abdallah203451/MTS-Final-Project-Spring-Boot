package com.example.WorkforceManagement.dto.employee;

import lombok.Data;

@Data
public class EmployeeDTO {
    private Long employeeId;
    private String name;
    private String email;
    private String phoneNumber;
    private String role;
}