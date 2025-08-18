package com.example.WorkforceManagement.dto.employee;

import lombok.Data;

@Data
public class AvailableEmployeeDTO {
    private Long employeeId;
    private String name;
    private String email;
    private String phoneNumber;
    private int availableSlots; // Number of available intervals for the date
}
